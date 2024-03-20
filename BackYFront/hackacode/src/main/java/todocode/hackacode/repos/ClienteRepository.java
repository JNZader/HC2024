package todocode.hackacode.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import todocode.hackacode.domain.Cliente;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
