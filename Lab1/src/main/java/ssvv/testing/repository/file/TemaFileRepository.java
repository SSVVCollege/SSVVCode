package ssvv.testing.repository.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

import ssvv.testing.domain.Tema;
import ssvv.testing.validation.ValidationException;
import ssvv.testing.validation.Validator;

public class TemaFileRepository extends AbstractFileRepository<String, Tema> {

    public TemaFileRepository(final Validator<Tema> validator, final String filename) {
        super(validator, filename);
        this.loadFromFile();
    }

    @Override
    protected void loadFromFile() {
        try (final BufferedReader buffer = new BufferedReader(new FileReader(this.filename))) {
            buffer.lines().collect(Collectors.toList()).forEach(line -> {
                final String[] result = line.split("#");
                final Tema tema = new Tema(result[0], result[1], Integer.parseInt(result[2]), Integer.parseInt(result[3]));
                try {
                    super.save(tema);
                } catch (final ValidationException ve) {
                    ve.printStackTrace();
                }
            });
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    protected void writeToFile(final Tema tema) {
        try (final BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename, true))) {
            bw.write(tema.getID() + "#" + tema.getDescriere() + "#" + tema.getDeadline() + "#" + tema.getStartline() + "\n");
        }
        catch(final IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    protected void writeToFileAll() {
        try (final BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename, false))) {
            super.entities.values().forEach(tema -> {
                try {
                    bw.write(tema.getID() + "#" + tema.getDescriere() + "#" + tema.getDeadline() + "#" + tema.getStartline() + "\n");
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
