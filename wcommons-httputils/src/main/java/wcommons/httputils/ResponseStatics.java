/**
 * 
 */
package wcommons.httputils;

import wcommons.lang.SystemUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:18:03 AM Nov 25, 2013
 */
public class ResponseStatics {
	// 编码
	public static final String CHARSET = SystemUtils.getSystemProperty(
			"wcommons.httputils.response.charset", "UTF-8");

}