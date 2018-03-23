package com.rrgy.core.toolbox.kit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTML工具类<br>
 * 
 * @author zhanggw
 * 
 */
public class HtmlKit {

	/**
	 * 常用正则表达式：匹配HTML标记
	 */
	public final static String regExp_html_1 = "/ <(.*)>.* <\\/\\1> ? <(.*) \\/>/";
	
	/**
	 * 定义script的正则表达式 
	 */
	public final static String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>";
	
	/**
	 * 定义style的正则表达式 
	 */
	public final static String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>";
	
	/**
	 * 定义HTML标签的正则表达式 
	 */
	public final static String regEx_html = "<[^>]+>";
	
	/**
	 * 定义HTML标签的空格,回车,换行符,制表符
	 */
	public final static String regEx_temp = "\\s*|\t|\r|\n";

	// -------------------------------------------------------------------------------------- Read
	
	/**
	 * 去除html
	 * @param htmlStr
	 * @return
	 */
	public static String html2Str(String htmlStr){
        
        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
        Matcher m_script=p_script.matcher(htmlStr); 
        htmlStr=m_script.replaceAll(""); //过滤script标签 
        
        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
        Matcher m_style=p_style.matcher(htmlStr); 
        htmlStr=m_style.replaceAll(""); //过滤style标签 
        
        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
        Matcher m_html=p_html.matcher(htmlStr); 
        htmlStr=m_html.replaceAll(""); //过滤html标签 
        htmlStr = htmlStr.replaceAll(regEx_temp, "");
        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        return htmlStr.trim(); //返回文本字符串 
	}

}
