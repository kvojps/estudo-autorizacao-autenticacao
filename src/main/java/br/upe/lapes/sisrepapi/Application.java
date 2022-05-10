package br.upe.lapes.sisrepapi;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.upe.lapes.sisrepapi.modelo.TipoUsuario;
import br.upe.lapes.sisrepapi.modelo.Usuario;
import br.upe.lapes.sisrepapi.servico.ServicoUsuario;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CommandLineRunner run(ServicoUsuario servicoUsuario) {
		return args -> {
			servicoUsuario.salvarTipoUsuario(new TipoUsuario(null, "ROLE_USER"));
			servicoUsuario.salvarTipoUsuario(new TipoUsuario(null, "ROLE_MANAGER"));
			servicoUsuario.salvarTipoUsuario(new TipoUsuario(null, "ROLE_ADMIN"));
			servicoUsuario.salvarTipoUsuario(new TipoUsuario(null, "ROLE_SUPER_ADMIN"));

			servicoUsuario.salvarUsuario(new Usuario(null, "Spock de vulcano", "Spock", "1234", new ArrayList()));
			servicoUsuario.salvarUsuario(new Usuario(null, "James T. Kirk", "Kirk", "1234", new ArrayList()));
			servicoUsuario.salvarUsuario(new Usuario(null, "Uhura", "Uhura", "1234", new ArrayList()));
			servicoUsuario.salvarUsuario(new Usuario(null, "Magro", "magro", "1234", new ArrayList()));
			
			servicoUsuario.atribuirTipoAoUsuario("Kirk", "ROLE_SUPER_ADMIN");
			servicoUsuario.atribuirTipoAoUsuario("Spock", "ROLE_USER");
			servicoUsuario.atribuirTipoAoUsuario("Spock", "ROLE_MANAGER");
		};
	}

}
