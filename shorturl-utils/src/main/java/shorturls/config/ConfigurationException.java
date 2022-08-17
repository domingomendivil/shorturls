package shorturls.config;

public class ConfigurationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -37238108715308397L;

	public ConfigurationException(String msg) {
		super(msg);
	}

	public ConfigurationException(Throwable e) {
		super(e);
	}

    public ConfigurationException(String msg,Throwable e) {
		super(msg,e);
    }

}
