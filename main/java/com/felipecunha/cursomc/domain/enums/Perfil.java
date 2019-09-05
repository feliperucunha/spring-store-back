package com.felipecunha.cursomc.domain.enums;

public enum Perfil {
	
	ADMIN(1, "ROLE_ADMIN"),
	CLIENTE(2, "ROLE_CLIENTE");

	
	private int cod;
	private String descricao;
	
	private Perfil(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	//criados apenas os GETTERS pois é um ENUM
	
	public int getCod() {
		return cod;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	//devolve o cliente a partir de um código
	public static Perfil toEnum(Integer cod) {
		
		if(cod == null) {
			return null;
		}
		
		//se o que for verificado for igual ao código procurado, retorne o mesmo
		for(Perfil x : Perfil.values()) {
			if(cod.equals(x.getCod())) {
				return x;
			}
		}
		
		//exceção 
		throw new IllegalArgumentException("Id inválido: " + cod);
	}
}
