package todocode.hackacode.service;

import todocode.hackacode.model.ServicioDTO;

import java.util.List;

public interface ServicioService {

   List<ServicioDTO> findAll();
   ServicioDTO get(Long id);
   Long create(ServicioDTO servicioDTO);
   void update(Long id, ServicioDTO servicioDTO);
   void delete(final Long id);
}
