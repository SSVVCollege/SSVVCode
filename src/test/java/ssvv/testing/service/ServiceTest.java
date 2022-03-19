package ssvv.testing.service;

import java.util.stream.StreamSupport;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ssvv.testing.domain.Student;
import ssvv.testing.exceptions.EntityAlreadyExistsException;
import ssvv.testing.exceptions.ValidationException;
import ssvv.testing.repository.crud.CRUDRepository;
import ssvv.testing.repository.xml.StudentXMLRepository;
import ssvv.testing.utils.FileHelper;
import ssvv.testing.validation.StudentValidator;
import ssvv.testing.validation.Validator;

public class ServiceTest {
    private static final String STUDENT_FILE = "files/test/studenti.xml";

    private static final String INVALID_ID_MSG = "ID invalid";
    private static final String INVALID_NAME_MSG = "Nume invalid";
    private static final String INVALID_GROUP_MSG = "Grupa invalida";

    private static Service service;

    @BeforeAll
    public static void setUpClass() {
        FileHelper.ensureXmlEntityFileExists(ServiceTest.STUDENT_FILE);
        final Validator<Student> studentValidator = new StudentValidator();
        final CRUDRepository<String, Student> studentRepository = new StudentXMLRepository(ServiceTest.STUDENT_FILE);
        ServiceTest.service = new Service(studentRepository, null, null, studentValidator, null, null);
    }

    @AfterAll
    public static void tearDownClass() {
        FileHelper.emptyXmlEntityFile(ServiceTest.STUDENT_FILE);
    }

    private void saveStudentInvalidId() {
        final ValidationException vex1 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.service.saveStudent(null, "Name", 0),
                "Validation for null ID not triggered");
        final ValidationException vex2 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.service.saveStudent("", "Name", 0),
                "Validation for empty ID not triggered");

        Assertions.assertTrue(vex1.getMessage().contains(ServiceTest.INVALID_ID_MSG));
        Assertions.assertTrue(vex2.getMessage().contains(ServiceTest.INVALID_ID_MSG));
    }

    private void saveStudentInvalidName() {
        final ValidationException vex1 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.service.saveStudent("ID", null, 0),
                "Validation for null Name not triggered");
        final ValidationException vex2 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.service.saveStudent("ID", "", 0),
                "Validation for empty Name not triggered");

        Assertions.assertTrue(vex1.getMessage().contains(ServiceTest.INVALID_NAME_MSG));
        Assertions.assertTrue(vex2.getMessage().contains(ServiceTest.INVALID_NAME_MSG));
    }

    private void saveStudentInvalidGroup() {
        final ValidationException vex1 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.service.saveStudent("ID", "Name", null),
                "Validation for null Group not triggered");
        final ValidationException vex2 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.service.saveStudent("ID", "Name", 109),
                "Validation for Group out of interval not triggered");
        final ValidationException vex3 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.service.saveStudent("ID", "Name", 939),
                "Validation for Group out of interval not triggered");
        final ValidationException vex4 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.service.saveStudent("ID", "Name", -17),
                "Validation for Group out of interval not triggered");
        final ValidationException vex5 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.service.saveStudent("ID", "Name", Integer.MAX_VALUE),
                "Validation for Group out of interval not triggered");

        Assertions.assertTrue(vex1.getMessage().contains(ServiceTest.INVALID_GROUP_MSG));
        Assertions.assertTrue(vex2.getMessage().contains(ServiceTest.INVALID_GROUP_MSG));
        Assertions.assertTrue(vex3.getMessage().contains(ServiceTest.INVALID_GROUP_MSG));
        Assertions.assertTrue(vex4.getMessage().contains(ServiceTest.INVALID_GROUP_MSG));
        Assertions.assertTrue(vex4.getMessage().contains(ServiceTest.INVALID_GROUP_MSG));
    }

    @Test
    public void saveStudentInvalid() {
        this.saveStudentInvalidId();
        this.saveStudentInvalidName();
        this.saveStudentInvalidGroup();
    }

    @Test
    public void saveStudentValid() {
        ServiceTest.service.saveStudent("1", "John", 300);

        Assertions.assertThrows(EntityAlreadyExistsException.class, () -> ServiceTest.service.saveStudent("1", "John", 300),
                "No duplicate check for student");
        Assertions.assertThrows(EntityAlreadyExistsException.class, () -> ServiceTest.service.saveStudent("1", "Mike", 400),
                "No duplicate check for student");

        ServiceTest.service.saveStudent("2", "Mike", 300);
        ServiceTest.service.saveStudent("3", "John", 400);
        ServiceTest.service.saveStudent("4", "John", 300);

        Assertions.assertEquals(4, StreamSupport.stream(ServiceTest.service.findAllStudents().spliterator(), false).count());
    }
}