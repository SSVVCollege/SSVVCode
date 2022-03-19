package ssvv.testing.exceptions;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException (Class<?> clazz) {
        super("Entity of type " + clazz.getSimpleName() + " already exists");
    }
}
