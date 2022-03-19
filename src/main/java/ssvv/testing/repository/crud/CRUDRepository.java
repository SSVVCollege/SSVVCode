package ssvv.testing.repository.crud;

import ssvv.testing.domain.HasID;
import ssvv.testing.exceptions.EntityAlreadyExistsException;
import ssvv.testing.exceptions.EntityNotFoundException;

public interface CRUDRepository<ID, E extends HasID<ID>> {
    /**
     * @param id - the id of the entity to be returned; must be valid and not-null
     * @return the entity with the specified id or null if not found
     **/
    E findOne(ID id);

    /**
     * @return all entities
     **/
    Iterable<E> findAll();

    /**
     * @param entity; entity must be not null, must be valid and not-null
     * @throws EntityAlreadyExistsException if the entity already exists
     **/
    void save(E entity) throws EntityAlreadyExistsException;

    /**
     * removes the entity with the specified id
     * @param id; id must be valid and not-null
     * @return the removed entity or null if there is no entity with the given id
     **/
    E delete(ID id);

    /**
     * @param entity; entity must not valid and not-null
     * @throws EntityNotFoundException if the entity does not exist
     **/
    void update(E entity) throws EntityNotFoundException;
}
