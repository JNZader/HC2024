package todocode.hackacode.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración personalizada de Jackson para la aplicación.
 */
@Configuration
public class JacksonConfig {

   /**
    * Bean que personaliza el constructor de objetos ObjectMapper de Jackson.
    *
    * @return Jackson2ObjectMapperBuilderCustomizer personalizado.
    */
   @Bean
   public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
      // Personaliza el constructor de objetos ObjectMapper de Jackson
      return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
            // Deshabilita la deserialización de propiedades desconocidas
            .featuresToDisable(
                  DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                  // Acepta números flotantes como enteros si es posible
                  DeserializationFeature.ACCEPT_FLOAT_AS_INT,
                  // Escribe fechas como marcas de tiempo en lugar de objetos Date
                  SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
            );
   }

}
