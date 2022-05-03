package ssvv.testing.integration;

import java.util.stream.StreamSupport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ssvv.testing.domain.Nota;
import ssvv.testing.domain.Pair;
import ssvv.testing.domain.Student;
import ssvv.testing.domain.Tema;
import ssvv.testing.exceptions.EntityAlreadyExistsException;
import ssvv.testing.exceptions.EntityNotFoundException;
import ssvv.testing.exceptions.ValidationException;
import ssvv.testing.repository.crud.CRUDRepository;
import ssvv.testing.repository.xml.NotaXMLRepository;
import ssvv.testing.service.Service;
import ssvv.testing.utils.GlobalMockRepo;
import ssvv.testing.utils.GlobalMockValidator;
import ssvv.testing.validation.Validator;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
public class ServiceIT {
    @Mock
    private Validator<Student> studentValidator;
    @Mock
    private Validator<Nota> notaValidator;
    @Mock
    private Validator<Tema> temaValidator;
    @Mock
    private CRUDRepository<String, Student> studentRepo;
    @Mock
    private NotaXMLRepository notaRepo;
    @Mock
    private CRUDRepository<String, Tema> temaRepo;

    private static final Student student = new Student("100", "Jacob", 500);
    private static final Tema tema = new Tema("1", "IMPORTANT", 5, 3);
    private static final Nota nota = new Nota(new Pair<>("100", "1"), 10.00, 4, "Foarte Bine");

    private Service service;

    @BeforeEach
    public void setUp() {
        this.service = new Service(this.studentRepo, this.temaRepo, this.notaRepo, this.studentValidator, this.temaValidator, this.notaValidator);
    }

    @AfterEach
    public void tearDown() {
        GlobalMockRepo.clear();
    }

    private void assertEqStudents(final Student s) {
        Assertions.assertEquals(ServiceIT.student.getID(), s.getID());
        Assertions.assertEquals(ServiceIT.student.getNume(), s.getNume());
        Assertions.assertEquals(ServiceIT.student.getGrupa(), s.getGrupa());
    }

    private void assertEqTeme(final Tema t) {
        Assertions.assertEquals(ServiceIT.tema.getID(), t.getID());
        Assertions.assertEquals(ServiceIT.tema.getDescriere(), t.getDescriere());
        Assertions.assertEquals(ServiceIT.tema.getStartline(), t.getStartline());
        Assertions.assertEquals(ServiceIT.tema.getDeadline(), t.getDeadline());
    }

    private void assertEqNote(final Nota n) {
        Assertions.assertEquals(ServiceIT.nota.getID().getObject1(), n.getID().getObject1());
        Assertions.assertEquals(ServiceIT.nota.getID().getObject2(), n.getID().getObject2());
        Assertions.assertEquals(ServiceIT.nota.getNota(), n.getNota());
        Assertions.assertEquals(ServiceIT.nota.getFeedback(), n.getFeedback());
        Assertions.assertEquals(ServiceIT.nota.getSaptamanaPredare(), n.getSaptamanaPredare());
    }

    private void setUpMocks() {
        //student components
        Mockito.lenient().doAnswer(invocation -> {
            GlobalMockValidator.validateStudent(invocation.getArgument(0));
            return null;
        }).when(this.studentValidator).validate(Mockito.any(Student.class));

        Mockito.lenient().doAnswer(invocation -> {
            GlobalMockRepo.addStudent(invocation.getArgument(0));
            return null;
        }).when(this.studentRepo).save(Mockito.any(Student.class));

        Mockito.lenient().when(this.studentRepo.findAll()).thenAnswer(invocation -> GlobalMockRepo.getAllStudents());
        Mockito.lenient().when(this.studentRepo.findOne(Mockito.anyString())).thenAnswer(invocation -> GlobalMockRepo.findStudent(invocation.getArgument(0)));

        //tema components
        Mockito.lenient().doAnswer(invocation -> {
            GlobalMockValidator.validateTema(invocation.getArgument(0));
            return null;
        }).when(this.temaValidator).validate(Mockito.any(Tema.class));

        Mockito.lenient().doAnswer(invocation -> {
            GlobalMockRepo.addTema(invocation.getArgument(0));
            return null;
        }).when(this.temaRepo).save(Mockito.any(Tema.class));

        Mockito.lenient().when(this.temaRepo.findAll()).thenAnswer(invocation -> GlobalMockRepo.getAllTeme());
        Mockito.lenient().when(this.temaRepo.findOne(Mockito.anyString())).thenAnswer(invocation -> GlobalMockRepo.findTema(invocation.getArgument(0)));

        //nota components
        Mockito.lenient().doAnswer(invocation -> {
            GlobalMockValidator.validateNota(invocation.getArgument(0));
            return null;
        }).when(this.notaValidator).validate(Mockito.any(Nota.class));

        Mockito.lenient().doAnswer(invocation -> {
            GlobalMockRepo.addNota(invocation.getArgument(0));
            return null;
        }).when(this.notaRepo).save(Mockito.any(Nota.class));

        Mockito.lenient().when(this.notaRepo.findAll()).thenAnswer(invocation -> GlobalMockRepo.getAllNote());
        Mockito.lenient().when(this.notaRepo.findOne(Mockito.any(Pair.class))).thenAnswer(invocation -> GlobalMockRepo.findNota(invocation.getArgument(0)));
        Mockito.lenient().doNothing().when(this.notaRepo).processFile(Mockito.any(Student.class));
    }

    /**
     * Top-Down Integration
     */
    @Test
    public void addStudentWithMocksIT() {
        //when
        this.setUpMocks();
        final ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);

        //then
        Assertions.assertThrows(ValidationException.class, () -> this.service.saveStudent("100", "Jacob", 1000));
        Assertions.assertThrows(ValidationException.class, () -> this.service.saveStudent("100", "", 500));
        Assertions.assertThrows(ValidationException.class, () -> this.service.saveStudent(null, "Jacob", 500));
        this.service.saveStudent(ServiceIT.student.getID(), ServiceIT.student.getNume(), ServiceIT.student.getGrupa());
        Assertions.assertThrows(EntityAlreadyExistsException.class, () -> this.service.saveStudent(ServiceIT.student.getID(), ServiceIT.student.getNume(), ServiceIT.student.getGrupa()));

        Mockito.verify(this.studentValidator, Mockito.times(5)).validate(Mockito.any(Student.class));
        Mockito.verify(this.studentRepo, Mockito.times(2)).save(studentCaptor.capture());

        this.assertEqStudents(studentCaptor.getValue());
        Assertions.assertEquals(1, (int) StreamSupport.stream(this.service.findAllStudents().spliterator(), false).count());
    }

    @Test
    public void addTemaWithMocksIT() {
        //when
        this.setUpMocks();
        final ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        final ArgumentCaptor<Tema> temaCaptor = ArgumentCaptor.forClass(Tema.class);

        //then
        this.service.saveStudent(ServiceIT.student.getID(), ServiceIT.student.getNume(), ServiceIT.student.getGrupa());

        Assertions.assertThrows(ValidationException.class, () -> this.service.saveTema("1", "Descriere", 3, 5));
        Assertions.assertThrows(ValidationException.class, () -> this.service.saveTema("1", "Descriere", 17, 5));
        Assertions.assertThrows(ValidationException.class, () -> this.service.saveTema("1", "", 12, 5));
        Assertions.assertThrows(ValidationException.class, () -> this.service.saveTema(null, "Descriere", 12, 5));

        this.service.saveTema(ServiceIT.tema.getID(), ServiceIT.tema.getDescriere(), ServiceIT.tema.getDeadline(), ServiceIT.tema.getStartline());
        Assertions.assertThrows(EntityAlreadyExistsException.class, () -> this.service.saveTema(ServiceIT.tema.getID(), ServiceIT.tema.getDescriere(), ServiceIT.tema.getDeadline(), ServiceIT.tema.getStartline()));

        Mockito.verify(this.studentValidator).validate(Mockito.any(Student.class));
        Mockito.verify(this.studentRepo).save(studentCaptor.capture());
        Mockito.verify(this.temaValidator, Mockito.times(6)).validate(Mockito.any(Tema.class));
        Mockito.verify(this.temaRepo, Mockito.times(2)).save(temaCaptor.capture());

        Assertions.assertEquals(1, (int) StreamSupport.stream(this.service.findAllStudents().spliterator(), false).count());
        Assertions.assertEquals(1, (int) StreamSupport.stream(this.service.findAllTeme().spliterator(), false).count());

        this.assertEqStudents(studentCaptor.getValue());
        this.assertEqTeme(temaCaptor.getValue());
    }

    @Test
    public void addNotaWithMocksIT() {
        //when
        this.setUpMocks();
        final ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        final ArgumentCaptor<Tema> temaCaptor = ArgumentCaptor.forClass(Tema.class);
        final ArgumentCaptor<Nota> notaCaptor = ArgumentCaptor.forClass(Nota.class);

        //then
        this.service.saveStudent(ServiceIT.student.getID(), ServiceIT.student.getNume(), ServiceIT.student.getGrupa());
        this.service.saveTema(ServiceIT.tema.getID(), ServiceIT.tema.getDescriere(), ServiceIT.tema.getDeadline(), ServiceIT.tema.getStartline());

        Assertions.assertThrows(EntityNotFoundException.class, () -> this.service.saveNota(ServiceIT.tema.getID(), ServiceIT.tema.getID(), 7.00, 5, "Ok"));
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.service.saveNota(ServiceIT.student.getID(), ServiceIT.student.getID(), 7.00, 5, "Ok"));
        Assertions.assertThrows(ValidationException.class, () -> this.service.saveNota(ServiceIT.student.getID(), ServiceIT.tema.getID(), 15.00, 5, "Ok"));
        Assertions.assertThrows(ValidationException.class, () -> this.service.saveNota(ServiceIT.student.getID(), ServiceIT.tema.getID(), 10.00, -1, "Ok"));

        this.service.saveNota(ServiceIT.nota.getID().getObject1(), ServiceIT.nota.getID().getObject2(), ServiceIT.nota.getNota(), ServiceIT.nota.getSaptamanaPredare(), ServiceIT.nota.getFeedback());
        Assertions.assertThrows(EntityAlreadyExistsException.class, () -> this.service.saveNota(ServiceIT.nota.getID().getObject1(), ServiceIT.nota.getID().getObject2(), ServiceIT.nota.getNota(), ServiceIT.nota.getSaptamanaPredare(), ServiceIT.nota.getFeedback()));

        Mockito.verify(this.studentValidator).validate(Mockito.any(Student.class));
        Mockito.verify(this.temaValidator).validate(Mockito.any(Tema.class));
        Mockito.verify(this.studentRepo).save(studentCaptor.capture());
        Mockito.verify(this.temaRepo).save(temaCaptor.capture());
        Mockito.verify(this.studentRepo, Mockito.times(6)).findOne(Mockito.anyString());
        Mockito.verify(this.temaRepo, Mockito.times(6)).findOne(Mockito.anyString());
        Mockito.verify(this.notaValidator, Mockito.times(4)).validate(Mockito.any(Nota.class));
        Mockito.verify(this.notaRepo, Mockito.times(2)).save(notaCaptor.capture());

        Assertions.assertEquals(1, (int) StreamSupport.stream(this.service.findAllStudents().spliterator(), false).count());
        Assertions.assertEquals(1, (int) StreamSupport.stream(this.service.findAllTeme().spliterator(), false).count());
        Assertions.assertEquals(1, (int) StreamSupport.stream(this.service.findAllNote().spliterator(), false).count());

        this.assertEqStudents(studentCaptor.getValue());
        this.assertEqTeme(temaCaptor.getValue());
        this.assertEqNote(notaCaptor.getValue());
    }

    /**
     * Big-Bang Integration
     */
    @Test
    public void addStudentWithDummysIT() {
        //when
        Mockito.lenient().doNothing().when(this.studentValidator).validate(Mockito.any(Student.class));
        Mockito.lenient().doNothing().when(this.studentRepo).save(Mockito.any(Student.class));

        //then
        this.service.saveStudent(ServiceIT.student.getID(), ServiceIT.student.getNume(), ServiceIT.student.getGrupa());

        Mockito.verify(this.studentValidator).validate(ServiceIT.student);
        Mockito.verify(this.studentRepo).save(ServiceIT.student);
    }

    @Test
    public void addTemaWithDummysIT() {
        //when
        Mockito.lenient().doNothing().when(this.temaValidator).validate(Mockito.any(Tema.class));
        Mockito.lenient().doNothing().when(this.temaRepo).save(Mockito.any(Tema.class));

        //then
        this.service.saveTema(ServiceIT.tema.getID(), ServiceIT.tema.getDescriere(), ServiceIT.tema.getDeadline(), ServiceIT.tema.getStartline());

        //assertions
        Mockito.verify(this.temaValidator).validate(ServiceIT.tema);
        Mockito.verify(this.temaRepo).save(ServiceIT.tema);
    }

    @Test
    public void addNotaWithDummysIT() {
        //when
        Mockito.lenient().doNothing().when(this.notaValidator).validate(Mockito.any(Nota.class));
        Mockito.lenient().doNothing().when(this.notaRepo).save(Mockito.any(Nota.class));
        Mockito.lenient().when(this.studentRepo.findOne(Mockito.anyString())).thenReturn(ServiceIT.student);
        Mockito.lenient().when(this.temaRepo.findOne(Mockito.anyString())).thenReturn(ServiceIT.tema);

        //then
        this.service.saveNota(ServiceIT.nota.getID().getObject1(), ServiceIT.nota.getID().getObject2(), ServiceIT.nota.getNota(), ServiceIT.nota.getSaptamanaPredare(), ServiceIT.nota.getFeedback());

        //assertions
        Mockito.verify(this.studentRepo).findOne(ServiceIT.student.getID());
        Mockito.verify(this.temaRepo).findOne(ServiceIT.tema.getID());
        Mockito.verify(this.notaValidator).validate(ServiceIT.nota);
        Mockito.verify(this.notaRepo).save(ServiceIT.nota);
        Mockito.verify(this.notaRepo).processFile(ServiceIT.student);
    }

    @Test
    public void systemWithDummysIT() {
        //when
        Mockito.lenient().doNothing().when(this.studentValidator).validate(Mockito.any(Student.class));
        Mockito.lenient().doNothing().when(this.studentRepo).save(Mockito.any(Student.class));
        Mockito.lenient().doNothing().when(this.temaValidator).validate(Mockito.any(Tema.class));
        Mockito.lenient().doNothing().when(this.temaRepo).save(Mockito.any(Tema.class));
        Mockito.lenient().doNothing().when(this.notaValidator).validate(Mockito.any(Nota.class));
        Mockito.lenient().doNothing().when(this.notaRepo).save(Mockito.any(Nota.class));
        Mockito.lenient().when(this.studentRepo.findOne(Mockito.anyString())).thenReturn(ServiceIT.student);
        Mockito.lenient().when(this.temaRepo.findOne(Mockito.anyString())).thenReturn(ServiceIT.tema);

        //then
        this.service.saveStudent(ServiceIT.student.getID(), ServiceIT.student.getNume(), ServiceIT.student.getGrupa());
        this.service.saveTema(ServiceIT.tema.getID(), ServiceIT.tema.getDescriere(), ServiceIT.tema.getDeadline(), ServiceIT.tema.getStartline());
        this.service.saveNota(ServiceIT.nota.getID().getObject1(), ServiceIT.nota.getID().getObject2(), ServiceIT.nota.getNota(), ServiceIT.nota.getSaptamanaPredare(), ServiceIT.nota.getFeedback());

        //assertions
        Mockito.verify(this.studentValidator).validate(ServiceIT.student);
        Mockito.verify(this.studentRepo).save(ServiceIT.student);
        Mockito.verify(this.temaValidator).validate(ServiceIT.tema);
        Mockito.verify(this.temaRepo).save(ServiceIT.tema);
        Mockito.verify(this.studentRepo).findOne(ServiceIT.student.getID());
        Mockito.verify(this.temaRepo).findOne(ServiceIT.tema.getID());
        Mockito.verify(this.notaValidator).validate(ServiceIT.nota);
        Mockito.verify(this.notaRepo).save(ServiceIT.nota);
        Mockito.verify(this.notaRepo).processFile(ServiceIT.student);
    }
}
