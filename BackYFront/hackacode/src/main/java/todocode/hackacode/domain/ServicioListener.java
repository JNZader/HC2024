package todocode.hackacode.domain;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import todocode.hackacode.service.PaqueteService;

@Component
public class ServicioListener {

    private final PaqueteService paqueteService;

    public ServicioListener(PaqueteService paqueteService) {
        this.paqueteService = paqueteService;
    }

    @TransactionalEventListener
    public void handlePostPersist(Servicio servicio) {
        Paquete paquete = servicio.getPaqueteid();
        if (paquete != null) {
            // Obtener todos los servicios asociados al mismo paquete
            Iterable<Servicio> servicios = paquete.getServicios();
            int count = 0;
            double precioVentaTotal = 0.0;
            for (Servicio s : servicios) {
                precioVentaTotal += s.getPrecioVenta().getPrecioVenta();
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
