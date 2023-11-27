package ee.veebiprojekt.veebiprojekttest.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entity, long id) {
        super("Could not find " + entity + " with id " + id);
    }
}
