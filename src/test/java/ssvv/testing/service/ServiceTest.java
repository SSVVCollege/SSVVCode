package ssvv.testing.service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ssvv.testing.domain.Student;
import ssvv.testing.domain.Tema;
import ssvv.testing.exceptions.EntityAlreadyExistsException;
import ssvv.testing.exceptions.ValidationException;
import ssvv.testing.repository.crud.CRUDRepository;
import ssvv.testing.repository.xml.StudentXMLRepository;
import ssvv.testing.repository.xml.TemaXMLRepository;
import ssvv.testing.utils.FileHelper;
import ssvv.testing.validation.StudentValidator;
import ssvv.testing.validation.TemaValidator;
import ssvv.testing.validation.Validator;

public class ServiceTest {
    private static final String STUDENT_FILE = "files/test/studenti.xml";
    private static final String ASSIGNMENT_FILE = "files/test/teme.xml";

    private static final Map<String, String> INVALID_MESSAGES = new HashMap<String, String>();

    private static Service serviceStudent;
    private static Service serviceAssignment;

    public static void setUpInvalidMessages() {
        ServiceTest.INVALID_MESSAGES.put("id", "ID invalid");
        ServiceTest.INVALID_MESSAGES.put("name", "Nume invalid");
        ServiceTest.INVALID_MESSAGES.put("group", "Grupa invalida");
    }

    public static void setUpStudent() {
        FileHelper.ensureXmlEntityFileExists(ServiceTest.STUDENT_FILE);
        final Validator<Student> studentValidator = new StudentValidator();
        final CRUDRepository<String, Student> studentRepository = new StudentXMLRepository(ServiceTest.STUDENT_FILE);
        ServiceTest.serviceStudent = new Service(studentRepository, null, null, studentValidator, null, null);
    }

    public static void setUpAssignment() {
        FileHelper.ensureXmlEntityFileExists(ServiceTest.ASSIGNMENT_FILE);
        final Validator<Tema> assignmentValidator = new TemaValidator();
        final CRUDRepository<String, Tema> assignmentRepository = new TemaXMLRepository(ServiceTest.ASSIGNMENT_FILE);
        ServiceTest.serviceAssignment = new Service(null, assignmentRepository, null, null, assignmentValidator, null);
    }

    @BeforeAll
    public static void setUpClass() {
        ServiceTest.setUpInvalidMessages();
        ServiceTest.setUpStudent();
        ServiceTest.setUpAssignment();
    }

    @AfterAll
    public static void tearDownClass() {
        FileHelper.emptyXmlEntityFile(ServiceTest.STUDENT_FILE);
        FileHelper.emptyXmlEntityFile(ServiceTest.ASSIGNMENT_FILE);
    }

    private void saveStudentInvalidId() {
        final ValidationException vex1 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.serviceStudent.saveStudent(null, "Name", 0),
                "Validation for null ID not triggered");
        final ValidationException vex2 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.serviceStudent.saveStudent("", "Name", 0),
                "Validation for empty ID not triggered");

        Assertions.assertTrue(vex1.getMessage().contains(ServiceTest.INVALID_MESSAGES.get("id")));
        Assertions.assertTrue(vex2.getMessage().contains(ServiceTest.INVALID_MESSAGES.get("id")));
    }

    private void saveStudentInvalidName() {
        final ValidationException vex1 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.serviceStudent.saveStudent("ID", null, 0),
                "Validation for null Name not triggered");
        final ValidationException vex2 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.serviceStudent.saveStudent("ID", "", 0),
                "Validation for empty Name not triggered");

        Assertions.assertTrue(vex1.getMessage().contains(ServiceTest.INVALID_MESSAGES.get("name")));
        Assertions.assertTrue(vex2.getMessage().contains(ServiceTest.INVALID_MESSAGES.get("name")));
    }

    private void saveStudentInvalidGroup() {
        final ValidationException vex1 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.serviceStudent.saveStudent("ID", "Name", null),
                "Validation for null Group not triggered");
        final ValidationException vex2 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.serviceStudent.saveStudent("ID", "Name", 109),
                "Validation for Group out of interval not triggered");
        final ValidationException vex3 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.serviceStudent.saveStudent("ID", "Name", 939),
                "Validation for Group out of interval not triggered");
        final ValidationException vex4 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.serviceStudent.saveStudent("ID", "Name", -17),
                "Validation for Group out of interval not triggered");
        final ValidationException vex5 = Assertions.assertThrows(ValidationException.class, () -> ServiceTest.serviceStudent.saveStudent("ID", "Name", Integer.MAX_VALUE),
                "Validation for Group out of interval not triggered");

        Assertions.assertTrue(vex1.getMessage().contains(ServiceTest.INVALID_MESSAGES.get("group")));
        Assertions.assertTrue(vex2.getMessage().contains(ServiceTest.INVALID_MESSAGES.get("group")));
        Assertions.assertTrue(vex3.getMessage().contains(ServiceTest.INVALID_MESSAGES.get("group")));
        Assertions.assertTrue(vex4.getMessage().contains(ServiceTest.INVALID_MESSAGES.get("group")));
        Assertions.assertTrue(vex4.getMessage().contains(ServiceTest.INVALID_MESSAGES.get("group")));
    }

    @Test
    public void saveStudentInvalid() {
        this.saveStudentInvalidId();
        this.saveStudentInvalidName();
        this.saveStudentInvalidGroup();
    }

    @Test
    public void saveStudentValid() {
        ServiceTest.serviceStudent.saveStudent("1", "John", 300);

        Assertions.assertThrows(EntityAlreadyExistsException.class, () -> ServiceTest.serviceStudent.saveStudent("1", "John", 300),
                "No duplicate check for student");
        Assertions.assertThrows(EntityAlreadyExistsException.class, () -> ServiceTest.serviceStudent.saveStudent("1", "Mike", 400),
                "No duplicate check for student");

        ServiceTest.serviceStudent.saveStudent("2", "Mike", 300);
        ServiceTest.serviceStudent.saveStudent("3", "John", 400);
        ServiceTest.serviceStudent.saveStudent("4", "John", 300);

        Assertions.assertEquals(4, StreamSupport.stream(ServiceTest.serviceStudent.findAllStudents().spliterator(), false).count());
    }

    @Test
    public void AssignmentNullIdException() {
        Assertions.assertThrows(ValidationException.class, () -> ServiceTest.serviceAssignment.saveTema(null, "descr", 4, 3));
    }

    @Test
    public void AssignmentEmptyIdException() {

    }

    @Test
    public void AssignmentNullDescriptionException() {

    }

    @Test
    public void AssignmentEmptyDescriptionException() {

    }

    @Test
    public void AssignmentNullDeadlineException() {

    }

    @Test
    public void AssignmentLowerBoundDeadlineException() {

    }

    @Test
    public void AssignmentUpperBoundDeadlineException() {

    }

    @Test
    public void AssignmentNullStartlineException() {

    }


    @Test
    public void AssignmentLowerBoundStartlineException() {

    }

    @Test
    public void AssignmentUpperBoundStartlineException() {

    }

    @Test
    public void AssignmentIntervalExcception() {

    }

}
