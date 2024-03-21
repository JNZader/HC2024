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
import todocode.hackacode.domain.Paquete;
import todocode.hackacode.model.PaqueteDTO;
import todocode.hackacode.service.PaqueteService;
import todocode.hackacode.service.impl.PaqueteServiceImpl;
import todocode.hackacode.util.ReferencedException;
import todocode.hackacode.util.ReferencedWarning;

@RestController
@RequestMapping(value = "/api/paquetes", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaqueteResource {

    private final PaqueteServiceImpl paqueteServiceImpl;
    private final EntityManager entityManager;

    @Autowired
    public PaqueteResource(final PaqueteServiceImpl paqueteServiceImpl, EntityManager entityManager) {
        this.paqueteServiceImpl = paqueteServiceImpl;
        this.entityManager = entityManager;
    }

    @GetMapping
    public ResponseEntity<List<PaqueteDTO>> getAllPaquetes() {
        return ResponseEntity.ok(paqueteServiceImpl.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaqueteDTO> getPaquete(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(paqueteServiceImpl.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPaquete(@RequestBody @Valid final PaqueteDTO paqueteDTO) {
        final Long createdId = paqueteServiceImpl.create(paqueteDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePaquete(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PaqueteDTO paqueteDTO) {
        paqueteServiceImpl.update(id, paqueteDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePaquete(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = paqueteServiceImpl.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        paqueteServiceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam String atributo, @RequestParam String valor,
            @RequestParam(required = false) String operador) {

        // Validación de atributos
        boolean atributoValido = false;
        for (Field field : Paquete.class.getDeclaredFields()) {
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
            valorConvertido = switch (Paquete.class.getDeclaredField(atributo).getType().getName()) {
                case "java.lang.Double" ->
                    Double.parseDouble(valor);
                case "java.lang.Integer" ->
                    Integer.parseInt(valor);
                case "java.lang.Boolean" ->
                    Boolean.parseBoolean(valor);
                default ->
                    valor;
            };
        } catch (NoSuchFieldException | NumberFormatException e) {
            return ResponseEntity.badRequest().body("Error al convertir el valor: " + valor);
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Paquete> criteriaQuery = criteriaBuilder.createQuery(Paquete.class);
        Root<Paquete> root = criteriaQuery.from(Paquete.class);

        Predicate predicate;

        if (operador != null) {
            switch (operador.toLowerCase()) {
                case "mayor":
                    predicate = criteriaBuilder.greaterThan(root.get(atributo), (Comparable) valorConvertido);
                    break;
                case "menor":
                    predicate = criteriaBuilder.lessThan(root.get(atributo), (Comparable) valorConvertido);
                    break;
                case "igual":
                    predicate = criteriaBuilder.equal(root.get(atributo), valorConvertido);
                    break;
                case "like":
                    predicate = criteriaBuilder.like(root.get(atributo), "%" + valor + "%");
                    break;
                default:
                    return ResponseEntity.badRequest().body("Operador no válido: " + operador);
            }
        } else {
            predicate = criteriaBuilder.equal(root.get(atributo), valorConvertido);
        }

        criteriaQuery.select(root).where(predicate);

        List<Paquete> resultados = entityManager.createQuery(criteriaQuery).getResultList();
        return ResponseEntity.ok(resultados);
    }

}
