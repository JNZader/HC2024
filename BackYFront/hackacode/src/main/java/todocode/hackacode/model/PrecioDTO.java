package todocode.hackacode.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrecioDTO {

    private Long id;

    @NotNull
    private Long idServico;

    @NotNull
    private Double costo;

    @NotNull
    private Double precioVenta;

    @NotNull
    @PrecioIdServicioUnique
    private Long idServicio;

}
