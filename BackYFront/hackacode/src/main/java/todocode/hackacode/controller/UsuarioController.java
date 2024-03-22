package todocode.hackacode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import todocode.hackacode.domain.Usuario;
import todocode.hackacode.model.CrearUsuarioDTO;
import todocode.hackacode.repos.UsuarioRepository;

import java.util.List;

@RestController
public class UsuarioController {

   private final UsuarioRepository usuarioRepository;
   private final BCryptPasswordEncoder passwordEncoder;

   @Autowired
   public UsuarioController(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder){
      this.usuarioRepository=usuarioRepository;
      this.passwordEncoder = passwordEncoder;
   }
   /**
    * Crea un nuevo usuario.
    *
    * @param crearUsuarioDTO Objeto que contiene la información del nuevo usuario.
    * @return ResponseEntity con el usuario creado.
    */
   @PostMapping("/crear")
   public ResponseEntity<?> crearUsuario(@RequestBody CrearUsuarioDTO crearUsuarioDTO){
      Usuario usuario = new Usuario();
      usuario.setUsername(crearUsuarioDTO.username());
      usuario.setPassword(passwordEncoder.encode(crearUsuarioDTO.password()));
      usuario.setRol(crearUsuarioDTO.cargo());
      usuario.setPassTemporaria(false);

      Usuario savedUsuario = usuarioRepository.save(usuario);
      return ResponseEntity.ok(savedUsuario);
   }
   /**
    * Lista todos los usuarios.
    *
    * @return ResponseEntity con la lista de usuarios.
    */
   @GetMapping("/listar")
   @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
   public ResponseEntity<List<Usuario>> listarUsuarios(){
      var users=usuarioRepository.findAll();

      return ResponseEntity.ok(users);
   }
}
