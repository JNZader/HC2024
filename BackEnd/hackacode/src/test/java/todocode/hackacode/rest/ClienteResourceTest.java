package todocode.hackacode.rest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import todocode.hackacode.domain.Cliente;
import todocode.hackacode.model.ClienteDTO;
import todocode.hackacode.service.impl.ClienteServiceImpl;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteResourceTest {
/*
    private ClienteResource clienteResource;
    private ClienteServiceImpl clienteService;

    private Validator validator;


    @BeforeEach
    public void setUp() {
        clienteService = mock(ClienteServiceImpl.class);
        clienteResource = new ClienteResource(clienteService, null); // No necesitamos el EntityManager en este test
    }

    @Test
    public void testGetAllClientes() {
        // Simular el resultado del servicio
        List<ClienteDTO> clientesMock = List.of(new ClienteDTO(), new ClienteDTO());
        when(clienteService.findAll()).thenReturn(clientesMock);

        // Llamar al método que estamos probando
        ResponseEntity<List<ClienteDTO>> responseEntity = clienteResource.getAllClientes();

        // Verificar que la respuesta no sea nula
        assertNotNull(responseEntity);
        // Verificar que el código de estado sea 200 OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Verificar que el cuerpo de la respuesta no sea nulo y contenga al menos un cliente
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().size() > 0);
    }

    @Test
    public void testGetCliente() {
        // Simular el resultado del servicio para un cliente específico
        Long clienteId = 1L;
        ClienteDTO clienteDTO = new ClienteDTO(); // Aquí deberías definir el DTO del cliente con datos simulados
        when(clienteService.get(clienteId)).thenReturn(clienteDTO);

        // Llamar al método que estamos probando con un ID específico
        ResponseEntity<ClienteDTO> responseEntity = clienteResource.getCliente(clienteId);

        // Verificar que la respuesta no sea nula
        assertNotNull(responseEntity);
        // Verificar que el código de estado sea 200 OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Verificar que el cuerpo de la respuesta no sea nulo y contenga el DTO del cliente esperado
        assertNotNull(responseEntity.getBody());

    }



        @Test
        public void testCreateCliente() {
            // Simulamos un DTO de cliente válido
            ClienteDTO clienteDTO = new ClienteDTO();
            // Aquí podrías inicializar los campos necesarios del DTO

            // Simulamos el ID creado por el servicio
            Long createdId = 1L;

            // Simulamos el comportamiento del servicio al crear un cliente
            ClienteServiceImpl clienteService = mock(ClienteServiceImpl.class);
            when(clienteService.create(clienteDTO)).thenReturn(createdId);

            // Creamos el recurso del cliente con el servicio simulado
            ClienteResource clienteResource = new ClienteResource(clienteService, null); // Aquí podrías necesitar inicializar otros componentes si son necesarios

            // Llamamos al método que estamos probando
            ResponseEntity<Long> responseEntity = clienteResource.createCliente(clienteDTO);

            // Verificamos que la respuesta no sea nula
            assertNotNull(responseEntity);
            // Verificamos que el código de estado sea 201 CREATED
            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
            // Verificamos que el cuerpo de la respuesta contenga el ID del cliente creado
            assertEquals(createdId, responseEntity.getBody());

            // Verificamos que el método create() del servicio haya sido llamado exactamente una vez con el DTO de cliente
            verify(clienteService, times(1)).create(clienteDTO);
        }





    @Test
    public void testUpdateCliente() {
        // Simulamos un ID de cliente existente y un DTO de cliente válido
        Long clientId = 1L;
        ClienteDTO clienteDTO = new ClienteDTO();
        // Aquí podrías inicializar los campos necesarios del DTO

        // Creamos un recurso de cliente con el servicio simulado
        ClienteServiceImpl clienteService = mock(ClienteServiceImpl.class);
        ClienteResource clienteResource = new ClienteResource(clienteService, null); // Aquí podrías necesitar inicializar otros componentes si son necesarios

        // Llamamos al método que estamos probando
        ResponseEntity<Long> responseEntity = clienteResource.updateCliente(clientId, clienteDTO);

        // Verificamos que la respuesta no sea nula
        assertNotNull(responseEntity);
        // Verificamos que el código de estado sea 200 OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Verificamos que el cuerpo de la respuesta contenga el mismo ID que el ID del cliente actualizado
        assertEquals(clientId, responseEntity.getBody());

        // Verificamos que el método update() del servicio haya sido llamado exactamente una vez con el ID del cliente y el DTO de cliente
        verify(clienteService, times(1)).update(clientId, clienteDTO);
    }

    @Test
    public void testDeleteCliente() {
        // Simulamos un ID de cliente existente
        Long clientId = 1L;

        // Creamos un recurso de cliente con el servicio simulado
        ClienteServiceImpl clienteService = mock(ClienteServiceImpl.class);
        ClienteResource clienteResource = new ClienteResource(clienteService, null); // Aquí podrías necesitar inicializar otros componentes si son necesarios

        // Llamamos al método que estamos probando
        ResponseEntity<Void> responseEntity = clienteResource.deleteCliente(clientId);

        // Verificamos que la respuesta no sea nula
        assertNotNull(responseEntity);
        // Verificamos que el código de estado sea 204 NO CONTENT
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        // Verificamos que el método getReferencedWarning() del servicio haya sido llamado exactamente una vez con el ID del cliente
        verify(clienteService, times(1)).getReferencedWarning(clientId);
        // Verificamos que el método delete() del servicio haya sido llamado exactamente una vez con el ID del cliente
        verify(clienteService, times(1)).delete(clientId);
    }


    @Test
    public void testBuscar() {
        // Simulamos los parámetros de la solicitud
        String atributo = "nombre";
        String valor = "Juan";
        String operador = "igual";

        // Simulamos el EntityManager y CriteriaBuilder
        EntityManager entityManager = mock(EntityManager.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);

        // Simulamos la consulta
        CriteriaQuery<Cliente> criteriaQuery = mock(CriteriaQuery.class);
        Root<Cliente> root = mock(Root.class);
        Predicate predicate = mock(Predicate.class);
        List<Cliente> resultados = new ArrayList<>();
        // Aquí podrías agregar algunos clientes simulados a la lista de resultados

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Cliente.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Cliente.class)).thenReturn(root);
        when(criteriaBuilder.equal(root.get(atributo), valor)).thenReturn(predicate);
        when(criteriaBuilder.like(root.get(atributo), "%" + valor + "%")).thenReturn(predicate);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.where(predicate)).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(mock(TypedQuery.class));
        when(entityManager.createQuery(criteriaQuery).getResultList()).thenReturn(resultados);

        // Creamos un recurso de cliente con el EntityManager simulado
        ClienteResource clienteResource = new ClienteResource(null, entityManager); // Aquí podrías necesitar inicializar otros componentes si son necesarios

        // Llamamos al método que estamos probando
        ResponseEntity<?> responseEntity = clienteResource.buscar(atributo, valor, operador);

        // Verificamos que la respuesta no sea nula
        assertNotNull(responseEntity);
        // Verificamos que el código de estado sea 200 OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Aquí podrías agregar más aserciones para verificar el cuerpo de la respuesta según los resultados esperados
    }
*/
}