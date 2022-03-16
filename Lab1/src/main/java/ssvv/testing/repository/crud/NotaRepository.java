package ssvv.testing.repository.crud;

import ssvv.testing.domain.Nota;
import ssvv.testing.domain.Pair;
import ssvv.testing.validation.Validator;

public class NotaRepository extends AbstractCRUDRepository<Pair<String, String>, Nota> {
    public NotaRepository(final Validator<Nota> validator) {
        super(validator);
    }
}
