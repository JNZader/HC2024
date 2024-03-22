package todocode.hackacode.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class VentaDTO {

    private Long id;

    @NotNull
    private Medpago medioPago;

    private Double monto;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private Boolean estado;

    private Long clienteId;

    private Long empleadoid;

    private List<Long> paquetes;

}
