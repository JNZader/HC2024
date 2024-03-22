package todocode.hackacode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import todocode.hackacode.model.CambiarPassDTO;
import todocode.hackacode.model.LoginRequest;
import todocode.hackacode.model.LoginResponse;
import todocode.hackacode.repos.UsuarioRepository;
import todocode.hackacode.util.TemporaryPasswordException;

import java.time.Instant;

@RestController
@EnableMethodSecurity
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

@Autowired
   public TokenController(JwtEncoder jwtEncoder, UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
      this.jwtEncoder = jwtEncoder;
      this.usuarioRepository = usuarioRepository;
   this.passwordEncoder = passwordEncoder;
}

   @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){

    var user=usuarioRepository.findByUsername(loginRequest.username());

    if (user.isEmpty()||!user.get().isLoginCorrect(loginRequest, passwordEncoder)){
        throw new BadCredentialsException("Usuario o contraseña invalido");
    }

    boolean passTemp=user.get().getPassTemporaria();

    if (Boolean.TRUE.equals(passTemp)) {
        throw new TemporaryPasswordException("Please change your temporary password.");
    }

    var now= Instant.now();
    var expiresIn=3600L;

    var scope=user.get().getRol();

    var claims= JwtClaimsSet.builder()
          .issuer("mybackend")
          .subject(user.get().getId().toString())
          .issuedAt(now)
          .expiresAt(now.plusSeconds(expiresIn))
          .claim("scope",scope)
          .build();

    var jwtValue=jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    return ResponseEntity.ok(new LoginResponse(jwtValue,expiresIn));
   }

   @PostMapping("/cambiar-pass")
   public ResponseEntity<Void> cambiarPass(@RequestBody CambiarPassDTO request) {
      var user = usuarioRepository.findByUsername(request.username()).orElseThrow();

      if (!passwordEncoder.matches(request.password(), user.getPassword())) {
         throw new BadCredentialsException("Invalid current password");
      }

      user.setPassword(passwordEncoder.encode(request.newPass()));
      user.setPassTemporaria(false);
      usuarioRepository.save(user);

      return ResponseEntity.ok().build();
   }
}
