package br.upe.lapes.sisrepapi.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import br.upe.lapes.sisrepapi.modelo.TipoUsuario;

public interface IRepositorioTipoUsuario extends JpaRepository<TipoUsuario, Long> {
	TipoUsuario buscarPorNome(String nome);
}
