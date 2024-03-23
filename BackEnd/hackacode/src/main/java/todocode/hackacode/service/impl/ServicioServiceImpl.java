package todocode.hackacode.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import todocode.hackacode.domain.Paquete;
import todocode.hackacode.domain.Precio;
import todocode.hackacode.domain.Servicio;
import todocode.hackacode.model.ServicioDTO;
import todocode.hackacode.repos.PaqueteRepository;
import todocode.hackacode.repos.PrecioRepository;
import todocode.hackacode.repos.ServicioRepository;
import todocode.hackacode.service.ServicioService;
import todocode.hackacode.util.NotFoundException;
import todocode.hackacode.util.ReferencedWarning;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;
    private final PaqueteRepository paqueteRepository;
    private final PrecioRepository precioRepository;

    public ServicioServiceImpl(final ServicioRepository servicioRepository,
            final PaqueteRepository paqueteRepository, final PrecioRepository precioRepository) {
        this.servicioRepository = servicioRepository;
        this.paqueteRepository = paqueteRepository;
        this.precioRepository = precioRepository;
    }

    @Override
    public List<ServicioDTO> findAll() {
        final List<Servicio> servicios = servicioRepository.findAll(Sort.by("id"));
        return servicios.stream()
                .map(servicio -> mapToDTO(servicio, new ServicioDTO()))
                .toList();
    }

    @Override
    public ServicioDTO get(final Long id) {
        return servicioRepository.findById(id)
                .map(servicio -> mapToDTO(servicio, new ServicioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final ServicioDTO servicioDTO) {
        final Servicio servicio = new Servicio();
        mapToEntity(servicioDTO, servicio);
        return servicioRepository.save(servicio).getId();
    }

    @Override
    public void update(final Long id, final ServicioDTO servicioDTO) {
        final Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(servicioDTO, servicio);
        servicioRepository.save(servicio);
    }

    @Override
    public void delete(final Long id) {
        servicioRepository.deleteById(id);
    }

    public ServicioDTO mapToDTO(final Servicio servicio, final ServicioDTO servicioDTO) {
        servicioDTO.setId(servicio.getId());
        servicioDTO.setNombre(servicio.getNombre());
        servicioDTO.setDescripcion(servicio.getDescripcion());
        servicioDTO.setFecha(servicio.getFecha());
        servicioDTO.setDuracion(servicio.getDuracion());
        servicioDTO.setEstado(servicio.getEstado());
        servicioDTO.setPaqueteid(servicio.getPaqueteid() == null ? null : servicio.getPaqueteid().getId());
        return servicioDTO;
    }

    public ServicioDTO mapToDTO(final Servicio servicio) {
        ServicioDTO servicioDTO = new ServicioDTO();
        servicioDTO.setId(servicio.getId());
        servicioDTO.setNombre(servicio.getNombre());
        servicioDTO.setDescripcion(servicio.getDescripcion());
        servicioDTO.setFecha(servicio.getFecha());
        servicioDTO.setDuracion(servicio.getDuracion());
        servicioDTO.setEstado(servicio.getEstado());
        servicioDTO.setPaqueteid(servicio.getPaqueteid() == null ? null : servicio.getPaqueteid().getId());
        return servicioDTO;
    }

    public Servicio mapToEntity(final ServicioDTO servicioDTO, final Servicio servicio) {
        servicio.setNombre(servicioDTO.getNombre());
        servicio.setDescripcion(servicioDTO.getDescripcion());
        servicio.setFecha(servicioDTO.getFecha());
        servicio.setDuracion(servicioDTO.getDuracion());
        servicio.setEstado(servicioDTO.getEstado());
        final Paquete paqueteid = servicioDTO.getPaqueteid() == null ? null : paqueteRepository.findById(servicioDTO.getPaqueteid())
                .orElseThrow(() -> new NotFoundException("paqueteid not found"));
        servicio.setPaqueteid(paqueteid);
        return servicio;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Precio idServicioPrecio = precioRepository.findFirstByIdServicio(servicio);
        if (idServicioPrecio != null) {
            referencedWarning.setKey("servicio.precio.idServicio.referenced");
            referencedWarning.addParam(idServicioPrecio.getId());
            return referencedWarning;
        }
        return null;
    }

    public Servicio updateServicio(ServicioDTO servicioDTO, Servicio servicio) {
        if (servicioDTO == null) {
            return servicio;
        }

        if (servicioDTO.getNombre() != null) {
            servicio.setNombre(servicioDTO.getNombre());
        }
        if (servicioDTO.getDescripcion() != null) {
            servicio.setDescripcion(servicioDTO.getDescripcion());
        }
        if (servicioDTO.getFecha() != null) {
            servicio.setFecha(servicioDTO.getFecha());
        }
        if (servicioDTO.getDuracion() != null) {
            servicio.setDuracion(servicioDTO.getDuracion());
        }
        if (servicioDTO.getEstado() != null) {
            servicio.setEstado(servicioDTO.getEstado());
        }
        if (servicioDTO.getPaqueteid() != null) {
            Paquete paquete = new Paquete();
            paquete.setId(servicioDTO.getPaqueteid());
            servicio.setPaqueteid(paquete);
        }

        return servicio;
    }

    public List<ServicioDTO> mapToDTOList(List<Servicio> resultados) {
        List<ServicioDTO> ServicioDTOs = new ArrayList<>();

        for (Servicio servicio : resultados) {
            ServicioDTO servicioDTO = mapToDTO(servicio);
            ServicioDTOs.add(servicioDTO);
        }

        return ServicioDTOs;
    }
}
