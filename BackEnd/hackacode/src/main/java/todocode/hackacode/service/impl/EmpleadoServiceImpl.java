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

import java.util.List;
import java.util.Optional;

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
        empleadoDTO.setUsuario_id(empleado.getUsuario().getId());
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
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(empleadoDTO.getUsuario_id());
            usuario = optionalUsuario.orElse(new Usuario());
        } catch (Exception e) {
            System.out.println("AAAAA");
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
}
