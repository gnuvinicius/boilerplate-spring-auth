package br.com.garage.commons.exceptions;

import java.io.Serial;

public class UnauthorizedException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	public UnauthorizedException(Exception e) {
		super(e);
	}

	public UnauthorizedException(String message) {
		super(message);
	}
}
