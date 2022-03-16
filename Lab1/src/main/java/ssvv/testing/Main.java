package ssvv.testing;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import ssvv.testing.console.UI;
import ssvv.testing.domain.Nota;
import ssvv.testing.domain.Student;
import ssvv.testing.domain.Tema;
import ssvv.testing.repository.xml.NotaXMLRepository;
import ssvv.testing.repository.xml.StudentXMLRepository;
import ssvv.testing.repository.xml.TemaXMLRepository;
import ssvv.testing.service.Service;
import ssvv.testing.validation.NotaValidator;
import ssvv.testing.validation.StudentValidator;
import ssvv.testing.validation.TemaValidator;
import ssvv.testing.validation.Validator;

@Slf4j
public class Main {
    private static final String STUDENT_FILE = "studenti";
    private static final String TEMA_FILE = "teme";
    private static final String NOTA_FILE = "note";

    private static String loadPath(String filePath) throws URISyntaxException {
        return Paths.get(Objects.requireNonNull(Main.class.getClassLoader().getResource(filePath)).toURI()).toString();
    }

    private static String getPathXml(String name) throws URISyntaxException {
        String filePath = "files/xml/" + name + ".xml";
        return loadPath(filePath);
    }

    private static String getPathText(String name) throws URISyntaxException {
        String filePath = "files/text/" + name + ".txt";
        return loadPath(filePath);
    }

    public static void main(final String[] args) {
        final Validator<Student> studentValidator = new StudentValidator();
        final Validator<Tema> temaValidator = new TemaValidator();
        final Validator<Nota> notaValidator = new NotaValidator();

        StudentXMLRepository fileRepository1 = null;
        TemaXMLRepository fileRepository2 = null;
        NotaXMLRepository fileRepository3 = null;

        try {
            fileRepository1 = new StudentXMLRepository(studentValidator, getPathXml(STUDENT_FILE));
            fileRepository2 = new TemaXMLRepository(temaValidator, getPathXml(TEMA_FILE));
            fileRepository3 = new NotaXMLRepository(notaValidator, getPathXml(NOTA_FILE));
        } catch (URISyntaxException | NullPointerException ex) {
            log.error("Invalid input file path. Aborting set up....");
            log.info(ex.getMessage());
            System.exit(-1);
        }

        final Service service = new Service(fileRepository1, fileRepository2, fileRepository3);
        final UI consola = new UI(service);
        consola.run();

        //PENTRU GUI
        // de avut un check: daca profesorul introduce sau nu saptamana la timp
        // daca se introduce nota la timp, se preia saptamana din sistem
        // altfel, se introduce de la tastatura
    }
}
