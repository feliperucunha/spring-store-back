package com.felipecunha.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.felipecunha.cursomc.domain.Cliente;
import com.felipecunha.cursomc.domain.Pedido;

//operações que o serviço de e-mail deve oferecer
public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
	//versão html dos mesmos métodos acima
	void sendOrderConfirmationHtmlEmail(Pedido obj);
	void sendHtmlEmail(MimeMessage msg);
	
	void sendNewPasswordEmail(Cliente cliente, String newPass);
}
