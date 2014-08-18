package wcommons.httputils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import wcommons.lang.ObjectUtils;
import wcommons.lang.UrlUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:55:35 AM May 7, 2014
 */
public class HttpHelper {

	protected final Log log = LogFactory.getLog(getClass());

	private final HttpClientFactory factory;

	private final HttpClient client;

	private boolean newHttpClientEveryRequest = true;

	public HttpHelper() {
		this.factory = new HttpClientFactory();
		this.client = this.factory.create();
	}

	public HttpHelper(HttpClientFactory factory) {
		this.factory = factory;
		this.client = this.factory.create();
	}

	public boolean isNewHttpClientEveryRequest() {
		return newHttpClientEveryRequest;
	}

	public void setNewHttpClientEveryRequest(boolean newHttpClientEveryRequest) {
		this.newHttpClientEveryRequest = newHttpClientEveryRequest;
	}

	HttpClient httpClient() {
		return isNewHttpClientEveryRequest() ? factory.create() : this.client;
	}

	HttpRequestBase addHeaders(HttpRequestBase req, Collection<Header> headers) {
		if (headers != null && !headers.isEmpty()) {
			for (final Header header : headers) {
				if (header == null) {
					continue;
				}
				req.addHeader(header);
			}
		}
		return req;
	}

	/**
	 * <pre>
	 * post 请求
	 * </pre>
	 * 
	 * @see #post(String, Map)
	 * @param url
	 * @return
	 */
	public byte[] post(String url) {
		return post(url, null);
	}

	/**
	 * <pre>
	 * 超时时间默认
	 * </pre>
	 * 
	 * @see #post(String, Map, long)
	 * @param url
	 * @param params
	 * @return
	 */
	public byte[] post(String url, Map<String, Object> params) {
		return post(url, params, 0);
	}

	/**
	 * <pre>
	 * 不设置 header
	 * </pre>
	 * 
	 * @see #post(String, Map, long, Collection)
	 * @param url
	 * @param params
	 * @param timeout
	 * @return
	 */
	public byte[] post(String url, Map<String, Object> params, long timeout) {
		return post(url, params, timeout, null);
	}

	/**
	 * <pre>
	 * post 请求
	 * </pre>
	 * 
	 * @param url 请求的URL地址
	 * @param params 请求的参数，支持集合、数组
	 * @param timeout 超时时间
	 * @param headers 请求的header
	 * @return
	 */
	public byte[] post(String url, Map<String, Object> params, long timeout, Collection<Header> headers) {
		final HttpPost httpPost = new HttpPost(url);

		final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (params != null) {
			for (final Map.Entry<String, Object> entry : params.entrySet()) {
				if (entry.getValue() == null) {
					nvps.add(new BasicNameValuePair(entry.getKey(), null));
				}
				else if (entry.getValue() instanceof char[]) {
					for (Object v : (char[]) entry.getValue()) {
						nvps.add(new BasicNameValuePair(entry.getKey(), ObjectUtils.toStringIfNotNull(v)));
					}
				}
				else if (entry.getValue() instanceof byte[]) {
					for (Object v : (byte[]) entry.getValue()) {
						nvps.add(new BasicNameValuePair(entry.getKey(), ObjectUtils.toStringIfNotNull(v)));
					}
				}
				else if (entry.getValue() instanceof short[]) {
					for (Object v : (short[]) entry.getValue()) {
						nvps.add(new BasicNameValuePair(entry.getKey(), ObjectUtils.toStringIfNotNull(v)));
					}
				}
				else if (entry.getValue() instanceof int[]) {
					for (Object v : (int[]) entry.getValue()) {
						nvps.add(new BasicNameValuePair(entry.getKey(), ObjectUtils.toStringIfNotNull(v)));
					}
				}
				else if (entry.getValue() instanceof long[]) {
					for (Object v : (long[]) entry.getValue()) {
						nvps.add(new BasicNameValuePair(entry.getKey(), ObjectUtils.toStringIfNotNull(v)));
					}
				}
				else if (entry.getValue() instanceof float[]) {
					for (Object v : (float[]) entry.getValue()) {
						nvps.add(new BasicNameValuePair(entry.getKey(), ObjectUtils.toStringIfNotNull(v)));
					}
				}
				else if (entry.getValue() instanceof double[]) {
					for (Object v : (double[]) entry.getValue()) {
						nvps.add(new BasicNameValuePair(entry.getKey(), ObjectUtils.toStringIfNotNull(v)));
					}
				}
				else if (Collection.class.isAssignableFrom(entry.getValue().getClass())) {
					for (Object v : (Collection<?>) entry.getValue()) {
						nvps.add(new BasicNameValuePair(entry.getKey(), ObjectUtils.toStringIfNotNull(v)));
					}
				}
				else if (entry.getValue().getClass().isArray()) {
					for (Object v : (Object[]) entry.getValue()) {
						nvps.add(new BasicNameValuePair(entry.getKey(), ObjectUtils.toStringIfNotNull(v)));
					}
				}
				else {
					nvps.add(new BasicNameValuePair(entry.getKey(), ObjectUtils.toStringIfNotNull(entry.getValue())));
				}
			}
		}

		final String charset = this.factory.httpContentCharset();
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, charset));
		}
		catch (UnsupportedEncodingException e) {
			log.error("Error to set UrlEncodedFormEntity by charset:" + charset, e);
			// ignore
		}

		return invoke(this.addHeaders(httpPost, headers), timeout);
	}

	/**
	 * <pre>
	 * 超时时间默认
	 * </pre>
	 * 
	 * @see #invoke(HttpRequestBase, long)
	 * @param req
	 * @return
	 */
	public byte[] invoke(HttpRequestBase req) {
		return invoke(req, 0);
	}

	/**
	 * <pre>
	 * 执行 request
	 * </pre>
	 * 
	 * @param req
	 * @param timeout
	 * @return
	 */
	public byte[] invoke(HttpRequestBase req, long timeout) {
		final String url = req.getURI().toString();
		if (log.isDebugEnabled()) {
			log.debug("invoke url: " + url);
		}

		if (timeout > 0) {
			req.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, (int) timeout);
		}

		try {
			final HttpResponse response = httpClient().execute(req);
			final StatusLine statusLine = response.getStatusLine();
			final HttpEntity entity = response.getEntity();
			final int statusCode = statusLine.getStatusCode();
			if (statusCode != 200) {
				EntityUtils.consume(entity);
				// 非200的响应，抛出异常
				throw new RequestException(statusCode, "Error to request " + url + ",statusCode=" + statusCode
						+ ",reasonPhrase=" + statusLine.getReasonPhrase());
			}
			return EntityUtils.toByteArray(entity);
		}
		catch (final Throwable e) {
			req.abort();
			// 向上层抛出异常
			throw new RequestException("Error to request " + url, e);
		}
		finally {
			req.reset();
		}
	}

	/**
	 * <pre>
	 * post 请求
	 * 响应结果转换成字符串
	 * </pre>
	 * 
	 * @see #post(String, Map)
	 * @param url
	 * @return
	 */
	public String postForString(String url) {
		return postForString(url, null);
	}

	/**
	 * <pre>
	 * 超时时间默认
	 * </pre>
	 * 
	 * @see #post(String, Map, long)
	 * @param url
	 * @param params
	 * @return
	 */
	public String postForString(String url, Map<String, Object> params) {
		return postForString(url, params, 0);
	}

	/**
	 * <pre>
	 * 不设置 header
	 * </pre>
	 * 
	 * @see #post(String, Map, long, Collection)
	 * @param url
	 * @param params
	 * @param timeout
	 * @return
	 */
	public String postForString(String url, Map<String, Object> params, long timeout) {
		return postForString(url, params, timeout, null);
	}

	/**
	 * <pre>
	 * post 请求
	 * </pre>
	 * 
	 * @param url 请求的URL地址
	 * @param params 请求参数
	 * @param timeout 超时时间，支持集合、数组
	 * @param headers 请求的header
	 * @return
	 */
	public String postForString(String url, Map<String, Object> params, long timeout, Collection<Header> headers) {
		final byte[] result = post(url, params, timeout, headers);
		if (result == null) {
			return null;
		}
		try {
			return new String(result, this.factory.httpContentCharset());
		}
		catch (UnsupportedEncodingException e) {
			return new String(result);
		}
	}

	/**
	 * <pre>
	 * get 请求
	 * 响应结果转换成字符串
	 * 超时时间默认
	 * </pre>
	 * 
	 * @see #get(String, Map)
	 * @param url
	 * @return
	 */
	public byte[] get(String url) {
		return get(url, null);
	}

	/**
	 * <pre>
	 * get 请求
	 * </pre>
	 * 
	 * @see #get(String, Map, long)
	 * @param url
	 * @param timeout
	 * @return
	 */
	public byte[] get(String url, long timeout) {
		return get(url, null, timeout);
	}

	/**
	 * @see #get(String, Map, long, Collection)
	 * @param url
	 * @param params
	 * @return
	 */
	public byte[] get(String url, Map<String, Object> params) {
		return get(url, params, 0);
	}

	/**
	 * @see #get(String, Map, long, Collection)
	 * @param url
	 * @param params
	 * @param timeout
	 * @return
	 */
	public byte[] get(String url, Map<String, Object> params, long timeout) {
		return get(url, params, timeout, null);
	}

	/**
	 * <pre>
	 * get 请求
	 * </pre>
	 * 
	 * @param url 请求的URL地址
	 * @param params 请求参数
	 * @param timeout 超时时间，支持集合、数组
	 * @param headers 请求的header
	 * @return
	 */
	public byte[] get(String url, Map<String, Object> params, long timeout, Collection<Header> headers) {
		final String uri = UrlUtils.spliceParams(url, params);

		if (log.isDebugEnabled()) {
			log.debug("request " + uri);
		}

		final HttpGet httpGet = new HttpGet(uri);

		return invoke(this.addHeaders(httpGet, headers), timeout);
	}

	/**
	 * @see #get(String, long)
	 * @param url
	 * @return
	 */
	public String getForString(String url) {
		return getForString(url, 0);
	}

	/**
	 * @see #getForString(String, Map, long)
	 * @param url
	 * @param timeout
	 * @return
	 */
	public String getForString(String url, long timeout) {
		return getForString(url, null, timeout);
	}

	/**
	 * @see #getForString(String, Map, long)
	 * @param url
	 * @param params
	 * @return
	 */
	public String getForString(String url, Map<String, Object> params) {
		return getForString(url, params, 0);
	}

	/**
	 * @see #getForString(String, Map, long, Collection)
	 * @param url
	 * @param params
	 * @param timeout
	 * @return
	 */
	public String getForString(String url, Map<String, Object> params, long timeout) {
		return getForString(url, params, timeout, null);
	}

	/**
	 * <pre>
	 * get 请求
	 * 响应结果转换成字符串
	 * </pre>
	 * 
	 * @param url 请求的URL地址
	 * @param params 请求参数，支持集合、数组
	 * @param timeout 超时时间
	 * @param headers 请求的header
	 * @return
	 */
	public String getForString(String url, Map<String, Object> params, long timeout, Collection<Header> headers) {
		final byte[] result = get(url, params, timeout, headers);
		if (result == null) {
			return null;
		}
		try {
			return new String(result, this.factory.httpContentCharset());
		}
		catch (UnsupportedEncodingException e) {
			return new String(result);
		}
	}
}
