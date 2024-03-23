package todocode.hackacode.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todocode.hackacode.domain.Cliente;
import todocode.hackacode.domain.Venta;
import todocode.hackacode.model.ClienteDTO;
import todocode.hackacode.repos.ClienteRepository;
import todocode.hackacode.repos.VentaRepository;
import todocode.hackacode.service.ClienteService;
import todocode.hackacode.util.NotFoundException;
import todocode.hackacode.util.ReferencedWarning;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final VentaRepository ventaRepository;

    public ClienteServiceImpl(final ClienteRepository clienteRepository,
            final VentaRepository ventaRepository) {
        this.clienteRepository = clienteRepository;
        this.ventaRepository = ventaRepository;
    }

    @Override
    public List<ClienteDTO> findAll() {
        final List<Cliente> clientes = clienteRepository.findAll(Sort.by("id"));
        return clientes.stream()
                .map(cliente -> mapToDTO(cliente, new ClienteDTO()))
                .toList();
    }

    @Override
    public ClienteDTO get(final Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> mapToDTO(cliente, new ClienteDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final ClienteDTO clienteDTO) {
        final Cliente cliente = new Cliente();
        mapToEntity(clienteDTO, cliente);
        return clienteRepository.save(cliente).getId();
    }

    @Override
    public void update(final Long id, final ClienteDTO clienteDTO) {
        final Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(clienteDTO, cliente);
        clienteRepository.save(cliente);
    }

    @Override
    public void delete(final Long id) {
        clienteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsById(Long id) {
        return clienteRepository.existsById(id);
    }

    public ClienteDTO mapToDTO(final Cliente cliente, final ClienteDTO clienteDTO) {
        clienteDTO.setNombre(cliente.getNombre());
        clienteDTO.setApellido(cliente.getApellido());
        clienteDTO.setDireccion(cliente.getDireccion());
        clienteDTO.setDni(cliente.getDni());
        clienteDTO.setFechaNacimiento(cliente.getFechaNacimiento());
        clienteDTO.setNacionalidad(cliente.getNacionalidad());
        clienteDTO.setCelular(cliente.getCelular());
        clienteDTO.setEmail(cliente.getEmail());
        clienteDTO.setId(cliente.getId());
        clienteDTO.setEstado(cliente.getEstado());
        return clienteDTO;
    }

    public List<ClienteDTO> mapToDTOList(List<Cliente> clientes) {
        List<ClienteDTO> clienteDTOs = new ArrayList<>();

        for (Cliente cliente : clientes) {
            ClienteDTO clienteDTO = mapToDTO(cliente, new ClienteDTO());
            clienteDTOs.add(clienteDTO);
        }

        return clienteDTOs;
    }

    public Cliente mapToEntity(final ClienteDTO clienteDTO, final Cliente cliente) {
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setApellido(clienteDTO.getApellido());
        cliente.setDireccion(clienteDTO.getDireccion());
        cliente.setDni(clienteDTO.getDni());
        cliente.setFechaNacimiento(clienteDTO.getFechaNacimiento());
        cliente.setNacionalidad(clienteDTO.getNacionalidad());
        cliente.setCelular(clienteDTO.getCelular());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setEstado(clienteDTO.getEstado());
        return cliente;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Venta clienteIdVenta = ventaRepository.findFirstByClienteId(cliente);
        if (clienteIdVenta != null) {
            referencedWarning.setKey("cliente.venta.clienteId.referenced");
            referencedWarning.addParam(clienteIdVenta.getId());
            return referencedWarning;
        }
        return null;
    }

    public Cliente updateCliente(ClienteDTO clienteDTO, Cliente cliente) {
        if (clienteDTO == null) {
            return cliente;
        }

        if (clienteDTO.getNombre() != null) {
            cliente.setNombre(clienteDTO.getNombre());
        }
        if (clienteDTO.getApellido() != null) {
            cliente.setApellido(clienteDTO.getApellido());
        }
        if (clienteDTO.getDireccion() != null) {
            cliente.setDireccion(clienteDTO.getDireccion());
        }
        if (clienteDTO.getDni() != null) {
            cliente.setDni(clienteDTO.getDni());
        }
        if (clienteDTO.getFechaNacimiento() != null) {
            cliente.setFechaNacimiento(clienteDTO.getFechaNacimiento());
        }
        if (clienteDTO.getNacionalidad() != null) {
            cliente.setNacionalidad(clienteDTO.getNacionalidad());
        }
        if (clienteDTO.getCelular() != null) {
            cliente.setCelular(clienteDTO.getCelular());
        }
        if (clienteDTO.getEmail() != null) {
            cliente.setEmail(clienteDTO.getEmail());
        }
        if (clienteDTO.getId() != null) {
            cliente.setId(clienteDTO.getId());
        }
        if (clienteDTO.getEstado() != null) {
            cliente.setEstado(clienteDTO.getEstado());
        }

        return cliente;
    }
}
