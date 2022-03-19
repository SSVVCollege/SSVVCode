package ssvv.testing.console;

import java.util.Scanner;

import lombok.extern.slf4j.Slf4j;
import ssvv.testing.domain.Nota;
import ssvv.testing.domain.Student;
import ssvv.testing.domain.Tema;
import ssvv.testing.exceptions.EntityAlreadyExistsException;
import ssvv.testing.exceptions.EntityNotFoundException;
import ssvv.testing.exceptions.ValidationException;
import ssvv.testing.service.Service;

@Slf4j
@SuppressWarnings("unchecked")
public class UI {
    private final Service service;
    private final Scanner scanner;

    public UI (Service service) {
        this.service = service;
        scanner = new Scanner(System.in);
    }

    public void printMenu() {
        System.out.println("11. Afiseaza toti studentii.");
        System.out.println("12. Afiseaza toate temele.");
        System.out.println("13. Afiseaza toate notele.");

        System.out.println("21. Adauga un nou student.");
        System.out.println("22. Adauga o tema noua.");
        System.out.println("23. Adauga o nota unui student pentru o tema.");

        System.out.println("31. Sterge un student existent.");
        System.out.println("32. Sterge o tema existenta.");

        System.out.println("4. Actualizeaza datele unui student.");

        System.out.println("5. Prelungeste deadline-ul unei teme.");

        System.out.println("0. EXIT \n");
    }

    public void uiPrintAllStudents() {
        for(final Student student : this.service.findAllStudents()) {
            System.out.println(student);
        }
    }

    public void uiPrintAllTeme() {
        for(final Tema tema : this.service.findAllTeme()) {
            System.out.println(tema);
        }
    }

    public void uiPrintAllNote() {
        for(final Nota note : this.service.findAllNote()) {
            System.out.println(note);
        }
    }

    private <T extends Number> T readNumber(Class<T> clazz, String tag, String message) {
        try {
            System.out.println(message);
            if (clazz.getSimpleName().equalsIgnoreCase("integer"))
                return (T) Integer.valueOf(scanner.nextLine());
            else if (clazz.getSimpleName().equalsIgnoreCase("double"))
                return (T) Double.valueOf(scanner.nextLine());
            else throw new NumberFormatException();
        } catch (NumberFormatException ne) {
            System.out.println("Invalid number value for " + tag);
            return null;
        }
    }

    public void uiSaveStudent() {
        try {
            System.out.println("Introduceti ID-ul studentului: ");
            final String id = scanner.nextLine();

            System.out.println("Introduceti numele studentului: ");
            final String nume = scanner.nextLine();

            final Integer grupa = readNumber(Integer.class, "GRUPA", "Introduceti grupa studentului: ");

            this.service.saveStudent(id, nume, grupa);
            System.out.println("Student adaugat cu succes! \n");
        } catch (ValidationException | EntityAlreadyExistsException ve) {
            System.out.println(ve.getMessage());
        }
    }

    public void uiSaveTema() {
        try {
            System.out.println("Introduceti ID-ul temei: ");
            final String id = scanner.nextLine();

            System.out.println("Introduceti o descriere a temei: ");
            final String descriere = scanner.nextLine();

            final Integer deadline = readNumber(Integer.class, "DEADLINE", "Introduceti saptamana deadline a temei: ");

            final Integer startline = readNumber(Integer.class, "STARTLINE", "Introduceti saptamana startline a temei: ");

            this.service.saveTema(id, descriere, deadline, startline);
            System.out.println("Tema adaugata cu succes! \n");
        } catch (ValidationException | EntityAlreadyExistsException ve) {
            System.out.println(ve.getMessage());
        }
    }

    public void uiSaveNota() {
        try {
            System.out.println("Introduceti ID-ul studentului: ");
            final String idStudent = scanner.nextLine();

            System.out.println("Introduceti ID-ul temei: ");
            final String idTema = scanner.nextLine();

            final Double valNota = readNumber(Double.class, "NOTA", "Introduceti valoarea notei: ");

            final Integer predata = readNumber(Integer.class, "SAPTAMANA PREDARE", "Introduceti saptamana de predare a temei: ");

            System.out.println("Dati un feedback temei: ");
            final String feedback = scanner.nextLine();

            this.service.saveNota(idStudent, idTema, valNota, predata, feedback);
            System.out.println("Nota adaugata cu succes! \n");
        } catch (ValidationException | EntityAlreadyExistsException | EntityNotFoundException ve) {
            System.out.println(ve.getMessage());
        }
    }

    public void uiDeleteStudent() {
        System.out.println("Introduceti ID-ul studentului: ");
        final String id = scanner.nextLine();

        if (this.service.deleteStudent(id) != null) {
            System.out.println("Student sters cu succes! \n");
        } else {
            System.out.println("Studentul nu exista! \n");
        }
    }

    public void uiDeleteTema() {
        System.out.println("Introduceti ID-ul temei: ");
        final String id = scanner.nextLine();

        if (this.service.deleteTema(id) != null) {
            System.out.println("Tema stearsa cu succes! \n");
        } else {
            System.out.println("Tema nu exista! \n");
        }
    }

    public void uiUpdateStudent() {
        try {
            System.out.println("Introduceti ID-ul studentului: ");
            final String id = scanner.nextLine();

            System.out.println("Introduceti noul nume al studentului: ");
            final String numeNou = scanner.nextLine();

            final Integer grupaNoua = readNumber(Integer.class, "GRUPA", "Introduceti noua grupa a studentului: ");

            this.service.updateStudent(id, numeNou, grupaNoua);
            System.out.println("Student actualizat cu succes! \n");
        } catch (ValidationException | EntityNotFoundException ve) {
            System.out.println(ve.getMessage());
        }
    }

    public void uiExtendDeadline() {
        try {
            System.out.println("Introduceti ID-ul temei: ");
            final String id = scanner.nextLine();

            final Integer nrWeeks = readNumber(Integer.class, "NR SAPTAMANI", "Introduceti numarul de saptamani adaugate la deadline: ");

            this.service.extendDeadline(id, nrWeeks);
            System.out.println("Deadline extins cu succes! \n");
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        Integer cmd = -1;

        this.printMenu();

        while(cmd != 0) {
            cmd = readNumber(Integer.class, "COMANDA", "Introduceti comanda: ");
            cmd = cmd == null ? -1 : cmd;

            switch(cmd) {
                case 11:
                    this.uiPrintAllStudents();
                    break;
                case 12:
                    this.uiPrintAllTeme();
                    break;
                case 13:
                    this.uiPrintAllNote();
                    break;
                case 21:
                    this.uiSaveStudent();
                    break;
                case 22:
                    this.uiSaveTema();
                    break;
                case 23:
                    this.uiSaveNota();
                    break;
                case 31:
                    this.uiDeleteStudent();
                    break;
                case 32:
                    this.uiDeleteTema();
                    break;
                case 4:
                    this.uiUpdateStudent();
                    break;
                case 5:
                    this.uiExtendDeadline();
                    break;
                case 0:
                    cmd = 0;
                    scanner.close();
                    break;
                default:
                    break;
            }
        }
    }
}
