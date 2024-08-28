package br.com.garage.commons.exceptions;

import java.io.Serial;

public class BusinessException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	public BusinessException(Exception e) {
		super(e);
	}

	public BusinessException(String message) {
		super(message);
	}

}
