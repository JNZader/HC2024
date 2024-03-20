package todocode.hackacode.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import todocode.hackacode.domain.Paquete;
import todocode.hackacode.domain.Servicio;
import todocode.hackacode.model.PaqueteDTO;
import todocode.hackacode.repos.PaqueteRepository;
import todocode.hackacode.repos.ServicioRepository;
import todocode.hackacode.repos.VentaRepository;
import todocode.hackacode.service.PaqueteService;
import todocode.hackacode.util.NotFoundException;
import todocode.hackacode.util.ReferencedWarning;

import java.util.List;

@Service
@Transactional
public class PaqueteServiceImpl implements PaqueteService {

    private final PaqueteRepository paqueteRepository;
    private final VentaRepository ventaRepository;
    private final ServicioRepository servicioRepository;

    public PaqueteServiceImpl(final PaqueteRepository paqueteRepository,
            final VentaRepository ventaRepository, final ServicioRepository servicioRepository) {
        this.paqueteRepository = paqueteRepository;
        this.ventaRepository = ventaRepository;
        this.servicioRepository = servicioRepository;
    }

    @Override
    public List<PaqueteDTO> findAll() {
        final List<Paquete> paquetes = paqueteRepository.findAll(Sort.by("id"));
        return paquetes.stream()
                .map(paquete -> mapToDTO(paquete, new PaqueteDTO()))
                .toList();
    }

    @Override
    public PaqueteDTO get(final Long id) {
        return paqueteRepository.findById(id)
                .map(paquete -> mapToDTO(paquete, new PaqueteDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final PaqueteDTO paqueteDTO) {
        final Paquete paquete = new Paquete();
        mapToEntity(paqueteDTO, paquete);
        return paqueteRepository.save(paquete).getId();
    }

    @Override
    public void update(final Long id, final PaqueteDTO paqueteDTO) {
        final Paquete paquete = paqueteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(paqueteDTO, paquete);
        paqueteRepository.save(paquete);
    }

    @Override
    public void delete(final Long id) {
        final Paquete paquete = paqueteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        ventaRepository.findAllByPaquetes(paquete)
                .forEach(venta -> venta.getPaquetes().remove(paquete));
        paqueteRepository.delete(paquete);
    }

    @Override
    public PaqueteDTO mapToDTO(final Paquete paquete) {
        PaqueteDTO paqueteDTO = new PaqueteDTO();
        paqueteDTO.setId(paquete.getId());
        paqueteDTO.setTipo(paquete.getTipo());
        paqueteDTO.setNombre(paquete.getNombre());
        paqueteDTO.setDescripcionBreve(paquete.getDescripcionBreve());
        paqueteDTO.setPrecioVenta(paquete.getPrecioVenta());
        paqueteDTO.setDuracion(paquete.getDuracion());
        paqueteDTO.setEstado(paquete.getEstado());
        return paqueteDTO;
    }

    private PaqueteDTO mapToDTO(final Paquete paquete, final PaqueteDTO paqueteDTO) {
        paqueteDTO.setId(paquete.getId());
        paqueteDTO.setTipo(paquete.getTipo());
        paqueteDTO.setNombre(paquete.getNombre());
        paqueteDTO.setDescripcionBreve(paquete.getDescripcionBreve());
        paqueteDTO.setPrecioVenta(paquete.getPrecioVenta());
        paqueteDTO.setDuracion(paquete.getDuracion());
        paqueteDTO.setEstado(paquete.getEstado());
        return paqueteDTO;
    }

    private Paquete mapToEntity(final PaqueteDTO paqueteDTO, final Paquete paquete) {
        paquete.setTipo(paqueteDTO.getTipo());
        paquete.setNombre(paqueteDTO.getNombre());
        paquete.setDescripcionBreve(paqueteDTO.getDescripcionBreve());
        paquete.setPrecioVenta(paqueteDTO.getPrecioVenta());
        paquete.setDuracion(paqueteDTO.getDuracion());
        paquete.setEstado(paqueteDTO.getEstado());
        return paquete;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Paquete paquete = paqueteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Servicio paqueteidServicio = servicioRepository.findFirstByPaqueteid(paquete);
        if (paqueteidServicio != null) {
            referencedWarning.setKey("paquete.servicio.paqueteid.referenced");
            referencedWarning.addParam(paqueteidServicio.getId());
            return referencedWarning;
        }
        return null;
    }

}
