package com.bob.boot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间处理工具类
 * 
 * @author bob
 *
 */
public class DateTimeUtil {

	private final static String ALL = "yyyy-MM-dd HH:mm:ss";
	private final static String MONTH = "yyyy-MM";
	private final static String DAY = "yyyy-MM-dd";
	private final static String YEAR_DAY = "yyyyMMdd";

//    public static void main(String[] args) {
////        String source = "2010-1-1 12:00:18";
////        Date date = parse(source);
////        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        System.out.println(String.format("%s", date == null ? "失败" : sdf.format(date)));
//    	System.out.println(formatYearDay(new Date()));
//    }

	public static long getDaysInter(String start, String end) {
		Date starts = null;
		Date ends = null;
		try {
			starts = new SimpleDateFormat(DAY).parse(start);
			ends = new SimpleDateFormat(DAY).parse(end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 获取相减后天�?
		return (ends.getTime() - starts.getTime()) / (24 * 60 * 60 * 1000);
	}

	/**
	 * 日期转为字符�?
	 * 
	 * @param date
	 * @return
	 */
	public static String formatAll(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(ALL);
		return sdf.format(date);
	}

	/**
	 * 日期转为字符�?
	 * 
	 * @param date
	 * @return
	 */
	public static String formatMonth(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(MONTH);
		return sdf.format(date);
	}

	/**
	 * 日期转为字符�?
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDay(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DAY);
		return sdf.format(date);
	}

	/**
	 * 日期转为字符�?
	 * 
	 * @param date
	 * @return
	 */
	public static String formatYearDay(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(YEAR_DAY);
		return sdf.format(date);
	}

	/**
	 * 字符串转换为日期时间
	 * 
	 * @param source
	 * @return
	 */
	public static Date parseAll(String source) {
		Date rtn = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(ALL);
			rtn = sdf.parse(source);
		} catch (Exception e) {

		}
		return rtn;
	}

	/**
	 * Date类型时间增加day天后的Date对象
	 * 
	 * @param date
	 * @return date
	 */
	public static Date addDays(Date date, int day) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day); // 把日期往后增加一�?,整数 �?后推,负数�?前移�?
		Date dates = calendar.getTime(); // 这个时间就是日期�?后推�?天的结果
		return dates;
	}

	/**
	 * 字符串转换为日期时间
	 * 
	 * @param source
	 * @return
	 */
	public static Date parseMonth(String source) {
		Date rtn = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(MONTH);
			rtn = sdf.parse(source);
		} catch (Exception e) {

		}
		return rtn;
	}

	/**
	 * 字符串转换为日期时间
	 * 
	 * @param source
	 * @return
	 */
	public static Date parseDay(String source) {
		Date rtn = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DAY);
			rtn = sdf.parse(source);
		} catch (Exception e) {

		}
		return rtn;
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss�?
	 */
//	public static String getDateTime() {
//		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
//	}

	/**
	 * 得到日期字符�? 默认格式（yyyy-MM-dd�? pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
//	public static String formatDate(Date date, Object... pattern) {
//		String formatDate = null;
//		if (pattern != null && pattern.length > 0) {
//			formatDate = DateFormatUtils.format(date, pattern[0].toString());
//		} else {
//			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
//		}
//		return formatDate;
//	}

	/**
	 * 将毫秒级别的时间戳转换成日期格式
	 * 
	 * @param time
	 * @return
	 */
	public static String longToTime(long timeLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeStr = format.format(new Date(timeLong));
		return timeStr;
	}
}