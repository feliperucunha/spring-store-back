package com.felipecunha.cursomc.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.felipecunha.cursomc.domain.Categoria;

//realiza operações de acesso a dados referentes ao objeto Categoria
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

}
//o objeto categoria está mapeado com a tabela categoria no banco de dados