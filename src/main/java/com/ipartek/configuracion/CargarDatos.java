package com.ipartek.configuracion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ipartek.auxiliar.HashUtil;
import com.ipartek.modelo.Cancion;
import com.ipartek.modelo.Estilo;
import com.ipartek.modelo.Pregunta;
import com.ipartek.modelo.PreguntasUsuario;
import com.ipartek.modelo.Rol;
import com.ipartek.modelo.Usuario;
import com.ipartek.repositorio.CancionRepositorio;
import com.ipartek.repositorio.EstiloRepositorio;
import com.ipartek.repositorio.PreguntaRepositorio;
import com.ipartek.repositorio.PreguntasUsuarioRepositorio;
import com.ipartek.repositorio.RolRepositorio;
import com.ipartek.repositorio.UsuarioRepositorio;

@Component
public class CargarDatos implements CommandLineRunner {

	@Autowired
	private RolRepositorio rolRepo;

	@Autowired
	private UsuarioRepositorio usuarioRepo;

	@Autowired
	private EstiloRepositorio estiloRepo;

	@Autowired
	private CancionRepositorio cancionRepo;

	@Autowired
	private PreguntaRepositorio preguntaRepo;

	@Autowired
	private PreguntasUsuarioRepositorio pregUsuRepo;

	@Override
	public void run(String... args) throws Exception {

		// 1. CARGA DE ROLES
		Rol rolAdmin = rolRepo.findByNombre("ADMIN").orElseGet(() -> rolRepo.save(new Rol(null, "ADMIN")));
		Rol rolUser = rolRepo.findByNombre("USER").orElseGet(() -> rolRepo.save(new Rol(null, "USER")));
		// Asegurar la existencia del Rol BANEADO para moderación y desbaneo
		rolRepo.findByNombre("BANEADO").orElseGet(() -> rolRepo.save(new Rol(null, "BANEADO")));

		// 2. CARGA DE PREGUNTAS DE SEGURIDAD
		if (preguntaRepo.count() == 0) {
			preguntaRepo.saveAll(List.of(
					new Pregunta(null, "¿Cuál es el nombre de tu primera mascota?"),
					new Pregunta(null, "¿En qué ciudad nació tu madre?"),
					new Pregunta(null, "¿Cuál es tu comida favorita?"),
					new Pregunta(null, "¿Cuál fue tu primer colegio o escuela?"),
					new Pregunta(null, "¿Cuál es tu color favorito?"),
					new Pregunta(null, "¿Cuál es tu película o serie favorita?"),
					new Pregunta(null, "¿Cómo se llamaba tu mejor amigo de la infancia?"),
					new Pregunta(null, "¿Cuál es el modelo de tu primer coche?")));
		}

		// 3. CARGA DE USUARIOS
		// Administrador: alain (pass: '1234' hasheado con 'Galea' + salt)
		Usuario alain = usuarioRepo.findByUser("alain").orElse(null);
		if (alain == null) {
			String salt = HashUtil.generarSalt();
			String hashCliente = HashUtil.sha256("1234" + "Galea");
			String passDb = HashUtil.calcularHashFinal(hashCliente, salt);

			alain = new Usuario(null, "alain", passDb, salt, rolAdmin, "Fondologin.png", 0);
			usuarioRepo.save(alain);
		}

		// Usuarios de prueba normales
		String[] nombres = { "luis", "carlos", "maria", "elena", "david" };
		for (String nombreUsu : nombres) {
			if (usuarioRepo.findByUser(nombreUsu).isEmpty()) {
				String salt = HashUtil.generarSalt();
				String hashCliente = HashUtil.sha256("1234" + "Galea");
				String passDb = HashUtil.calcularHashFinal(hashCliente, salt);

				Usuario u = new Usuario(null, nombreUsu, passDb, salt, rolUser, "avatar.png", 0);
				Usuario guardado = usuarioRepo.save(u);

				// Asignar respuestas de seguridad por defecto para pruebas
				List<Pregunta> pregs = preguntaRepo.findAll();
				if (pregs.size() >= 3) {
					pregUsuRepo.save(new PreguntasUsuario(null, guardado, pregs.get(0), "perro"));
					pregUsuRepo.save(new PreguntasUsuario(null, guardado, pregs.get(1), "madrid"));
					pregUsuRepo.save(new PreguntasUsuario(null, guardado, pregs.get(2), "pizza"));
				}
			}
		}

		// 4. CARGA DE ESTILOS
		Estilo rock = estiloRepo.findByNombre("Rock").orElseGet(() -> estiloRepo.save(new Estilo(null, "Rock")));
		Estilo pop = estiloRepo.findByNombre("Pop").orElseGet(() -> estiloRepo.save(new Estilo(null, "Pop")));
		Estilo electro = estiloRepo.findByNombre("Electrónica")
				.orElseGet(() -> estiloRepo.save(new Estilo(null, "Electrónica")));
		Estilo jazz = estiloRepo.findByNombre("Jazz").orElseGet(() -> estiloRepo.save(new Estilo(null, "Jazz")));
		Estilo metal = estiloRepo.findByNombre("Heavy Metal")
				.orElseGet(() -> estiloRepo.save(new Estilo(null, "Heavy Metal")));

		// 5. CARGA DE CANCIONES (Mínimo 5 usuarios con canciones, 1 de ellas baneada/reportada)
		Usuario uLuis = usuarioRepo.findByUser("luis").orElse(null);
		Usuario uCarlos = usuarioRepo.findByUser("carlos").orElse(null);
		Usuario uMaria = usuarioRepo.findByUser("maria").orElse(null);
		Usuario uElena = usuarioRepo.findByUser("elena").orElse(null);
		Usuario uDavid = usuarioRepo.findByUser("david").orElse(null);

		if (cancionRepo.count() < 5) {
			if (uLuis != null && cancionRepo.findByTitulo("Titanium").isEmpty()) {
				cancionRepo.save(new Cancion(null, "Titanium", "https://www.youtube.com/watch?v=JRfuAukYTKg", 245, electro,
						"Éxito electrónico de David Guetta y Sia", false, uLuis));
			}

			if (uCarlos != null && cancionRepo.findByTitulo("Bohemian Rhapsody").isEmpty()) {
				cancionRepo.save(new Cancion(null, "Bohemian Rhapsody", "https://www.youtube.com/watch?v=fJ9rUzIMcZQ", 354,
						rock, "Clásico inmortal de Queen", false, uCarlos));
			}

			if (uMaria != null) {
				var optMaria = cancionRepo.findByTitulo("Blinding Lights (Remix)");
				if (optMaria.isEmpty()) {
					cancionRepo.save(new Cancion(null, "Blinding Lights (Remix)", "https://www.youtube.com/watch?v=4NRXx6U8ABQ",
							200, pop, "Audio reportado para moderación de Admin", true, uMaria));
				} else {
					Cancion c = optMaria.get();
					c.setReportada(true);
					cancionRepo.save(c);
				}
			}

			if (uElena != null && cancionRepo.findByTitulo("Take Five").isEmpty()) {
				cancionRepo.save(new Cancion(null, "Take Five", "https://www.youtube.com/watch?v=vmDDOFXSgAs", 324, jazz,
						"Obra maestra del jazz clásico", false, uElena));
			}

			if (uDavid != null && cancionRepo.findByTitulo("Master of Puppets").isEmpty()) {
				cancionRepo.save(new Cancion(null, "Master of Puppets", "https://www.youtube.com/watch?v=E0ozmU9cJDg", 515, metal,
						"Clásico del trash metal de Metallica", false, uDavid));
			}
		}
	}
}
