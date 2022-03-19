package ssvv.testing.validation;

import ssvv.testing.domain.Student;
import ssvv.testing.exceptions.ValidationException;

public class StudentValidator implements Validator<Student> {
    @Override
    public void validate(final Student student) throws ValidationException {
        if (student.getID() == null || student.getID().equals(""))
            throw new ValidationException("ID invalid! \n");

        if (student.getNume() == null || student.getNume().equals(""))
            throw new ValidationException("Nume invalid! \n");

        if (student.getGrupa() == null || student.getGrupa() <= 110 || student.getGrupa() >= 938)
            throw new ValidationException("Grupa invalida! \n");
    }
}

