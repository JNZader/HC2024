package todocode.hackacode.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

import lombok.*;

@Entity
@Table(name = "Servicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(ServicioListener.class)
public class Servicio {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Column
    private String descripcion;

    @Column
    private LocalDate fecha;

    @Column
    private Integer duracion;

    @Column(nullable = false)
    private Boolean estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paqueteid_id")
    private Paquete paqueteid;

    @OneToOne(mappedBy = "idServicio", fetch = FetchType.LAZY)
    private Precio precioVenta;

    public Paquete getPaqueteid() {

        return this.paqueteid;

    }

    public Object getPrecioVenta() {

        return this.precioVenta;

    }

}
