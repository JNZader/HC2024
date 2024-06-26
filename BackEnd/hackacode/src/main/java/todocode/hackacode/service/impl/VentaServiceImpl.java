package todocode.hackacode.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import todocode.hackacode.domain.Cliente;
import todocode.hackacode.domain.Empleado;
import todocode.hackacode.domain.Paquete;
import todocode.hackacode.domain.Venta;
import todocode.hackacode.model.Medpago;
import todocode.hackacode.model.VentaDTO;
import todocode.hackacode.repos.ClienteRepository;
import todocode.hackacode.repos.EmpleadoRepository;
import todocode.hackacode.repos.PaqueteRepository;
import todocode.hackacode.repos.VentaRepository;
import todocode.hackacode.service.VentaService;
import todocode.hackacode.util.NotFoundException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PaqueteRepository paqueteRepository;

    public VentaServiceImpl(final VentaRepository ventaRepository,
            final ClienteRepository clienteRepository, final EmpleadoRepository empleadoRepository,
            final PaqueteRepository paqueteRepository) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.empleadoRepository = empleadoRepository;
        this.paqueteRepository = paqueteRepository;
    }

    public VentaDTO mapToDTO(final Venta venta, final VentaDTO ventaDTO) {
        ventaDTO.setId(venta.getId());
        ventaDTO.setMedioPago(venta.getMedioPago());
        ventaDTO.setMonto(venta.getMonto());
        ventaDTO.setFecha(venta.getFecha());
        ventaDTO.setEstado(venta.getEstado());
        ventaDTO.setClienteId(venta.getClienteId() == null ? null : venta.getClienteId().getId());
        ventaDTO.setEmpleadoid(venta.getEmpleadoid() == null ? null : venta.getEmpleadoid().getId());
        ventaDTO.setPaquetes(venta.getPaquetes().stream()
                .map(Paquete::getId)
                .toList());
        return ventaDTO;
    }

    public VentaDTO mapToDTO(final Venta venta) {
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setId(venta.getId());
        ventaDTO.setMedioPago(venta.getMedioPago());
        ventaDTO.setMonto(venta.getMonto());
        ventaDTO.setFecha(venta.getFecha());
        ventaDTO.setEstado(venta.getEstado());
        ventaDTO.setClienteId(venta.getClienteId() == null ? null : venta.getClienteId().getId());
        ventaDTO.setEmpleadoid(venta.getEmpleadoid() == null ? null : venta.getEmpleadoid().getId());
        ventaDTO.setPaquetes(venta.getPaquetes().stream()
                .map(Paquete::getId)
                .toList());
        return ventaDTO;
    }

    public Venta mapToEntity(final VentaDTO ventaDTO, final Venta venta) {
        venta.setMedioPago(ventaDTO.getMedioPago());
        venta.setMonto(ventaDTO.getMonto());
        venta.setFecha(ventaDTO.getFecha());
        venta.setEstado(ventaDTO.getEstado());
        final Cliente clienteId = ventaDTO.getClienteId() == null ? null : clienteRepository.findById(ventaDTO.getClienteId())
                .orElseThrow(() -> new NotFoundException("clienteId not found"));
        venta.setClienteId(clienteId);
        final Empleado empleadoid = ventaDTO.getEmpleadoid() == null ? null : empleadoRepository.findById(ventaDTO.getEmpleadoid())
                .orElseThrow(() -> new NotFoundException("empleadoid not found"));
        venta.setEmpleadoid(empleadoid);
        final List<Paquete> paquetes = paqueteRepository.findAllById(
                ventaDTO.getPaquetes() == null ? Collections.emptyList() : ventaDTO.getPaquetes());
        if (paquetes.size() != (ventaDTO.getPaquetes() == null ? 0 : ventaDTO.getPaquetes().size())) {
            throw new NotFoundException("one of paquetes not found");
        }
        venta.setPaquetes(new HashSet<>(paquetes));
        return venta;
    }

    @Override
    public List<VentaDTO> findAll() {
        final List<Venta> ventas = ventaRepository.findAll(Sort.by("id"));
        return ventas.stream()
                .map(venta -> mapToDTO(venta, new VentaDTO()))
                .toList();
    }

    @Override
    public VentaDTO get(final Long id) {
        return ventaRepository.findById(id)
                .map(venta -> mapToDTO(venta, new VentaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final VentaDTO ventaDTO) {
        final Venta venta = new Venta();
        mapToEntity(ventaDTO, venta);
        return ventaRepository.save(venta).getId();
    }

    @Override
    public void update(final Long id, final VentaDTO ventaDTO) {
        final Venta venta = ventaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ventaDTO, venta);
        ventaRepository.save(venta);
    }

    @Override
    public void delete(final Long id) {
        ventaRepository.deleteById(id);
    }

    @Override
    public Double calcularGananciasDiarias(LocalDate fecha) {
        List<Venta> ventas = ventaRepository.findByFecha(fecha);
        return ventas.stream()
                .mapToDouble(this::calcularGananciaVenta)
                .sum();
    }

    @Override
    public Double calcularGananciasMensuales(Integer mes, Integer anio) {
        List<Venta> ventas = ventaRepository.findByFechaBetween(
                LocalDate.of(anio, mes, 1),
                LocalDate.of(anio, mes, YearMonth.of(anio, mes).lengthOfMonth())
        );
        return ventas.stream()
                .mapToDouble(this::calcularGananciaVenta)
                .sum();
    }

    private Double calcularGananciaVenta(Venta venta) {
        Double monto = venta.getMonto();
        Medpago medioPago = venta.getMedioPago();

        double comision = medioPago.getComision();

        return monto * (1 - comision);
    }

    @Override
    public Double calcularGananciasEnRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Venta> ventas = ventaRepository.findByFechaBetween(fechaInicio, fechaFin);
        return ventas.stream()
                .mapToDouble(this::calcularGananciaVenta)
                .sum();
    }

    public Venta updateVenta(VentaDTO ventaDTO, Venta venta) {
        if (ventaDTO == null) {
            return venta;
        }

        if (ventaDTO.getMedioPago() != null) {
            venta.setMedioPago(ventaDTO.getMedioPago());
        }
        if (ventaDTO.getMonto() != null) {
            venta.setMonto(ventaDTO.getMonto());
        }
        if (ventaDTO.getFecha() != null) {
            venta.setFecha(ventaDTO.getFecha());
        }
        if (ventaDTO.getEstado() != null) {
            venta.setEstado(ventaDTO.getEstado());
        }
        if (ventaDTO.getId() != null) {
            venta.setId(ventaDTO.getId());
        }
        if (ventaDTO.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(ventaDTO.getClienteId())
                    .orElseThrow(NotFoundException::new);
            venta.setClienteId(cliente);
        }
        if (ventaDTO.getEmpleadoid() != null) {
            Empleado empleado = empleadoRepository.findById(ventaDTO.getEmpleadoid())
                    .orElseThrow(NotFoundException::new);
            venta.setEmpleadoid(empleado);
        }
        if (ventaDTO.getPaquetes() != null) {
            Set<Paquete> paquetes = new HashSet<>();
            for (Long paqueteId : ventaDTO.getPaquetes()) {
                Paquete paquete = paqueteRepository.findById(paqueteId)
                        .orElseThrow(NotFoundException::new);
                paquetes.add(paquete);
            }
            venta.setPaquetes(paquetes);
        }

        return venta;
    }

    public List<VentaDTO> mapToDTOList(List<Venta> resultados) {
        List<VentaDTO> ventaDTOS = new ArrayList<>();

        for (Venta venta : resultados) {
            VentaDTO ventaDTO = mapToDTO(venta);

            ventaDTOS.add(ventaDTO);
        }

        return ventaDTOS;
    }
}
