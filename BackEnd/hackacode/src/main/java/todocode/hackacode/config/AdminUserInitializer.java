package todocode.hackacode.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import todocode.hackacode.domain.Usuario;
import todocode.hackacode.model.Cargo;
import todocode.hackacode.repos.UsuarioRepository;

/**
 * Clase que inicializa un usuario administrador en la base de datos si no
 * existe.
 */
@Component
public class AdminUserInitializer {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${security.user.name}")
    private String adminUsername;

    @Value("${security.user.password}")
    private String adminPassword;

    /**
     * Constructor de la clase AdminUserInitializer.
     *
     * @param usuarioRepository Repositorio de usuarios.
     * @param passwordEncoder Codificador de contraseñas.
     */
    @Autowired
    public AdminUserInitializer(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Método ejecutado después de la construcción del bean para crear el
     * usuario administrador si no existe.
     */
    @PostConstruct
    public void createAdminUser() {
        // Verifica si el usuario administrador ya existe en la base de datos
        if (usuarioRepository.findByUsername(adminUsername).isEmpty()) {
            // Si no existe, crea un nuevo usuario administrador
            Usuario admin = Usuario.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .rol(Cargo.ADMIN)
                    .passTemporaria(true) // Indica que la contraseña es temporal
                    .build();

            // Guarda el usuario administrador en la base de datos
            usuarioRepository.save(admin);
        }
    }
}
