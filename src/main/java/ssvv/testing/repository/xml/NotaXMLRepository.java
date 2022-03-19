package ssvv.testing.repository.xml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.StreamSupport;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ssvv.testing.domain.Nota;
import ssvv.testing.domain.Pair;
import ssvv.testing.domain.Student;
import ssvv.testing.domain.Tema;
import ssvv.testing.repository.crud.CRUDRepository;
import ssvv.testing.repository.crud.NotaRepository;

@Slf4j
public class NotaXMLRepository extends AbstractXMLRepository<Pair<String, String>, Nota> implements NotaRepository<Student> {
    private final CRUDRepository<String, Tema> temaRepository;
    private final String filePath;

    public NotaXMLRepository(final String XMLfilename, final String filePath, final CRUDRepository<String, Tema> temaRepository) {
        super(XMLfilename);
        this.filePath = filePath;
        this.temaRepository = temaRepository;
    }

    private String getFilename(final String filename) {
        return Paths.get(this.filePath + filename + ".xml").toString();
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
        final String IDTema = node.getAttributeNode("IDTema").getValue();
        final double nota = Double.parseDouble(node.getElementsByTagName("Nota").item(0).getTextContent());
        final int saptamanaPredare = Integer.parseInt(node.getElementsByTagName("SaptamanaPredare").item(0).getTextContent());
        final String feedback = node.getElementsByTagName("Feedback").item(0).getTextContent();

        return new Nota(new Pair<>(IDStudent, IDTema), nota, saptamanaPredare, feedback);
    }

    private Element getCombinedElement(final Document xmlDocument, final Nota nota) {
        final Tema tema = this.temaRepository.findOne(nota.getID().getObject2());

        final Element element = xmlDocument.createElement("nota");

        element.appendChild(this.createElement(xmlDocument, "Tema", nota.getID().getObject2()));
        element.appendChild(this.createElement(xmlDocument, "Nota", String.valueOf(nota.getNota())));
        element.appendChild(this.createElement(xmlDocument, "SaptamanaPredare", String.valueOf(nota.getSaptamanaPredare())));
        element.appendChild(this.createElement(xmlDocument, "Deadline", String.valueOf(tema.getDeadline())));
        element.appendChild(this.createElement(xmlDocument, "Feedback", nota.getFeedback()));

        return element;
    }

    @Override
    public void processFile(final Student student) {
        final String filename = this.getFilename(student.getNume());

        try {
            final Document XMLdocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final Element root = XMLdocument.createElement("NoteStudent");
            XMLdocument.appendChild(root);

            StreamSupport.stream(super.findAll().spliterator(), false)
                    .filter(nota -> nota.getID().getObject1().equals(student.getID()))
                    .forEach(nota -> root.appendChild(this.getCombinedElement(XMLdocument, nota)));

            new File(filename).createNewFile();

            final Transformer XMLtransformer = TransformerFactory.newInstance().newTransformer();
            XMLtransformer.setOutputProperty(OutputKeys.INDENT, "yes");
            XMLtransformer.transform(new DOMSource(XMLdocument), new StreamResult(filename));
        } catch (final ParserConfigurationException | TransformerException | IOException pce) {
            NotaXMLRepository.log.error("An error occured while writing data to XML document {}.\n {}", filename, pce.getMessage());
        }
    }

    @Override
    public void deleteFile(final Student student) {
        final String filename = this.getFilename(student.getNume());

        try {
            Files.delete(Paths.get(filename));
        } catch (final IOException ioe) {
            NotaXMLRepository.log.error("Couldn't delete grades file {}", filename);
        }
    }
}
