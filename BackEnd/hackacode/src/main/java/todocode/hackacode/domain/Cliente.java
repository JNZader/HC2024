package todocode.hackacode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "Clientes")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Cliente extends Persona {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean estado;

    @OneToMany(mappedBy = "clienteId")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Venta> ventas;

}
