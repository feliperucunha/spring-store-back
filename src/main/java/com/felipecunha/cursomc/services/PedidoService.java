package com.felipecunha.cursomc.services;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.felipecunha.cursomc.domain.Cliente;
import com.felipecunha.cursomc.domain.ItemPedido;
import com.felipecunha.cursomc.domain.PagamentoComBoleto;
import com.felipecunha.cursomc.domain.Pedido;
import com.felipecunha.cursomc.domain.enums.EstadoPagamento;
import com.felipecunha.cursomc.repositories.ItemPedidoRepository;
import com.felipecunha.cursomc.repositories.PagamentoRepository;
import com.felipecunha.cursomc.repositories.PedidoRepository;
import com.felipecunha.cursomc.security.UserSS;
import com.felipecunha.cursomc.services.exception.AuthorizationException;
import com.felipecunha.cursomc.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {

	//instanciada automaticamente pelo spring (pelo mecanismo de inspeção)
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	//classe responsável por realizar a consulta no repositório
	//retorna um objeto do tipo categoria quando faz a procura por ID, caso não ache, retorna nulo
	public Pedido find(Integer id) {
		//Optional<Pedido> obj = repo.findById(id); solução sem tratamento
		//return obj.orElse(null);
		Optional<Pedido> obj = repo.findById(id);
		//solução com tratamento de exceção
		return obj.orElseThrow(() -> new ObjectNotFoundException( 
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	//método para inserir pedido
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		//buscar no banco de dados o cliente inteiro pelo ID
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		//um pedido inserido recentemente tem o pagamento pendente
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		//já que não há webservice para boleto, criamos esse trecho para criar uma data de vencimento do boleto
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		//salva o pedido e o pagamento no banco
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		//percorre itens associados para buscar os preços e no final salva no banco de dados
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			//busca no banco de dados (item associado ao produto)
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente =  clienteService.find(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}
}
