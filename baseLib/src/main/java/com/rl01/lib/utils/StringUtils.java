package com.rl01.lib.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static final String NEW_LINE = "\n";// 换行符
	public static final String NEW_LINE_MARK = "#@n#";// 换行标示
	public static final String NEW_HTML_LINE = "<br/>";

	public static String generateTime(long time) {
		int totalSeconds = (int) (time / 1000);
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes,
				seconds) : String.format("%02d:%02d", minutes, seconds);
	}

	public static boolean isNull(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	public static boolean notNull(String str) {
		if (str == null || "".equals(str.trim())) {
			return false;
		}
		return true;
	}

	public static String nullToString(String str) {
		if (str == null || "".equals(str.trim())/* ||"null".equals(str.trim()) */) {
			return "";
		}
		return str.trim();
	}

	public static String nullToString(Object str) {
		if (str == null) {
			return "";
		}
		return str.toString();
	}

	public static int stringToInt(String str) {
		int tmp = 0;
		if (!isNull(str)) {
			try {
				tmp = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				logger.w(e.getMessage());
			}
		}
		return tmp;
	}

	public static String replaseToNewLine(String str) {
		str = nullToString(str);
		String newStr = str.replaceAll(NEW_LINE_MARK, NEW_LINE);
		return newStr;
	}

	/**
	 * 换行
	 * 
	 * @param line
	 *            行数
	 * @return
	 */
	public static String getNewLine(int line) {
		StringBuffer newStr = new StringBuffer();
		for (int i = 0; i < line; i++) {
			newStr.append(NEW_LINE);
		}
		return newStr.toString();
	}

	/**
	 * 换行
	 * 
	 * @param line
	 *            行数
	 * @return
	 */
	public static String getNewHtmlLine(int line) {
		StringBuffer newStr = new StringBuffer();
		for (int i = 0; i < line; i++) {
			newStr.append(NEW_HTML_LINE);
		}
		return newStr.toString();
	}

	public static int[] getHourMin(String timeStr) {
		String hour = timeStr.substring(0, timeStr.indexOf(":"));
		String min = timeStr.substring(timeStr.indexOf(":") + 1);
		int h = Integer.parseInt(hour);
		int m = Integer.parseInt(min);
		return new int[] { h, m };
	}

	/**
	 * 编码转换
	 * 
	 * @return
	 */
	public static String getDecodeText(String Url) {
		String StrUrl = Url;
		// try {
		// StrUrl = URLDecoder.decode(StrUrl, "UTF-8");
		// } catch (Exception e) {
		// logger.e("decode---error---error");
		// }
		return StrUrl;
	}

	public static String getEncodeText(String Url) {
		String StrUrl = Url;
		// try {
		// StrUrl = URLEncoder.encode(StrUrl, "UTF-8");
		// } catch (Exception e) {
		// logger.e("decode---error---error");
		// }
		return StrUrl;
	}

	public static boolean isUrl(String text) {
		if (StringUtils.isNull(text)) {
			return false;
		} else {
			String reString = "^((https|http|ftp|rtsp|mms)?://)"
					+ "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" // ftp的user@
					+ "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 221.2.162.15
					+ "|" // 允许IP和DOMAIN（域名）
					+ "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
					+ "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
					+ "[a-z]{2,6})" // first level domain- .com or .museum
					+ "(:[0-9]{1,4})?" // 端口- :80
					+ "((/?)|" // a slash isn't required if there is no file
								// name
					+ "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
			Pattern pattern = Pattern.compile(reString);
			Matcher matcher = pattern.matcher(text);
			return matcher.matches();
		}
	}

	public static boolean isEmail(String strEmail) {
		String strPattern = "^\\b([a-zA-Z0-9%_.+\\-]+)@([a-zA-Z0-9.\\-]+?\\.[a-zA-Z]{2,6})\\b$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}
	
	/**
	 * 字母，数字，下划线
	 * @param password
	 * @return
	 */
	public static boolean isPassword2(String password){
//		String strPattern = "^[a-zA-Z\\d_]{4,20}$";
		String strPattern = "^[a-zA-Z\\d]{4,20}$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(password);
		return m.matches();
	}
	
	public static boolean isPhone(String phone){
		String strPattern = "^(13[0-9]|14[0|1|2|3|4|5|6|7|8|9]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|4|5|6|7|8|9]|17[0-9])\\d{8}$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(phone);
		return m.matches();
	}
	
	public static boolean isPeopleName(String name){
		if(isNull(name)){
			return false;
		}else{
			return true;
		}
//		String strPattern = "^(13[0-9]|14[0-9]|15[0-9]|18[0-9]|17[0-9])\\d{8}$";
//		Pattern p = Pattern.compile(strPattern);
//		Matcher m = p.matcher(name);
//		return m.matches();
	}
	
	/**
	 * 不能输入中文
	 * @param password
	 * @return
	 */
	public static boolean isTopic(String topic){
		String strPattern = ".*[\u4e00-\u9fa5].*";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(topic);
		return m.matches();
	}
	
	/**
	 * 字母，数字，下划线
	 * @param password
	 * @return
	 */
	public static boolean isPasswordLength(String password){
		if(password!=null && password.length()>3 && password.length()<21){
			return true;
		}
		return false;
	}
	
	/**
	 * 字母，数字，下划线
	 * @param lastName
	 * @return
	 */
	public static boolean isName(String name){
		String strPattern = "^[a-zA-Z0-9]{1,50}";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(name);
		return m.matches();
	}
	
	public static String getStringMD5(String data) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte buffer[] = data.getBytes();
			digest.update(buffer);
			BigInteger bigInt = new BigInteger(1, digest.digest());
			return bigInt.toString(16);
		} catch (Exception e) {
			logger.e(e);
		}
		return null;
	}
	
	public static final String createStatus(){
		String temp = PreferUtils.getInstance().getToken() + 
				"@@"+ PreferUtils.getInstance().getUserId() + 
				"$$" + DeviceUtils.sn();
		return getStringMD5(temp);
	}
}
