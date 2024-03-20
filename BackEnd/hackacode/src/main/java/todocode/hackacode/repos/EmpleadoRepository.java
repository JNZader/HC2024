package todocode.hackacode.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import todocode.hackacode.domain.Empleado;


public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}
