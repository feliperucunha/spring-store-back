package com.felipecunha.cursomc.domain;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

//Entity para transformar os dados em BD (JPA)
@Entity
//serializable para que possa ser convertido
public class Categoria implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//strategy para gerar IDs para os dados automaticamente
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	
	
	@ManyToMany(mappedBy="categorias")
	//Lista de produtos 
	private List<Produto> produtos = new ArrayList<>();
	
	//instancia o objeto sem jogar nada para os atributos
	public Categoria() {
	}
    
	//método construtor para ambos
	public Categoria(Integer id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	//getters and setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	//comparando objeto por valor (geralmente só entra o ID da classe)
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	//comparando objeto por valor (geralmente só entra o ID da classe)
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	

	

}
