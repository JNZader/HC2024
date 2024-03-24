package todocode.hackacode.rest;



import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



import todocode.hackacode.domain.Usuario;
import todocode.hackacode.model.EmpleadoDTO;
import todocode.hackacode.repos.UsuarioRepository;
import todocode.hackacode.service.impl.EmpleadoServiceImpl;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmpleadoResourceTest {
/*
    @Test
    public void testGetAllEmpleados(){

    // Simulamos una lista de empleados
    List<EmpleadoDTO> empleados = new ArrayList<>();
    // Agrega empleados simulados a la lista si es necesario

    // Mock del servicio de empleado
    EmpleadoServiceImpl empleadoService = mock(EmpleadoServiceImpl.class);
    when(empleadoService.findAll()).thenReturn(empleados);

    // Creamos el recurso de empleado con el servicio simulado
    EmpleadoResource empleadoResource = new EmpleadoResource(empleadoService, null, null, null);

    // Llamamos al método getAllEmpleados()
    ResponseEntity<List<EmpleadoDTO>> responseEntity = empleadoResource.getAllEmpleados();

    // Verificamos que la respuesta no sea nula
    assertNotNull(responseEntity);
    // Verificamos que el código de estado sea 200 OK
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    // Verificamos que el cuerpo de la respuesta contenga la lista de empleados simulada
    assertEquals(empleados, responseEntity.getBody());
}

    @Test
    public void testGetEmpleado() {
        // Simulamos un ID de empleado existente
        Long empleadoId = 1L;
        // Aquí podrías inicializar un objeto EmpleadoDTO simulado si es necesario

        // Mock del servicio de empleado
        EmpleadoServiceImpl empleadoService = mock(EmpleadoServiceImpl.class);
        when(empleadoService.get(empleadoId)).thenReturn(null); // Reemplaza null con el objeto EmpleadoDTO simulado

        // Creamos el recurso de empleado con el servicio simulado
        EmpleadoResource empleadoResource = new EmpleadoResource(empleadoService, null, null, null);

        // Llamamos al método getEmpleado()
        ResponseEntity<EmpleadoDTO> responseEntity = empleadoResource.getEmpleado(empleadoId);

        // Verificamos que la respuesta no sea nula
        assertNotNull(responseEntity);
        // Verificamos que el código de estado sea 200 OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Verificamos que el cuerpo de la respuesta contenga el objeto EmpleadoDTO simulado
        assertNull(responseEntity.getBody()); // Reemplaza null con el objeto EmpleadoDTO simulado
    }

    @Test
    public void testCreateEmpleado() {
        // Crear un DTO de empleado válido (asegúrate de que cumpla con las validaciones)
        EmpleadoDTO empleadoDTO = new EmpleadoDTO(); // Aquí deberías definir el DTO del empleado con datos simulados


        // Simular el resultado del servicio al crear un empleado
        Long createdId = 1L; // ID simulado del empleado creado
        EmpleadoServiceImpl empleadoService = mock(EmpleadoServiceImpl.class);
        when(empleadoService.create(empleadoDTO)).thenReturn(createdId);

        // Simular el usuario asociado al empleado
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        Usuario usuario = Usuario.builder()
                .id(empleadoDTO.getUsuario_id())
                .username(empleadoDTO.getEmail())
                .password(passwordEncoder.encode(empleadoDTO.getDni()))
                .rol(empleadoDTO.getCargo())
                .passTemporaria(true)
                .build();
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Crear el recurso del empleado con los servicios simulados
        EmpleadoResource empleadoResource = new EmpleadoResource(empleadoService, null, usuarioRepository, passwordEncoder);

        // Llamar al método que estamos probando con el DTO de empleado
        ResponseEntity<Long> responseEntity = empleadoResource.createEmpleado(empleadoDTO);

        // Verificar que la respuesta no sea nula
        assertNotNull(responseEntity);
        // Verificar que el código de estado sea 201 CREATED
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        // Verificar que el cuerpo de la respuesta contenga el ID del empleado creado
        assertEquals(createdId, responseEntity.getBody());

        // Verificar que el método create() del servicio haya sido llamado exactamente una vez con el DTO de empleado
        verify(empleadoService, times(1)).create(empleadoDTO);
        // Verificar que el método save() del repositorio de usuario haya sido llamado exactamente una vez
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
*/
}