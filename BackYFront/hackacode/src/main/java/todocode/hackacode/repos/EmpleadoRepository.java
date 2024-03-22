package todocode.hackacode.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import todocode.hackacode.domain.Empleado;
@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}
