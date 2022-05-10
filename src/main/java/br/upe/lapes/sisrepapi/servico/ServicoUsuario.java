package br.upe.lapes.sisrepapi.servico;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.upe.lapes.sisrepapi.modelo.TipoUsuario;
import br.upe.lapes.sisrepapi.modelo.Usuario;
import br.upe.lapes.sisrepapi.repositorio.IRepositorioTipoUsuario;
import br.upe.lapes.sisrepapi.repositorio.IRepositorioUsuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ServicoUsuario implements IServicoUsuario {
	private final IRepositorioUsuario repositorioUsuario;
	private final IRepositorioTipoUsuario repositorioTipoUsuario;

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		log.info("Salvando usuário {} no banco de dados", usuario.getNome());
		return repositorioUsuario.save(usuario);
	}

	@Override
	public TipoUsuario salvarTipoUsuario(TipoUsuario tipoUsuario) {
		log.info("Salvando novo tipo de usuário {} no banco de dados", tipoUsuario.getNome());
		return repositorioTipoUsuario.save(tipoUsuario);
	}

	@Override
	public void atribuirTipoAoUsuario(String nickname, String tipoUsuarioNome) {
		log.info("Adicionando tipo {} ao usuário{}", tipoUsuarioNome, nickname);
		Usuario usuario = repositorioUsuario.findByNickname(nickname);
		TipoUsuario tipo = repositorioTipoUsuario.findByNome(tipoUsuarioNome);
		usuario.getTipos().add(tipo);
	}

	@Override
	public Usuario getUsuario(String nickname) {
		log.info("Obtendo usuário {}", nickname);
		return repositorioUsuario.findByNickname(nickname);
	}

	@Override
	public List<Usuario> getUsuarios() {
		log.info("Obtendo todos os usuários");
		return repositorioUsuario.findAll();
	}

}
