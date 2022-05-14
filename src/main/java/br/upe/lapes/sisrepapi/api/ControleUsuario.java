package br.upe.lapes.sisrepapi.api;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.upe.lapes.sisrepapi.modelo.TipoUsuario;
import br.upe.lapes.sisrepapi.modelo.Usuario;
import br.upe.lapes.sisrepapi.servico.ServicoUsuario;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ControleUsuario {
	private final ServicoUsuario servicoUsuario;

	@GetMapping("/usuarios")
	public ResponseEntity<List<Usuario>> getUsuarios() {
		return ResponseEntity.ok().body(servicoUsuario.getUsuarios());
	}

	@PostMapping("/usuario/salvar")
	public ResponseEntity<Usuario> salvarUsuario(@RequestBody Usuario usuario) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
		return ResponseEntity.created(uri).body(servicoUsuario.salvarUsuario(usuario));
	}

	@PostMapping("/tipousuario/salvar")
	public ResponseEntity<TipoUsuario> salvarTipoUsuario(@RequestBody TipoUsuario tipoUsuario) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/tipoUsuario/salvar").toUriString());
		return ResponseEntity.created(uri).body(servicoUsuario.salvarTipoUsuario(tipoUsuario));
	}

	@PostMapping("/tipousuario/adicionaraousuario")
	public ResponseEntity<?> addTipoParaUsuario(@RequestBody TipoParaUsuarioForm form) {
		servicoUsuario.atribuirTipoAoUsuario(form.getNickname(), form.getTipoUsuarioNome());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String headerAutorizacao = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (headerAutorizacao != null && headerAutorizacao.startsWith("Bearer ")) {
			try {
				String tokenRefresh = headerAutorizacao.substring("Bearer ".length());
				Algorithm algoritmo = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verificador = JWT.require(algoritmo).build();
				DecodedJWT decodedJWT = verificador.verify(tokenRefresh);
				String username = decodedJWT.getSubject();
				Usuario usuario = servicoUsuario.getUsuario(username);
				String tokenAcesso = JWT.create().withSubject(usuario.getNickname())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("tipos",
								usuario.getTipos().stream().map(TipoUsuario::getNome).collect(Collectors.toList()))
						.sign(algoritmo);
				Map<String, String> tokens = new HashMap<>();
				tokens.put("token_acesso", tokenAcesso);
				tokens.put("token_refresh", tokenRefresh);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
			} catch (Exception exception) {
				response.setHeader("erro", exception.getMessage());
				response.setStatus(HttpStatus.FORBIDDEN.value());
//				response.sendError(HttpStatus.FORBIDDEN.value());
				Map<String, String> erro = new HashMap<>();
				erro.put("error_message", exception.getMessage());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), erro);
			}
		} else {
			throw new RuntimeException("Refresh token is missing");
		}
	}

}

@Data
class TipoParaUsuarioForm {
	private String nickname;
	private String tipoUsuarioNome;
}