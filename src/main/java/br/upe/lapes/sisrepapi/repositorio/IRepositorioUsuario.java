package br.upe.lapes.sisrepapi.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import br.upe.lapes.sisrepapi.modelo.Usuario;

public interface IRepositorioUsuario extends JpaRepository<Usuario, Long> {
	Usuario findByNickname(String nickname);
}
