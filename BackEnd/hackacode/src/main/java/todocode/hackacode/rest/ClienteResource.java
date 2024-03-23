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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import todocode.hackacode.domain.Cliente;
import todocode.hackacode.model.ClienteDTO;
import todocode.hackacode.service.ClienteService;
import todocode.hackacode.service.impl.ClienteServiceImpl;
import todocode.hackacode.util.ReferencedException;
import todocode.hackacode.util.ReferencedWarning;

import java.lang.reflect.Field;
import java.util.List;

@RestController
@RequestMapping(value = "/api/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClienteResource {

   private final ClienteServiceImpl clienteServiceImpl;
   private final EntityManager entityManager;
   private final ClienteService clienteService;

   @Autowired
   public ClienteResource(final ClienteServiceImpl clienteServiceImpl, EntityManager entityManager, ClienteService clienteService) {
      this.clienteServiceImpl = clienteServiceImpl;
      this.entityManager = entityManager;
      this.clienteService = clienteService;
   }

   /**
    * Obtiene todos los clientes.
    *
    * @return ResponseEntity con la lista de clientes.
    */
   @GetMapping
   public ResponseEntity<List<ClienteDTO>> getAllClientes() {
      return ResponseEntity.ok(clienteServiceImpl.findAll());
   }

   /**
    * Obtiene un cliente por su ID.
    *
    * @param id ID del cliente.
    * @return ResponseEntity con el cliente encontrado.
    */
   @GetMapping("/{id}")
   public ResponseEntity<ClienteDTO> getCliente(@PathVariable(name = "id") final Long id) {
      return ResponseEntity.ok(clienteServiceImpl.get(id));
   }

   /**
    * Crea un nuevo cliente.
    *
    * @param clienteDTO DTO del cliente a crear.
    * @return ResponseEntity con el ID del cliente creado.
    */
   @PostMapping
   @ApiResponse(responseCode = "201")
   public ResponseEntity<Long> createCliente(@RequestBody @Valid final ClienteDTO clienteDTO) {
      final Long createdId = clienteServiceImpl.create(clienteDTO);
      return new ResponseEntity<>(createdId, HttpStatus.CREATED);
   }

   /**
    * Actualiza un cliente existente.
    *
    * @param id         ID del cliente a actualizar.
    * @param clienteDTO DTO con los datos actualizados del cliente.
    * @return ResponseEntity con el ID del cliente actualizado.
    */
   @PutMapping("/{id}")
   public ResponseEntity<Long> updateCliente(@PathVariable(name = "id") final Long id,
                                             @RequestBody @Valid final ClienteDTO clienteDTO) {
      Cliente cliente = clienteService.findById(id);

      Cliente clienteActualizado = clienteServiceImpl.updateCliente(clienteDTO, cliente);

      clienteServiceImpl.update(id, clienteServiceImpl.mapToDTO(clienteActualizado, clienteDTO));
      return ResponseEntity.ok(id);
   }

   /**
    * Elimina un cliente por su ID.
    *
    * @param id ID del cliente a eliminar.
    * @return ResponseEntity que indica el éxito de la operación.
    */
   @DeleteMapping("/{id}")
   @ApiResponse(responseCode = "204")
   @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
   public ResponseEntity<Void> deleteCliente(@PathVariable(name = "id") final Long id) {
      final ReferencedWarning referencedWarning = clienteServiceImpl.getReferencedWarning(id);
      if (referencedWarning != null) {
         throw new ReferencedException(referencedWarning);
      }
      clienteServiceImpl.delete(id);
      return ResponseEntity.noContent().build();
   }

   /**
    * Busca clientes por un atributo específico.
    *
    * @param atributo Atributo por el cual buscar.
    * @param valor    Valor del atributo por el cual buscar.
    * @param operador Operador de comparación (opcional).
    * @return ResponseEntity con la lista de clientes que coinciden con la búsqueda.
    */

   //ejemplo /buscar?atributo=nombre&valor=Juan
   @GetMapping("/buscar")
   public ResponseEntity<?> buscar(@RequestParam String atributo, @RequestParam String valor,
                                   @RequestParam(required = false) String operador) {

      // Validación de atributos
      boolean atributoValido = false;
      for (Field field : ClienteDTO.class.getDeclaredFields()) {

         if (field.getName().equals(atributo)) {

            atributoValido = true;

            break;
         }
      }

      if (!atributoValido) {
         return ResponseEntity.badRequest().body("Atributo no válido: " + atributo);
      }

      CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

      CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);

      Root<Cliente> root = criteriaQuery.from(Cliente.class);

      Predicate predicate;

      if (operador != null) {
         switch (operador.toLowerCase()) {
            case "igual" -> predicate = criteriaBuilder.equal(root.get(atributo), valor);
            case "like" -> predicate = criteriaBuilder.like(root.get(atributo), "%" + valor + "%");
            default -> {
               return ResponseEntity.badRequest().body("Operador no válido: " + operador);
            }
         }
      } else {

         predicate = criteriaBuilder.equal(root.get(atributo), valor);
      }

      criteriaQuery.select(root).where(predicate);

      List<Cliente> resultados = entityManager.createQuery(criteriaQuery).getResultList();

      List<ClienteDTO> resultadosDTO = clienteServiceImpl.mapToDTOList(resultados);

      return ResponseEntity.ok(resultadosDTO);
   }

}
