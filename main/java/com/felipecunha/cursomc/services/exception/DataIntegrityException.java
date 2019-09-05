package com.felipecunha.cursomc.services.exception;

public class DataIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DataIntegrityException(String msg) {
		super(msg);
	}

	//retorna o erro (do próprio java)
	public DataIntegrityException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
