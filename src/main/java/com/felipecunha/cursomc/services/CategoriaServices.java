package com.felipecunha.cursomc.services;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.felipecunha.cursomc.domain.Categoria;
import com.felipecunha.cursomc.dto.CategoriaDTO;
import com.felipecunha.cursomc.repositories.CategoriaRepository;
import com.felipecunha.cursomc.services.exception.DataIntegrityException;
import com.felipecunha.cursomc.services.exception.ObjectNotFoundException;

@Service
public class CategoriaServices {

	//instanciada automaticamente pelo spring (pelo mecanismo de inspeção)
	@Autowired
	private CategoriaRepository repo;
	
	//classe responsável por realizar a consulta no repositório
	//retorna um objeto do tipo categoria quando faz a procura por ID, caso não ache, retorna nulo
	public Categoria find(Integer id) {
		//Optional<Categoria> obj = repo.findById(id); solução sem tratamento
		//return obj.orElse(null);
		Optional<Categoria> obj = repo.findById(id);
		//solução com tratamento de exceção
		return obj.orElseThrow(() -> new ObjectNotFoundException( 
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	//método para incluir categorias por json
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		//o método find verifica se o id existe e também já dá o erro caso não exista
		Categoria newObj = find(obj.getId());
		//atualiza os dados do newobj de acordo com o obj
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}
		//tentando deletar uma categoria com itens, esse erro é encontrado no postman assim  "trace": "org.springframework.dao.DataIntegrityViolationException: could not execute statement;
		catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
		}
		
	}
	
	public List<Categoria> findAll(){
		return repo.findAll();
	}
	
	//page encapsula informações sobre as páginas
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);	
	}
	
	//instancia uma categoria a partir do dto
	public Categoria fromDTO(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}
	
	private void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
	}
}
