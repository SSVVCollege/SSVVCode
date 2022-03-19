package ssvv.testing.exceptions;

import ssvv.testing.domain.HasID;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException (Class<?> clazz) {
        super("Entity of type " + clazz.getSimpleName() + " not found");
    }
}
