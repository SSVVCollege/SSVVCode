package ssvv.testing.service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

import ssvv.testing.domain.Nota;
import ssvv.testing.domain.Pair;
import ssvv.testing.domain.Student;
import ssvv.testing.domain.Tema;
import ssvv.testing.repository.xml.NotaXMLRepository;
import ssvv.testing.repository.xml.StudentXMLRepository;
import ssvv.testing.repository.xml.TemaXMLRepository;

public class Service {
    private final StudentXMLRepository studentXmlRepo;
    private final TemaXMLRepository temaXmlRepo;
    private final NotaXMLRepository notaXmlRepo;

    public Service(final StudentXMLRepository studentXmlRepo, final TemaXMLRepository temaXmlRepo, final NotaXMLRepository notaXmlRepo) {
        this.studentXmlRepo = studentXmlRepo;
        this.temaXmlRepo = temaXmlRepo;
        this.notaXmlRepo = notaXmlRepo;
    }

    public Iterable<Student> findAllStudents() { return this.studentXmlRepo.findAll(); }

    public Iterable<Tema> findAllTeme() { return this.temaXmlRepo.findAll(); }

    public Iterable<Nota> findAllNote() { return this.notaXmlRepo.findAll(); }

    public int saveStudent(final String id, final String nume, final int grupa) {
        final Student student = new Student(id, nume, grupa);
        final Student result = this.studentXmlRepo.save(student);

        if (result == null) {
            return 1;
        }
        return 0;
    }

    public int saveTema(final String id, final String descriere, final int deadline, final int startline) {
        final Tema tema = new Tema(id, descriere, deadline, startline);
        final Tema result = this.temaXmlRepo.save(tema);

        if (result == null) {
            return 1;
        }
        return 0;
    }

    public int saveNota(final String idStudent, final String idTema, double valNota, final int predata, final String feedback) {
        Student student = this.studentXmlRepo.findOne(idStudent);
        Tema tema = this.temaXmlRepo.findOne(idTema);

        if (student == null || tema == null) {
            return -1;
        } else {
            final int deadline = tema.getDeadline();

            if (predata - deadline > 2) {
                valNota =  1;
            } else {
                valNota =  valNota - 2.5 * (predata - deadline);
            }
            final Nota nota = new Nota(new Pair<>(idStudent, idTema), valNota, predata, feedback);
            final Nota result = this.notaXmlRepo.save(nota);

            if (result == null) {
                return 1;
            }
            return 0;
        }
    }

    public int deleteStudent(final String id) {
        final Student result = this.studentXmlRepo.delete(id);

        if (result == null) {
            return 0;
        }
        return 1;
    }

    public int deleteTema(final String id) {
        final Tema result = this.temaXmlRepo.delete(id);

        if (result == null) {
            return 0;
        }
        return 1;
    }

    public int updateStudent(final String id, final String numeNou, final int grupaNoua) {
        final Student studentNou = new Student(id, numeNou, grupaNoua);
        final Student result = this.studentXmlRepo.update(studentNou);

        if (result == null) {
            return 0;
        }
        return 1;
    }

    public int updateTema(final String id, final String descriereNoua, final int deadlineNou, final int startlineNou) {
        final Tema temaNoua = new Tema(id, descriereNoua, deadlineNou, startlineNou);
        final Tema result = this.temaXmlRepo.update(temaNoua);

        if (result == null) {
            return 0;
        }
        return 1;
    }

    public int extendDeadline(final String id, final int noWeeks) {
        final Tema tema = this.temaXmlRepo.findOne(id);

        if (tema != null) {
            final LocalDate date = LocalDate.now();
            final WeekFields weekFields = WeekFields.of(Locale.getDefault());
            int currentWeek = date.get(weekFields.weekOfWeekBasedYear());

            if (currentWeek >= 39) {
                currentWeek = currentWeek - 39;
            } else {
                currentWeek = currentWeek + 12;
            }

            if (currentWeek <= tema.getDeadline()) {
                final int deadlineNou = tema.getDeadline() + noWeeks;
                return this.updateTema(tema.getID(), tema.getDescriere(), deadlineNou, tema.getStartline());
            }
        }
        return 0;
    }

    private void createStudentFile(final String idStudent, final String idTema) {
        final Nota nota = this.notaXmlRepo.findOne(new Pair<>(idStudent, idTema));

        this.notaXmlRepo.createFile(nota);
    }
}
