/**
 * 
 */
package wcommons.httputils;


/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:03:26 AM Nov 25, 2013
 */
public interface HttpStatics {
	// 编码
	String DEFAULT_CHARSET = "UTF-8";
	String CHARSET = "wcommons.httputils.http.charset";

	// 连接超时 时间ms
	int DEFAULT_CONNECTION_TIMEOUT = 5000;
	String CONNECTION_TIMEOUT = "wcommons.httputils.http.connectionTimeout";

	// 请求超时 时间ms
	int DEFAULT_SO_TIMEOUT = 50000;
	String SO_TIMEOUT = "wcommons.httputils.http.soTimeout";

	// 是否开启socket的keep-alive功能
	boolean DEFAULT_SO_KEEPALIVE = true;
	String SO_KEEPALIVE = "wcommons.httputils.http.soKeepalive";

	/**
	 * <pre>
     * 服务端断掉连接后，客户端需重建连接
     * 该参数表示这些 CLOSE_WAIT 状态的连接能保持多长时间，超过这个时间则重新创建连接
     * 该参数在开启socket的keep-alive功能后生效
     * </pre>
	 */
	int DEFAULT_KEEPIDLE_DURATION = 5000;
	String KEEPIDLE_DURATION = "wcommons.httputils.http.keepidleDuration";

	// 请求之前是否检查连接，每次需消耗30ms的时间
	boolean DEFAULT_STALE_CHECKING_ENABLED = true;
	String STALE_CHECKING_ENABLED = "wcommons.httputils.http.staleCheckingEnabled";

	// 最大连接数
	int DEFAULT_MAX_TOTAL_CONNECTIONS = 50;
	String MAX_TOTAL_CONNECTIONS = "wcommons.httputils.http.maxTotalConnections";

	// 每个路由默认最大连接数
	int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;
	String MAX_CONNECTIONS_PER_ROUTE = "wcommons.httputils.http.maxConnectionsPerRoute";

	// 
	boolean DEFAULT_TCP_NO_DELAY = true;
	String TCP_NO_DELAY = "wcommons.httputils.http.tcpNoDelay";

}