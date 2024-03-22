package todocode.hackacode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import todocode.hackacode.domain.Usuario;
import todocode.hackacode.model.CrearUsuarioDTO;
import todocode.hackacode.repos.UsuarioRepository;

import java.util.List;

@RestController
public class UsuarioController {

   private UsuarioRepository usuarioRepository;
   private BCryptPasswordEncoder passwordEncoder;

   @Autowired
   public UsuarioController(UsuarioRepository usuarioRepository){
      this.usuarioRepository=usuarioRepository;
      this.passwordEncoder=passwordEncoder;
   }

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

   @GetMapping("/listar")
   @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
   public ResponseEntity<List<Usuario>> listarUsuarios(){
      var users=usuarioRepository.findAll();

      return ResponseEntity.ok(users);
   }
}
