package todocode.hackacode.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import todocode.hackacode.model.Medpago;

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
    @Transient
    private Double monto;

    @Column(nullable = false)
    @CreationTimestamp
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

    @PrePersist
    public void calcularMontoTotal() {
        double total = 0.0;
        if (paquetes != null) {
            for (Paquete paquete : paquetes) {
                total += paquete.getPrecioVenta();
            }
        }
        setMonto(total);
    }

}
