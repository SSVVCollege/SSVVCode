package ssvv.testing.repository.crud;

import java.util.HashMap;
import java.util.Map;

import lombok.NoArgsConstructor;
import ssvv.testing.domain.HasID;
import ssvv.testing.exceptions.EntityAlreadyExistsException;
import ssvv.testing.exceptions.EntityNotFoundException;

@NoArgsConstructor
public abstract class AbstractCRUDRepository<ID, E extends HasID<ID>> implements CRUDRepository<ID, E> {
    protected final Map<ID, E> entities = new HashMap<>();

    @Override
    public E findOne(final ID id) { return this.entities.get(id); }

    @Override
    public Iterable<E> findAll() { return this.entities.values(); }

    @Override
    public void save(final E entity) throws EntityAlreadyExistsException {
        E result = this.entities.putIfAbsent(entity.getID(), entity);
        if (result != null) throw new EntityAlreadyExistsException(entity.getClass());
    }

    @Override
    public E delete(final ID id) { return this.entities.remove(id); }

    @Override
    public void update(final E entity) throws EntityNotFoundException {
        E result = this.entities.replace(entity.getID(), entity);
        if (result == null) throw new EntityNotFoundException(entity.getClass());
    }


}
