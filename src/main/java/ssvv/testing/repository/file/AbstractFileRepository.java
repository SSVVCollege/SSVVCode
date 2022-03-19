package ssvv.testing.repository.file;

import lombok.extern.slf4j.Slf4j;
import ssvv.testing.domain.HasID;
import ssvv.testing.exceptions.EntityAlreadyExistsException;
import ssvv.testing.exceptions.EntityNotFoundException;
import ssvv.testing.repository.crud.AbstractCRUDRepository;

@Slf4j
public abstract class AbstractFileRepository<ID, E extends HasID<ID>> extends AbstractCRUDRepository<ID,E> {
    protected final String filename;

    public AbstractFileRepository(final String filename) {
        this.filename = filename;
        this.loadFromFile();
    }

    protected abstract void loadFromFile();

    protected abstract void writeToFile(E entity);

    protected abstract void writeToFileAll();

    @Override
    public void save(final E entity) throws EntityAlreadyExistsException {
        super.save(entity);
        writeToFile(entity);
    }

    @Override
    public E delete(final ID id) {
        final E result = super.delete(id);
        if (result != null) this.writeToFileAll();
        return result;
    }

    @Override
    public void update(final E newEntity) throws EntityNotFoundException  {
        super.update(newEntity);
        this.writeToFileAll();
    }
}
