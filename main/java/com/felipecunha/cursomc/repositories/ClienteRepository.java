package com.felipecunha.cursomc.repositories;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.felipecunha.cursomc.domain.Cliente;

//realiza operações de acesso a dados referentes ao objeto Categoria
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	//o repository busca um cliente passando um email como argumento automaticamente devido ao spring bastando utilizar o findBy...
	Cliente findByEmail(String email);

}
//o objeto categoria está mapeado com a tabela categoria no banco de dados