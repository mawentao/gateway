package org.gateway.util;

import org.joda.time.DateTime;

/**
 * 时间日期工具类
 * @author admin
 * @since 2019-06-11 15:49
 *
 */

public class DateTimeUtil {

	/**
	 * @return 当前时间
	 */
	public static String getNowDateTime() {
		return DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * @return 当前时间戳
	 */
	public static long getTimestamp() {
		return System.currentTimeMillis()/1000;
	}
}
