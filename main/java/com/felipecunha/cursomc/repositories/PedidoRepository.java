package com.felipecunha.cursomc.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.felipecunha.cursomc.domain.Cliente;
import com.felipecunha.cursomc.domain.Pedido;

//realiza operações de acesso a dados referentes ao objeto Categoria
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
	
	@Transactional(readOnly=true)
	Page<Pedido> findByCliente(Cliente cliente, Pageable pageRequest);
}

//o objeto categoria está mapeado com a tabela categoria no banco de dados