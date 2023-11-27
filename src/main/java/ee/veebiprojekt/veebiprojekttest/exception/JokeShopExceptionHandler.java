package ee.veebiprojekt.veebiprojekttest.exception;

import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class JokeShopExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleUserException(EntityNotFoundException e) {
        log.debug("Entity not found exception: {}", e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<Object> handleUserException(InvalidValueException e) {
        log.debug("Invalid value exception: {}", e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(FieldNotUniqueException.class)
    public ResponseEntity<Object> handleUserException(FieldNotUniqueException e) {
        log.debug("Not unique field exception: {}", e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<Object> handleUserException(ServletException e) {
        log.debug("Servlet exception: {}", e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleUserException(IOException e) {
        log.debug("IO exception: {}", e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Object> handleUserException(SecurityException e) {
        log.debug("Security exception: {}", e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUserException(Exception e) {
        log.error("Server exception: {}", e.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(e.getMessage());
    }
}
