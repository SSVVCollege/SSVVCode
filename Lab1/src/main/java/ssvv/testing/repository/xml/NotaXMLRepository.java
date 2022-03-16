package ssvv.testing.repository.xml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ssvv.testing.domain.Nota;
import ssvv.testing.domain.Pair;
import ssvv.testing.domain.Student;
import ssvv.testing.repository.file.StudentFileRepository;
import ssvv.testing.repository.file.TemaFileRepository;
import ssvv.testing.validation.StudentValidator;
import ssvv.testing.validation.TemaValidator;
import ssvv.testing.validation.Validator;

public class NotaXMLRepository extends AbstractXMLRepository<Pair<String, String>, Nota> {

    public NotaXMLRepository(final Validator<Nota> validator, final String XMLfilename) {
        super(validator, XMLfilename);
        this.loadFromXmlFile();
    }

    @Override
    protected Element getElementFromEntity(final Nota nota, final Document XMLdocument) {
        final Element element = XMLdocument.createElement("nota");
        element.setAttribute("IDStudent", nota.getID().getObject1());
        element.setAttribute("IDTema", nota.getID().getObject2());

        element.appendChild(this.createElement(XMLdocument, "Nota", String.valueOf(nota.getNota())));
        element.appendChild(this.createElement(XMLdocument, "SaptamanaPredare", String.valueOf(nota.getSaptamanaPredare())));
        element.appendChild(this.createElement(XMLdocument, "Feedback", nota.getFeedback()));

        return element;
    }

    @Override
    protected Nota getEntityFromNode(final Element node) {
        final String IDStudent = node.getAttributeNode("IDStudent").getValue();
        final String IDTema= node.getAttributeNode("IDTema").getValue();
        final double nota = Double.parseDouble(node.getElementsByTagName("Nota").item(0).getTextContent());
        final int saptamanaPredare = Integer.parseInt(node.getElementsByTagName("SaptamanaPredare").item(0).getTextContent());
        final String feedback = node.getElementsByTagName("Feedback").item(0).getTextContent();

        return new Nota(new Pair<>(IDStudent, IDTema), nota, saptamanaPredare, feedback);
    }

    public void createFile(final Nota notaObj) {
        final String idStudent = notaObj.getID().getObject1();
        final StudentValidator sval = new StudentValidator();
        final TemaValidator tval = new TemaValidator();
        final StudentFileRepository srepo = new StudentFileRepository(sval, "studenti.txt");
        final TemaFileRepository trepo = new TemaFileRepository(tval, "teme.txt");

        final Student student = srepo.findOne(idStudent);
        try (final BufferedWriter bw = new BufferedWriter(new FileWriter(student.getNume() + ".txt", false))) {
            super.findAll().forEach(nota -> {
                if (nota.getID().getObject1().equals(idStudent)) {
                    try {
                        bw.write("Tema: " + nota.getID().getObject2() + "\n");
                        bw.write("Nota: " + nota.getNota() + "\n");
                        bw.write("Predata in saptamana: " + nota.getSaptamanaPredare() + "\n");
                        bw.write("Deadline: " + trepo.findOne(nota.getID().getObject2()).getDeadline() + "\n");
                        bw.write("Feedback: " + nota.getFeedback() + "\n\n");
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
//    public void createFile(Nota notaObj) {
//        String idStudent = notaObj.getID().getObject1();
//        StudentValidator sval = new StudentValidator();
//        TemaValidator tval = new TemaValidator();
//        StudentXMLRepository srepo = new StudentXMLRepository(sval, "studenti.xml");
//        TemaXMLRepository trepo = new TemaXMLRepository(tval, "teme.xml");
//
//        Student student = srepo.findOne(idStudent);
//        try {
//            Document XMLdocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
//            Element root = XMLdocument.createElement("NoteStudent");
//            XMLdocument.appendChild(root);
//
//            super.findAll().forEach(nota -> {
//                if (nota.getID().getObject1().equals(idStudent)) {
//                    try {
//                        Document XMLstudent = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
//                        Element element = XMLstudent.createElement("nota");
//
//                        Document XMLdocument2 = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(trepo.XMLfilename);
//                        Node n = XMLdocument2.getFirstChild();
//                        Node temaNode = XMLstudent.importNode(XMLdocument2, true);
//                        Tema t = trepo.getEntityFromNode((Element) temaNode);
//
//                        element.appendChild(createElement(XMLstudent, "Tema", t.getID()));
//                        element.appendChild(createElement(XMLstudent, "Nota", String.valueOf(nota.getNota())));
//                        element.appendChild(createElement(XMLstudent, "SaptamanaPredare", String.valueOf(nota.getSaptamanaPredare())));
//                        element.appendChild(createElement(XMLstudent, "Deadline", String.valueOf(t.getDeadline())));
//                        element.appendChild(createElement(XMLstudent, "Feedback", nota.getFeedback()));
//
//                        root.appendChild(element);
//
//                    } catch (ParserConfigurationException e) {
//                        e.printStackTrace();
//                    } catch (SAXException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            });
//
//            Transformer XMLtransformer = TransformerFactory.newInstance().newTransformer();
//            XMLtransformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            XMLtransformer.transform(new DOMSource(XMLdocument), new StreamResult(XMLfilename));
//        }
//        catch(ParserConfigurationException pce) {
//            pce.printStackTrace();
//        }
//        catch(TransformerConfigurationException tce) {
//            tce.printStackTrace();
//        }
//        catch(TransformerException te) {
//            te.printStackTrace();
//        }
//    }}
