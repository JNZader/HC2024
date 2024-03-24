package todocode.hackacode.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import todocode.hackacode.model.Cargo;
import todocode.hackacode.model.LoginRequest;

import java.util.UUID;

@Entity
@Table(name = "Usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @Column(name = "user_id")
   private UUID id;

   private String username;

   private String password;

   private Cargo rol;

   private Boolean passTemporaria;

   @OneToOne(mappedBy = "usuario", fetch = FetchType.LAZY)
   private Empleado empleado;
   public boolean isLoginCorrect(LoginRequest loginRequest, BCryptPasswordEncoder passwordEncoder){
      return passwordEncoder.matches(loginRequest.password(),this.password);
   }

}
