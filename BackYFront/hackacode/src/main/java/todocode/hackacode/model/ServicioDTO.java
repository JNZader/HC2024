package todocode.hackacode.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicioDTO {

    private Long id;

    @Size(max = 255)
    private String nombre;

    @Size(max = 255)
    private String descripcion;

    private LocalDate fecha;

    private Integer duracion;

    @NotNull
    private Boolean estado;

    private Long paqueteid;

}
