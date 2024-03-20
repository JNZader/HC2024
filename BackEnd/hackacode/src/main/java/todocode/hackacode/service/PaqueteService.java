package todocode.hackacode.service;

import todocode.hackacode.domain.Paquete;
import todocode.hackacode.model.PaqueteDTO;

import java.util.List;

public interface PaqueteService {

   List<PaqueteDTO> findAll();
   PaqueteDTO get(Long id);
   Long create(PaqueteDTO paqueteDTO);
   void update(Long id, PaqueteDTO paqueteDTO);
   void delete(Long id);

   PaqueteDTO mapToDTO(Paquete paquete);
}
