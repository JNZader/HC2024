package todocode.hackacode.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import todocode.hackacode.model.TipoPaquete;

import java.util.Set;

@Entity
@Table(name = "Paquetes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paquete {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoPaquete tipo;

    @Column
    private String nombre;

    @Column
    private String descripcionBreve;

    @Column(nullable = false)
    private Double precioVenta;

    @Column
    private Integer duracion;

    @Column(nullable = false)
    private Boolean estado;

    @OneToMany(mappedBy = "paqueteid")
    private Set<Servicio> servicios;

    @ManyToMany(mappedBy = "paquetes")
    private Set<Venta> ventas;

}
