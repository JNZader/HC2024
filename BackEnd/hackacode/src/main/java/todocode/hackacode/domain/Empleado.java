package todocode.hackacode.domain;

import jakarta.persistence.*;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import todocode.hackacode.model.Cargo;

@Entity
@Table(name = "Empleados")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Empleado extends Persona {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Cargo cargo;

    @Column
    @Positive(message = "El sueldo debe ser un número positivo")
    private Double sueldo;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 caracteres")
    private String email;

    @Column(nullable = false, unique = true)
    @Email(message = "El email debe tener un formato válido")
    @NotBlank(message = "El email es obligatorio")
    private String dni;

    @Column(nullable = false)
    private Boolean estado;

    @OneToMany(mappedBy = "empleadoid")
    private Set<Venta> ventas;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

}
