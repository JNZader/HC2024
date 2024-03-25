package todocode.hackacode.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
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
    @Pattern(regexp = "^[a-zA-Záéíóúñü ]+$", message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @Column
    @Pattern(regexp = "^[a-zA-Záéíóúñü0-9\\-\\., ]+$", message = "La descripcion solo puede " +
          "contener letras, números, guiones, comas, puntos y espacios")
    private String descripcion;

    @Column
    @PastOrPresent(message = "La fecha debe ser una fecha presente o futura")
    private LocalDate fecha;

    @Column
    @Positive(message = "La duración debe ser un número positivo")
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
