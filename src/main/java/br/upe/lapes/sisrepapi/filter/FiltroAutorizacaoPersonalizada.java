package br.upe.lapes.sisrepapi.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class FiltroAutorizacaoPersonalizada extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getServletPath().equals("/api/login")) {
			filterChain.doFilter(request, response);
		} else {
			String headerAutorizacao = request.getHeader(HttpHeaders.AUTHORIZATION);
			if (headerAutorizacao != null && headerAutorizacao.startsWith("Bearer ")) {
				try {
					String token = headerAutorizacao.substring("Bearer ".length());
					Algorithm algoritmo = Algorithm.HMAC256("secret".getBytes());
					JWTVerifier verificador = JWT.require(algoritmo).build();
					DecodedJWT decodedJWT = verificador.verify(token);
					String username = decodedJWT.getSubject();
					String[] tiposUsuario = decodedJWT.getClaim("tipos").asArray(String.class);
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
					Arrays.stream(tiposUsuario).forEach(tipo -> {
						authorities.add(new SimpleGrantedAuthority(tipo));
					});
					UsernamePasswordAuthenticationToken tokenAutenticacao = new UsernamePasswordAuthenticationToken(
							username, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(tokenAutenticacao);
					filterChain.doFilter(request, response);
				} catch (Exception exception) {
					// TODO: handle exception
				}
			} else {
				filterChain.doFilter(request, response);
			}
		}
	}

}
