package todocode.hackacode.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;

import lombok.*;
import lombok.experimental.SuperBuilder;
import todocode.hackacode.model.Cargo;

@Entity
@Table(name = "Empleadoes")
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

    @Column(nullable = false)
    private Boolean estado;

    @OneToMany(mappedBy = "empleadoid")
    private Set<Venta> ventas;

}
