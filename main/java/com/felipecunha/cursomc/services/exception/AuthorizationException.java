package com.felipecunha.cursomc.services.exception;

public class AuthorizationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public AuthorizationException(String msg) {
		super(msg);
	}

	//retorna o erro (do próprio java)
	public AuthorizationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
