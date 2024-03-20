package todocode.hackacode.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todocode.hackacode.domain.Cliente;
import todocode.hackacode.domain.Empleado;
import todocode.hackacode.domain.Paquete;
import todocode.hackacode.domain.Venta;
import todocode.hackacode.model.VentaDTO;
import todocode.hackacode.repos.ClienteRepository;
import todocode.hackacode.repos.EmpleadoRepository;
import todocode.hackacode.repos.PaqueteRepository;
import todocode.hackacode.service.VentaService;
import todocode.hackacode.service.impl.VentaServiceImpl;
import todocode.hackacode.util.CustomCollectors;


@RestController
@RequestMapping(value = "/api/ventas", produces = MediaType.APPLICATION_JSON_VALUE)
public class VentaResource {

    private final VentaServiceImpl ventaServiceImpl;
    private final EntityManager entityManager;

    @Autowired
    public VentaResource(VentaServiceImpl ventaServiceImpl, EntityManager entityManager) {
        this.ventaServiceImpl = ventaServiceImpl;
        this.entityManager = entityManager;
    }

    @GetMapping
    public ResponseEntity<List<VentaDTO>> getAllVentas() {
        return ResponseEntity.ok(ventaServiceImpl.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> getVenta(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(ventaServiceImpl.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVenta(@RequestBody @Valid final VentaDTO ventaDTO) {
        final Long createdId = ventaServiceImpl.create(ventaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateVenta(@PathVariable(name = "id") final Long id,
                                            @RequestBody @Valid final VentaDTO ventaDTO) {
        ventaServiceImpl.update(id, ventaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteVenta(@PathVariable(name = "id") final Long id) {
        ventaServiceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam String atributo, @RequestParam String valor,
                                    @RequestParam(required = false) String operador) {

        // Validación de atributos
        boolean atributoValido = false;
        for (Field field : Venta.class.getDeclaredFields()) {
            if (field.getName().equals(atributo)) {
                atributoValido = true;
                break;
            }
        }
        if (!atributoValido) {
            return ResponseEntity.badRequest().body("Atributo no válido: " + atributo);
        }

        // Conversión de tipos
        Object valorConvertido = null;
        try {
            valorConvertido = switch (Venta.class.getDeclaredField(atributo).getType().getName()) {
                case "java.lang.Integer" -> Integer.parseInt(valor);
                case "java.lang.Double" -> Double.parseDouble(valor);
                case "java.time.LocalDate" -> LocalDate.parse(valor);
                case "java.lang.Boolean" -> Boolean.parseBoolean(valor);
                default -> valor;
            };
        } catch (NoSuchFieldException | NumberFormatException e) {
            return ResponseEntity.badRequest().body("Error al convertir el valor: " + valor);
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Venta> criteriaQuery = criteriaBuilder.createQuery(Venta.class);
        Root<Venta> root = criteriaQuery.from(Venta.class);

        Predicate predicate;

        if (operador != null) {
            switch (operador.toLowerCase()) {
                case "mayor":
                    predicate = criteriaBuilder.greaterThan(root.get(atributo), valorConvertido.toString());
                    break;
                case "menor":
                    predicate = criteriaBuilder.lessThan(root.get(atributo),valorConvertido.toString());
                    break;
                case "igual":
                    predicate = criteriaBuilder.equal(root.get(atributo), valorConvertido);
                    break;
                case "like":
                    predicate = criteriaBuilder.like(root.get(atributo), "%" + valorConvertido + "%");
                    break;
                default:
                    return ResponseEntity.badRequest().body("Operador no válido: " + operador);
            }
        } else {
            predicate = criteriaBuilder.equal(root.get(atributo), valorConvertido);
        }

        criteriaQuery.select(root).where(predicate);

        List<Venta> resultados = entityManager.createQuery(criteriaQuery).getResultList();
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/ganancias/diarias")
    public ResponseEntity<Double> calcularGananciasDiarias(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        Double gananciasDiarias = ventaServiceImpl.calcularGananciasDiarias(fecha);
        return ResponseEntity.ok(gananciasDiarias);
    }

    @GetMapping("/ganancias/mensuales")
    public ResponseEntity<Double> calcularGananciasMensuales(@RequestParam Integer mes, @RequestParam Integer anio) {
        Double gananciasMensuales = ventaServiceImpl.calcularGananciasMensuales(mes, anio);
        return ResponseEntity.ok(gananciasMensuales);
    }

    @GetMapping("/ganancias/rango-fechas")
    public ResponseEntity<Double> calcularGananciasEnRangoFechas(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        Double gananciasEnRango = ventaServiceImpl.calcularGananciasEnRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(gananciasEnRango);
    }
}

