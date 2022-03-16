package ssvv.testing.console;

import java.util.Scanner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ssvv.testing.domain.Nota;
import ssvv.testing.domain.Student;
import ssvv.testing.domain.Tema;
import ssvv.testing.service.Service;

@Slf4j
@RequiredArgsConstructor
public class UI {
    private final Service service;

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

    public void uiSaveStudent() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Introduceti ID-ul studentului: ");
        final String id = scanner.nextLine();

        System.out.println("Introduceti numele studentului: ");
        final String nume = scanner.nextLine();

        System.out.println("Introduceti grupa studentului: ");
        final int grupa = scanner.nextInt();

        if (this.service.saveStudent(id, nume, grupa) != 0) {
            System.out.println("Student adaugat cu succes! \n");
        } else {
            System.out.println("Student existent sau invalid! \n");
        }
    }

    public void uiSaveTema() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Introduceti ID-ul temei: ");
        final String id = scanner.nextLine();

        System.out.println("Introduceti o descriere a temei: ");
        final String descriere = scanner.nextLine();

        System.out.println("Introduceti saptamana deadline a temei: ");
        final int deadline = scanner.nextInt();

        System.out.println("Introduceti saptamana startline a temei: ");
        final int startline = scanner.nextInt();

        if (this.service.saveTema(id, descriere, deadline, startline) != 0) {
            System.out.println("Tema adaugata cu succes! \n");
        } else {
            System.out.println("Tema existenta sau invalida! \n");
        }
    }

    public void uiSaveNota() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Introduceti ID-ul studentului: ");
        final String idStudent = scanner.nextLine();

        System.out.println("Introduceti ID-ul temei: ");
        final String idTema = scanner.nextLine();

        System.out.println("Introduceti valoarea notei: ");
        final String linie = scanner.nextLine();
        final double valNota = Double.parseDouble(linie);

        System.out.println("Introduceti saptamana de predare a temei: ");
        final String linie2 = scanner.nextLine();
        final int predata = Integer.parseInt(linie2);

        System.out.println("Dati un feedback temei: ");
        final String feedback = scanner.nextLine();

        final int result = this.service.saveNota(idStudent, idTema, valNota, predata, feedback);
        if (result == 1) {
//            this.service.createStudentFile(idStudent, idTema);
            System.out.println("Nota adaugata cu succes! \n");
        } else if (result == 0) {
            System.out.println("Nota existenta! \n");
        } else {
            System.out.println("Student sau tema inexistenta! \n");
        }
    }

    public void uiDeleteStudent() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Introduceti ID-ul studentului: ");
        final String id = scanner.nextLine();

        if (this.service.deleteStudent(id) != 0) {
            System.out.println("Student sters cu succes! \n");
        } else {
            System.out.println("Studentul nu exista! \n");
        }
    }

    public void uiDeleteTema() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Introduceti ID-ul temei: ");
        final String id = scanner.nextLine();

        if (this.service.deleteTema(id) != 0) {
            System.out.println("Tema stearsa cu succes! \n");
        } else {
            System.out.println("Tema nu exista! \n");
        }
    }

    public void uiUpdateStudent() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Introduceti ID-ul studentului: ");
        final String id = scanner.nextLine();

        System.out.println("Introduceti noul nume al studentului: ");
        final String numeNou = scanner.nextLine();

        System.out.println("Introduceti noua grupa a studentului: ");
        final int grupaNoua = scanner.nextInt();

        if (this.service.updateStudent(id, numeNou, grupaNoua) != 0) {
            System.out.println("Student actualizat cu succes! \n");
        } else {
            System.out.println("Studentul nu exista! \n");
        }
    }

    public void uiExtendDeadline() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Introduceti ID-ul temei: ");
        final String id = scanner.nextLine();

        System.out.println("Introduceti numarul de saptamani adaugate la deadline: ");
        final int nrWeeks = scanner.nextInt();

        if (this.service.extendDeadline(id, nrWeeks) != 0) {
            System.out.println("Deadline extins cu succes! \n");
        } else {
            System.out.println("Tema nu exista! \n");
        }
    }

    public void run() {
        final Scanner scanner = new Scanner(System.in);
        int cmd = -1;

        this.printMenu();

        while(cmd != 0) {
            System.out.println("Introduceti comanda: ");
            cmd = scanner.nextInt();

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
                    break;
            }
        }
    }
}
