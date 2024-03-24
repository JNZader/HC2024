package todocode.hackacode.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Precios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Precio {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idServico;

    @Column(nullable = false)
    private Double costo;

    @Column(nullable = false)
    private Double precioVenta;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servicio_id", nullable = false, unique = true)
    private Servicio idServicio;

}
