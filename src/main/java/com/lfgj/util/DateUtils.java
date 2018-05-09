package com.lfgj.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>Description: </p>
 * @date 2015年11月10日
 * @author 周顺得
 * @version 1.0
 * <p>Company:Mopon</p>
 * <p>Copyright:Copyright(c)2013</p>
 */
public class DateUtils {

	/**
	 * 缺省的日期显示格式： yyyy-MM-dd
	 * @see #DEFAULT_DATE_FORMATOR
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String YMDHMS = "yyyyMMddHHmmss";
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String SHORT_DATE_FORMAT = "yyyyMMdd";
	public static final String YMDHMSS = "yyyyMMddHHmmssSSS";


	/**
	 * 得到一年中的多少天是星期几
	 * 
	 * @param 星期天为1，星期六是7，，一周以周日开始；
	 */
	public static List<Integer> getValidWeekDays(Date beginDate, Date endDate, int[] validWeekDays) {

		Calendar c = Calendar.getInstance();
		c.setTime(beginDate);
		int start = c.get(Calendar.DAY_OF_YEAR);
		c.setTime(endDate);
		int end = c.getActualMaximum(Calendar.DAY_OF_YEAR);
		System.out.println(end);
		List<Integer> validWeekDayLst = new ArrayList<Integer>();
		for (int i = start; i <= end; i++) {
			c.set(Calendar.DAY_OF_YEAR, i);
			for (int validWeekDay : validWeekDays) {
				if (validWeekDay == c.get(Calendar.DAY_OF_WEEK)) {
					validWeekDayLst.add(i);
				}
			}
		}
		return validWeekDayLst;
	}

	/**
	 * 
	 * 方法用途:得到使用的日期范围 <br>
	 * 实现步骤: <br>
	 * @param beginDate
	 * @param endDate
	 * @param forbidTemp 限制模板
	 * @return
	 */
	public static List<Integer> getValidDate(Date beginDate, Date endDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(beginDate);
		int start = c.get(Calendar.DAY_OF_YEAR);
		c.setTime(endDate);
		int end = c.get(Calendar.DAY_OF_YEAR);
		List<Integer> forbidDays = new ArrayList<Integer>();
		for (int i = start; i <= end; i++) {
			c.set(Calendar.DAY_OF_YEAR, i);
			forbidDays.add(i);
		}
		return forbidDays;
	}

	/**
	 * 判断有效期是否超过默认设置的年数
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int getYearNum(Date beginDate, Date endDate) {
		Calendar beginCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		int year = endCal.get(Calendar.YEAR) - beginCal.get(Calendar.YEAR);
		// 初始化三年
		if (year > 3) {
			endCal.set(Calendar.YEAR, beginCal.get(Calendar.YEAR) + 3);
		}
		return endCal.get(Calendar.YEAR);
	}

	/**
	 * 
	 * 方法用途:根据限制模板及有效时间段生成日程 <br>
	 * 实现步骤: <br>
	 * @param beginDate
	 * @param endDate
	 * @param temp
	 * @param forbidFlag
	 * @param validFlag
	 * @return
	 */
	public static String getDateTemp(String validTemp, String forbidTemp) {

		char[] forbidChs = forbidTemp.toCharArray();
		char[] validChs = validTemp.toCharArray();
		for (int i = 0; i < forbidChs.length; i++) {
			if (forbidChs[i] == '1') {
				validChs[i] = '1';
			}
		}
		return String.copyValueOf(validChs);
	}

	/**
	 * 
	 * 方法用途:返回毫秒 <br>
	 * 实现步骤: <br>
	 * @param date
	 * @return
	 */
	public static long getMillis(Date date) {
		if (date == null) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}

	/**
	 * 
	 * 方法用途: 指定日期减去指定天数 <br>
	 * 实现步骤: <br>
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date diffDate(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) - ((long) day) * 24 * 3600 * 1000);
		return c.getTime();
	}

	public static String getDateTempByWeek(int year, String validTemp, Integer[] validWeekLst, Date endDate) {
		for (int i = 0; i < validWeekLst.length; i++) {
			if (validWeekLst[i] == 7) {
				validWeekLst[i] = 1;
			} else {
				validWeekLst[i] = validWeekLst[i] + 1;
			}
		}
		List<Integer> weekDayList = Arrays.asList(validWeekLst);

		Calendar cal = Calendar.getInstance();
		int currentTear = cal.get(Calendar.YEAR);
		cal.set(Calendar.YEAR, year);
		int getYear = cal.get(Calendar.YEAR);
		int end = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
		int currentDay = cal.get(Calendar.DAY_OF_YEAR);
		char[] validChs = validTemp.toCharArray();

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		int endYear = endCal.get(Calendar.YEAR);
		int endDay = endCal.get(Calendar.DAY_OF_YEAR);
		if (currentTear == getYear) {
			for (int i = currentDay; i < end; i++) {
				cal.set(Calendar.DAY_OF_YEAR, i + 1);
				if (!weekDayList.contains(cal.get(Calendar.DAY_OF_WEEK))) {
					validChs[i] = '1';
				}
			}
		} else if (getYear == endYear) {
			for (int i = 0; i < endDay; i++) {
				cal.set(Calendar.DAY_OF_YEAR, i + 1);
				if (!weekDayList.contains(cal.get(Calendar.DAY_OF_WEEK))) {
					validChs[i] = '1';
				}
			}
		} else {
			// 中间年
			for (int i = 0; i < end; i++) {
				cal.set(Calendar.DAY_OF_YEAR, i + 1);
				if (!weekDayList.contains(cal.get(Calendar.DAY_OF_WEEK))) {
					validChs[i] = '1';
				}
			}
		}
		return String.copyValueOf(validChs);
	}

	public static Integer[] StringConvertInt(String[] arrays) {
		Integer[] intArrays = null;
		if (null != arrays && arrays.length > 0) {
			intArrays = new Integer[arrays.length];
			for (int i = 0; i < arrays.length; i++) {
				intArrays[i] = Integer.parseInt(arrays[i]);
			}
		}
		return intArrays;
	}

	/**
	 * 
	 * 方法用途:数据库保存的星期与日历中的星期不一致问题：由于日历星期天为一周的第一天，而数据库保存的是星期一为一周的第一天 <br>
	 * 实现步骤: <br>
	 * @param weekArray
	 * @return
	 */

	public static int dayConvertWeekDay(int week) {
		for (int i = 0; i < 7; i++) {
			if (week == 7) {
				week = 1;
			} else {
				week = week + 1;
			}
		}
		return week;
	}

	/**
	 * 
	 * 方法用途: 得到某年的第几天是几年几月几日<br>
	 * 实现步骤: <br>
	 * @param year
	 * @param dayNum
	 * @return
	 */
	public static String getWhereDay(int year, int dayNum) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		return dateFormat.format(add(calendar.getTime(), dayNum, Calendar.DATE));
	}

	/**
	 * 为指定日期增加相应的天数或月数
	 * 
	 * @param date
	 *            基准日期
	 * @param amount
	 *            增加的数量
	 * @param field
	 *            增加的单位，年，月或者日 {@link #java.util.Calendar calendar fields}
	 * @return 增加以后的日期
	 */
	public static Date add(Date date, int amount, int field) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.add(field, amount);

		return calendar.getTime();
	}

	/** 
	 * 方法用途: 格式化显示时间：XX天XX时XX分<br>
	 * 实现步骤: <br>
	 * @param effectEndTime 分钟
	 * @return   
	 */
	public static String getEffectEndTimeText(Integer effectEndTime) {
		int time = effectEndTime == null ? 0 : effectEndTime.intValue();
		// 60m * 24h = 1 day
		int day = time / (60 * 24);
		int hour = (time / 60) % 24;
		int minute = time % 60;

		StringBuilder sb = new StringBuilder();
		if (day > 0) {
			sb.append(day + "天");
		}

		if (hour > 0) {
			sb.append(hour + "时");
		}

		if (minute > 0) {
			sb.append(minute + "分");
		}

		return sb.toString();
	}

	public static Date getDayTimeAtFirst(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		cd.set(Calendar.MILLISECOND, 0);
		return cd.getTime();
	}

	public static Date getDayTimeAtLast(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.set(Calendar.HOUR_OF_DAY, 23);
		cd.set(Calendar.MINUTE, 59);
		cd.set(Calendar.SECOND, 59);
		cd.set(Calendar.MILLISECOND, 0);
		return cd.getTime();
	}

	public static Date getNow() {
		return Calendar.getInstance().getTime();
	}

	public static String getShortDate() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	public static String getDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	public static long diffDays(Date one, Date two) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(one);
		calendar.set(calendar.get(1), calendar.get(2), calendar.get(5), 0, 0, 0);

		Date d1 = calendar.getTime();
		calendar.clear();
		calendar.setTime(two);
		calendar.set(calendar.get(1), calendar.get(2), calendar.get(5), 0, 0, 0);

		Date d2 = calendar.getTime();
		BigDecimal r = new BigDecimal(new Double(d1.getTime() - d2.getTime()).doubleValue() / 86400000.0D);
		return Math.round(r.doubleValue());
	}

	public static Date addDays(Date date, int days) {
		return add(date, days, 5);
	}

	public static Date addDays(int days) {
		return add(getNow(), days, 5);
	}

	public static int getDayOfWeek(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		return cd.get(7);
	}

	public static int getDayOfYear(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		return cd.get(6);
	}


	public static int getYear(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		return calendar.get(1);
	}

	public static int compareDate(Date startTime, Date endTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		try {

			c1.setTime(dateFormat.parse(dateFormat.format(startTime)));
			c2.setTime(dateFormat.parse(dateFormat.format(endTime)));

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return c1.compareTo(c2);

	}

	public static String formatyyyyMMddHHmmss(Date date) {
		SimpleDateFormat YMDHMS = new SimpleDateFormat("yyyyMMddHHmmss");
		return YMDHMS.format(date);
	}

	public static String formatyyyyMMdd(Date date) {
		String result = "";
		SimpleDateFormat dateFormatTODB = new SimpleDateFormat("yyyyMMdd");
		result = dateFormatTODB.format(date);
		return result;
	}

	public static String format(Date date, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	public static String formatDefault(Date date) {

        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
        return df.format(date);
    }

	/**
	 * 
	 * 方法用途: 传入时间字符串按所需格式返回时间<br>
	 * 实现步骤: <br>
	 * @param dateStr 时间字符串
	 * @param format 跟传入dateStr时间的格式必须一样 yyyy-MM-dd HH:mm:ss | yyyy年MM月dd日 HH时mm分ss秒
	 * @return
	 */
	public static Date format(String dateStr, String format) {
		if (StringUtils.isBlank(dateStr)) {
			return new Date();
		}
		if (StringUtils.isBlank(format)) {
			format = "yyyy-MM-dd";
		}
		Date date = null;
		DateFormat f = new SimpleDateFormat(format);
		try {
			date = f.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String formateDateToStr(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String str = dateFormat.format(date);
		return str;
	}

	public static String formateDateTimeToStr(Date date) {
		SimpleDateFormat dataTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = dataTimeFormat.format(date);
		return str;
	}

	public static String getHourMin(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		StringBuilder sf = new StringBuilder();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		sf.append(hour > 9 ? Integer.valueOf(hour) : new StringBuilder().append("0").append(hour).toString());
		sf.append(":");
		sf.append(min > 9 ? Integer.valueOf(min) : new StringBuilder().append("0").append(min).toString());
		return sf.toString();
	}

	public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        StringBuilder sf = new StringBuilder();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    public static Set<String> getNearDay(Date date, int days) {
	    Set<String> nearDays = new LinkedHashSet<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        nearDays.add(sdf.format(calendar.getTime()));
        for(int i=1; i<days; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            nearDays.add(sdf.format(calendar.getTime()));
        }
        return nearDays;
    }

    public static List<String> getNearDayRange(Date date, int days) {

        List<String> nearDays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        String endDate = sdf.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -days);
		nearDays.add(sdf.format(calendar.getTime()));
        nearDays.add(endDate);
        return nearDays;
    }

    public static List<String> getNearYearRange(Date date, int year) {

        List<String> nearDays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        String curDate = sdf.format(calendar.getTime());
        calendar.add(Calendar.YEAR, -year);
        nearDays.add(sdf.format(calendar.getTime()));
        nearDays.add(curDate);
        return nearDays;
    }

    /**
     *  获取整年范围, 例如参数2017, 返回[2017-01-01, 2017-12-31]
     * @param date
     * @param year
     * @return
     */
    public static List<String> getWholeYearRange(int year) {

        List<String> rangeList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        rangeList.add(sdf.format(calendar.getTime()));
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        rangeList.add(sdf.format(calendar.getTime()));

        return rangeList;
    }


    public static List<String> getSpecialMonthDayRange(Date date, int monthRange) {
	    List<String> dayRangeList = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DEFAULT_DATE_FORMAT);
        String endDate = sdf.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -monthRange);
        String startDate = sdf.format(calendar.getTime());
        dayRangeList.add(startDate);
        dayRangeList.add(endDate);
        return dayRangeList;
    }

    public static List<String> getSpecialYearDayRange(Date date, int yearRange) {
	    List<String> dayRangeList = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DEFAULT_DATE_FORMAT);
        String endDate = sdf.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -yearRange);
        String startDate = sdf.format(calendar.getTime());
        dayRangeList.add(startDate);
        dayRangeList.add(endDate);
        return dayRangeList;
    }



	public static String getHourMin(int minutes) {
		int minute = minutes % 60;
		int hour = minutes / 60;
		StringBuilder sf = new StringBuilder();
		sf.append(hour > 9 ? Integer.valueOf(hour) : new StringBuilder().append("0").append(hour).toString());
		sf.append(":");
		sf.append(minute > 9 ? Integer.valueOf(minute) : new StringBuilder().append("0").append(minute).toString());
		return sf.toString();
	}

	/**
	 * 
	 * 方法用途: 指定日期加上指定分钟<br>
	 * 实现步骤: <br>
	 * @param date 日期
	 * @param minute 分钟
	 * @return 返回相加后的日期
	 */
	public static Date addMinute(Date date, int minute) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, minute);
		return c.getTime();
	}
	
	/**
	 * 
	 * 方法用途: 指定日期加上指定分钟<br>
	 * 实现步骤: <br>
	 * @param date 日期
	 * @param minute 分钟
	 * @return 返回相加后的日期
	 */
	public static Date addSecond(Date date, int second) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, second);
		return c.getTime();
	}
	
	/**
	 * 
	 * 方法用途:获取当前日期 格式:2010-10-10 <br>
	 * 实现步骤: <br>
	 * @return
	 */
	public static String getCurrentDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	/**
	 * 
	 * 方法用途:解析数据库中的日期字符串 格式:yyyy-MM-dd <br>
	 * 实现步骤: <br>
	 * @param dateStr
	 * @return
	 */
	public static Date parseDate(String dateStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 
	 * 方法用途: 获取日期字符串所属于的星期<br>
	 * 实现步骤: <br>
	 * @param dateStr
	 * @return
	 */
	public static String getWeekFromDate(String dateStr) {
		Date date = parseDate(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		String weekStr = "星期";
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		switch(dayOfWeek){
			case 1 : 
				weekStr += "日";
				break;
			case 2 :
				weekStr += "一";
				break;
			case 3 : 
				weekStr += "二";
				break;
			case 4 :
				weekStr += "三";
				break;
			case 5 : 
				weekStr += "四";
				break;
			case 6 :
				weekStr += "五";
				break;
			case 7 :
				weekStr += "六";
				break;
				default:
					break;
		}
		return weekStr;
		
	}
	
	/**
	 * 
	 * 方法用途: 校验日期格式<br>
	 * 实现步骤: <br>
	 * @param date
	 * @return
	 */
	public static boolean checkDate(String date) {
		String rexp = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";
		Pattern p = Pattern.compile(rexp);
		Matcher m = p.matcher(date);
		boolean dateFlag = m.matches();
		return dateFlag;
	}
	
	public static void main(String[] args) {
		System.out.println(getWeekFromDate("2017-06-21"));
		System.out.println(("2017-06-21").substring(5, 10));
	}

}
