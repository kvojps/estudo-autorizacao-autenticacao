package br.upe.lapes.sisrepapi.servico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class ServicoUsuario implements IServicoUsuario, UserDetailsService {
	private final IRepositorioUsuario repositorioUsuario;
	private final IRepositorioTipoUsuario repositorioTipoUsuario;
	private final PasswordEncoder senhaCriptografada;

	@Override
	public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
		Usuario usuario = repositorioUsuario.findByNickname(nickname);
		if (usuario == null) {
			log.error("Usuário não encontrado no banco de dados");
			throw new UsernameNotFoundException("Usuário não encontrado no banco de dados");
		} else {
			log.info("Usuário {} encontrado no banco de dados", nickname);
		}

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		usuario.getTipos().forEach(tipo -> {
			authorities.add(new SimpleGrantedAuthority(tipo.getNome()));
		});
		return new org.springframework.security.core.userdetails.User(usuario.getNickname(), usuario.getSenha(),
				authorities);
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		log.info("Salvando usuário {} no banco de dados", usuario.getNome());
		usuario.setSenha(senhaCriptografada.encode(usuario.getSenha()));
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
