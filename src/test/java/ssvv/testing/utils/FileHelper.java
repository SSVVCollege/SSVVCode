package ssvv.testing.utils;

import java.io.File;

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

@Slf4j
public class FileHelper {

    public static void emptyXmlEntityFile(final String filename) {
        try {
            FileHelper.log.info("Writing XML data to file: {}", filename);

            final Document XMLdocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final Element root = XMLdocument.createElement("Entitati");
            XMLdocument.appendChild(root);

            final Transformer XMLtransformer = TransformerFactory.newInstance().newTransformer();
            XMLtransformer.setOutputProperty(OutputKeys.INDENT, "yes");
            XMLtransformer.transform(new DOMSource(XMLdocument), new StreamResult(filename));
        } catch (final ParserConfigurationException | TransformerException pce) {
            FileHelper.log.error("An error occured while writing data to XML document {}.\n {}", filename, pce.getMessage());
            throw new RuntimeException();
        }
    }

    public static void ensureXmlEntityFileExists(final String filename) {
        if (!new File(filename).exists()) {
            FileHelper.log.error("No such file: {}", filename);
            throw new RuntimeException();
        }

        FileHelper.emptyXmlEntityFile(filename);
    }
}
