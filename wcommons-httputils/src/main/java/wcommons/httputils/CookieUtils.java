/**
 * 
 */
package wcommons.httputils;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:03:19 AM Nov 25, 2013
 */
public class CookieUtils {

	/**
	 * 获取cookie值
	 * 
	 * @param req
	 * @param name
	 * @return
	 */
	public static String get(HttpServletRequest req, String name) {
		final Cookie[] cookies = req.getCookies();
		if (ArrayUtils.isEmpty(cookies)) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

	/**
	 * 清除cookie
	 * 
	 * @param req
	 * @param resp
	 * @param name
	 */
	public static boolean clear(HttpServletRequest req, HttpServletResponse resp, String name) {
		final Cookie[] cookies = req.getCookies();
		if (ArrayUtils.isEmpty(cookies)) {
			return false;
		}
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				cookie.setMaxAge(0);
				cookie.setValue("");
				resp.addCookie(cookie);
				return true;
			}
		}
		return false;
	}

	/**
	 * 添加cookie
	 * 
	 * @param resp
	 * @param name
	 * @param value
	 * @param expiry
	 */
	public static void add(HttpServletResponse resp, String name, String value, int expiry) {
		add(resp, name, value, expiry, TimeUnit.SECONDS);
	}

	/**
	 * 添加cookie
	 * 
	 * @param resp
	 * @param name
	 * @param value
	 * @param expiry
	 * @param timeUnit
	 */
	public static void add(HttpServletResponse resp, String name, String value, int expiry,
			TimeUnit timeUnit) {
		final Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge((int) timeUnit.convert(expiry, timeUnit));
		resp.addCookie(cookie);
	}
}
