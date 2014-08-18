/**
 * 
 */
package wcommons.httputils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:11:19 AM Nov 25, 2013
 */
public class ResponseRenderException extends ResponseException {

	private static final long serialVersionUID = -3702751212711138128L;

	public ResponseRenderException() {
		super();
	}

	public ResponseRenderException(String message) {
		super(message);
	}

	public ResponseRenderException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResponseRenderException(Throwable cause) {
		super(cause);
	}
}
