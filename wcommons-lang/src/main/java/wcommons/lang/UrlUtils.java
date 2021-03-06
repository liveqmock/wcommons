/**
 * 
 */
package wcommons.lang;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 4:50:35 PM Dec 11, 2013
 */
public class UrlUtils {

	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final char AND_CHAR = '&';
	public static final String AND_STR = "&";
	public static final char EQUAL_CHAR = '=';
	public static final String EQUAL_STR = "=";
	public static final char QUESTION_CHAR = '?';
	public static final String QUESTION_STR = "?";
	public static final char URL_CONNECT_SYMBOL_CHAR = '/';
	public static final String URL_CONNECT_SYMBOL_STR = "/";
	public static final char EXCLAMATION_CHAR = '!';
	public static final String EXCLAMATION_STR = "!";
	public static final char POINT_CHAR = '.';
	public static final String POINT_STR = ".";

	public static final String STRUTS2_ACTION_SUFFIX = ".action";
	public static final String STRUTS2_ACTION_PREFIX = "action=";
	public static final String STRUTS2_REGEX = "^([\\d\\D]*\\/)?[\\d\\D]+(\\![\\d\\D]+)?\\.action(\\?[\\d\\D]+)?$";

	/**
	 * @see #getStringParams(Map, String)
	 */
	public static String getStringParams(Map<String, Object> params) {
		return getStringParams(params, DEFAULT_CHARSET);
	}

	/**
	 * @see #getStringParams(Map, boolean, String)
	 */
	public static String getStringParams(Map<String, Object> params, String encodeCharset) {
		return getStringParams(params, true, encodeCharset);
	}

	/**
	 * @see #getStringParams(Map, boolean, String)
	 */
	public static String getStringParams(Map<String, Object> params, boolean encode) {
		return getStringParams(params, encode, DEFAULT_CHARSET);
	}

	/**
	 * <pre>
	 * 拼接http参数，参数之间用&符号分隔。
	 * 参数可能会编码，如果自定义编码出现异常，则使用默认编码。
	 * 
	 * UrlUtils.getStringParams(null)  =  ""
	 * UrlUtils.getStringParams({})  =  ""
	 * UrlUtils.getStringParams({a:b,c:1,d:null})  =  "a=b&c=1&d="
	 * </pre>
	 * 
	 * @param params
	 * @param encode 是否需要编码
	 * @param encodeCharset 编码格式
	 * @return
	 */
	public static String getStringParams(Map<String, Object> params, boolean encode, String encodeCharset) {
		if (params == null || params.isEmpty()) {
			return StringUtils.EMPTY;
		}
		final StringBuilder result = new StringBuilder();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (entry.getValue() == null) {
				append(result, entry.getKey(), entry.getValue(), encode, encodeCharset);
			}
			else if (entry.getValue() instanceof char[]) {
				for (Object v : (char[]) entry.getValue()) {
					append(result, entry.getKey(), v, encode, encodeCharset);
				}
			}
			else if (entry.getValue() instanceof byte[]) {
				for (Object v : (byte[]) entry.getValue()) {
					append(result, entry.getKey(), v, encode, encodeCharset);
				}
			}
			else if (entry.getValue() instanceof short[]) {
				for (Object v : (short[]) entry.getValue()) {
					append(result, entry.getKey(), v, encode, encodeCharset);
				}
			}
			else if (entry.getValue() instanceof int[]) {
				for (Object v : (int[]) entry.getValue()) {
					append(result, entry.getKey(), v, encode, encodeCharset);
				}
			}
			else if (entry.getValue() instanceof long[]) {
				for (Object v : (long[]) entry.getValue()) {
					append(result, entry.getKey(), v, encode, encodeCharset);
				}
			}
			else if (entry.getValue() instanceof float[]) {
				for (Object v : (float[]) entry.getValue()) {
					append(result, entry.getKey(), v, encode, encodeCharset);
				}
			}
			else if (entry.getValue() instanceof double[]) {
				for (Object v : (double[]) entry.getValue()) {
					append(result, entry.getKey(), v, encode, encodeCharset);
				}
			}
			else if (Collection.class.isAssignableFrom(entry.getValue().getClass())) {
				for (Object v : (Collection<?>) entry.getValue()) {
					append(result, entry.getKey(), v, encode, encodeCharset);
				}
			}
			else if (entry.getValue().getClass().isArray()) {
				for (Object v : (Object[]) entry.getValue()) {
					append(result, entry.getKey(), v, encode, encodeCharset);
				}
			}
			else {
				append(result, entry.getKey(), entry.getValue(), encode, encodeCharset);
			}
		}
		if (result.length() > 0) {
			result.deleteCharAt(0);
		}
		return result.toString();
	}

	private static void append(StringBuilder result, String name, Object value, boolean encode, String encodeCharset) {
		result.append(AND_CHAR).append(name).append(EQUAL_CHAR);
		if (value != null) {
			if (encode) {
				try {
					result.append(URLEncoder.encode(value.toString(), encodeCharset));
				}
				catch (UnsupportedEncodingException e) {
					result.append(value);
				}
			}
			else {
				result.append(value);
			}
		}
	}

	/**
	 * @see #spliceParams(String, String)
	 */
	public static String spliceParams(String url, Map<String, Object> params) {
		if (StringUtils.isBlank(url)) {
			return getStringParams(params);
		}
		if (params == null || params.isEmpty()) {
			return url;
		}
		return spliceParams(url, getStringParams(params));
	}

	/**
	 * <pre>
	 * UrlUtils.spliceParams("http://g.cn", "a=b&c=1")  =  "http://g.cn?a=b&c=1"
	 * UrlUtils.spliceParams("http://g.cn", "&a=b&c=1")  =  "http://g.cn?a=b&c=1"
	 * UrlUtils.spliceParams("http://g.cn?", "&a=b&c=1")  =  "http://g.cn?a=b&c=1"
	 * UrlUtils.spliceParams("http://g.cn?d=5", "&a=b&c=1")  =  "http://g.cn?d=5&a=b&c=1"
	 * </pre>
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String spliceParams(String url, String params) {
		final StringBuilder result = new StringBuilder();
		result.append(url);
		if (url.contains(QUESTION_STR)) {
			if (url.contains(AND_STR)) {
				if (url.endsWith(AND_STR)) {
					if (params.startsWith(AND_STR)) {
						result.append(params.substring(1));
					}
					else {
						result.append(params);
					}
				}
				else {
					if (params.startsWith(AND_STR)) {
						result.append(params);
					}
					else {
						result.append(AND_STR).append(params);
					}
				}
			}
			else {
				if (url.endsWith(QUESTION_STR)) {
					if (params.startsWith(AND_STR)) {
						result.append(params.substring(1));
					}
					else {
						result.append(params);
					}
				}
				else {
					if (params.startsWith(AND_STR)) {
						result.append(params);
					}
					else {
						result.append(AND_STR).append(params);
					}
				}
			}
		}
		else {
			result.append(QUESTION_STR);
			if (params.startsWith(AND_STR)) {
				result.append(params.substring(1));
			}
			else {
				result.append(params);
			}
		}

		return result.toString();
	}

	/**
	 * <pre>
	 * UrlUtils.spliceParams("http://g.cn", "/gmail/", "/login.do")  =  "http://g.cn/gmail/login.do"
	 * UrlUtils.spliceParams("http://g.cn", "gmail", null, "login.do")  =  "http://g.cn/gmail/login.do"
	 * </pre>
	 * 
	 * @param url
	 * @param urls
	 * @return
	 */
	public static String spliceUrls(String url, String... urls) {
		url = StringUtils.trimToEmpty(url);

		final StringBuilder result = new StringBuilder();

		result.append(url);

		if (ArrayUtils.isEmpty(urls)) {
			return result.toString();
		}

		String text = url;
		for (String str : urls) {
			if (StringUtils.isBlank(str)) {
				continue;
			}
			final boolean a = text.endsWith(URL_CONNECT_SYMBOL_STR);
			final boolean b = str.startsWith(URL_CONNECT_SYMBOL_STR);
			if (a & b) {
				result.append(str.substring(1));
			}
			else if (a || b) {
				result.append(str);
			}
			else {
				result.append(URL_CONNECT_SYMBOL_STR).append(str);
			}

			text = str;
		}

		return result.toString();
	}

	/**
	 * <pre>
	 * 拆分URL与参数
	 * 如果参数不规范，会解析错误
	 * UrlUtils.splitUrlParams(null) = new String[]{"", ""}
	 * UrlUtils.splitUrlParams("") = new String[]{"", ""}
	 * UrlUtils.splitUrlParams("/test.do") = new String[]{"/test.do", ""}
	 * UrlUtils.splitUrlParams("/test.do?action=list") = new String[]{"/test.do", "action=list"}
	 * UrlUtils.splitUrlParams("/test.action") = new String[]{"/test.action", ""}
	 * UrlUtils.splitUrlParams("/test.action?action=list") = new String[]{"/test.action", "action=list"}
	 * UrlUtils.splitUrlParams("/test!list.action") = new String[]{"/test.action", "action=list"}
	 * UrlUtils.splitUrlParams("/test!list.action?search=g.cn") = new String[]{"/test.action", "action=list&search=g.cn"}
	 * </pre>
	 * 
	 * @param url
	 * @return
	 */
	public static String[] splitUrlParams(String url) {
		if (StringUtils.isBlank(url)) {
			return new String[] { StringUtils.EMPTY, StringUtils.EMPTY };
		}

		// struts2
		if (url.matches(STRUTS2_REGEX)) {
			// 问号的位置
			final int qIdx = url.indexOf(QUESTION_CHAR);
			// 没有问号，说明没有参数
			// /test!list.action
			if (qIdx < 0) {
				// 惊叹号的位置
				final int eIdx = url.indexOf(EXCLAMATION_CHAR);
				// 没有惊叹号
				// /test.action
				if (eIdx < 0) {
					return new String[] { url, StringUtils.EMPTY };
				}
				// 有惊叹号
				// /test!list.action
				// .action 的位置
				final int sIdx = url.lastIndexOf(STRUTS2_ACTION_SUFFIX);
				final String action = url.substring(eIdx + 1, sIdx);

				return new String[] { url.substring(0, eIdx) + STRUTS2_ACTION_SUFFIX, STRUTS2_ACTION_PREFIX + action };
			}
			else {
				// .action 的位置
				final int sIdx = url.lastIndexOf(STRUTS2_ACTION_SUFFIX, qIdx);
				// 惊叹号的位置
				final int eIdx = url.lastIndexOf(EXCLAMATION_CHAR, sIdx);
				// 没有惊叹号
				// /test.action?search=g.cn
				if (eIdx < 0) {
					return new String[] { url.substring(0, qIdx), url.substring(qIdx + 1) };
				}

				// 有惊叹号
				// /test!list.action?search=g.cn
				final String action = url.substring(eIdx + 1, sIdx);

				return new String[] { url.substring(0, eIdx) + STRUTS2_ACTION_SUFFIX,
						STRUTS2_ACTION_PREFIX + action + AND_CHAR + url.substring(qIdx + 1) };
			}
		}

		final int index = url.indexOf(QUESTION_CHAR);
		if (index < 0) {
			return new String[] { url.trim(), StringUtils.EMPTY };
		}

		return new String[] { url.substring(0, index).trim(), url.substring(index + 1).trim() };
	}

	/**
	 * <pre>
	 * URL参数转换成Map
	 * 如果参数不规范，会解析错误
	 * UrlUtils.getListParamMap(null) = {}
	 * UrlUtils.getListParamMap("") = {}
	 * UrlUtils.getListParamMap("a=b&c=1&c=2&d=&e=&e=") = {a:["b"],c:["1","2"],d:[""],e:["",""]}
	 * </pre>
	 * 
	 * @param params
	 * @return
	 */
	public static Map<String, List<String>> getListParamMap(String params) {
		final Map<String, List<String>> result = new HashMap<String, List<String>>();

		if (StringUtils.isBlank(params)) {
			return result;
		}

		for (String param : params.split(AND_STR)) {
			final int index = param.indexOf(EQUAL_CHAR);
			if (index < 0) {
				continue;
			}
			final String key = param.substring(0, index);
			final String value = param.substring(index + 1);

			if (!result.containsKey(key)) {
				result.put(key, new ArrayList<String>());
			}

			result.get(key).add(value);
		}

		return result;
	}

	/**
	 * <pre>
	 * URL参数转换成Map
	 * 如果参数不规范，会解析错误
	 * UrlUtils.getStringParamMap(null) = {}
	 * UrlUtils.getStringParamMap("") = {}
	 * UrlUtils.getStringParamMap("a=b&c=1&c=2&d=&e=&e=") = {a:"b",c:"2",d:"",e:""}
	 * </pre>
	 * 
	 * @param params
	 * @return
	 */
	public static Map<String, String> getStringParamMap(String params) {
		final Map<String, String> result = new HashMap<String, String>();

		if (StringUtils.isBlank(params)) {
			return result;
		}

		for (String param : params.split(AND_STR)) {
			final int index = param.indexOf(EQUAL_CHAR);
			if (index < 0) {
				continue;
			}
			final String key = param.substring(0, index);
			final String value = param.substring(index + 1);

			result.put(key, value);
		}

		return result;
	}
}
