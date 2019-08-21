package com.felipecunha.cursomc.services;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.felipecunha.cursomc.domain.Categoria;
import com.felipecunha.cursomc.domain.Produto;
import com.felipecunha.cursomc.repositories.CategoriaRepository;
import com.felipecunha.cursomc.repositories.ProdutoRepository;
import com.felipecunha.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ProdutoService {

	//instanciada automaticamente pelo spring (pelo mecanismo de inspeção)
	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	//classe responsável por realizar a consulta no repositório
	//retorna um objeto do tipo categoria quando faz a procura por ID, caso não ache, retorna nulo
	public Produto find(Integer id) {
		//Optional<Produto> obj = repo.findById(id); solução sem tratamento
		//return obj.orElse(null);
		Optional<Produto> obj = repo.findById(id);
		//solução com tratamento de exceção
		return obj.orElseThrow(() -> new ObjectNotFoundException( 
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
	}
	
	//vindo do pdf do projeto para fazer a pesquisa + código de CategoriaServices
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);	
	}

}
