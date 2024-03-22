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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todocode.hackacode.domain.Servicio;
import todocode.hackacode.model.ServicioDTO;
import todocode.hackacode.service.impl.ServicioServiceImpl;
import todocode.hackacode.util.ReferencedException;
import todocode.hackacode.util.ReferencedWarning;

@RestController
@RequestMapping(value = "/api/servicios", produces = MediaType.APPLICATION_JSON_VALUE)
public class ServicioResource {

    private final ServicioServiceImpl servicioServiceImpl;
    private final EntityManager entityManager;

    @Autowired
    public ServicioResource(final ServicioServiceImpl servicioServiceImpl, EntityManager entityManager) {
        this.servicioServiceImpl = servicioServiceImpl;
        this.entityManager = entityManager;
    }
    /**
     * Obtiene todos los servicios.
     *
     * @return ResponseEntity con la lista de servicios.
     */
    @GetMapping
    public ResponseEntity<List<ServicioDTO>> getAllServicios() {
        return ResponseEntity.ok(servicioServiceImpl.findAll());
    }
    /**
     * Obtiene un servicio por su ID.
     *
     * @param id ID del servicio.
     * @return ResponseEntity con el servicio encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTO> getServicio(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(servicioServiceImpl.get(id));
    }
    /**
     * Crea un nuevo servicio.
     *
     * @param servicioDTO DTO del servicio a crear.
     * @return ResponseEntity con el ID del servicio creado.
     */
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createServicio(@RequestBody @Valid final ServicioDTO servicioDTO) {
        final Long createdId = servicioServiceImpl.create(servicioDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }
    /**
     * Actualiza un servicio existente.
     *
     * @param id         ID del servicio a actualizar.
     * @param servicioDTO DTO con los datos actualizados del servicio.
     * @return ResponseEntity con el ID del servicio actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateServicio(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ServicioDTO servicioDTO) {
        servicioServiceImpl.update(id, servicioDTO);
        return ResponseEntity.ok(id);
    }
    /**
     * Elimina un servicio por su ID.
     *
     * @param id ID del servicio a eliminar.
     * @return ResponseEntity que indica el éxito de la operación.
     */
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteServicio(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = servicioServiceImpl.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        servicioServiceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }
    /**
     * Busca servicios por un atributo específico.
     *
     * @param atributo  Atributo por el cual buscar.
     * @param valor     Valor del atributo por el cual buscar.
     * @param operador  Operador de comparación (opcional).
     * @return ResponseEntity con la lista de servicios que coinciden con la búsqueda.
     */
    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam String atributo, @RequestParam String valor,
            @RequestParam(required = false) String operador) {

        // Validación de atributos
        boolean atributoValido = false;
        for (Field field : Servicio.class.getDeclaredFields()) {
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
            valorConvertido = switch (Servicio.class.getDeclaredField(atributo).getType().getName()) {
                case "java.lang.Integer" ->
                    Integer.parseInt(valor);
                case "java.lang.Double" ->
                    Double.parseDouble(valor);
                case "java.time.LocalDate" ->
                    LocalDate.parse(valor);
                case "java.lang.Boolean" ->
                    Boolean.parseBoolean(valor);
                default ->
                    valor;
            };
        } catch (NoSuchFieldException | NumberFormatException e) {
            return ResponseEntity.badRequest().body("Error al convertir el valor: " + valor);
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Servicio> criteriaQuery = criteriaBuilder.createQuery(Servicio.class);
        Root<Servicio> root = criteriaQuery.from(Servicio.class);

        Predicate predicate;

        if (operador != null) {
            switch (operador.toLowerCase()) {
                case "mayor" -> predicate = criteriaBuilder.greaterThan(root.get(atributo), valorConvertido.toString());
                case "menor" -> predicate = criteriaBuilder.lessThan(root.get(atributo), valorConvertido.toString());
                case "igual" -> predicate = criteriaBuilder.equal(root.get(atributo), valorConvertido);
                case "like" -> predicate = criteriaBuilder.like(root.get(atributo), "%" + valorConvertido + "%");
                default -> {
                    return ResponseEntity.badRequest().body("Operador no válido: " + operador);
                }
            }
        } else {
            predicate = criteriaBuilder.equal(root.get(atributo), valorConvertido);
        }

        criteriaQuery.select(root).where(predicate);

        List<Servicio> resultados = entityManager.createQuery(criteriaQuery).getResultList();
        return ResponseEntity.ok(resultados);
    }

}
