package todocode.hackacode.service;

import todocode.hackacode.model.VentaDTO;

import java.time.LocalDate;
import java.util.List;

public interface VentaService {

   List<VentaDTO> findAll();

   VentaDTO get(final Long id);

   Long create(final VentaDTO ventaDTO);

   void update(final Long id, final VentaDTO ventaDTO);

   void delete(final Long id);

   Double calcularGananciasDiarias(LocalDate fecha);

   Double calcularGananciasMensuales(Integer mes, Integer anio);

   Double calcularGananciasEnRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);


}
