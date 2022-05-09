package br.upe.lapes.sisrepapi.servico;

import java.util.List;

import br.upe.lapes.sisrepapi.modelo.TipoUsuario;
import br.upe.lapes.sisrepapi.modelo.Usuario;

public interface IServicoUsuario {
	Usuario salvarUsuario(Usuario usuario);

	TipoUsuario salvarTipoUsuario(TipoUsuario tipoUsuario);

	void atribuirTipoAoUsuario(String nickname, String tipoUsuarioNome);

	Usuario getUsuario(String nickname);

	List<Usuario> getUsuarios();

}
