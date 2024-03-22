package todocode.hackacode.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import todocode.hackacode.domain.Usuario;
import todocode.hackacode.model.Cargo;
import todocode.hackacode.repos.UsuarioRepository;

@Component
public class AdminUserInitializer {

   private final UsuarioRepository usuarioRepository;
   private final BCryptPasswordEncoder passwordEncoder;

   @Autowired
   public AdminUserInitializer(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
      this.usuarioRepository = usuarioRepository;
      this.passwordEncoder = passwordEncoder;
   }

   @PostConstruct
   public void createAdminUser() {
      if (usuarioRepository.findByUsername("admin").isEmpty()) {
         Usuario admin = Usuario.builder()
               .username("admin")
               .password(passwordEncoder.encode("admin"))
               .rol(Cargo.ADMIN)
               .passTemporaria(true)
               .build();

         usuarioRepository.save(admin);
      }
   }
}
