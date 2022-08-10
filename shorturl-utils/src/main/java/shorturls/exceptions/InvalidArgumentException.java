package shorturls.exceptions;

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
