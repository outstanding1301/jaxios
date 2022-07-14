package com.outstandingboy.jaxios.exception;

public class JaxiosMappingException extends RuntimeException {
	public JaxiosMappingException() {
	}

	public JaxiosMappingException(String message) {
		super(message);
	}

	public JaxiosMappingException(String message, Throwable cause) {
		super(message, cause);
	}

	public JaxiosMappingException(Throwable cause) {
		super(cause);
	}

	public JaxiosMappingException(String message, Throwable cause, boolean enableSuppression,
								  boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
