package ssvv.testing;

import ssvv.testing.console.UI;
import ssvv.testing.domain.Nota;
import ssvv.testing.domain.Pair;
import ssvv.testing.domain.Student;
import ssvv.testing.domain.Tema;
import ssvv.testing.repository.crud.CRUDRepository;
import ssvv.testing.repository.xml.NotaXMLRepository;
import ssvv.testing.repository.xml.StudentXMLRepository;
import ssvv.testing.repository.xml.TemaXMLRepository;
import ssvv.testing.service.Service;
import ssvv.testing.validation.NotaValidator;
import ssvv.testing.validation.StudentValidator;
import ssvv.testing.validation.TemaValidator;
import ssvv.testing.validation.Validator;

public class Main {
    private static final String XML_PATH = "files/xml/";
    private static final String STUDENT_XML_FILE = "files/xml/studenti.xml";
    private static final String TEMA_XML_FILE = "files/xml/teme.xml";
    private static final String NOTA_XML_FILE = "files/xml/note.xml";

    private static final String TXT_PATH = "files/text/";
    private static final String STUDENT_TXT_FILE = "files/text/studenti.txt";
    private static final String TEMA_TXT_FILE = "files/text/teme.txt";
    private static final String NOTA_TXT_FILE = "files/text/note.txt";

    public static void main(final String[] args) {
        final Validator<Student> studentValidator = new StudentValidator();
        final Validator<Tema> temaValidator = new TemaValidator();
        final Validator<Nota> notaValidator = new NotaValidator();

        CRUDRepository<String, Student> studentRepo = null;
        CRUDRepository<String, Tema> temaRepo = null;
        CRUDRepository<Pair<String, String>, Nota> notaRepo = null;

        try {
            studentRepo = new StudentXMLRepository(Main.STUDENT_XML_FILE);
            temaRepo = new TemaXMLRepository(Main.TEMA_XML_FILE);
            notaRepo = new NotaXMLRepository(Main.NOTA_XML_FILE, Main.XML_PATH, temaRepo);

//            studentRepo = new StudentFileRepository(STUDENT_XML_FILE);
//            temaRepo = new TemaFileRepository(TEMA_XML_FILE);
//            notaRepo = new NotaFileRepository(NOTA_XML_FILE, TXT_PATH, temaRepo);
        } catch (final RuntimeException rex) {
            System.exit(-1);
        }

        final Service service = new Service(studentRepo, temaRepo, notaRepo, studentValidator, temaValidator, notaValidator);
        final UI consola = new UI(service);
        consola.run();
    }
}
