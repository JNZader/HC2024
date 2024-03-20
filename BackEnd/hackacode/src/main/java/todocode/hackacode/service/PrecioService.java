package todocode.hackacode.service;

import todocode.hackacode.model.PrecioDTO;

import java.util.List;

public interface PrecioService {
   List<PrecioDTO> findAll();
   PrecioDTO get(Long id);
   Long create(PrecioDTO precioDTO);
   void update(Long id, PrecioDTO precioDTO);
   void delete(Long id);
}
