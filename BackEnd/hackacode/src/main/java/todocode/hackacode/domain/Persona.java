package todocode.hackacode.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Persona {

    @Column
    @Pattern(regexp = "^[a-zA-Záéíóúñü ]+$", message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @Column
    @Pattern(regexp = "^[a-zA-Záéíóúñü ]+$", message = "El apellido solo puede contener letras y espacios")
    private String apellido;

    @Column
    @Pattern(regexp = "^[a-zA-Záéíóúñü0-9\\-\\., ]+$", message = "La dirección solo puede contener letras, números, guiones, comas, puntos y espacios")
    private String direccion;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 caracteres")
    private String dni;

    @Column
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @Column
    @Pattern(regexp = "^[a-zA-Záéíóúñü ]+$", message = "La nacionalidad solo puede contener letras y espacios")
    private String nacionalidad;

    @Column
    @Size(min = 10, max = 15, message = "El celular debe tener entre 10 y 15 caracteres")
    @Pattern(regexp = "^[\\d\\+-]+$", message = "El celular solo puede contener números, +, -")
    private String celular;

    @Column(nullable = false, unique = true)
    @Email(message = "El email debe tener un formato válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

}
