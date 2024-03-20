package todocode.hackacode.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import todocode.hackacode.domain.Precio;
import todocode.hackacode.domain.Servicio;
import todocode.hackacode.model.PrecioDTO;
import todocode.hackacode.repos.PrecioRepository;
import todocode.hackacode.repos.ServicioRepository;
import todocode.hackacode.service.PrecioService;
import todocode.hackacode.util.NotFoundException;

import java.util.List;

@Service
public class PrecioServiceImpl implements PrecioService {

    private final PrecioRepository precioRepository;
    private final ServicioRepository servicioRepository;

    public PrecioServiceImpl(final PrecioRepository precioRepository,
            final ServicioRepository servicioRepository) {
        this.precioRepository = precioRepository;
        this.servicioRepository = servicioRepository;
    }

    @Override
    public List<PrecioDTO> findAll() {
        final List<Precio> precios = precioRepository.findAll(Sort.by("id"));
        return precios.stream()
                .map(precio -> mapToDTO(precio, new PrecioDTO()))
                .toList();
    }

    @Override
    public PrecioDTO get(final Long id) {
        return precioRepository.findById(id)
                .map(precio -> mapToDTO(precio, new PrecioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final PrecioDTO precioDTO) {
        final Precio precio = new Precio();
        mapToEntity(precioDTO, precio);
        return precioRepository.save(precio).getId();
    }

    @Override
    public void update(final Long id, final PrecioDTO precioDTO) {
        final Precio precio = precioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(precioDTO, precio);
        precioRepository.save(precio);
    }

    @Override
    public void delete(final Long id) {
        precioRepository.deleteById(id);
    }

    private PrecioDTO mapToDTO(final Precio precio, final PrecioDTO precioDTO) {
        precioDTO.setId(precio.getId());
        precioDTO.setIdServico(precio.getIdServico());
        precioDTO.setCosto(precio.getCosto());
        precioDTO.setPrecioVenta(precio.getPrecioVenta());
        precioDTO.setIdServicio(precio.getIdServicio() == null ? null : precio.getIdServicio().getId());
        return precioDTO;
    }

    private Precio mapToEntity(final PrecioDTO precioDTO, final Precio precio) {
        precio.setIdServico(precioDTO.getIdServico());
        precio.setCosto(precioDTO.getCosto());
        precio.setPrecioVenta(precioDTO.getPrecioVenta());
        final Servicio idServicio = precioDTO.getIdServicio() == null ? null : servicioRepository.findById(precioDTO.getIdServicio())
                .orElseThrow(() -> new NotFoundException("idServicio not found"));
        precio.setIdServicio(idServicio);
        return precio;
    }

    public boolean idServicioExists(final Long id) {
        return precioRepository.existsByIdServicioId(id);
    }

}
