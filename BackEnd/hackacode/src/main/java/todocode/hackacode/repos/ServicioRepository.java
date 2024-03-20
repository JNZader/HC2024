package todocode.hackacode.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import todocode.hackacode.domain.Paquete;
import todocode.hackacode.domain.Servicio;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    Servicio findFirstByPaqueteid(Paquete paquete);

}
