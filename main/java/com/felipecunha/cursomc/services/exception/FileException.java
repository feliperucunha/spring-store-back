package com.felipecunha.cursomc.services.exception;

public class FileException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public FileException(String msg) {
		super(msg);
	}

	//retorna o erro (do próprio java)
	public FileException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
