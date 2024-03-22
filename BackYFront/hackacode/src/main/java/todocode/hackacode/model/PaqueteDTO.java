package todocode.hackacode.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PaqueteDTO {

    private Long id;

    @NotNull
    private TipoPaquete tipo;

    @Size(max = 255)
    private String nombre;

    @Size(max = 255)
    private String descripcionBreve;

    @NotNull
    private Double precioVenta;

    private Integer duracion;

    @NotNull
    private Boolean estado;

}
