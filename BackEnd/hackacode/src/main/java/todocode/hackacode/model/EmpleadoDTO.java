package todocode.hackacode.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class EmpleadoDTO {

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
    private Cargo cargo;

    private Double sueldo;

    @NotNull
    private Boolean estado;

    private UUID idUsuario;

}
