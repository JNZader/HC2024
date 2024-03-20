package todocode.hackacode.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;

import lombok.*;
import todocode.hackacode.model.TipoPaquete;


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