package ee.veebiprojekt.veebiprojekttest.exception;

public class InvalidValueException extends RuntimeException {
    public InvalidValueException(String entity, String fieldName, int value) {
        super("Invalid value " + value + " for " + fieldName + " in " + entity);
    }
}
