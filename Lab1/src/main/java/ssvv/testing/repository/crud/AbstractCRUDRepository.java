package ssvv.testing.repository.crud;

import java.util.HashMap;
import java.util.Map;

import ssvv.testing.domain.HasID;
import ssvv.testing.validation.ValidationException;
import ssvv.testing.validation.Validator;

public abstract class AbstractCRUDRepository<ID, E extends HasID<ID>> implements CRUDRepository<ID, E> {
    protected Map<ID, E> entities;
    protected Validator<E> validator;

    public AbstractCRUDRepository(final Validator<E> validator) {
        this.entities = new HashMap<>();
        this.validator = validator;
    }

    @Override
    public E findOne(final ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID-ul nu poate fi null! \n");
        }
        else {
            return this.entities.get(id);
        }
    }

    @Override
    public Iterable<E> findAll() { return this.entities.values(); }

    @Override
    public E save(final E entity) throws ValidationException {
        try {
            this.validator.validate(entity);
            return this.entities.putIfAbsent(entity.getID(), entity);
        }
        catch (final ValidationException ve) {
            System.out.println("Entitatea nu este valida! \n");
            return null;
        }
    }

    @Override
    public E delete(final ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID-ul nu poate fi nul! \n");
        }
        else {
            return this.entities.remove(id);
        }
    }

    @Override
    public E update(final E entity) {
        try {
            this.validator.validate(entity);
            return this.entities.replace(entity.getID(), entity);
        }
        catch (final ValidationException ve) {
            System.out.println("Entitatea nu este valida! \n");
            return null;
        }
    }
}
