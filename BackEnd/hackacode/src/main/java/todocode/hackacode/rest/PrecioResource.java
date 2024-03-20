package todocode.hackacode.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todocode.hackacode.domain.Precio;
import todocode.hackacode.model.PrecioDTO;
import todocode.hackacode.service.impl.PrecioServiceImpl;


@RestController
@RequestMapping(value = "/api/precios", produces = MediaType.APPLICATION_JSON_VALUE)
public class PrecioResource {

    private final PrecioServiceImpl precioServiceImpl;
    private final EntityManager entityManager;

    @Autowired
    public PrecioResource(final PrecioServiceImpl precioServiceImpl, EntityManager entityManager) {
        this.precioServiceImpl = precioServiceImpl;
        this.entityManager = entityManager;
    }

    @GetMapping
    public ResponseEntity<List<PrecioDTO>> getAllPrecios() {
        return ResponseEntity.ok(precioServiceImpl.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrecioDTO> getPrecio(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(precioServiceImpl.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPrecio(@RequestBody @Valid final PrecioDTO precioDTO) {
        final Long createdId = precioServiceImpl.create(precioDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePrecio(@PathVariable(name = "id") final Long id,
                                             @RequestBody @Valid final PrecioDTO precioDTO) {
        precioServiceImpl.update(id, precioDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePrecio(@PathVariable(name = "id") final Long id) {
        precioServiceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam String atributo, @RequestParam String valor,
                                    @RequestParam(required = false) String operador) {

        // Validación de atributos
        boolean atributoValido = false;
        for (Field field : Precio.class.getDeclaredFields()) {
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
            valorConvertido = switch (Precio.class.getDeclaredField(atributo).getType().getName()) {
                case "java.lang.Long" -> Long.parseLong(valor);
                case "java.lang.Double" -> Double.parseDouble(valor);
                default -> valor;
            };
        } catch (NoSuchFieldException | NumberFormatException e) {
            return ResponseEntity.badRequest().body("Error al convertir el valor: " + valor);
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Precio> criteriaQuery = criteriaBuilder.createQuery(Precio.class);
        Root<Precio> root = criteriaQuery.from(Precio.class);

        Predicate predicate;

        if (operador != null) {
            switch (operador.toLowerCase()) {
                case "mayor":
                    predicate = criteriaBuilder.greaterThan(root.get(atributo),valorConvertido.toString());
                    break;
                case "menor":
                    predicate = criteriaBuilder.lessThan(root.get(atributo),valorConvertido.toString());
                    break;
                case "igual":
                    predicate = criteriaBuilder.equal(root.get(atributo), valorConvertido);
                    break;
                default:
                    return ResponseEntity.badRequest().body("Operador no válido: " + operador);
            }
        } else {
            predicate = criteriaBuilder.equal(root.get(atributo), valorConvertido);
        }

        criteriaQuery.select(root).where(predicate);

        List<Precio> resultados = entityManager.createQuery(criteriaQuery).getResultList();
        return ResponseEntity.ok(resultados);
    }

}