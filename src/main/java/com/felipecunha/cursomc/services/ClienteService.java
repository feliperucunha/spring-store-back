package com.felipecunha.cursomc.services;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.felipecunha.cursomc.domain.Cidade;
import com.felipecunha.cursomc.domain.Cliente;
import com.felipecunha.cursomc.domain.Endereco;
import com.felipecunha.cursomc.domain.enums.Perfil;
import com.felipecunha.cursomc.domain.enums.TipoCliente;
import com.felipecunha.cursomc.dto.ClienteDTO;
import com.felipecunha.cursomc.dto.ClienteNewDTO;
import com.felipecunha.cursomc.repositories.ClienteRepository;
import com.felipecunha.cursomc.repositories.EnderecoRepository;
import com.felipecunha.cursomc.security.UserSS;
import com.felipecunha.cursomc.services.exception.AuthorizationException;
import com.felipecunha.cursomc.services.exception.DataIntegrityException;
import com.felipecunha.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private BCryptPasswordEncoder pe;
	
	//instanciada automaticamente pelo spring (pelo mecanismo de inspeção)
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	
	//classe responsável por realizar a consulta no repositório
	//retorna um objeto do tipo categoria quando faz a procura por ID, caso não ache, retorna nulo
	public Cliente find(Integer id) {
		//tratamento personalizado + autenticação
		UserSS user = UserService.authenticated();
		if (user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		//Optional<Cliente> obj = repo.findById(id); solução sem tratamento
		//return obj.orElse(null);
		Optional<Cliente> obj = repo.findById(id);
		//solução com tratamento de exceção
		return obj.orElseThrow(() -> new ObjectNotFoundException( 
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		//save sobrecarregado de lista
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}
	
	//o método save serve tanto para inserir quanto para atualizar
	public Cliente update(Cliente obj) {
		//o método find verifica se o id existe e também já dá o erro caso não exista
		Cliente newObj = find(obj.getId());
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
			throw new DataIntegrityException("Não é possível excluir, pois há pedidos relacionados ao cliente");
		}
		
	}
	
	public List<Cliente> findAll(){
		return repo.findAll();
	}
	
	//page encapsula informações sobre as páginas
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);	
	}
	
	//instancia uma categoria a partir do dto
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
		//throw new UnsupportedOperationException();
		//return new Cliente(objDto.getId(), objDto.getNome());
	}
	
	//sobrecarga do método acima
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefone().add(objDto.getTelefone1());
		if (objDto.getTelefone2()!=null) {
			cli.getTelefone().add(objDto.getTelefone2());
		}
		if (objDto.getTelefone3()!=null) {
			cli.getTelefone().add(objDto.getTelefone3());
		}
		return cli;
	}
	
	//atualiza os dados inseridos sem deixar os outros dados nulos
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

}