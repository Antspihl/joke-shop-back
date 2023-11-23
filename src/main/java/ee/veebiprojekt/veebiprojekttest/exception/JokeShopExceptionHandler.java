package ee.veebiprojekt.veebiprojekttest.exception;

import jakarta.servlet.ServletException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class JokeShopExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleUserException(EntityNotFoundException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<Object> handleUserException(InvalidValueException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(FieldNotUniqueException.class)
    public ResponseEntity<Object> handleUserException(FieldNotUniqueException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<Object> handleUserException(ServletException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleUserException(IOException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Object> handleUserException(SecurityException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }
}
