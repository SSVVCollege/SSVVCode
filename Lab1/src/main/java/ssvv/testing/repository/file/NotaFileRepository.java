package ssvv.testing.repository.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

import ssvv.testing.domain.Nota;
import ssvv.testing.domain.Pair;
import ssvv.testing.validation.ValidationException;
import ssvv.testing.validation.Validator;

public class NotaFileRepository extends AbstractFileRepository<Pair<String, String>, Nota> {

    public NotaFileRepository(final Validator<Nota> validator, final String filename) {
        super(validator, filename);
        this.loadFromFile();
    }

    @Override
    protected void loadFromFile() {
        try (final BufferedReader buffer = new BufferedReader(new FileReader(this.filename))) {
            buffer.lines().collect(Collectors.toList()).forEach(line -> {
                final String[] result = line.split("#");
                final Nota nota = new Nota(new Pair(result[0], result[1]), Double.parseDouble(result[2]),
                        Integer.parseInt(result[3]), result[4]);
                try {
                    super.save(nota);
                } catch (final ValidationException ve) {
                    ve.printStackTrace();
                }
            });
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    protected void writeToFile(final Nota nota) {
        try (final BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename, true))) {
            bw.write(nota.getID().getObject1() + "#" + nota.getID().getObject2() + "#" + nota.getNota() + "#"
                    + nota.getSaptamanaPredare() + "#" + nota.getFeedback() + "\n");
        } catch (final IOException ioe) {
            ioe.printStackTrace();
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
                    e.printStackTrace();
                }
            });
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }
    }

//    protected void createFile(Nota notaObj) {
//        String idStudent = notaObj.getID().getObject1();
//        StudentValidator sval = new StudentValidator();
//        TemaValidator tval = new TemaValidator();
//        StudentXMLRepository srepo = new StudentXMLRepository(sval, "studenti.txt");
//        TemaXMLRepository trepo = new TemaXMLRepository(tval, "teme.txt");
//
//        Student student = srepo.findOne(idStudent);
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(student.getNume() + ".txt", false))) {
//            super.findAll().forEach(nota -> {
//                if (nota.getID().getObject1().equals(idStudent)) {
//                    try {
//                        bw.write("Tema: " + nota.getID().getObject2() + "\n");
//                        bw.write("Nota: " + nota.getNota() + "\n");
//                        bw.write("Predata in saptamana: " + nota.getSaptamanaPredare() + "\n");
//                        bw.write("Deadline: " + trepo.findOne(nota.getID().getObject2()).getDeadline() + "\n");
//                        bw.write("Feedback: " + nota.getFeedback() + "\n\n");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//    }
}