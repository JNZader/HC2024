package todocode.hackacode.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class TemporaryPasswordException extends RuntimeException {

    public TemporaryPasswordException(String message) {
        super(message);
    }
}
