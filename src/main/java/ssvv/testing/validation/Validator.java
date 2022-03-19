package ssvv.testing.validation;

import ssvv.testing.exceptions.ValidationException;

public interface Validator<E> {
    void validate(E entity) throws ValidationException;
}