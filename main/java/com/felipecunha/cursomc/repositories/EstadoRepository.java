package com.felipecunha.cursomc.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.felipecunha.cursomc.domain.Estado;

//realiza operações de acesso a dados referentes ao objeto Categoria
@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
	

	@Transactional(readOnly=true)
	public List<Estado> findAllByOrderByNome();

}
//o objeto categoria está mapeado com a tabela categoria no banco de dados