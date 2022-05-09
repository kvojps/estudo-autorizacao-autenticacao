package br.upe.lapes.sisrepapi.modelo;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nome;
	private String nickname;
	private String senha;

	@ManyToMany(fetch = FetchType.EAGER) // Carregar todos os papéis sempre que carregar o usuário
	private Collection<TipoUsuario> tipos = new ArrayList<>();
}
