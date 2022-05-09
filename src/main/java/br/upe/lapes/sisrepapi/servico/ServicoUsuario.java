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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TipoUsuario salvarTipoUsuario(TipoUsuario tipoUsuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void atribuirTipoAoUsuario(String nickname, String tipoUsuarioNome) {
		// TODO Auto-generated method stub

	}

	@Override
	public Usuario getUsuario(String nickname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Usuario> getUsuarios() {
		// TODO Auto-generated method stub
		return null;
	}

}
