package todocode.hackacode.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import todocode.hackacode.domain.Empleado;
import todocode.hackacode.domain.Usuario;
import todocode.hackacode.domain.Venta;
import todocode.hackacode.model.EmpleadoDTO;
import todocode.hackacode.repos.EmpleadoRepository;
import todocode.hackacode.repos.UsuarioRepository;
import todocode.hackacode.repos.VentaRepository;
import todocode.hackacode.service.EmpleadoService;
import todocode.hackacode.util.NotFoundException;
import todocode.hackacode.util.ReferencedWarning;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public EmpleadoServiceImpl(final EmpleadoRepository empleadoRepository,
            final VentaRepository ventaRepository, UsuarioRepository usuarioRepository) {
        this.empleadoRepository = empleadoRepository;
        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<EmpleadoDTO> findAll() {
        final List<Empleado> empleadoes = empleadoRepository.findAll(Sort.by("id"));
        return empleadoes.stream()
                .map(empleado -> mapToDTO(empleado, new EmpleadoDTO()))
                .toList();
    }

    @Override
    public EmpleadoDTO get(final Long id) {
        return empleadoRepository.findById(id)
                .map(empleado -> mapToDTO(empleado, new EmpleadoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final EmpleadoDTO empleadoDTO) {
        final Empleado empleado = new Empleado();
        mapToEntity(empleadoDTO, empleado);
        return empleadoRepository.save(empleado).getId();
    }

    @Override
    public void update(final Long id, final EmpleadoDTO empleadoDTO) {
        final Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(empleadoDTO, empleado);
        empleadoRepository.save(empleado);
    }

    @Override
    public void delete(final Long id) {
        empleadoRepository.deleteById(id);
    }

    public EmpleadoDTO mapToDTO(final Empleado empleado, final EmpleadoDTO empleadoDTO) {
        empleadoDTO.setNombre(empleado.getNombre());
        empleadoDTO.setApellido(empleado.getApellido());
        empleadoDTO.setDireccion(empleado.getDireccion());
        empleadoDTO.setDni(empleado.getDni());
        empleadoDTO.setFechaNacimiento(empleado.getFechaNacimiento());
        empleadoDTO.setNacionalidad(empleado.getNacionalidad());
        empleadoDTO.setCelular(empleado.getCelular());
        empleadoDTO.setEmail(empleado.getEmail());
        empleadoDTO.setId(empleado.getId());
        empleadoDTO.setCargo(empleado.getCargo());
        empleadoDTO.setSueldo(empleado.getSueldo());
        empleadoDTO.setEstado(empleado.getEstado());
        empleadoDTO.setIdUsuario(empleado.getUsuario().getId());
        return empleadoDTO;
    }

    public Empleado mapToEntity(final EmpleadoDTO empleadoDTO, final Empleado empleado) {
        empleado.setNombre(empleadoDTO.getNombre());
        empleado.setApellido(empleadoDTO.getApellido());
        empleado.setDireccion(empleadoDTO.getDireccion());
        empleado.setDni(empleadoDTO.getDni());
        empleado.setFechaNacimiento(empleadoDTO.getFechaNacimiento());
        empleado.setNacionalidad(empleadoDTO.getNacionalidad());
        empleado.setCelular(empleadoDTO.getCelular());
        empleado.setEmail(empleadoDTO.getEmail());
        empleado.setCargo(empleadoDTO.getCargo());
        empleado.setSueldo(empleadoDTO.getSueldo());
        empleado.setEstado(empleadoDTO.getEstado());
        Usuario usuario = null;
        try {
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(empleadoDTO.getIdUsuario());
            usuario = optionalUsuario.orElse(new Usuario());
        } catch (Exception e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.info("Error en mapToEntity");
        }
        empleado.setUsuario(usuario);
        return empleado;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Venta empleadoidVenta = ventaRepository.findFirstByEmpleadoid(empleado);
        if (empleadoidVenta != null) {
            referencedWarning.setKey("empleado.venta.empleadoid.referenced");
            referencedWarning.addParam(empleadoidVenta.getId());
            return referencedWarning;
        }
        return null;
    }

    public Empleado updateEmpleado(EmpleadoDTO empleadoDTO, Empleado empleado) {
        if (empleadoDTO == null) {
            return empleado;
        }

        if (empleadoDTO.getNombre() != null) {
            empleado.setNombre(empleadoDTO.getNombre());
        }
        if (empleadoDTO.getApellido() != null) {
            empleado.setApellido(empleadoDTO.getApellido());
        }
        if (empleadoDTO.getDireccion() != null) {
            empleado.setDireccion(empleadoDTO.getDireccion());
        }
        if (empleadoDTO.getDni() != null) {
            empleado.setDni(empleadoDTO.getDni());
        }
        if (empleadoDTO.getFechaNacimiento() != null) {
            empleado.setFechaNacimiento(empleadoDTO.getFechaNacimiento());
        }
        if (empleadoDTO.getNacionalidad() != null) {
            empleado.setNacionalidad(empleadoDTO.getNacionalidad());
        }
        if (empleadoDTO.getCelular() != null) {
            empleado.setCelular(empleadoDTO.getCelular());
        }
        if (empleadoDTO.getEmail() != null) {
            empleado.setEmail(empleadoDTO.getEmail());
        }
        if (empleadoDTO.getId() != null) {
            empleado.setId(empleadoDTO.getId());
        }
        if (empleadoDTO.getCargo() != null) {
            empleado.setCargo(empleadoDTO.getCargo());
        }
        if (empleadoDTO.getSueldo() != null) {
            empleado.setSueldo(empleadoDTO.getSueldo());
        }
        if (empleadoDTO.getEstado() != null) {
            empleado.setEstado(empleadoDTO.getEstado());
        }
        if (empleadoDTO.getIdUsuario() != null) {
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(empleadoDTO.getIdUsuario());

            if (optionalUsuario.isEmpty()) {
                return empleado;
            }
            Usuario usuario = optionalUsuario.get();
            empleado.setUsuario(usuario);

        }
        return empleado;
    }

    public List<EmpleadoDTO> mapToDTOList(List<Empleado> empleados) {
        List<EmpleadoDTO> empleadoDTOS = new ArrayList<>();

        for (Empleado empleado : empleados) {
            EmpleadoDTO empleadoDTO = mapToDTO(empleado, new EmpleadoDTO());
            empleadoDTOS.add(empleadoDTO);
        }
        return empleadoDTOS;
    }
}
