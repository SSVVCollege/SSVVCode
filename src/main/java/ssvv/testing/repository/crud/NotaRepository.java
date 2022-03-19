package ssvv.testing.repository.crud;

public interface NotaRepository<E> {
    void processFile(E entity);

    void deleteFile(E entity);
}
