/**
 * 
 */
package wcommons.httputils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:11:19 AM Nov 25, 2013
 */
public class RequestException extends RuntimeException {

	private static final long serialVersionUID = 8474852401550787928L;

	private int errorCode = -1;

	public RequestException() {
		super();
	}

	public RequestException(int errorCode) {
		this();
		this.setErrorCode(errorCode);
	}

	public RequestException(String message) {
		super(message);
	}

	public RequestException(int errorCode, String message) {
		this(message);
		this.setErrorCode(errorCode);
	}

	public RequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public RequestException(int errorCode, String message, Throwable cause) {
		this(message, cause);
		this.setErrorCode(errorCode);
	}

	public RequestException(Throwable cause) {
		super(cause);
	}

	public RequestException(int errorCode, Throwable cause) {
		this(cause);
		this.setErrorCode(errorCode);
	}

	public int getErrorCode() {
		return errorCode;
	}

	void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
