package todocode.hackacode.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;

import lombok.*;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Persona {

    @Column
    private String nombre;

    @Column
    private String apellido;

    @Column
    private String direccion;

    @Column(nullable = false, unique = true)
    private String dni;

    @Column
    private LocalDate fechaNacimiento;

    @Column
    private String nacionalidad;

    @Column
    private String celular;

    @Column(nullable = false, unique = true)
    private String email;

}
