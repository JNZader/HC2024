package todocode.hackacode.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ClienteDTO {

    private Long id;

    @Size(max = 255)
    private String nombre;

    @Size(max = 255)
    private String apellido;

    @Size(max = 255)
    private String direccion;

    @Size(max = 255)
    private String dni;

    private LocalDate fechaNacimiento;

    @Size(max = 255)
    private String nacionalidad;

    @Size(max = 255)
    private String celular;

    @Size(max = 255)
    private String email;

    @NotNull
    private Boolean estado;

}
