package ssvv.testing.repository.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ssvv.testing.domain.Tema;
import ssvv.testing.validation.Validator;

public class TemaXMLRepository extends AbstractXMLRepository<String, Tema> {

    public TemaXMLRepository(final Validator<Tema> validator, final String XMLfilename) {
        super(validator, XMLfilename);
        this.loadFromXmlFile();
    }

    @Override
    protected Element getElementFromEntity(final Tema tema, final Document XMLdocument) {
        final Element element = XMLdocument.createElement("tema");
        element.setAttribute("ID", tema.getID());

        element.appendChild(this.createElement(XMLdocument, "Descriere", tema.getDescriere()));
        element.appendChild(this.createElement(XMLdocument, "Deadline", String.valueOf(tema.getDeadline())));
        element.appendChild(this.createElement(XMLdocument, "Startline", String.valueOf(tema.getStartline())));

        return element;
    }

    @Override
    protected Tema getEntityFromNode(final Element node) {
        final String ID = node.getAttributeNode("ID").getValue();
        final String descriere = node.getElementsByTagName("Descriere").item(0).getTextContent();
        final int deadline = Integer.parseInt(node.getElementsByTagName("Deadline").item(0).getTextContent());
        final int startline = Integer.parseInt(node.getElementsByTagName("Startline").item(0).getTextContent());

        return new Tema(ID, descriere, deadline, startline);
    }
}
