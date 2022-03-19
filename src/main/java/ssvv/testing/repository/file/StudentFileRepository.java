package ssvv.testing.repository.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ssvv.testing.domain.Student;

@Slf4j
public class StudentFileRepository extends AbstractFileRepository<String, Student> {

    public StudentFileRepository(final String filename) {
        super(filename);
    }

    @Override
    protected void loadFromFile() {
        try {
            Files.readAllLines(Paths.get(filename).toAbsolutePath()).forEach(line -> {
                List<String> results = Arrays.asList(line.strip().split("#"));
                final Student student = new Student(results.get(0), results.get(1), Integer.parseInt(results.get(2)));
                super.save(student);
            });
        } catch (final IOException ioe) {
            log.error("Error occured while reading from text file: {}. Error message: {}", filename, ioe.getMessage());
        }
    }

    @Override
    protected void writeToFile(final Student student) {
        try (final BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename, true))) {
            bw.write(student.getID() + "#" + student.getNume() + "#" + student.getGrupa() + "\n");
        } catch (final IOException ioe) {
            log.error("Error occured while appending to text file: {}. Error message: {}", filename, ioe.getMessage());
        }
    }

    @Override
    protected void writeToFileAll() {
        try (final BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename, false))) {
            super.entities.values().forEach(student -> {
                try {
                    bw.write(student.getID() + "#" + student.getNume() + "#" + student.getGrupa() + "\n");
                } catch (final IOException e) {
                    log.error("Error occured while appending to text file: {}. Error message: {}", filename, e.getMessage());
                }
            });
        } catch (final IOException ioe) {
            log.error("Error occured while opening text file: {}. Error message: {}", filename, ioe.getMessage());
        }
    }
}
