package br.upe.lapes.sisrepapi.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
	public ResponseEntity<?> addTipoParaUsuario(@RequestBody TipoParaUsuarioForm form ) {
		servicoUsuario.atribuirTipoAoUsuario(form.getNickname(), form.getTipoUsuarioNome());
		return ResponseEntity.ok().build();
	}
	
}

@Data
class TipoParaUsuarioForm {
	private String nickname;
	private String tipoUsuarioNome;
}