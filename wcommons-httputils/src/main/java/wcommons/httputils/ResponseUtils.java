/**
 * 
 */
package wcommons.httputils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:18:10 AM Nov 25, 2013
 */
public class ResponseUtils {

	private static final Log LOG = LogFactory.getLog(ResponseUtils.class);

	public static final String TEXT_CONTENT_TYPE = "text/plain";
	public static final String JSON_CONTENT_TYPE = "application/json";
	public static final String XML_CONTENT_TYPE = "text/xml";
	public static final String HTML_CONTENT_TYPE = "text/html";

	/**
	 * 默认的contentType
	 */
	public static final String DEFAULT_CONTENT_TYPE = "text/plain;charset="
			+ ResponseStatics.CHARSET;

	/**
	 * 设置header
	 * 
	 * @see javax.servlet.http.HttpServletResponse.setHeader
	 * @param resp
	 * @param headers
	 */
	public static void setHeader(HttpServletResponse resp, String... headers) {
		if (ArrayUtils.isEmpty(headers)) {
			return;
		}

		for (String header : headers) {
			final String[] arr = header.split(":");
			if (arr.length == 0) {
				LOG.warn("ignore invalid header setting: " + header);
			}
			resp.setHeader(arr[0], arr[1]);
		}
	}

	private static void initBeforeRender(HttpServletResponse resp, String contentType,
			String... headers) {
		if (StringUtils.isBlank(contentType)) {
			contentType = DEFAULT_CONTENT_TYPE;
		}
		else if (!contentType.contains(";charset=")) {
			contentType += ";charset=" + ResponseStatics.CHARSET;
		}

		// 设置header
		setHeader(resp, headers);
		// 设置contentType
		resp.setContentType(contentType);
	}

	/**
	 * 直接输出内容
	 * 
	 * <pre>
	 * render("text/plain", "hello", "encoding:GBK");
	 * render("text/plain", "hello", "no-cache:false");
	 * render("text/plain", "hello", "encoding:GBK", "no-cache:false");
	 * </pre>
	 * 
	 * @param resp
	 * @param contentType
	 * @param content
	 * @param headers
	 */
	public static void render(HttpServletResponse resp, String contentType, String content,
			String... headers) {
		try {
			// 初始化
			initBeforeRender(resp, contentType, headers);

			resp.getWriter().write(content);
			resp.getWriter().flush();
		}
		catch (IOException e) {
			throw new ResponseRenderException(e.getMessage(), e);
		}
	}

	/**
	 * 输出文本
	 * 
	 * @param resp
	 * @param text
	 * @param headers
	 */
	public static void renderText(HttpServletResponse resp, String text, String... headers) {
		render(resp, TEXT_CONTENT_TYPE, text, headers);
	}

	/**
	 * 输出html
	 * 
	 * @param resp
	 * @param html
	 * @param headers
	 */
	public static void renderHtml(HttpServletResponse resp, String html, String... headers) {
		render(resp, HTML_CONTENT_TYPE, html, headers);
	}

	/**
	 * 输出xml
	 * 
	 * @param resp
	 * @param xml
	 * @param headers
	 */
	public static void renderXml(HttpServletResponse resp, String xml, String... headers) {
		render(resp, XML_CONTENT_TYPE, xml, headers);
	}

	/**
	 * 输出json
	 * 
	 * @param resp
	 * @param jsonString json字符串.
	 * @param headers
	 */
	public static void renderJson(HttpServletResponse resp, String jsonString, String... headers) {
		render(resp, JSON_CONTENT_TYPE, jsonString, headers);
	}
}
