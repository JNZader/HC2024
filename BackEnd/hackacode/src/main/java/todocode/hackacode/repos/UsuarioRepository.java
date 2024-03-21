package todocode.hackacode.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import todocode.hackacode.domain.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

   Optional<Usuario> findByUsername(String username);

}
