package com.felipecunha.cursomc.services.exception;

public class ObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ObjectNotFoundException(String msg) {
		super(msg);
	}

	//retorna o erro (do próprio java)
	public ObjectNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
