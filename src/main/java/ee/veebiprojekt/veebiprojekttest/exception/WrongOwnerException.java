package ee.veebiprojekt.veebiprojekttest.exception;

public class WrongOwnerException extends RuntimeException {
    public WrongOwnerException(String entity, long id, long ownerId) {
        super("Wrong owner for " + entity + " with id " + id + ": " + ownerId);
    }
}
