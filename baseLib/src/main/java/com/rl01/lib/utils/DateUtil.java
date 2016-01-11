package com.rl01.lib.utils;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


public class DateUtil {
	
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String DATE_TIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.S";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_YYYYMMDD_PATTERN = "yyyyMMdd";
    public static final String TIME_HHMM_PATTERN = "HH:mm";
    public static final String TIME_HHMM_PATTERN2 = "HHmm";
    public static final String DATE_TIME_NO_HORI_PATTERN = "yyyyMMdd HH:mm:ss";
    public static final String DATE_TIME_NO_SPACE_PATTERN = "yyyyMMddHHmmss";
    public static final String DATE_TIME_MILLISECOND_PATTERN = "yyyyMMddHHmmssS";
    public static final String DATE_TIME_PLAYBILL_PATTERN = "yyyyMMdd HH:mm";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_ENGLISH_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
    public static final String DATE_DAY_NO_SPACE_PATTERN = "MMdd";
    
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat(DATE_TIME_MS_PATTERN);
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
    public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat(DATE_YYYYMMDD_PATTERN);
    public static final SimpleDateFormat HHmm = new SimpleDateFormat(TIME_HHMM_PATTERN);
    public static final SimpleDateFormat HHmm2 = new SimpleDateFormat(TIME_HHMM_PATTERN2);
    public static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat(DATE_TIME_NO_HORI_PATTERN);
    public static final SimpleDateFormat yyyyMMddHHmmssFile = new SimpleDateFormat(DATE_TIME_NO_SPACE_PATTERN);
    public static final SimpleDateFormat PLAYBILL_TIME_PATTERN = new SimpleDateFormat(DATE_TIME_PLAYBILL_PATTERN);
    public static final SimpleDateFormat ENGLISH_SDF = new SimpleDateFormat(DATE_ENGLISH_FORMAT,Locale.ENGLISH);
    public static final SimpleDateFormat MMDD = new SimpleDateFormat(DATE_DAY_NO_SPACE_PATTERN);
    
    private static Map<String, SimpleDateFormat> patternFormatMap;
    
    /**
     * Checkstyle rule: utility classes should not have public constructor
     */
    private DateUtil() {
    }
    
    public static Map<String, SimpleDateFormat> getInstance(){
    	if (patternFormatMap == null) {
    		patternFormatMap = new HashMap<String, SimpleDateFormat>();
    		patternFormatMap.put(DATE_TIME_MS_PATTERN, timeFormat);
    		patternFormatMap.put(DATE_TIME_PATTERN, dateFormat);
    		patternFormatMap.put(DATE_YYYYMMDD_PATTERN, yyyyMMdd);
    		patternFormatMap.put(TIME_HHMM_PATTERN, HHmm);
    		patternFormatMap.put(TIME_HHMM_PATTERN2, HHmm2);
    		patternFormatMap.put(DATE_TIME_NO_HORI_PATTERN, yyyyMMddHHmmss);
    		patternFormatMap.put(DATE_TIME_NO_SPACE_PATTERN, yyyyMMddHHmmssFile);
    		patternFormatMap.put(DATE_TIME_PLAYBILL_PATTERN, PLAYBILL_TIME_PATTERN);
    		patternFormatMap.put(DATE_ENGLISH_FORMAT, ENGLISH_SDF);
    	}
    	return patternFormatMap;
    } 
   
    public static String formatDate(String pattern, Date adate) {
    	SimpleDateFormat sdf = DateUtil.getInstance().get(pattern);
		if (sdf == null) {
    		sdf = new SimpleDateFormat(pattern);
    		DateUtil.getInstance().put(pattern, sdf);
    	}
    	return sdf.format(adate);
    } 
    
    public static Date parseDate(String pattern, String dateStr){
    	SimpleDateFormat sdf = DateUtil.getInstance().get(pattern);
		if (sdf == null) {
    		sdf = new SimpleDateFormat(pattern);
    		DateUtil.getInstance().put(pattern, sdf);
    	}
		
    	try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
    } 
    
	/**
	 * 计算时间的起始时间
	 * */
	private final static String BASIC_DATE = "2000-01-01 00:00:00";
    
    /**
     * 把日期字符串yyyy-MM-dd HH:mm:ss转换成HH:mm形式
     */
    public static String strToString(String date){
    	if(date == null || "".equals(date)){
    		return date;
    	}
    	String temp="";
    	try{
    		Date dateStr = dateFormat.parse(date);
    		temp = HHmm.format(dateStr);
    	}catch(Exception ex){
    		logger.d(ex.getStackTrace());
    	}
    	return temp;
    }
    
    public static String dateToString(Date date){
    	SimpleDateFormat df;
        String returnValue = "";
        if (date != null) {
            df = new SimpleDateFormat(DATE_FORMAT);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * This method generates a string representation of a date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @see java.text.SimpleDateFormat
     * @throws ParseException when String doesn't match the expected format
     */
    public static Date convertStringToDate(String aMask, String strDate)
      throws ParseException {
        Date date;
        SimpleDateFormat df = new SimpleDateFormat(aMask);
//        if (logger.isDebugEnabled()) {
            logger.d("converting '" + strDate + "' to date with mask '" + aMask + "'");
//        }

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            //logger.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }

    /**
     * This method returns the current date time in the format:
     * MM/dd/yyyy HH:MM a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(TIME_PATTERN, theTime);
    }

    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * 
     * @see java.text.SimpleDateFormat
     */
    public static String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            logger.e("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    public static java.sql.Date convertDateToSqlDate(Date date){
    	return new java.sql.Date(date.getTime());
	}
    
    public static java.sql.Timestamp convertDateToTimestamp(Date date){
    	return new java.sql.Timestamp(date.getTime());
	}
    
    
    public static String getNowTime(Date date) {
    	if (date==null){
    		return "";
    	}
		return timeFormat.format(date);
	}
    
    public static String getDateTime(String sdate) {
    	try{
    	java.sql.Timestamp date =stringToTimestamp(sdate);
    		return dateFormat.format(date);
    	}catch(Exception e){
    		return sdate;
    	}
	}
    
    public static java.sql.Timestamp stringToTimestamp(String timestampStr) {  
    	if (timestampStr == null || timestampStr.length() < 1)  
    	return null;  
    	return java.sql.Timestamp.valueOf(timestampStr);  
    }  
    /**
     *根据日期计算出所在周的日期，并返回大小为7的数组 
     * @param date
     * @return
     */
    public static  String[] getWholeWeekByDate(Date date){
		String[] ss = new String[7];
		Calendar calendar = Calendar.getInstance();
		for (int i=0,j=2;i<6&&j<8;i++,j++){
		     calendar.setTime(date);
		     calendar.setFirstDayOfWeek(Calendar.MONDAY); 
		     calendar.set(Calendar.DAY_OF_WEEK, j);
		     ss[i] =  getFormatDate(calendar.getTime());
		}
	    calendar.setTime(date);
	    calendar.setFirstDayOfWeek(Calendar.MONDAY); 
	    calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6); 
		ss[6]= getFormatDate(calendar.getTime());
		return ss;
	}
    
    /**
     * 返回格式 yyyyMMdd的日期格式
     * @param d
     * @return
     */
    public static String getFormatDate(Date d) {
    	return yyyyMMdd.format(d);
    }
    public static String getHHmm2(Date d) {
    	return HHmm2.format(d);
    }
    
    public static Date getDateByString(String pattern) throws ParseException {
    	return yyyyMMdd.parse(pattern);
    }
   
    public static Date getPlayBillTimeByPattern(String date) throws ParseException {
    	return PLAYBILL_TIME_PATTERN.parse(date);
    }
    
	/**
	 * @return 当前标准日期yyyy-MM-dd HH:mm:ss
	 */
	public static String getNowTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String nowTime = df.format(date);
		return nowTime;
	}
	
	/**
	 * @return 当前标准日期yyyyMMddHHmmss
	 */
	public static String getNowTimeNumber() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String nowTime = df.format(date);
		return nowTime;
	}
	
	/**
	 * 获取从2000年1月1日 00:00:00开始到指定日期的秒数
	 * 
	 * @param 日期
	 * @return long
	 */
	public static Long getSeconds(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		Date basicDate = formatter.parse(BASIC_DATE, new ParsePosition(0));
		long secLong = (date.getTime() - basicDate.getTime()) / 1000L;
		return secLong;
	}

	/**
	 * 获取从2000年1月1日 00:00:00开始到指定日期的秒数
	 * 
	 * @param 日期
	 * @param 日期格式
	 *            例如：yyyy-MM-dd HH:mm:ss
	 * @return long
	 */
	public static Long getSeconds(String dateStr, String df) {
		if (dateStr == null || "".equals(dateStr)) {
			return null;
		}
		if (df == null || "".equals(df)) {
			df = DATE_FORMAT;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(df);
		Date date = formatter.parse(dateStr, new ParsePosition(0));
		return getSeconds(date);
	}
	
	/**
     * 返回格式 yyyyMMdd的日期格式
     * @param d
     * @return
     */
    public static Date getDateByStringyyyyMMddHHmmss(String pattern) throws ParseException {
    	return yyyyMMddHHmmssFile.parse(pattern);
    }
    
    public static Date getFormatDateByEnglishSDF(String s) {
    	try {
			return ENGLISH_SDF.parse(s);
		} catch (ParseException e) {
			logger.e(e);
			return null;
		}
    }
    
    public static String getFormatDateByyyyyMMddHHmmssFile(Date d) {
    	return yyyyMMddHHmmssFile.format(d);
    }
    
    public static String formateYYStrDate(String d) {
		Date formateDate = null;
		try {
			formateDate = yyyyMMdd.parse(d);
//			String dateStr = getMessageFormatDateBy(formateDate);
			return yyyyMMdd.format(formateDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    
    public static void main(String[] a){
    	//DateUtil da = new DateUtil();
    	 //da.parseDate(DATE_TIME_PATTERN,"Mon Apr 11 18:09:36 CST 2011");
    	String curTime = new Timestamp(System.currentTimeMillis()).toString();
    	System.out.println(curTime);
    }
    
    public static String formatLongToTimeStr(long currentTime,long time){
    	String result = "";
    	long current = currentTime - time;
    	if(current > 1000*60*60*24){
    		result = current/1000/60/60/24+"days";
    	}else if(current > 1000*60*60){
    		result = current/60/60/1000+"hours";
    	}else if(current > 1000*60){
    		result = current/60/1000+"mins";
    	}else {
    		result = "1mins";
    	}
    	return result;
    }
 	
 	
	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int daysBetween(Calendar date1, Calendar date2) {
		long time1 = date1.getTimeInMillis();
		long time2 = date2.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}
	
	/**
	 * 获取服务器当前时间
	 * 
	 * @return 返回服务器当前时间
	 */
	public static long getCurrentTimeToLong() {
		Date date = new Date();
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		long dateLong = date.getTime() + com.rl01.lib.utils.UIUtils.getTimeDuration();
		return dateLong;
	}
	/**
	 * 当前客户端时间 yyyyMMddHHmmssS格式
	 * @param time
	 * @return
	 */
	public static String getDateToString() {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyyMMddHHmmssS");
		String ctime = formatter.format(new Date());
		return ctime;
	}

	/**
	 * 获取服务器当前时间
	 * 
	 * @return 返回服务器当前时间 默认格式"yyyyMMddHHmmss"
	 */
	public static String getCurrentTimeToString() {
		return getCurrentTimeToString(DateUtil.DATE_TIME_NO_SPACE_PATTERN);
	}

	/**
	 * 获取服务器指定格式的当前时间
	 * 
	 * @return 返回服务器指定格式的当前时间
	 */
	public static String getCurrentTimeToString(String pattern) {
		Date date = new Date();
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		long dateLong = date.getTime() + com.rl01.lib.utils.UIUtils.getTimeDuration();
		String currentTime = DateUtil.formatDate(pattern, new Date(dateLong));
		return currentTime;
	}
	
	public static String dateToString2(Date date) {
		SimpleDateFormat df;
		String returnValue = "";
		if (date != null) {
			df = new SimpleDateFormat(DATE_TIME_NO_SPACE_PATTERN);
			returnValue = df.format(date);
		}
		return (returnValue);
	}
}
