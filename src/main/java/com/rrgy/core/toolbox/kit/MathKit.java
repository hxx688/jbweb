package com.rrgy.core.toolbox.kit;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class MathKit {

	// 默认除法运算精度
	public static int DEF_DIV_SCALE = 2; 
	
	private MathKit(){
		
	}
	
	/**
	 * 加法，保留两位小数
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static BigDecimal add(Object o1, Object o2) {
		o1 = o1 == null ? "0" : o1;
		o2 = o2 == null ? "0" : o2;
		BigDecimal b1 = new BigDecimal(o1.toString());
		BigDecimal b2 = new BigDecimal(o2.toString());
		return roundDown(b1.add(b2), DEF_DIV_SCALE);
	}
	
	/**
	 * 减法，保留两位小数
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static BigDecimal sub(Object o1, Object o2) {
		o1 = o1 == null ? "0" : o1;
		o2 = o2 == null ? "0" : o2;
		BigDecimal b1 = new BigDecimal(o1.toString());
		BigDecimal b2 = new BigDecimal(o2.toString());
		return roundDown(b1.subtract(b2), DEF_DIV_SCALE); 
	} 
	
	/**
	 * 减法，保留两位小数
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static BigDecimal sub(Object o1, Object o2,int scale) {
		o1 = o1 == null ? "0" : o1;
		o2 = o2 == null ? "0" : o2;
		BigDecimal b1 = new BigDecimal(o1.toString());
		BigDecimal b2 = new BigDecimal(o2.toString());
		return roundDown(b1.subtract(b2), scale); 
	} 
	
	/**
	 * 乘法，保留两位小数
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static BigDecimal mul(Object o1, Object o2) {
		o1 = o1 == null ? "0" : o1;
		o2 = o2 == null ? "0" : o2;
		BigDecimal b1 = new BigDecimal(o1.toString());
		BigDecimal b2 = new BigDecimal(o2.toString());
		return roundDown(b1.multiply(b2), DEF_DIV_SCALE);  
	} 
	
	/**
	 * 乘法，保留scale个小数
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static BigDecimal mul(Object o1, Object o2, int scale) { 
		o1 = o1 == null ? "0" : o1;
		o2 = o2 == null ? "0" : o2;
		BigDecimal b1 = new BigDecimal(o1.toString());
		BigDecimal b2 = new BigDecimal(o2.toString());
		BigDecimal v = b1.multiply(b2); 
		return roundDown(v, scale);
	} 


	/**
	 * 除法，保留二位小数
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static BigDecimal div(Object o1, Object o2) {
		BigDecimal b1 = new BigDecimal(o1.toString());
		BigDecimal b2 = new BigDecimal(o2.toString());
		return div(b1, b2, DEF_DIV_SCALE);
	} 
	
	/**
	 * 除法，保留scale个小数
	 * @param o1
	 * @param o2
	 * @param scale 小数个数
	 * @return
	 */
	public static BigDecimal div(Object o1, Object o2, int scale) { 
		BigDecimal b1 = new BigDecimal(o1.toString());
		BigDecimal b2 = new BigDecimal(o2.toString());
		if (scale < 0) { 
				throw new IllegalArgumentException( "The scale must be a positive integer or zero"); 
		}
		if(b2.compareTo(new BigDecimal(0)) == 1){
			BigDecimal b3 = b1.divide(b2,10,BigDecimal.ROUND_HALF_DOWN);
			return roundDown(b3, scale);
		}
		return new BigDecimal(0);
	}
	
	/**
	 * 取余数，四舍五入，保留scale个小数
	 * @param o1
	 * @param o2
	 * @param scale
	 * @return
	 */
	public static BigDecimal remainder(Object o1, Object o2, int scale){
		o1 = o1 == null ? "0" : o1;
		o2 = o2 == null ? "0" : o2;
		BigDecimal b1 = new BigDecimal(o1.toString());
		BigDecimal b2 = new BigDecimal(o2.toString());
		BigDecimal[] results = b1.divideAndRemainder(b2);
        return roundHalfUp(results[1], scale);
	}
	
	/**
	 * 去尾数，保留scale个小数
	 * @param v
	 * @param scale
	 * @return
	 */
	public static BigDecimal roundDown(BigDecimal v,int scale){
		return v.setScale(scale,BigDecimal.ROUND_DOWN); 
	}
	
	/**
	 * 四舍五入，保留scale个小数
	 * @param v
	 * @param scale
	 * @return
	 */
	public static BigDecimal roundHalfUp(BigDecimal v,int scale){
		return v.setScale(scale,BigDecimal.ROUND_HALF_UP); 
	}
	
	public static int len(String s) {
        int pointl = s.indexOf(".");
        if(pointl==-1){
        	return 1;
        }
        
        int len = s.substring(pointl+1).length();
        Double v = Math.pow(10, len);
        return v.intValue();
	}

	public static int point(String s) {
        int pointl = s.indexOf(".");
        if(pointl==-1){
        	return 0;
        }
        int len = s.substring(pointl+1).length();
        return len;
	}

	public static String clearZero(Object number){
		String s = number.toString();
		if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
	}

	public static BigDecimal getMax(BigDecimal a,BigDecimal b,BigDecimal c){
		BigDecimal max = (a.floatValue() > b.floatValue()) ? a : b;
		max = (max.floatValue() > c.floatValue()) ? max : c;
		return max;
	}
	
	public static BigDecimal getMin(BigDecimal a,BigDecimal b,BigDecimal c){
		BigDecimal min = (a.floatValue() < b.floatValue()) ? a : b;
		min = (min.floatValue() < c.floatValue()) ? min : c;
		return min;
	}
	
	public static void main(String[] args) {
//		BigDecimal b1 = new BigDecimal(53319.147);
//		BigDecimal b2 = new BigDecimal(5554337);
//		BigDecimal i=div(b1,b2,4);
//		System.out.print(i);
		BigDecimal b1 = new BigDecimal(53319.147);
		System.out.print(MathKit.clearZero("1.10"));
	}
}
