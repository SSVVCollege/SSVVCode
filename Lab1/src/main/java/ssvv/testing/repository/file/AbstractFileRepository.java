package ssvv.testing.repository.file;

import ssvv.testing.domain.HasID;
import ssvv.testing.repository.crud.AbstractCRUDRepository;
import ssvv.testing.validation.ValidationException;
import ssvv.testing.validation.Validator;

public abstract class AbstractFileRepository<ID, E extends HasID<ID>> extends AbstractCRUDRepository<ID,E> {
    protected String filename;

    public AbstractFileRepository(final Validator<E> validator, final String filename) {
        super(validator);
        this.filename = filename;
    }

    protected abstract void loadFromFile();
    protected abstract void writeToFile(E entity);
    protected abstract void writeToFileAll();

    @Override
    public Iterable<E> findAll() {
        this.loadFromFile();
        return super.findAll();
    }

    @Override
    public E save(final E entity) throws ValidationException {
        final E result = super.save(entity);
        if (result == null) {
            this.writeToFile(entity);
        }
        return result;
    }

    @Override
    public E delete(final ID id) {
        final E result = super.delete(id);
        this.writeToFileAll();

        return result;
    }

    @Override
    public E update(final E newEntity) {
        final E result = super.update(newEntity);
        this.writeToFileAll();

        return result;
    }
}
