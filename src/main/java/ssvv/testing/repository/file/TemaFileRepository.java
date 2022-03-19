package ssvv.testing.repository.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ssvv.testing.domain.Tema;

@Slf4j
public class TemaFileRepository extends AbstractFileRepository<String, Tema> {

    public TemaFileRepository(final String filename) {
        super(filename);
    }

    @Override
    protected void loadFromFile() {
        try {
            Files.readAllLines(Paths.get(filename).toAbsolutePath()).forEach(line -> {
                List<String> results = Arrays.asList(line.strip().split("#"));
                final Tema tema = new Tema(results.get(0), results.get(1), Integer.parseInt(results.get(2)), Integer.parseInt(results.get(3)));
                super.save(tema);
            });
        } catch (final IOException ioe) {
            log.error("Error occured while reading from text file: {}. Error message: {}", filename, ioe.getMessage());
        }
    }

    @Override
    protected void writeToFile(final Tema tema) {
        try (final BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename, true))) {
            bw.write(tema.getID() + "#" + tema.getDescriere() + "#" + tema.getDeadline() + "#" + tema.getStartline() + "\n");
        } catch (final IOException ioe) {
            log.error("Error occured while appending to text file: {}. Error message: {}", filename, ioe.getMessage());
        }
    }

    @Override
    protected void writeToFileAll() {
        try (final BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename, false))) {
            super.entities.values().forEach(tema -> {
                try {
                    bw.write(tema.getID() + "#" + tema.getDescriere() + "#" + tema.getDeadline() + "#" + tema.getStartline() + "\n");
                } catch (final IOException e) {
                    log.error("Error occured while appending to text file: {}. Error message: {}", filename, e.getMessage());
                }
            });
        } catch (final IOException ioe) {
            log.error("Error occured while opening text file: {}. Error message: {}", filename, ioe.getMessage());
        }
    }
}
