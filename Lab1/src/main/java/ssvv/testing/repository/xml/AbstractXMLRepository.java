package ssvv.testing.repository.xml;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ssvv.testing.domain.HasID;
import ssvv.testing.repository.crud.AbstractCRUDRepository;
import ssvv.testing.validation.ValidationException;
import ssvv.testing.validation.Validator;

public abstract class AbstractXMLRepository<ID, E extends HasID<ID>> extends AbstractCRUDRepository<ID, E> {
    protected String XMLfilename;

    public AbstractXMLRepository(final Validator<E> validator, final String XMLfilename) {
        super(validator);
        this.XMLfilename = XMLfilename;
    }

    protected abstract E getEntityFromNode(Element node);
    protected abstract Element getElementFromEntity(E entity, Document XMLdocument);

    protected void loadFromXmlFile() {
        try {
            final Document XMLdocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.XMLfilename);
            final Element root = XMLdocument.getDocumentElement();
            final NodeList list = root.getChildNodes();

            for(int i = 0; i < list.getLength(); i++) {
                final Node node = list.item(i);
                if (node.getNodeType() == Element.ELEMENT_NODE) {
                    try {
                        super.save(this.getEntityFromNode((Element)node));
                    }
                    catch(final ValidationException ve) {
                        ve.printStackTrace();
                    }
                }
            }
        }
        catch(final ParserConfigurationException | SAXException | IOException pce) {
            pce.printStackTrace();
        }
    }

    protected void writeToXmlFile() {
        try {
            final Document XMLdocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final Element root = XMLdocument.createElement("Entitati");
            XMLdocument.appendChild(root);

            this.entities.values().forEach(entity -> root.appendChild(this.getElementFromEntity(entity, XMLdocument)));
            final Transformer XMLtransformer = TransformerFactory.newInstance().newTransformer();
            XMLtransformer.setOutputProperty(OutputKeys.INDENT, "yes");
            XMLtransformer.transform(new DOMSource(XMLdocument), new StreamResult(this.XMLfilename));
        } catch(final ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    protected Element createElement(final Document XMLdocument, final String tag, final String value) {
        final Element element = XMLdocument.createElement(tag);
        element.setTextContent(value);
        return element;
    }

    @Override
    public E save(final E entity) throws ValidationException {
        final E result = super.save(entity);
        if (result == null) {
            this.writeToXmlFile();
        }
        return result;
    }

    @Override
    public E delete(final ID id) {
        final E result = super.delete(id);
        this.writeToXmlFile();

        return result;
    }

    @Override
    public E update(final E newEntity) {
        final E result = super.update(newEntity);
        this.writeToXmlFile();

        return result;
    }
}
