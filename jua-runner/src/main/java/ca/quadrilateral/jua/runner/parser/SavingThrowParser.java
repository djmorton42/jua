package ca.quadrilateral.jua.runner.parser;

import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.quadrilateral.jua.game.entity.impl.SavingThrowDefinition;
import ca.quadrilateral.jua.game.entity.impl.SavingThrowTable;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;
import ca.switchcase.commons.util.XmlDomUtilities;

public class SavingThrowParser {
    private static final Logger logger = LoggerFactory.getLogger(SavingThrowParser.class);
    
    public Map<Integer, SavingThrowTable> parseSavingThrowTables(Document document) {

        try {
            final XPathFactory factory = XPathFactory.newInstance();

            final NodeList savingThrowTableNodes = (NodeList)factory.newXPath().evaluate("jua-adventure/classes/savingThrows/savingThrowTable", document, XPathConstants.NODESET);

            final Map<Integer, SavingThrowTable> result = new HashMap<Integer, SavingThrowTable>();

            for(int i = 0; i < savingThrowTableNodes.getLength(); i++) {
                final Node savingThrowTableNode = savingThrowTableNodes.item(i);

                final Integer tableId = XmlDomUtilities.getAttributeValueAsInteger(savingThrowTableNode, "saveTableId");

                final SavingThrowTable saveTable = new SavingThrowTable();
                saveTable.setSaveTableId(tableId);

                final NodeList definitionNodes = (NodeList)factory.newXPath().evaluate("savingThrowDefinition", savingThrowTableNode, XPathConstants.NODESET);
                for(int j = 0; j < definitionNodes.getLength(); j++) {
                    final Node definitionNode = definitionNodes.item(j);

                    final SavingThrowDefinition saveDefinition = new SavingThrowDefinition();

                    saveDefinition.setMaxLevel(XmlDomUtilities.getAttributeValueAsInteger(definitionNode, "experienceLevelMax"));
                    saveDefinition.setParalyzationPoisonDeath(XmlDomUtilities.getAttributeValueAsInteger(definitionNode, "paralyzationPoisonDeath"));
                    saveDefinition.setRodStaffWand(XmlDomUtilities.getAttributeValueAsInteger(definitionNode, "rodStaffWand"));
                    saveDefinition.setPetrificationPolymorph(XmlDomUtilities.getAttributeValueAsInteger(definitionNode, "petrificationPolymorph"));
                    saveDefinition.setBreathWeapon(XmlDomUtilities.getAttributeValueAsInteger(definitionNode, "breathWeapon"));
                    saveDefinition.setSpell(XmlDomUtilities.getAttributeValueAsInteger(definitionNode, "spell"));

                    saveTable.addSavingThrowDefinition(saveDefinition);
                }

                logger.debug("In Parser Save Table: {}", saveTable.toString());

                result.put(tableId, saveTable);
            }
            
            return result;
        } catch (Exception e) {
            throw new JUARuntimeException(e);
        }
    }
}
