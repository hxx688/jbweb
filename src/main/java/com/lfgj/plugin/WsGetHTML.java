package com.lfgj.plugin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class WsGetHTML {
	static String ContentType = ""; // 实际下载得到的内容类型，来自 HTTP头部的 Content-Type，例如
									// Content-Type: text/plain; charset=utf-8
	static String ContentEncoding = ""; // 实际下载得到的内容类型，来自 HTTP头部的
										// Content-Encoding，例如 Content-Encoding:
										// gzip

	// 追加方式写入，将byte数组写入文件，从off偏移开始，写入len字节
	// 微盛投资 wstock.net 2015/10/13
	public static void AppendToFile(String filename, byte[] data, int off, int len) { // throws
																						// IOException
																						// {
		if (filename.length() > 0 && len > 0) {
			try {
				FileOutputStream fos = new FileOutputStream(filename, true); // true：采用追加方式写入
				fos.write(data, off, len); // 如文件存在，会自动追加写入到文件末尾
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 获取网页编码方式，支持匹配 http头 或 html代码
	// 微盛投资 wstock.net 2015/10/10
	private static String getCharset(String str) {
		String s2 = "";

		// 先匹配 html 中的charset，类似： <meta http-equiv='Content-Type'
		// content='text/html; charset=gb2312'>
		Pattern pattern = Pattern.compile("(?s)^.*?charset *= *(.*?)[ ?\'?\"?>?\r\n].*?$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			s2 = matcher.replaceAll("$1");
			if (s2 != null)
				if (s2.length() > 1) {
					return s2;
				}
		}

		// 然后匹配 http header中的charset，类似：Content-Type: text/plain; charset=utf-8
		pattern = Pattern.compile("(?s)^.*?charset *= *(.*?)$", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(str);
		if (matcher.find()) {
			s2 = matcher.replaceAll("$1");
			return s2;
		}

		return null;
	}

	// 解压gzip数据，data就是接收到的数据；返回解压缩后的数据（字节数组）
	public static byte[] decompressGzip(byte[] data) {
		GZIPInputStream gis = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			gis = new GZIPInputStream(bais);
			int count;
			byte data2[] = new byte[data.length];
			while ((count = gis.read(data2, 0, data.length)) != -1) {
				baos.write(data2, 0, count);
			}
			gis.close();
			data = baos.toByteArray();
			baos.flush();
			baos.close();
			bais.close(); // System.out.println("解压成功");
		} catch (IOException e) {
			e.printStackTrace(); // System.out.println(ex);
		} finally {
			try {
				gis.close();
			} catch (IOException e) {
				e.printStackTrace();// System.out.println(ex);
			}
		}
		return data;
	}

	// 通过字节数组下载网页,这种方法更灵活，支持下载（抓取）图片、文件，以及压缩格式gzip的网页
	// 这里通过java的 ByteArrayOutputStream 来作为缓存，代码会简单些（不用自己管理缓存大小）
	// 返回下载的数据
	// 微盛投资 wstock.net 2015/10/12
	public static byte[] wsGetHTMLByByte(String strURL) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); // 初始化缓存

		try {
			URL url = new URL(strURL);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(10 * 1000); // 设置超时间为10秒
			conn.setRequestProperty("User-Agent", "wstock java demo:wsGetHTMLByByte"); // 告诉服务器，自己程序的名称。
																						// 在一些特殊应用中，可设置为浏览器名称（一般不用）。例如
																						// Mozilla/5.0
																						// (Windows
																						// NT
																						// 6.1;
																						// WOW64;
																						// rv:41.0)
																						// Gecko/20100101
																						// Firefox/41.0

			InputStream in = conn.getInputStream(); // 输入流
			byte[] bs = new byte[10240]; // 每次最多读取10KB
			int len = 0;
			int total_size = 0;
			int total_len = 0;
			total_len = conn.getContentLength(); // getContentLength 实际来自
													// HTTP头部的 content-length
													// ，对于动态输出页面，WebServer未必包括content-length
			ContentType = conn.getContentType(); // 例如：Content-Type: text/plain;
													// charset=utf-8
			ContentEncoding = conn.getContentEncoding(); // 例如：Content-Encoding:
															// gzip
			System.out.println("ContentType:" + ContentType);
			System.out.println("ContentEncoding from HTTP Header:" + ContentEncoding);
			System.out.println("content-length:" + total_len);

			// 循环判断，如果bs为空，则in.read()方法返回-1，具体请参考InputStream的read();
			while ((len = in.read(bs)) > 0) { // while ((len = in.read(bs)) !=
												// -1) {
				baos.write(bs, 0, len); // 将新收到的数据放入缓存baos尾部， java的
										// ByteArrayOutputStream 会自动管理缓存大小
				total_size += len;
				System.out.println("downloading:" + total_size + "/" + total_len); // 显示下载进度
			}
			in.close(); // System.out.println("下载成功");
			if (total_size > 0) {
				byte[] data = baos.toByteArray();
				baos.flush();
				baos.close();
				return data;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 参数1：URL，如果缺省，则默认为
	// http://db2015.wstock.cn/wsDB_API/stock.php?market=SH60&r_type=2
	// 参数2：编码方式，例如 utf-8 （如果缺省，则默认为服务器端返回的charset，否则默认为 utf-8）
	// 参数3：存储到文件（含路径名），例如 d:/temp/db2015.txt 如果缺省，则在命令行中直接输出，不存储
	// 微盛投资 wstock.net 2015/10/09
	public static void main(String args[]) {
		String tURL = "http://db2015.wstock.cn/wsDB_API/stock.php?market=SH60&r_type=2"; // 参数1：URL，如果缺省，则默认
		String encoding = "a"; // utf-8
		String filename = "";
		int i = 0;

		if (args.length >= 1) {
			if (args[0].length() > 1) {
				tURL = args[0];
			}
		}
		if (args.length >= 2) {
			if (args[1].length() > 1) {
				encoding = args[1];
			}
		}
		if (args.length >= 3) {
			filename = args[2];
		}

		if (tURL.toLowerCase().indexOf("http") < 0) {
			tURL = "http://" + tURL;
		} // 增加默认协议头
		System.out.println("URL:" + tURL);
		byte[] html_buffer = wsGetHTMLByByte(tURL); // 下载文件 或者 获取对应网页的数据
		if (html_buffer == null) {
			System.out.println("未能下载到文件，退出");
			System.exit(-1);
		}

		// gzip解压缩
		String str = ContentEncoding;
		if (str != null) {
			if (str.toLowerCase().indexOf("gzip") >= 0) { // 是gzip压缩方式，需要先解压缩
				html_buffer = decompressGzip(html_buffer); // 解压缩
				System.out.println("gzip unzip OK.");
			}
		}
		// gzip解压缩

		// 提取 编码方式
		str = ContentType;
		if (str != null) {
			if (encoding.length() < 2) {
				str = getCharset(str);
				if (str != null) {
					encoding = str;
				}
			} // 如果用户没有手动指定编码方式，则尝试从服务器端返回Header数据中提取 charset
		}
		if (encoding.length() < 2) { // 尝试从数据中提取 字符集，例如 html中的
			i = 1024;
			if (i > html_buffer.length) {
				i = html_buffer.length;
			}
			str = new String(html_buffer, 0, i); // 这里将 字节数组最多前面1K字节 转化为
													// 字符串，直接按默认字符集转化，以便提取charset
			if (str != null) {
				str = getCharset(str);
				if (str != null) {
					encoding = str;
				}
			}
		}
		if (encoding.length() < 2) {
			encoding = "utf-8";
		} // 如果无法正确提取到编码方式，则默认为utf-8
		System.out.println("encoding:" + encoding + ";");
		// 提取 编码方式

		if (filename.length() > 0 && html_buffer.length > 0) { // 存储到文件
			AppendToFile(filename, html_buffer, 0, html_buffer.length); // 原始数据追加存储到文件，这个是二进制原始数据，因此不必判断
																		// 编码方式，直接存储即可
		} else { // 显示下载的内容
			try {
				str = new String(html_buffer, 0, html_buffer.length, encoding); // 这里将
																				// 字节数组
																				// 转化为
																				// 字符串，必须指定正确的编码方式（encoding）
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			System.out.println(str);
		}
	}
}
