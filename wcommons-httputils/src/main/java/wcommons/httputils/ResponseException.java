/**
 * 
 */
package wcommons.httputils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 4:52:33 PM Jan 7, 2014
 */
public class ResponseException extends RuntimeException {

	private static final long serialVersionUID = 5713692906553751387L;

	public ResponseException() {
		super();
	}

	public ResponseException(String message) {
		super(message);
	}

	public ResponseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResponseException(Throwable cause) {
		super(cause);
	}
}
