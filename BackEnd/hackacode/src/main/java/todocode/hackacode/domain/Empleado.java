package todocode.hackacode.domain;

import jakarta.persistence.*;

import java.util.Set;

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
    private Double sueldo;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String dni;

    @Column(nullable = false)
    private Boolean estado;

    @OneToMany(mappedBy = "empleadoid")
    private Set<Venta> ventas;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

}
