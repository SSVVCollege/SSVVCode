package ssvv.testing.repository.crud;

import ssvv.testing.domain.Tema;
import ssvv.testing.validation.Validator;

public class TemaRepository extends AbstractCRUDRepository<String, Tema> {
    public TemaRepository(final Validator<Tema> validator) {
        super(validator);
    }
}

