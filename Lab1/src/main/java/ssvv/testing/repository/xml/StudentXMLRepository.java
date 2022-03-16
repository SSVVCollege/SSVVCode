package ssvv.testing.repository.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ssvv.testing.domain.Student;
import ssvv.testing.validation.Validator;

public class StudentXMLRepository extends AbstractXMLRepository<String, Student> {

    public StudentXMLRepository(final Validator<Student> validator, final String XMLfilename) {
        super(validator, XMLfilename);
        this.loadFromXmlFile();
    }

    @Override
    protected Element getElementFromEntity(final Student student, final Document XMLdocument) {
        final Element element = XMLdocument.createElement("student");
        element.setAttribute("ID", student.getID());

        element.appendChild(this.createElement(XMLdocument, "Nume", student.getNume()));
        element.appendChild(this.createElement(XMLdocument, "Grupa", String.valueOf(student.getGrupa())));

        return element;
    }

    @Override
    protected Student getEntityFromNode(final Element node) {
        final String ID = node.getAttributeNode("ID").getValue();
        final String nume = node.getElementsByTagName("Nume").item(0).getTextContent();
        final int grupa = Integer.parseInt(node.getElementsByTagName("Grupa").item(0).getTextContent());

        return new Student(ID, nume, grupa);
    }
}
