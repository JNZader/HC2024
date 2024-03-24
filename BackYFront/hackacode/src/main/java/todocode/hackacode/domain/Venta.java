package todocode.hackacode.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import todocode.hackacode.model.Medpago;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "Ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Medpago medioPago;

    @Column
    private Double monto;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private Boolean estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id_id")
    private Cliente clienteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleadoid_id")
    private Empleado empleadoid;

    @ManyToMany
    @JoinTable(
            name = "VentaPaquetes",
            joinColumns = @JoinColumn(name = "ventaId"),
            inverseJoinColumns = @JoinColumn(name = "paqueteId")
    )
    private Set<Paquete> paquetes;

}
