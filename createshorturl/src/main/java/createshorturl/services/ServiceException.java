package createshorturl.services;

public class ServiceException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4700059618121660739L;

	public ServiceException(Throwable e) {
        super(e);
    }

	public ServiceException(String msg, Throwable e) {
		super(msg,e);
	}

	public ServiceException() {
	}

}