package todocode.hackacode.service;

import todocode.hackacode.domain.Cliente;
import todocode.hackacode.model.ClienteDTO;

import java.util.List;

public interface ClienteService {

    List<ClienteDTO> findAll();

    ClienteDTO get(Long id);

    Long create(ClienteDTO clienteDTO);

    void update(Long id, ClienteDTO clienteDTO);

    void delete(Long id);

    Cliente findById(Long id);

    boolean existsById(Long id);

}
