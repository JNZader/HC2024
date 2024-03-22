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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import todocode.hackacode.domain.Empleado;
import todocode.hackacode.domain.Usuario;
import todocode.hackacode.model.EmpleadoDTO;
import todocode.hackacode.repos.UsuarioRepository;
import todocode.hackacode.service.impl.EmpleadoServiceImpl;
import todocode.hackacode.util.ReferencedException;
import todocode.hackacode.util.ReferencedWarning;

import java.lang.reflect.Field;
import java.util.List;

@RestController
@RequestMapping(value = "/api/empleados", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmpleadoResource {

    private final EmpleadoServiceImpl empleadoServiceImpl;
    private final EntityManager entityManager;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public EmpleadoResource(final EmpleadoServiceImpl empleadoServiceImpl, EntityManager entityManager, UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.empleadoServiceImpl = empleadoServiceImpl;
        this.entityManager = entityManager;
       this.usuarioRepository = usuarioRepository;
       this.passwordEncoder = passwordEncoder;
    }
    /**
     * Obtiene todos los empleados.
     *
     * @return ResponseEntity con la lista de empleados.
     */
    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> getAllEmpleados() {
        return ResponseEntity.ok(empleadoServiceImpl.findAll());
    }
    /**
     * Obtiene un empleado por su ID.
     *
     * @param id ID del empleado.
     * @return ResponseEntity con el empleado encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> getEmpleado(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(empleadoServiceImpl.get(id));
    }
    /**
     * Crea un nuevo empleado.
     *
     * @param empleadoDTO DTO del empleado a crear.
     * @return ResponseEntity con el ID del empleado creado.
     */
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createEmpleado(@RequestBody @Valid final EmpleadoDTO empleadoDTO) {

        Usuario usuario=Usuario.builder()
              .id(empleadoDTO.getIdUsuario())
              .username(empleadoDTO.getEmail())
              .password(passwordEncoder.encode(empleadoDTO.getDni()))
              .rol(empleadoDTO.getCargo())
              .passTemporaria(true)
              .build();

        final Long createdId = empleadoServiceImpl.create(empleadoDTO);

        usuarioRepository.save(usuario);

        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }
    /**
     * Actualiza un empleado existente.
     *
     * @param id         ID del empleado a actualizar.
     * @param empleadoDTO DTO con los datos actualizados del empleado.
     * @return ResponseEntity con el ID del empleado actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateEmpleado(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final EmpleadoDTO empleadoDTO) {
        empleadoServiceImpl.update(id, empleadoDTO);
        return ResponseEntity.ok(id);
    }
    /**
     * Elimina un empleado por su ID.
     *
     * @param id ID del empleado a eliminar.
     * @return ResponseEntity que indica el éxito de la operación.
     */
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = empleadoServiceImpl.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        empleadoServiceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }
    /**
     * Busca empleados por un atributo específico.
     *
     * @param atributo  Atributo por el cual buscar.
     * @param valor     Valor del atributo por el cual buscar.
     * @param operador  Operador de comparación (opcional).
     * @return ResponseEntity con la lista de empleados que coinciden con la búsqueda.
     */
    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam String atributo, @RequestParam String valor,
            @RequestParam(required = false) String operador) {

        // Validación de atributos
        boolean atributoValido = false;
        for (Field field : Empleado.class.getDeclaredFields()) {
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
            valorConvertido = switch (Empleado.class.getDeclaredField(atributo).getType().getName()) {
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
        CriteriaQuery<Empleado> criteriaQuery = criteriaBuilder.createQuery(Empleado.class);
        Root<Empleado> root = criteriaQuery.from(Empleado.class);

        Predicate predicate;

        if (operador != null) {
            switch (operador.toLowerCase()) {
                case "mayor" -> predicate = criteriaBuilder.greaterThan(root.get(atributo), (Comparable) valorConvertido);
                case "menor" -> predicate = criteriaBuilder.lessThan(root.get(atributo), (Comparable) valorConvertido);
                case "igual" -> predicate = criteriaBuilder.equal(root.get(atributo), valorConvertido);
                case "like" -> predicate = criteriaBuilder.like(root.get(atributo), "%" + valor + "%");
                default -> {
                    return ResponseEntity.badRequest().body("Operador no válido: " + operador);
                }
            }
        } else {
            predicate = criteriaBuilder.equal(root.get(atributo), valorConvertido);
        }

        criteriaQuery.select(root).where(predicate);

        List<Empleado> resultados = entityManager.createQuery(criteriaQuery).getResultList();
        return ResponseEntity.ok(resultados);
    }

}
