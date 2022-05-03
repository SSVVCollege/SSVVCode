package ssvv.testing.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ssvv.testing.domain.Nota;
import ssvv.testing.domain.Pair;
import ssvv.testing.domain.Student;
import ssvv.testing.domain.Tema;
import ssvv.testing.exceptions.EntityAlreadyExistsException;

public class GlobalMockRepo {
    private static final Map<String, Student> studentRepo = new HashMap<>();
    private static final Map<String, Tema> temaRepo = new HashMap<>();
    private static final Map<Pair<String, String>, Nota> notaRepo = new HashMap<>();

    public static void clear() {
        GlobalMockRepo.studentRepo.clear();
        GlobalMockRepo.temaRepo.clear();
        GlobalMockRepo.notaRepo.clear();
    }

    public static void addStudent(final Student student) {
        if (GlobalMockRepo.studentRepo.putIfAbsent(student.getID(), student) != null)
            throw new EntityAlreadyExistsException(Student.class);
    }

    public static void addTema(final Tema tema) {
        if (GlobalMockRepo.temaRepo.putIfAbsent(tema.getID(), tema) != null)
            throw new EntityAlreadyExistsException(Tema.class);
    }

    public static void addNota(final Nota nota) {
        if (GlobalMockRepo.notaRepo.putIfAbsent(nota.getID(), nota) != null)
            throw new EntityAlreadyExistsException(Nota.class);
    }

    public static List<Student> getAllStudents() {
        return new ArrayList<>(GlobalMockRepo.studentRepo.values());
    }

    public static List<Tema> getAllTeme() {
        return new ArrayList<>(GlobalMockRepo.temaRepo.values());
    }

    public static List<Nota> getAllNote() {
        return new ArrayList<>(GlobalMockRepo.notaRepo.values());
    }

    public static Student findStudent(final String id) {
        return GlobalMockRepo.studentRepo.get(id);
    }

    public static Tema findTema(final String id) {
        return GlobalMockRepo.temaRepo.get(id);
    }

    public static Nota findNota(final Pair<String, String> id) {
        return GlobalMockRepo.notaRepo.get(id);
    }
}
