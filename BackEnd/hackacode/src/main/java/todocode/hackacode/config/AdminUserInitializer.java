package todocode.hackacode.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import todocode.hackacode.domain.Usuario;
import todocode.hackacode.model.Cargo;
import todocode.hackacode.repos.UsuarioRepository;

@Component
public class AdminUserInitializer {

   private final UsuarioRepository usuarioRepository;
   private final BCryptPasswordEncoder passwordEncoder;

   @Value("${security.user.name}")
   private String adminUsername;

   @Value("${security.user.password}")
   private String adminPassword;

   @Autowired
   public AdminUserInitializer(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
      this.usuarioRepository = usuarioRepository;
      this.passwordEncoder = passwordEncoder;
   }

   @PostConstruct
   public void createAdminUser() {
      if (usuarioRepository.findByUsername(adminUsername).isEmpty()) {
         Usuario admin = Usuario.builder()
               .username(adminUsername)
               .password(passwordEncoder.encode(adminPassword))
               .rol(Cargo.ADMIN)
               .passTemporaria(true)
               .build();

         usuarioRepository.save(admin);
      }
   }
}
