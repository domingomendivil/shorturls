package shorturls.exceptions;

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
