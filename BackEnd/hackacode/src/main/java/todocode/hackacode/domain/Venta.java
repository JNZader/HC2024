package todocode.hackacode.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

}
