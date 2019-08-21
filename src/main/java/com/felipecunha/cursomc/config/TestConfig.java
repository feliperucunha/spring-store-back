package com.felipecunha.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.felipecunha.cursomc.services.DBService;
import com.felipecunha.cursomc.services.EmailService;
import com.felipecunha.cursomc.services.MockEmailService;

@Configuration
//indica que tudo a ser configurado só vai ser ativado quando o PROFILE test estiver ativado
@Profile("test")

public class TestConfig {

	
	@Autowired
	private DBService dbService;
	
	//bean indica que está disponível como compenente no sistema
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		dbService.instantiateDatabase();
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
}
