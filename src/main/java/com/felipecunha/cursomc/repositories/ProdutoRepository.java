package com.felipecunha.cursomc.repositories;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.felipecunha.cursomc.domain.Categoria;
import com.felipecunha.cursomc.domain.Produto;

//realiza operações de acesso a dados referentes ao objeto Produto
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

	@Transactional(readOnly=true)
	//consulta diferenciada que precisa dizer o JPQL referente à consulta utilizando o @query do framework
	//@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN : categorias")
	//Page<Produto> findDistinctByNomeContainingAndCategoriasIn(@Param("nome") String nome,@Param("categorias") List<Categoria> categorias, Pageable pageRequest);
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
}
//o objeto categoria está mapeado com a tabela produto no banco de dados
//o nome de método findDistinctByNomeContainingAndCategoriasIn gera a consulta e torna obsoleto o @query e @param