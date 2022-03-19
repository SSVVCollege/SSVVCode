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

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ssvv.testing.domain.HasID;
import ssvv.testing.exceptions.EntityAlreadyExistsException;
import ssvv.testing.exceptions.EntityNotFoundException;
import ssvv.testing.repository.crud.AbstractCRUDRepository;

@Slf4j
public abstract class AbstractXMLRepository<ID, E extends HasID<ID>> extends AbstractCRUDRepository<ID, E> {
    protected final String XMLfilename;

    public AbstractXMLRepository (String filename) {
        this.XMLfilename = filename;
        this.loadFromXmlFile();
    }

    protected abstract E getEntityFromNode(Element node);

    protected abstract Element getElementFromEntity(E entity, Document XMLdocument);

    private void loadFromXmlFile() {
        try {
            log.info("Loading data from file: {}", this.XMLfilename);

            final Document XMLdocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.XMLfilename);
            final Element root = XMLdocument.getDocumentElement();
            final NodeList list = root.getChildNodes();

            for(int i = 0; i < list.getLength(); i++) {
                final Node node = list.item(i);
                if (node.getNodeType() == Element.ELEMENT_NODE) {
                    super.save(this.getEntityFromNode((Element)node));
                }
            }
        } catch (final ParserConfigurationException | SAXException | IOException pce) {
            log.error("An error occured while reading data from XML Document {}.\n{}", this.XMLfilename, pce.getMessage());
            throw new RuntimeException();
        }
    }

    private void writeToXmlFile() {
        try {
            log.info("Writing XML data to file: {}", this.XMLfilename);

            final Document XMLdocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final Element root = XMLdocument.createElement("Entitati");
            XMLdocument.appendChild(root);

            this.entities.values().forEach(entity -> root.appendChild(this.getElementFromEntity(entity, XMLdocument)));
            final Transformer XMLtransformer = TransformerFactory.newInstance().newTransformer();
            XMLtransformer.setOutputProperty(OutputKeys.INDENT, "yes");
            XMLtransformer.transform(new DOMSource(XMLdocument), new StreamResult(this.XMLfilename));
        } catch (final ParserConfigurationException | TransformerException pce) {
            log.error("An error occured while writing data to XML document {}.\n {}", this.XMLfilename, pce.getMessage());
        }
    }

    protected Element createElement(final Document XMLdocument, final String tag, final String value) {
        final Element element = XMLdocument.createElement(tag);
        element.setTextContent(value);
        return element;
    }

    @Override
    public void save(final E entity) throws EntityAlreadyExistsException {
        super.save(entity);
        this.writeToXmlFile();
    }

    @Override
    public E delete(final ID id) {
        final E result = super.delete(id);
        if (result != null) this.writeToXmlFile();
        return result;
    }

    @Override
    public void update(final E newEntity) throws EntityNotFoundException {
        super.update(newEntity);
        this.writeToXmlFile();
    }
}
