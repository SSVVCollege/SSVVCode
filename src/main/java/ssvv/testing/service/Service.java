package ssvv.testing.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lombok.RequiredArgsConstructor;
import ssvv.testing.domain.Nota;
import ssvv.testing.domain.Pair;
import ssvv.testing.domain.Student;
import ssvv.testing.domain.Tema;
import ssvv.testing.exceptions.EntityNotFoundException;
import ssvv.testing.repository.crud.CRUDRepository;
import ssvv.testing.repository.crud.NotaRepository;
import ssvv.testing.validation.Validator;

@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class Service {
    private final CRUDRepository<String, Student> studentRepo;
    private final CRUDRepository<String, Tema> temaRepo;
    private final CRUDRepository<Pair<String, String>, Nota> notaRepo;

    private final Validator<Student> studentValidator;
    private final Validator<Tema> temaValidator;
    private final Validator<Nota> notaValidator;

    public Iterable<Student> findAllStudents() {
        return this.studentRepo.findAll();
    }

    public Iterable<Tema> findAllTeme() {
        return this.temaRepo.findAll();
    }

    public Iterable<Nota> findAllNote() {
        return this.notaRepo.findAll();
    }

    public void saveStudent(final String id, final String nume, final Integer grupa) {
        final Student student = new Student(id, nume, grupa);
        this.studentValidator.validate(student);
        this.studentRepo.save(student);
    }

    public void saveTema(final String id, final String descriere, final Integer deadline, final Integer startline) {
        final Tema tema = new Tema(id, descriere, deadline, startline);
        this.temaValidator.validate(tema);
        this.temaRepo.save(tema);
    }

    public void saveNota(final String idStudent, final String idTema, Double valNota, final Integer predata, final String feedback) {
        final Student student = this.studentRepo.findOne(idStudent);
        final Tema tema = this.temaRepo.findOne(idTema);

        if (student == null) {
            throw new EntityNotFoundException(Student.class);
        } else if (tema == null) {
            throw new EntityNotFoundException(Tema.class);
        }

        valNota = this.computeDeadlineAndGrade(predata, tema.getDeadline(), valNota);

        final Nota nota = new Nota(new Pair<>(idStudent, idTema), valNota, predata, feedback);
        this.notaValidator.validate(nota);

        this.notaRepo.save(nota);
        ((NotaRepository<Student>) this.notaRepo).processFile(student);

    }

    public Student deleteStudent(final String id) {
        //get all grades of student
        final List<Nota> toDelete = StreamSupport.stream(this.notaRepo.findAll().spliterator(), false)
                .filter(nota -> nota.getID().getObject1().equals(id))
                .collect(Collectors.toList());

        //delete student, drop processing if it doesn't exist
        final Student student = this.studentRepo.delete(id);
        if (student == null) return null;

        //delete all student grades
        toDelete.forEach(nota -> this.notaRepo.delete(nota.getID()));

        //empty grades file of student
        ((NotaRepository<Student>) this.notaRepo).deleteFile(student);

        return student;
    }

    public Tema deleteTema(final String id) {
        //get tema and drop processing if it does not exist
        final Tema tema = this.temaRepo.findOne(id);
        if (tema == null) return null;

        //get all students with grades for this tema
        //delete tema
        final List<Student> studentsWithGrades = this.getStudentsWithGradesForTema(tema);
        this.temaRepo.delete(tema.getID());

        //delete all grades for this tema
        final List<Nota> toDelete = StreamSupport.stream(this.notaRepo.findAll().spliterator(), false)
                .filter(nota -> nota.getID().getObject2().equals(id))
                .collect(Collectors.toList());
        toDelete.forEach(nota -> this.notaRepo.delete(nota.getID()));

        //remove grades for this tema, from all student files associated with this tema
        studentsWithGrades.forEach(((NotaRepository<Student>) this.notaRepo)::processFile);

        return tema;
    }

    public void updateStudent(final String id, final String numeNou, final Integer grupaNoua) {
        final Student studentNou = new Student(id, numeNou, grupaNoua);
        this.studentValidator.validate(studentNou);
        this.studentRepo.update(studentNou);
    }

    public void extendDeadline(final String id, final Integer noWeeks) {
        final Tema tema = this.temaRepo.findOne(id);

        if (tema == null) throw new EntityNotFoundException(Tema.class);

        int deadlineNou = tema.getDeadline() + noWeeks;
        if (deadlineNou > 52) deadlineNou = 1;
        this.updateTema(tema.getID(), tema.getDescriere(), deadlineNou, tema.getStartline());

        this.getStudentsWithGradesForTema(tema).forEach(((NotaRepository<Student>) this.notaRepo)::processFile);
    }

    private void updateTema(final String id, final String descriereNoua, final Integer deadlineNou, final Integer startlineNou) {
        final Tema temaNoua = new Tema(id, descriereNoua, deadlineNou, startlineNou);
        this.temaValidator.validate(temaNoua);
        this.temaRepo.update(temaNoua);
    }

    private List<Student> getStudentsWithGradesForTema(final Tema tema) {
        return StreamSupport.stream(this.notaRepo.findAll().spliterator(), false)
                .filter(nota -> nota.getID().getObject2().equals(tema.getID()))
                .map(nota -> nota.getID().getObject1())
                .map(this.studentRepo::findOne)
                .collect(Collectors.toList());
    }

    private Double computeDeadlineAndGrade(Integer turnedIn, final Integer deadline, final Double grade) {
        if (turnedIn - deadline > 2) {
            return 1.00;
        } else {
            turnedIn = Math.max(turnedIn, deadline);
            return grade - 2.5 * (turnedIn - deadline);
        }
    }
}
