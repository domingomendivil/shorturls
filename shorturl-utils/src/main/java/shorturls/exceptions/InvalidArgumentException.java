package shorturls.exceptions;

/**
 * Exception thrown when arguments in a call to 
 * the services of URL shortening (creation, deletion ,etc)
 * are wrong. E.g. trying to delete an invalid URL
 *
 */
public class InvalidArgumentException extends Exception{

	public InvalidArgumentException(Throwable e) {
        super(e);
    }

    public InvalidArgumentException(String msg) {
        super(msg);
    }

    /**
	 * .services
	 */
	private static final long serialVersionUID = 2740898516000444847L;

}
