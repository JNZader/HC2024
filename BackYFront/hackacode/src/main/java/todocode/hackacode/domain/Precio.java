package todocode.hackacode.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

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
