package ee.veebiprojekt.veebiprojekttest.exception;

public class FieldNotUniqueException extends RuntimeException {
    public FieldNotUniqueException(String field) {
        super(String.format("Given %s is already taken", field));
    }
}
