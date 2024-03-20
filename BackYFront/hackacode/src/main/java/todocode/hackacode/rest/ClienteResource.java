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
import todocode.hackacode.domain.Cliente;
import todocode.hackacode.model.ClienteDTO;
import todocode.hackacode.service.impl.ClienteServiceImpl;
import todocode.hackacode.util.ReferencedException;
import todocode.hackacode.util.ReferencedWarning;

@RestController
@RequestMapping(value = "/api/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClienteResource {

    private final ClienteServiceImpl clienteServiceImpl;
    private final EntityManager entityManager;

    @Autowired
    public ClienteResource(final ClienteServiceImpl clienteServiceImpl, EntityManager entityManager) {
        this.clienteServiceImpl = clienteServiceImpl;
        this.entityManager = entityManager;
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getAllClientes() {
        return ResponseEntity.ok(clienteServiceImpl.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getCliente(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(clienteServiceImpl.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCliente(@RequestBody @Valid final ClienteDTO clienteDTO) {
        final Long createdId = clienteServiceImpl.create(clienteDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCliente(@PathVariable(name = "id") final Long id,
                                              @RequestBody @Valid final ClienteDTO clienteDTO) {
        clienteServiceImpl.update(id, clienteDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCliente(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = clienteServiceImpl.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        clienteServiceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam String atributo, @RequestParam String valor,
                                    @RequestParam(required = false) String operador) {

        // Validación de atributos
        boolean atributoValido = false;
        for (Field field : Cliente.class.getDeclaredFields()) {
            if (field.getName().equals(atributo)) {
                atributoValido = true;
                break;
            }
        }
        if (!atributoValido) {
            return ResponseEntity.badRequest().body("Atributo no válido: " + atributo);
        }

        // Conversión de tipos
        Object valorConvertido = valor;
        // Si necesitas convertir el valor a otro tipo, puedes hacerlo aquí

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> root = criteriaQuery.from(Cliente.class);

        Predicate predicate;

        if (operador != null) {
            switch (operador.toLowerCase()) {
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

        List<Cliente> resultados = entityManager.createQuery(criteriaQuery).getResultList();
        return ResponseEntity.ok(resultados);
    }

}
