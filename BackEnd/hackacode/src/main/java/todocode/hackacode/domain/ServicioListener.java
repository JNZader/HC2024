package todocode.hackacode.domain;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import todocode.hackacode.service.PaqueteService;
/**
 * Componente que escucha eventos transaccionales relacionados
 * con la entidad Servicio.
 */
@Component
public class ServicioListener {

    private final PaqueteService paqueteService;

    public ServicioListener(PaqueteService paqueteService) {
        this.paqueteService = paqueteService;
    }
    /**
     * Maneja el evento de persistencia posterior de un Servicio.
     *
     * @param servicio El servicio que ha sido persistido.
     */
    @TransactionalEventListener
    public void handlePostPersist(Servicio servicio) {
        Paquete paquete = servicio.getPaqueteid();
        if (paquete != null) {
            // Obtener todos los servicios asociados al mismo paquete
            Iterable<Servicio> servicios = paquete.getServicios();
            int count = 0;
            double precioVentaTotal = 0.0;
            for (Servicio s : servicios) {
                Precio precio = (Precio) s.getPrecioVenta();
                precioVentaTotal += precio.getPrecioVenta();
                count++;
            }
            // Si hay más de un servicio asociado, aplicar descuento del 10%
            if (count > 1) {
                double precioVentaConDescuento = precioVentaTotal * 0.9;
                paquete.setPrecioVenta(precioVentaConDescuento);

                paqueteService.update(paquete.getId(), paqueteService.mapToDTO(paquete)); // Actualizar el paquete en la base de datos
            }
        }
    }
}
