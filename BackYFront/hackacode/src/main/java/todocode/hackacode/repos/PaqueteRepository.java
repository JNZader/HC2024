package todocode.hackacode.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import todocode.hackacode.domain.Paquete;


public interface PaqueteRepository extends JpaRepository<Paquete, Long> {
}
