package com.rrgy.common.iface;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

/**
 * 2010-10-8
 * 
 * @author wuhb
 * 
 */
public class RegisterCodeImpl {
	private static Log log = LogFactory.getLog(RegisterCodeImpl.class);

	public static String requestPage(String strPageURL, String param) {
		String content = "";
		strPageURL = strPageURL.trim();
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpProtocolParams
					.setUserAgent(
							httpclient.getParams(),
							"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.9) Gecko/20100315 Firefox/3.5.9");
			HttpPost httpPost = new HttpPost(strPageURL);
			httpPost.setURI(new java.net.URI(strPageURL));
			StringEntity myEntity = new StringEntity(param, "UTF-8");
			httpPost.addHeader("Content-Type", "text/html; charset=UTF-8");
			httpPost.setEntity(myEntity);
			HttpResponse response = httpclient.execute(httpPost);
			content = getContent(response, strPageURL);
			httpPost.abort();
			clearHttpClient(httpclient);
		} catch (ConnectException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return content;
	}

	public static String getContent(HttpResponse response, String strPageURL)
			throws ParseException, IOException {
		String content = "";
		StatusLine status = response.getStatusLine();
		if (status.getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			content = paserSuccess(entity, strPageURL);
		} else if (status.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY
				|| status.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
			content = requestPage(reLocaltion(response), "");
		} else {

		}
		return content;
	}

	public static String reLocaltion(HttpResponse response) {
		String location = "";
		Header[] header = response.getHeaders("Location");
		if (header != null && header.length != 0)
			location = header[0].toString();
		if (location.indexOf("http://") == -1) {
			location = "http://" + location;
		}
		return location;
	}

	public static String paserSuccess(HttpEntity entity, String strPageURL)
			throws ParseException, IOException {
		String charset = EntityUtils.getContentCharSet(entity);
		byte[] bytes = EntityUtils.toByteArray(entity);
		if (charset == null)
			charset = getEncoding(bytes);
		if (charset == null)
			charset = getFileEncoding(new URL(strPageURL));
		String strPageResource = "";
		if (entity == null)
			return strPageResource;
		strPageResource = new String(bytes, charset);
		return strPageResource;
	}

	public static String getEncoding(byte[] bytes) {
		return "gbk";
	}

	public static String getFileEncoding(URL url) {
		return url.toString();
	}

	public static void clearHttpClient(DefaultHttpClient httpclient) {
		httpclient.getConnectionManager().shutdown();
		httpclient.clearRequestInterceptors();
		httpclient.clearResponseInterceptors();
	}

	/**
	 * @param argsq
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String url = "http://localhost:8080/ynet/clientServices.do?iscrypt=1";
		// String param1 =
		// "{\"serviceId\":\"eu_2014V01modifyMeMail\",\"params\":{\"staffId\":\"1358903647345\""
		// +
		// ",\"username\":\"wu98hay@163.com\"" +
		// ",\"pass\":\"983233\",\"smtp\":\"smtp.163.com\",\"port\":\"25\",\"serverport\":\"110\",\"recserver\":\"pop.163.com\"}}";
		String param1 = "{\"serviceId\":\"eu_2014V01sendMeMail\",\"params\":{\"staffId\":\"1358903647345\",\"unameo\":\"wuhay@qq.com\",\"subject\":\"TTEST\",\"content\":\"TTEST\"}}";
		String rs1 = requestPage(url, param1);
		System.out.println(rs1);
		// String param2 =
		// "{\"serviceId\":\"eu_2013V01loadOrg\",\"params\":{\"staffId\":\"1351080397432\",\"groupId\":\"1353562919772\"}}";
		// String rs2 = requestPage(url,param2);
		// System.out.println(rs2);
	}
}
