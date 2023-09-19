package ee.veebiprojekt.veebiprojekttest.exception;

public class InvalidValueException extends RuntimeException {
    public InvalidValueException(int value) {
        super(String.format("Given invalid value: %d", value));
    }
}
