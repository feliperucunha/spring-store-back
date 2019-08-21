package com.felipecunha.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.felipecunha.cursomc.services.DBService;
import com.felipecunha.cursomc.services.EmailService;
import com.felipecunha.cursomc.services.SmtpEmailService;

@Configuration
//indica que tudo a ser configurado só vai ser ativado quando o PROFILE test estiver ativado
@Profile("dev")

public class DevConfig {

	
	@Autowired
	private DBService dbService;
	
	//estratégia para puxar o dado do application-dev.properties para saber se deve criar ou não a tabela dependendo do dado
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		
		//se não tiver create no application-dev, retorna falso
		if (!"create".equals(strategy)) {
			return false;
		}
		
		dbService.instantiateDatabase();
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
}
