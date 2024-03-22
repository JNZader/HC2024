package todocode.hackacode.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuración del dominio de la aplicación.
 */
@Configuration
@EntityScan("todocode.hackacode.domain") // Escanea y detecta las entidades JPA en este paquete y sus subpaquetes.
@EnableJpaRepositories("todocode.hackacode.repos") // Habilita los repositorios JPA en este paquete y sus subpaquetes.
@EnableTransactionManagement // Habilita la administración de transacciones para la capa de persistencia.
public class DomainConfig {
}
