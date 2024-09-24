package ca.switchcase.commons.util;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlDomUtilities {

    public static String getAttributeValue(Node node, String attributeName) {
        return node.getAttributes().getNamedItem(attributeName).getTextContent();
    }

    public static String getAttributeValue(Node node, String attributeName, String defaultValue) {
        final Node attributeNode = node.getAttributes().getNamedItem(attributeName);
        return attributeNode != null && !StringUtils.isBlank(attributeNode.getTextContent()) ? attributeNode.getTextContent() : defaultValue;
    }

    public static Double getAttributeValueAsDouble(Node node, String attributeName, Double defaultValue) {
        final Node attributeNode = node.getAttributes().getNamedItem(attributeName);
        return attributeNode != null ? Double.parseDouble(attributeNode.getTextContent()) : defaultValue;
    }

    public static UUID getAttributeValueAsUUID(Node node, String attributeName) {
        final Node attributeNode = node.getAttributes().getNamedItem(attributeName);
        return attributeNode != null && !StringUtils.isBlank(attributeNode.getTextContent()) ? UUID.fromString(attributeNode.getTextContent()) : null;
    }

    public static Boolean getAttributeValueAsBoolean(Node node, String attributeName, Boolean defaultValue) {
        final Node attributeNode = node.getAttributes().getNamedItem(attributeName);

        return attributeNode != null ? Boolean.parseBoolean(attributeNode.getTextContent()) : defaultValue;
    }

    public static Boolean getAttributeValueAsBoolean(Node node, String attributeName) {
        final Node attributeNode = node.getAttributes().getNamedItem(attributeName);

        return attributeNode != null ? Boolean.parseBoolean(attributeNode.getTextContent()) : null;
    }

    public static Integer getAttributeValueAsInteger(Node node, String attributeName, Integer defaultValue) {
        final Node attributeNode = node.getAttributes().getNamedItem(attributeName);

        return attributeNode != null ? Integer.parseInt(attributeNode.getTextContent()) : defaultValue;
    }

    public static Integer getAttributeValueAsInteger(Node node, String attributeName) {
        final Node attributeNode = node.getAttributes().getNamedItem(attributeName);

        return attributeNode != null ? Integer.parseInt(attributeNode.getTextContent()) : null;
    }

    public static Node getChildNode(Node node, String childNodeName) {
        final NodeList children = node.getChildNodes();
        for(int i = 0; i < children.getLength(); i++) {
            final Node childNode = children.item(i);
            if (childNode.getNodeName().equals(childNodeName)) {
                return childNode;
            }
        }
        return null;
    }
}
