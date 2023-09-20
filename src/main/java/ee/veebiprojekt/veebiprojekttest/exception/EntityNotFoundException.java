package ee.veebiprojekt.veebiprojekttest.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(long id) {
        super(String.format("Did not found the requested object with given id: %d", id));
    }
}
