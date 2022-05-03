package ssvv.testing.utils;

import ssvv.testing.domain.Nota;
import ssvv.testing.domain.Student;
import ssvv.testing.domain.Tema;
import ssvv.testing.exceptions.ValidationException;

public class GlobalMockValidator {
    public static void validateStudent(final Student student) {
        if (student.getID() == null || student.getID().equals(""))
            throw new ValidationException("ID invalid! \n");

        if (student.getNume() == null || student.getNume().equals(""))
            throw new ValidationException("Nume invalid! \n");

        if (student.getGrupa() == null || student.getGrupa() <= 110 || student.getGrupa() >= 938)
            throw new ValidationException("Grupa invalida! \n");
    }

    public static void validateTema(final Tema tema) {
        if (tema.getID() == null || tema.getID().equals(""))
            throw new ValidationException("ID invalid! \n");

        if (tema.getDescriere() == null || tema.getDescriere().equals(""))
            throw new ValidationException("Descriere invalida! \n");

        if (tema.getDeadline() == null || tema.getDeadline() < 1 || tema.getDeadline() > 14)
            throw new ValidationException("Deadline invalid! \n");

        if (tema.getStartline() == null || tema.getStartline() < 1 || tema.getStartline() > 14)
            throw new ValidationException("Data de primire invalida! \n");

        if (tema.getStartline() > tema.getDeadline())
            throw new ValidationException("Interval startline-deadline invalid! \n");
    }

    public static void validateNota(final Nota nota) {
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
