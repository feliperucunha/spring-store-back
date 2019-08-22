package com.felipecunha.cursomc.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipecunha.cursomc.dto.CredenciaisDTO;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

    private JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
    	setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

	@Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {

		try {
			CredenciaisDTO creds = new ObjectMapper()
	                .readValue(req.getInputStream(), CredenciaisDTO.class);

			//token SpringSecurity com lista vazia
	        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());

	        //verifica se id e senha são válidos com base no que foi implementado anteriormente
	        Authentication auth = authenticationManager.authenticate(authToken);
	        return auth;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	//dando certo a autenticação, o token de segurança é gerado
	@Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

		String username = ((UserSS) auth.getPrincipal()).getUsername();
        String token = jwtUtil.generateToken(username);
        //gera o token no postman para autorizar
        res.addHeader("Authorization", "Bearer " + token);
        res.addHeader("access-control-expose-headers", "Authorization");
	}

	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                throws IOException, ServletException {
            response.setStatus(401);
            response.setContentType("application/json"); 
            response.getWriter().append(json());
        }

        private String json() {
            long date = new Date().getTime();
            return "{\"timestamp\": " + date + ", "
                + "\"status\": 401, "
                + "\"error\": \"Não autorizado\", "
                + "\"message\": \"Email ou senha inválidos\", "
                + "\"path\": \"/login\"}";
        }
    }
}
