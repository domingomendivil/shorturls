package com.meli.factory;

public class ConfigurationException extends RuntimeException {

	public ConfigurationException(Throwable e) {
		super(e);
	}

	public ConfigurationException(String msg) {
		super(msg);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2574301952204601123L;

}
