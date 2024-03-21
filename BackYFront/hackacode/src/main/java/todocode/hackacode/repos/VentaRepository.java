package todocode.hackacode.repos;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import todocode.hackacode.domain.Cliente;
import todocode.hackacode.domain.Empleado;
import todocode.hackacode.domain.Paquete;
import todocode.hackacode.domain.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    Venta findFirstByClienteId(Cliente cliente);

    Venta findFirstByEmpleadoid(Empleado empleado);

    Venta findFirstByPaquetes(Paquete paquete);

    List<Venta> findAllByPaquetes(Paquete paquete);

    List<Venta> findByFecha(LocalDate fecha);

    List<Venta> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

}
