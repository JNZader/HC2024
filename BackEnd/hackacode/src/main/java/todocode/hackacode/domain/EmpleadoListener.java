package todocode.hackacode.domain;

import jakarta.persistence.PrePersist;

public class EmpleadoListener {

   @PrePersist
   public void onPrePersist(Empleado empleado) {
      Usuario usuario = new Usuario();
      usuario.setUsername(empleado.getEmail());
      usuario.setPassword(empleado.getDni());
      usuario.setRol(empleado.getCargo());
      usuario.setPassTemporaria(true);
      empleado.setUsuario(usuario);
   }

}
