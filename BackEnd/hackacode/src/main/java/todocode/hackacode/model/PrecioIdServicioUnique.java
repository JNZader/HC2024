package todocode.hackacode.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;
import todocode.hackacode.service.impl.PrecioServiceImpl;

/**
 * Validate that the id value isn't taken yet.
 */
@Target({FIELD, METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = PrecioIdServicioUnique.PrecioIdServicioUniqueValidator.class
)
public @interface PrecioIdServicioUnique {

    String message() default "{Exists.precio.idServicio}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class PrecioIdServicioUniqueValidator implements ConstraintValidator<PrecioIdServicioUnique, Long> {

        private final PrecioServiceImpl precioServiceImpl;
        private final HttpServletRequest request;

        public PrecioIdServicioUniqueValidator(final PrecioServiceImpl precioServiceImpl,
                final HttpServletRequest request) {
            this.precioServiceImpl = precioServiceImpl;
            this.request = request;
        }

        @Override
        public boolean isValid(final Long value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked")
            final Map<String, String> pathVariables
                    = ((Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equals(precioServiceImpl.get(Long.getLong(currentId)))) {
                // value hasn't changed
                return true;
            }
            return !precioServiceImpl.idServicioExists(value);
        }

    }

}
