package ssvv.testing.repository.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

import lombok.extern.slf4j.Slf4j;
import ssvv.testing.domain.Nota;
import ssvv.testing.domain.Pair;
import ssvv.testing.domain.Student;
import ssvv.testing.domain.Tema;
import ssvv.testing.repository.crud.CRUDRepository;
import ssvv.testing.repository.crud.NotaRepository;

@Slf4j
public class NotaFileRepository extends AbstractFileRepository<Pair<String, String>, Nota> implements NotaRepository<Student> {
    private final CRUDRepository<String, Tema> temaRepo;
    private final String filePath;

    public NotaFileRepository(final String filename, final String filePath, final CRUDRepository<String, Tema> temaRepo) {
        super(filename);
        this.filePath = filePath;
        this.temaRepo = temaRepo;
    }

    private String getFilename(final String filename) {
        return Paths.get(this.filePath + filename + ".txt").toString();
    }

    @Override
    protected void loadFromFile() {
        try {
            Files.readAllLines(Paths.get(this.filename).toAbsolutePath()).forEach(line -> {
                final List<String> results = Arrays.asList(line.strip().split("#"));
                final Nota nota = new Nota(new Pair<>(results.get(0), results.get(1)), Double.parseDouble(results.get(2)),
                        Integer.parseInt(results.get(3)), results.get(4));
                super.save(nota);
            });
        } catch (final IOException ioe) {
            NotaFileRepository.log.error("Error occured while reading from text file: {}. Error message: {}", this.filename, ioe.getMessage());
        }
    }

    @Override
    protected void writeToFile(final Nota nota) {
        try (final BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename, true))) {
            bw.write(nota.getID().getObject1() + "#" + nota.getID().getObject2() + "#" + nota.getNota() + "#"
                    + nota.getSaptamanaPredare() + "#" + nota.getFeedback() + "\n");
        } catch (final IOException ioe) {
            NotaFileRepository.log.error("Error occured while appending to text file: {}. Error message: {}", this.filename, ioe.getMessage());
        }
    }

    @Override
    protected void writeToFileAll() {
        try (final BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename, false))) {
            super.entities.values().forEach(nota -> {
                try {
                    bw.write(nota.getID().getObject1() + "#" + nota.getID().getObject2() + "#" + nota.getNota()
                            + "#" + nota.getSaptamanaPredare() + "#" + nota.getFeedback() + "\n");
                } catch (final IOException e) {
                    NotaFileRepository.log.error("Error occured while appending to text file: {}. Error message: {}", this.filename, e.getMessage());
                }
            });
        } catch (final IOException ioe) {
            NotaFileRepository.log.error("Error occured while opening text file: {}. Error message: {}", this.filename, ioe.getMessage());
        }
    }

    private void writeCombinedEntity(final BufferedWriter bw, final Nota nota) {
        final Tema tema = this.temaRepo.findOne(nota.getID().getObject2());

        try {
            bw.write("Tema: " + nota.getID().getObject2() + "\n");
            bw.write("Nota: " + nota.getNota() + "\n");
            bw.write("Predata in saptamana: " + nota.getSaptamanaPredare() + "\n");
            bw.write("Deadline: " + tema.getDeadline() + "\n");
            bw.write("Feedback: " + nota.getFeedback() + "\n\n");
        } catch (final IOException e) {
            NotaFileRepository.log.error("Error occured while writing to text file: {}. Error message: {}", this.filename, e.getMessage());
        }
    }

    @Override
    public void processFile(final Student student) {
        final String filename = this.getFilename(student.getNume());

        try (final BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false))) {
            StreamSupport.stream(super.findAll().spliterator(), false)
                    .filter(nota -> nota.getID().getObject1().equals(student.getID()))
                    .forEach(nota -> this.writeCombinedEntity(bw, nota));
        } catch (final IOException ioe) {
            NotaFileRepository.log.error("Error occured while opening text file: {}. Error message: {}", filename, ioe.getMessage());
        }
    }

    @Override
    public void deleteFile(final Student student) {
        final String filename = this.getFilename(student.getNume());

        try {
            Files.delete(Paths.get(filename));
        } catch (final IOException ioe) {
            NotaFileRepository.log.error("Couldn't delete grades file {}", filename);
        }
    }
}