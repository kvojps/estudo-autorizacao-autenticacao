package br.upe.lapes.sisrepapi.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FiltroAutenticacaoPersonalizada extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;

	public FiltroAutenticacaoPersonalizada(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String nickname = request.getParameter("username");
		String senha = request.getParameter("senha");
		log.info("Nome do usuário é: {}", nickname);
		log.info("Senha é: {}", senha);
		UsernamePasswordAuthenticationToken tokenAutenticacao = new UsernamePasswordAuthenticationToken(nickname,
				senha);

		return authenticationManager.authenticate(tokenAutenticacao);

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication autenticacao) throws IOException, ServletException {
		User user = (User) autenticacao.getPrincipal();
		Algorithm algoritmo = Algorithm.HMAC256("secret".getBytes());
		String tokenAcesso = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("tipos",
						user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algoritmo);
		String tokenRefresh = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
				.withIssuer(request.getRequestURL().toString()).sign(algoritmo);
		
//		response.setHeader("token_acesso", tokenAcesso);
//		response.setHeader("refresh_token", tokenRefresh);
		
		Map<String, String> tokens = new HashMap<>();
		tokens.put("token_acesso",tokenAcesso);
		tokens.put("token_refresh", tokenRefresh);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);
	}

}
