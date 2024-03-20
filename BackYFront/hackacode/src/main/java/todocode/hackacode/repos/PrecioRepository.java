package todocode.hackacode.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import todocode.hackacode.domain.Precio;
import todocode.hackacode.domain.Servicio;


public interface PrecioRepository extends JpaRepository<Precio, Long> {

    Precio findFirstByIdServicio(Servicio servicio);

    boolean existsByIdServicioId(Long id);

}
