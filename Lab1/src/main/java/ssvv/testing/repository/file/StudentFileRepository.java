package ssvv.testing.repository.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

import ssvv.testing.domain.Student;
import ssvv.testing.validation.ValidationException;
import ssvv.testing.validation.Validator;

public class StudentFileRepository extends AbstractFileRepository<String, Student> {

    public StudentFileRepository(final Validator<Student> validator, final String filename) {
        super(validator, filename);
        this.loadFromFile();
    }

    @Override
    protected void loadFromFile() {
        try (final BufferedReader buffer = new BufferedReader(new FileReader(this.filename))) {
            buffer.lines().collect(Collectors.toList()).forEach(line -> {
                final String[] result = line.split("#");
                final Student student = new Student(result[0], result[1], Integer.parseInt(result[2]));
                try {
                    super.save(student);
                } catch (final ValidationException ve) {
                    ve.printStackTrace();
                }
            });
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    protected void writeToFile(final Student student) {
        try (final BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename, true))) {
            bw.write(student.getID() + "#" + student.getNume() + "#" + student.getGrupa() + "\n");
        }
        catch(final IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    protected void writeToFileAll() {
        try (final BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename, false))) {
            super.entities.values().forEach(student -> {
                try {
                    bw.write(student.getID() + "#" + student.getNume() + "#" + student.getGrupa() + "\n");
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            });
        }
        catch(final IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
