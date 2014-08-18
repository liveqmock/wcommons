package wcommons.httputils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import wcommons.lang.properties.PropertiesUtils;

/**
 * <pre>
 * 参考  {@link HttpHelper}
 * </pre>
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:03:32 AM Nov 25, 2013
 */
public class HttpUtils {

	private static final HttpHelper HELPER = new HttpHelper();

	/**
	 * @see #createHttpParamsProperties()
	 * @see #createHttpParams(Properties)
	 */
	public static HttpParams createHttpParams() {
		return createHttpParams(createHttpParamsProperties());
	}

	/**
	 * <pre>
	 * 创建 {@link HttpParams} 对象
	 * 参数通过 {@link System#getProperty(String)} 获取
	 * key 参考 {@link HttpStatics}
	 * </pre>
	 * @return
	 */
	public static Properties createHttpParamsProperties() {
		final Properties properties = new Properties();
		// 编码
		properties.put(HttpStatics.CHARSET, System.getProperty(HttpStatics.CHARSET, HttpStatics.DEFAULT_CHARSET));

		// 连接超时 时间ms
		properties.put(
				HttpStatics.CONNECTION_TIMEOUT,
				System.getProperty(HttpStatics.CONNECTION_TIMEOUT,
						String.valueOf(HttpStatics.DEFAULT_CONNECTION_TIMEOUT)));

		// 请求超时 时间ms
		properties.put(HttpStatics.SO_TIMEOUT,
				System.getProperty(HttpStatics.SO_TIMEOUT, String.valueOf(HttpStatics.DEFAULT_SO_TIMEOUT)));

		// 是否开启socket的keep-alive功能
		properties.put(HttpStatics.SO_KEEPALIVE,
				System.getProperty(HttpStatics.SO_KEEPALIVE, String.valueOf(HttpStatics.DEFAULT_SO_KEEPALIVE)));

		// 请求之前是否检查连接，每次需消耗30ms的时间
		properties.put(
				HttpStatics.STALE_CHECKING_ENABLED,
				System.getProperty(HttpStatics.STALE_CHECKING_ENABLED,
						String.valueOf(HttpStatics.DEFAULT_STALE_CHECKING_ENABLED)));

		// 最大连接数
		properties.put(
				HttpStatics.MAX_TOTAL_CONNECTIONS,
				System.getProperty(HttpStatics.MAX_TOTAL_CONNECTIONS,
						String.valueOf(HttpStatics.DEFAULT_MAX_TOTAL_CONNECTIONS)));

		// 每个路由默认最大连接数
		properties.put(
				HttpStatics.MAX_CONNECTIONS_PER_ROUTE,
				System.getProperty(HttpStatics.MAX_CONNECTIONS_PER_ROUTE,
						String.valueOf(HttpStatics.DEFAULT_MAX_CONNECTIONS_PER_ROUTE)));

		/**
		 * <pre>
	     * 服务端断掉连接后，客户端需重建连接
	     * 该参数表示这些 CLOSE_WAIT 状态的连接能保持多长时间，超过这个时间则重新创建连接
	     * 该参数在开启socket的keep-alive功能后生效
	     * </pre>
		 */
		properties.put(HttpStatics.KEEPIDLE_DURATION, System.getProperty(HttpStatics.KEEPIDLE_DURATION,
				String.valueOf(HttpStatics.DEFAULT_KEEPIDLE_DURATION)));
		return properties;
	}

	/**
	 * <pre>
	 * 创建 {@link HttpParams} 对象
	 * 参数通过 {@link Properties#getProperty(String)} 获取
	 * key 参考 {@link HttpStatics}
	 * </pre>
	 * @param properties
	 * @return
	 */
	public static HttpParams createHttpParams(Properties properties) {
		final HttpParams httpParams = new BasicHttpParams();

		HttpProtocolParams.setContentCharset(httpParams,
				PropertiesUtils.getProperty(properties, HttpStatics.CHARSET, HttpStatics.DEFAULT_CHARSET));

		HttpProtocolParams.setHttpElementCharset(httpParams,
				PropertiesUtils.getProperty(properties, HttpStatics.CHARSET, HttpStatics.DEFAULT_CHARSET));

		HttpConnectionParams.setSoTimeout(httpParams, PropertiesUtils.getPropertyForInteger(properties,
				HttpStatics.SO_TIMEOUT, HttpStatics.DEFAULT_SO_TIMEOUT));

		HttpConnectionParams.setConnectionTimeout(httpParams, PropertiesUtils.getPropertyForInteger(properties,
				HttpStatics.CONNECTION_TIMEOUT, HttpStatics.DEFAULT_CONNECTION_TIMEOUT));

		HttpConnectionParams.setSoKeepalive(httpParams, PropertiesUtils.getPropertyForBoolean(properties,
				HttpStatics.SO_KEEPALIVE, HttpStatics.DEFAULT_SO_KEEPALIVE));

		HttpConnectionParams.setStaleCheckingEnabled(httpParams, PropertiesUtils.getPropertyForBoolean(properties,
				HttpStatics.STALE_CHECKING_ENABLED, HttpStatics.DEFAULT_STALE_CHECKING_ENABLED));

		HttpConnectionParams.setTcpNoDelay(httpParams, PropertiesUtils.getPropertyForBoolean(properties,
				HttpStatics.TCP_NO_DELAY, HttpStatics.DEFAULT_TCP_NO_DELAY));
		return httpParams;
	}

	/**
	 * @see #post(String, Map)
	 * @param url
	 * @return
	 */
	public static byte[] post(String url) {
		return post(url, null);
	}

	/**
	 * @see #post(String, Map, long)
	 * @param url
	 * @param params
	 * @return
	 */
	public static byte[] post(String url, Map<String, Object> params) {
		return post(url, params, 0);
	}

	/**
	 * @see #post(String, Map, long, List)
	 * @param url
	 * @param params
	 * @param timeout
	 * @return
	 */
	public static byte[] post(String url, Map<String, Object> params, long timeout) {
		return post(url, params, timeout, null);
	}

	/**
	 * @see HttpHelper#post(String, Map, long, Collection)
	 * @param url
	 * @param params
	 * @param timeout
	 * @param headers
	 * @return
	 */
	public static byte[] post(String url, Map<String, Object> params, long timeout, Collection<Header> headers) {
		return HELPER.post(url, params, timeout, headers);
	}

	/**
	 * @see #invoke(HttpRequestBase, long)
	 * @param req
	 * @return
	 */
	public static byte[] invoke(HttpRequestBase req) {
		return invoke(req, 0);
	}

	/**
	 * @see HttpHelper#invoke(HttpRequestBase, long)
	 * @param req
	 * @param timeout
	 * @return
	 */
	public static byte[] invoke(HttpRequestBase req, long timeout) {
		return HELPER.invoke(req, timeout);
	}

	/**
	 * @see #postForString(String, Map)
	 * @param url
	 * @return
	 */
	public static String postForString(String url) {
		return postForString(url, null);
	}

	/**
	 * @see #postForString(String, Map, long)
	 * @param url
	 * @param params
	 * @return
	 */
	public static String postForString(String url, Map<String, Object> params) {
		return postForString(url, params, 0);
	}

	/**
	 * @see #postForString(String, Map, long, Collection)
	 * @param url
	 * @param params
	 * @param timeout
	 * @return
	 */
	public static String postForString(String url, Map<String, Object> params, long timeout) {
		return postForString(url, params, timeout, null);
	}

	/**
	 * @see HttpHelper#postForString(String, Map, long, Collection)
	 * @param url
	 * @param params
	 * @param timeout
	 * @param headers
	 * @return
	 */
	public static String postForString(String url, Map<String, Object> params, long timeout, Collection<Header> headers) {
		return HELPER.postForString(url, params, timeout, headers);
	}

	/**
	 * @see #get(String, long)
	 * @param url
	 * @return
	 */
	public static byte[] get(String url) {
		return get(url, null);
	}

	/**
	 * @see #get(String, Map, long)
	 * @param url
	 * @param timeout
	 * @return
	 */
	public static byte[] get(String url, long timeout) {
		return get(url, null, timeout);
	}

	/**
	 * @see #get(String, Map, long)
	 * @param url
	 * @param params
	 * @return
	 */
	public static byte[] get(String url, Map<String, Object> params) {
		return get(url, params, 0);
	}

	/**
	 * @see #get(String, Map, long, Collection)
	 * @param url
	 * @param params
	 * @param timeout
	 * @return
	 */
	public static byte[] get(String url, Map<String, Object> params, long timeout) {
		return get(url, params, timeout, null);
	}

	/**
	 * @see HttpHelper#get(String, Map, long, Collection)
	 * @param url
	 * @param params
	 * @param timeout
	 * @param headers
	 * @return
	 */
	public static byte[] get(String url, Map<String, Object> params, long timeout, Collection<Header> headers) {
		return HELPER.get(url, params, timeout, headers);
	}

	/**
	 * @see #getForString(String, long)
	 * @param url
	 * @return
	 */
	public static String getForString(String url) {
		return getForString(url, 0);
	}

	/**
	 * @see #getForString(String, Map, long)
	 * @param url
	 * @param timeout
	 * @return
	 */
	public static String getForString(String url, long timeout) {
		return getForString(url, null, timeout);
	}

	/**
	 * @see #getForString(String, Map, long)
	 * @param url
	 * @param params
	 * @return
	 */
	public static String getForString(String url, Map<String, Object> params) {
		return getForString(url, params, 0);
	}

	/**
	 * @see #getForString(String, Map, long, Collection)
	 * @param url
	 * @param params
	 * @param timeout
	 * @return
	 */
	public static String getForString(String url, Map<String, Object> params, long timeout) {
		return getForString(url, params, timeout, null);
	}

	/**
	 * @see HttpHelper#getForString(String, Map, long, Collection)
	 * @param url
	 * @param params
	 * @param timeout
	 * @param headers
	 * @return
	 */
	public static String getForString(String url, Map<String, Object> params, long timeout, Collection<Header> headers) {
		return HELPER.getForString(url, params, timeout, headers);
	}
}
