package ssvv.testing.repository.crud;

import ssvv.testing.domain.Student;
import ssvv.testing.validation.Validator;

public class StudentRepository extends AbstractCRUDRepository<String, Student> {
    public StudentRepository(final Validator<Student> validator) {
        super(validator);
    }
}

