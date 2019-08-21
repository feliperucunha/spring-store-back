package com.felipecunha.cursomc.domain.enums;

public enum TipoCliente {

	//não foi gravado em APENAS string para economizar espaço e otimizar o banco
	PESSOAFISICA(1, "Pessoa Física"),
	PESSOAJURIDICA(2, "Pessoa Jurídica");
	
	private int cod;
	private String descricao;
	
	private TipoCliente(int cod, String descricao) {
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
	public static TipoCliente toEnum(Integer cod) {
		
		if(cod == null) {
			return null;
		}
		
		//se o que for verificado for igual ao código procurado, retorne o mesmo
		for(TipoCliente x : TipoCliente.values()) {
			if(cod.equals(x.getCod())) {
				return x;
			}
		}
		
		//exceção 
		throw new IllegalArgumentException("Id inválido: " + cod);
	}
}
