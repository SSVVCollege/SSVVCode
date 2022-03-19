package ssvv.testing.validation;

import ssvv.testing.domain.Nota;
import ssvv.testing.exceptions.ValidationException;

public class NotaValidator implements Validator<Nota> {
    @Override
    public void validate(final Nota nota) throws ValidationException {
        if (nota.getID() == null)
            throw new ValidationException("ID Nota invalid! \n");

        if (nota.getID().getObject1() == null || nota.getID().getObject1().equals(""))
            throw new ValidationException("ID Student invalid! \n");

        if (nota.getID().getObject2() == null || nota.getID().getObject2().equals(""))
            throw new ValidationException("ID Tema invalid! \n");

        if (nota.getNota() == null || nota.getNota() < 0 || nota.getNota() > 10)
            throw new ValidationException("Nota invalida! \n");

        if (nota.getSaptamanaPredare() == null || nota.getSaptamanaPredare() < 0) {
            throw new ValidationException("Saptamana de predare invalida! \n");
        }
    }
}
