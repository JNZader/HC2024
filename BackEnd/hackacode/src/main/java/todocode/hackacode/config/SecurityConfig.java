package todocode.hackacode.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Configuración de seguridad para la aplicación.
 */
@Configuration
@EnableWebSecurity // Habilita la configuración de seguridad web de Spring Security.
public class SecurityConfig{

   @Value("${jwt.public.key}")
   private RSAPublicKey publicKey;
   @Value("${jwt.private.key}")
   private RSAPrivateKey privateKey;
   private static final String[] AUTH_WHITELIST = {
         "/v2/api-docs",
         "/swagger-resources",
         "/swagger-resources/**",
         "/configuration/ui",
         "/configuration/security",
         "/swagger-ui.html",
         "/webjars/**",
         "/v3/api-docs/**",
         "/swagger-ui/**"
   };
   /**
    * Configuración del filtro de seguridad HTTP.
    *
    * @param httpSecurity Configuración de seguridad HTTP.
    * @return SecurityFilterChain configurado.
    * @throws Exception Excepción si hay un error durante la configuración.
    */
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

      httpSecurity
            // Configura la autorización de las solicitudes HTTP
            .authorizeHttpRequests(authorize -> authorize
                  .requestMatchers(AUTH_WHITELIST).permitAll()
                  .requestMatchers(HttpMethod.POST, "/login").permitAll() // Permite el acceso a /login sin autenticación
                  .requestMatchers(HttpMethod.POST, "/crear").permitAll() // Permite el acceso a /crear sin autenticación
                  .requestMatchers(HttpMethod.POST, "/cambiar-pass").permitAll() // Permite el acceso a /cambiar-pass sin autenticación
                  .requestMatchers(HttpMethod.POST, "/cambiar-pass").permitAll() // Permite el acceso a /cambiar-pass sin autenticación.
                  .requestMatchers(HttpMethod.POST,"/recuperar-pass").permitAll()
                  .anyRequest().authenticated()) // Cualquier otra solicitud requiere autenticación
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())) // Configura el servidor de recursos OAuth2 con JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configura la gestión de sesiones como STATELESS
            .csrf(csrf -> csrf.disable() /*csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse
            ())*/)// Deshabilita la protección CSRF
            .logout(logout->logout.logoutSuccessUrl("/logout").permitAll());

      return httpSecurity.build();
   }

   /**
    * Bean para decodificar JWT.
    *
    * @return JwtDecoder configurado.
    */
   @Bean
   public JwtDecoder jwtDecoder() {
      return NimbusJwtDecoder.withPublicKey(publicKey).build(); // Crea un decodificador JWT con la clave pública
   }

   /**
    * Bean para codificar JWT.
    *
    * @return JwtEncoder configurado.
    */
   @Bean
   public JwtEncoder jwtEncoder() {
      // Crea un objeto JWK con la clave pública y privada
      JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();
      // Crea un conjunto de claves JWK inmutable
      var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
      // Crea un codificador JWT con el conjunto de claves JWK
      return new NimbusJwtEncoder(jwks);
   }

   /**
    * Bean para codificar contraseñas usando BCrypt.
    *
    * @return BCryptPasswordEncoder configurado.
    */
   @Bean
   public BCryptPasswordEncoder bCryptPasswordEncoder() {
      return new BCryptPasswordEncoder(); // Crea un codificador de contraseñas BCrypt
   }

}
