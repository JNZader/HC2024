package todocode.hackacode.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import todocode.hackacode.domain.Usuario;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

   Optional<Usuario> findById(UUID id);

   Optional<Usuario> findByUsername(String username);

}
