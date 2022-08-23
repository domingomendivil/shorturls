package shorturls.exceptions;

/**
 * Runtime exception thrown when an unexpected behaviour happens.
 * Example of an unexpected behaviour is getting
 * an invalid URL from the database, when it's supposed that 
 * this can't happen.
 */
public class ShortURLRuntimeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8117612302977713113L;

	public ShortURLRuntimeException(String msg) {
		super(msg);
	}
	
	public ShortURLRuntimeException(String msg,Throwable e) {
		super(msg,e);
	}

	public ShortURLRuntimeException(Throwable e) {
		super(e);
	}


}
