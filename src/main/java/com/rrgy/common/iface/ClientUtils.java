package com.rrgy.common.iface;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.rrgy.core.annotation.Client;

public class ClientUtils {

	
	public static List<Class<?>> getClasses() throws IOException,
			ClassNotFoundException {
		List<Class<?>> classFiles = new ArrayList<Class<?>>();
		String pk = "com.lfgj";
		String path = pk.replace('.', '/');
		ClassLoader classloader = Thread.currentThread()
				.getContextClassLoader();
		URL url = classloader.getResource(path);
		classFiles.addAll(getClasses(new File(url.getFile()), pk));
		return classFiles;
	}

	private static List<Class<?>> getClasses(File dir, String pk)
			throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!dir.exists()) {
			return classes;
		}
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				classes.addAll(getClasses(f, pk + "." + f.getName()));
			}
			String name = f.getName();
			if (name.endsWith(".class")) {
				Class clz = Class.forName(pk + "." + name.substring(0, name.length() - 6));
				Client client = (Client)clz.getAnnotation(Client.class);
				if(client!=null){
					classes.add(clz);
				}
			}
		}
		return classes;
	}

	public static boolean isEmptyStr(String source) {
		if (source == null || source.trim().length() == 0)
			return true;
		return false;
	}

	public static String getCurrDate() {
		Date currDate = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String strCurrDate = df.format(currDate);
		return strCurrDate;
	}

	public static String streamToString(InputStream is)
			throws UnsupportedEncodingException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		try {
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String str = buffer.toString();
		return str;
	}

	public static Element loadXML(String path) {
		SAXReader reader = new SAXReader(false);
		Document doc = null;
		try {
			doc = reader.read(new File(path));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element root = doc.getRootElement();
		return root;
	}

	public String processChar(String str) {
		str = str.replaceAll("'", "''");
		str = str.replaceAll("", "''");
		return str;
	}
}
