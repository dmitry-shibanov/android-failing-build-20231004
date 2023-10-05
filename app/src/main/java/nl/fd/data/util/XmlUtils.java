package nl.fd.data.util;

import androidx.annotation.NonNull;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlUtils {

    public static int nodeToInt(Node node) {
        var number = 0;
        String nodeValue = node.getTextContent();
        try {
            number = Integer.parseInt(nodeValue);
        } catch (NumberFormatException e) {
            log.error( "Failed while parsing {}  for field: {}", nodeValue, node.getNodeName());
        }
        return number;
    }

    @java.lang.SuppressWarnings("java:S2755")
    private static String asXmlString(Node node) {
        var sw = new StringWriter();
        try {
            var transformerFactory = TransformerFactory.newInstance();
            Transformer t = transformerFactory.newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            throw new XmlUtilsRuntimeException(te);
        }
        return sw.toString();
    }

    @NonNull
    public static String childsAsXmlString(Node node) {
        StringBuilder builder = new StringBuilder();
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node child = nodes.item(i);
            builder.append(asXmlString(child));
        }
        return builder.toString().trim();
    }

    private static class XmlUtilsRuntimeException extends RuntimeException {
        XmlUtilsRuntimeException(Exception e) {
            super(e);
        }
    }
}
