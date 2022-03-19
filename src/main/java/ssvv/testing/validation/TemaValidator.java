package ssvv.testing.validation;

import ssvv.testing.domain.Tema;
import ssvv.testing.exceptions.ValidationException;

public class TemaValidator implements Validator<Tema> {
    @Override
    public void validate(final Tema tema) throws ValidationException {
        if (tema.getID() == null || tema.getID().equals(""))
            throw new ValidationException("ID invalid! \n");

        if (tema.getDescriere() == null || tema.getDescriere().equals(""))
            throw new ValidationException("Descriere invalida! \n");

        if (tema.getDeadline() == null || tema.getDeadline() < 1 || tema.getDeadline() > 14)
            throw new ValidationException("Deadline invalid! \n");

        if (tema.getStartline() == null || tema.getStartline() < 1 || tema.getStartline() > 14)
            throw new ValidationException("Data de primire invalida! \n");

        if (tema.getStartline() > tema.getDeadline())
            throw new ValidationException("Interval startline - deadline invalid! \n");
    }
}

