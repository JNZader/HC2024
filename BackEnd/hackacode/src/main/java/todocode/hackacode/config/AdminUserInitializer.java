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

   @Autowired
   private UsuarioRepository usuarioRepository;
   @Autowired
   private BCryptPasswordEncoder passwordEncoder;

   @PostConstruct
   public void createAdminUser() {
      if (usuarioRepository.findByUsername("admin").isEmpty()) {
         Usuario admin = new Usuario();
         admin.setUsername("admin");
         admin.setPassword(passwordEncoder.encode("admin"));
         admin.setRol(Cargo.ADMIN);
         admin.setPassTemporaria(true);
         usuarioRepository.save(admin);
      }
   }
}
