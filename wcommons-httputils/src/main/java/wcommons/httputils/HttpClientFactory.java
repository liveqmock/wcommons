package wcommons.httputils;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import wcommons.lang.properties.PropertiesUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:03:32 AM Nov 25, 2013
 */
public class HttpClientFactory {

	protected final Log log = LogFactory.getLog(getClass());

	private final PoolingClientConnectionManager conman = new PoolingClientConnectionManager();

	private final Properties properties;
	private final HttpParams params;

	public HttpClientFactory() {
		this(HttpUtils.createHttpParamsProperties());
	}

	public HttpClientFactory(Properties properties) {
		this.properties = properties;
		this.params = HttpUtils.createHttpParams(properties);

		this.conman.setMaxTotal(PropertiesUtils.getPropertyForInteger(properties, HttpStatics.MAX_TOTAL_CONNECTIONS,
				HttpStatics.DEFAULT_MAX_TOTAL_CONNECTIONS));
		this.conman.setDefaultMaxPerRoute(PropertiesUtils.getPropertyForInteger(properties,
				HttpStatics.MAX_CONNECTIONS_PER_ROUTE, HttpStatics.DEFAULT_MAX_CONNECTIONS_PER_ROUTE));
	}

	public HttpClient create() {
		DefaultHttpClient dhc = new DefaultHttpClient(this.conman, this.params);
		// 增加客户端 keep alive 策略
		dhc.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				return PropertiesUtils.getPropertyForInteger(properties, HttpStatics.KEEPIDLE_DURATION,
						HttpStatics.DEFAULT_KEEPIDLE_DURATION).longValue();
			}
		});
		return dhc;
	}

	public String httpContentCharset() {
		return (String) this.params.getParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET);
	}
}
