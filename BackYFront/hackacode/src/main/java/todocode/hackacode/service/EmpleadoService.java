package todocode.hackacode.service;

import todocode.hackacode.model.EmpleadoDTO;

import java.util.List;

public interface EmpleadoService {

   List<EmpleadoDTO> findAll();

   EmpleadoDTO get(Long id);

   Long create(EmpleadoDTO empleadoDTO);

   void update(Long id, EmpleadoDTO empleadoDTO);

   void delete(Long id);

}
