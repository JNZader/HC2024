package todocode.hackacode.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todocode.hackacode.domain.Precio;
import todocode.hackacode.model.PrecioDTO;
import todocode.hackacode.service.impl.PrecioServiceImpl;

import java.lang.reflect.Field;
import java.util.List;

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
    /**
     * Obtiene todos los precios.
     *
     * @return ResponseEntity con la lista de precios.
     */
    @GetMapping
    public ResponseEntity<List<PrecioDTO>> getAllPrecios() {
        return ResponseEntity.ok(precioServiceImpl.findAll());
    }
    /**
     * Obtiene un precio por su ID.
     *
     * @param id ID del precio.
     * @return ResponseEntity con el precio encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PrecioDTO> getPrecio(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(precioServiceImpl.get(id));
    }
    /**
     * Crea un nuevo precio.
     *
     * @param precioDTO DTO del precio a crear.
     * @return ResponseEntity con el ID del precio creado.
     */
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPrecio(@RequestBody @Valid final PrecioDTO precioDTO) {
        final Long createdId = precioServiceImpl.create(precioDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }
    /**
     * Actualiza un precio existente.
     *
     * @param id         ID del precio a actualizar.
     * @param precioDTO DTO con los datos actualizados del precio.
     * @return ResponseEntity con el ID del precio actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePrecio(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PrecioDTO precioDTO) {
        precioServiceImpl.update(id, precioDTO);
        return ResponseEntity.ok(id);
    }
    /**
     * Elimina un precio por su ID.
     *
     * @param id ID del precio a eliminar.
     * @return ResponseEntity que indica el éxito de la operación.
     */
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePrecio(@PathVariable(name = "id") final Long id) {
        precioServiceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }
    /**
     * Busca precios por un atributo específico.
     *
     * @param atributo  Atributo por el cual buscar.
     * @param valor     Valor del atributo por el cual buscar.
     * @param operador  Operador de comparación (opcional).
     * @return ResponseEntity con la lista de precios que coinciden con la búsqueda.
     */
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
                case "java.lang.Long" ->
                    Long.parseLong(valor);
                case "java.lang.Double" ->
                    Double.parseDouble(valor);
                default ->
                    valor;
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
                case "mayor" -> predicate = criteriaBuilder.greaterThan(root.get(atributo), valorConvertido.toString());
                case "menor" -> predicate = criteriaBuilder.lessThan(root.get(atributo), valorConvertido.toString());
                case "igual" -> predicate = criteriaBuilder.equal(root.get(atributo), valorConvertido);
                default -> {
                    return ResponseEntity.badRequest().body("Operador no válido: " + operador);
                }
            }
        } else {
            predicate = criteriaBuilder.equal(root.get(atributo), valorConvertido);
        }

        criteriaQuery.select(root).where(predicate);

        List<Precio> resultados = entityManager.createQuery(criteriaQuery).getResultList();
        return ResponseEntity.ok(resultados);
    }

}
