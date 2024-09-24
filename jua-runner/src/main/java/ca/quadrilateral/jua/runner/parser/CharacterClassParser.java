package ca.quadrilateral.jua.runner.parser;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.quadrilateral.jua.game.entity.impl.CharacterClass;
import ca.quadrilateral.jua.game.entity.impl.CharacterRace;
import ca.quadrilateral.jua.game.entity.impl.LevelBasedAbilityDefinition;
import ca.quadrilateral.jua.game.entity.impl.LevelRank;
import ca.quadrilateral.jua.game.entity.impl.SavingThrowTable;
import ca.quadrilateral.jua.game.entity.impl.VariableAbilityDefinition;
import ca.quadrilateral.jua.game.enums.EntityAbilityType;
import ca.quadrilateral.jua.game.enums.EntityAttributeType;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;
import ca.quadrilateral.jua.game.impl.DiceExpression;
import ca.quadrilateral.jua.game.impl.Thac0Formula;
import ca.switchcase.commons.util.XmlDomUtilities;

public class CharacterClassParser {
    private static final Logger logger = LoggerFactory.getLogger(CharacterClassParser.class);
    
    private Map<Integer, SavingThrowTable> savingThrowTables = null;

    public CharacterClassParser(Map<Integer, SavingThrowTable> savingThrowTableDefinitions) {
        savingThrowTables = savingThrowTableDefinitions;
    }

    public Set<CharacterClass> parseClassesNode(Node classesNode, Set<CharacterRace> characterRaces) {
        final Set<CharacterClass> classes = new HashSet<CharacterClass>();

        final NodeList classNodeList = classesNode.getChildNodes();
        for(int n = 0; n < classNodeList.getLength(); n++) {
            final Node classNode = classNodeList.item(n);
            if (classNode.getNodeName().equals("class")) {
                classes.add(parseCharacterClass(classNode, characterRaces));
            }
        }
        return classes;
    }

    private CharacterClass parseCharacterClass(Node characterClassNode, Set<CharacterRace> characterRaces) {
        final CharacterClass characterClass = new CharacterClass();

        final Integer savingThrowTableId = XmlDomUtilities.getAttributeValueAsInteger(characterClassNode, "savingThrowTable");
        characterClass.setSavingThrowTable(savingThrowTables.get(savingThrowTableId));

        characterClass.setThac0Formula(new Thac0Formula(XmlDomUtilities.getAttributeValue(characterClassNode, "thac0Formula")));

        final Node levelsNode = XmlDomUtilities.getChildNode(characterClassNode, "levels");

        characterClass.setName(XmlDomUtilities.getAttributeValue(characterClassNode, "name"));
        characterClass.setAdvancedRankAdditionalExperience(XmlDomUtilities.getAttributeValueAsInteger(levelsNode, "advancedRankAdditionalXpRequired"));
        characterClass.setAdvancedRankAdditionalHitPoints(XmlDomUtilities.getAttributeValueAsInteger(levelsNode, "advancedRankAdditionalHitPoints"));

        logger.debug("Character Class: {} Save Table: {}", characterClass.getName(), characterClass.getSavingThrowTable());

        final NodeList levelNodeList = levelsNode.getChildNodes();
        for(int n = 0; n < levelNodeList.getLength(); n++) {
            final Node levelNode = levelNodeList.item(n);
            if (levelNode.getNodeName().equals("level")) {
                characterClass.addLevelRank(parseLevelRankNode(levelNode));
            }
        }

        final NodeList primeRequisiteNodeList = XmlDomUtilities.getChildNode(characterClassNode, "primeRequisites").getChildNodes();
        for(int n = 0; n < primeRequisiteNodeList.getLength(); n++) {
            final Node primeRequisiteNode = primeRequisiteNodeList.item(n);
            if (primeRequisiteNode.getNodeName().equals("ability")) {
                characterClass.addPrimeRequisite(EntityAttributeType.valueOfString(XmlDomUtilities.getAttributeValue(primeRequisiteNode, "name")));
            }
        }

        final NodeList abilityMinimumNodeList = XmlDomUtilities.getChildNode(characterClassNode, "abilityMinimums").getChildNodes();
        for(int n = 0; n < abilityMinimumNodeList.getLength(); n++) {
            final Node abilityMinimumNode = abilityMinimumNodeList.item(n);
            if (abilityMinimumNode.getNodeName().equals("ability")) {
                characterClass.addAbilityMinimum(
                        EntityAttributeType.valueOfString(XmlDomUtilities.getAttributeValue(abilityMinimumNode, "name")),
                        XmlDomUtilities.getAttributeValueAsInteger(abilityMinimumNode, "minimum")
                );
            }
        }

        parseLevelBasedAbilities(characterClass, characterClassNode);

        parseVariableAbilities(characterClass, characterClassNode, characterRaces);

        return characterClass;
    }

    private CharacterRace getCharacterRaceFromName(String characterRaceName, Set<CharacterRace> characterRaces) {
        for(CharacterRace race : characterRaces) {
            if (race.getName().equalsIgnoreCase(characterRaceName)) {
                return race;
            }
        }
        throw new JUARuntimeException("Character Race " + characterRaceName + " not found!");
    }

    private void parseVariableAbilities(CharacterClass characterClass, Node characterClassNode, Set<CharacterRace> characterRaces) {
        try {
            final XPathFactory factory = XPathFactory.newInstance();
            final Node variableAbilitiesNode = XmlDomUtilities.getChildNode(characterClassNode, "variableAbilities");

            if (variableAbilitiesNode != null) {
                final NodeList nodeList = (NodeList)factory.newXPath().evaluate("ability", variableAbilitiesNode, XPathConstants.NODESET);
                for(int i = 0; i < nodeList.getLength(); i++) {
                    final Node variableAbilityNode = nodeList.item(i);
                    final VariableAbilityDefinition variableAbilityDefinition = new VariableAbilityDefinition();

                    variableAbilityDefinition.setBaseValue(XmlDomUtilities.getAttributeValueAsInteger(variableAbilityNode, "baseValue"));
                    variableAbilityDefinition.setEntityAbilityType(EntityAbilityType.valueOfString(
                            XmlDomUtilities.getAttributeValue(variableAbilityNode, "name")
                        ));

                    final NodeList raceAdjustmentNodes = (NodeList)factory.newXPath().evaluate("raceAdjustment", variableAbilityNode, XPathConstants.NODESET);
                    for(int j = 0; j < raceAdjustmentNodes.getLength(); j++) {
                        final Node raceAdjustmentNode = raceAdjustmentNodes.item(j);

                        variableAbilityDefinition.addRacialAdjustmentValue(
                            getCharacterRaceFromName(XmlDomUtilities.getAttributeValue(raceAdjustmentNode, "raceName"), characterRaces),
                            XmlDomUtilities.getAttributeValueAsInteger(raceAdjustmentNode, "adjustment"));
                    }

                    final NodeList attributeAdjustmentNodes = (NodeList)factory.newXPath().evaluate("attributeAdjustment", variableAbilityNode, XPathConstants.NODESET);
                    for(int j = 0; j < attributeAdjustmentNodes.getLength(); j++) {
                        final Node attributeAdjustmentNode = attributeAdjustmentNodes.item(j);

                        variableAbilityDefinition.addAttributeAdjustmentValue(
                            EntityAttributeType.valueOfString(XmlDomUtilities.getAttributeValue(attributeAdjustmentNode, "attribute")),
                            XmlDomUtilities.getAttributeValueAsInteger(attributeAdjustmentNode, "value"),
                            XmlDomUtilities.getAttributeValueAsInteger(attributeAdjustmentNode, "adjustment"));
                    }

                }
            }
        } catch (Exception e) {
            throw new JUARuntimeException(e);
        }
    }

    private void parseLevelBasedAbilities(CharacterClass characterClass, Node characterClassNode) {
        try {
            final XPathFactory factory = XPathFactory.newInstance();
            final NodeList nodeList = (NodeList)factory.newXPath().evaluate("levelBasedAbilities/levelBasedAbility", characterClassNode, XPathConstants.NODESET);
            for(int i = 0; i < nodeList.getLength(); i++) {
                final Node levelBasedAbilityNode = nodeList.item(i);

                final LevelBasedAbilityDefinition levelBasedAbility = new LevelBasedAbilityDefinition(
                        EntityAbilityType.valueOfString(XmlDomUtilities.getAttributeValue(levelBasedAbilityNode, "name"))
                      );

                final NodeList abilityValueNodes = (NodeList)factory.newXPath().evaluate("abilityValue", levelBasedAbilityNode, XPathConstants.NODESET);
                for(int j = 0; j < abilityValueNodes.getLength(); j++) {
                    final Node abilityValueNode = abilityValueNodes.item(j);
                    levelBasedAbility.addLevelAbility(XmlDomUtilities.getAttributeValueAsInteger(abilityValueNode, "level"), XmlDomUtilities.getAttributeValueAsInteger(abilityValueNode, "percentage"));
                }

                characterClass.addLevelBasedAbility(levelBasedAbility);
            }

        } catch (Exception e) {
            throw new JUARuntimeException(e);
        }
    }

    private LevelRank parseLevelRankNode(Node levelRankNode) {
        final LevelRank levelRank = new LevelRank();

        final int rank = XmlDomUtilities.getAttributeValueAsInteger(levelRankNode, "rank");
        final int xpRequired = XmlDomUtilities.getAttributeValueAsInteger(levelRankNode, "xpRequired");
        final String hitPointExpression = XmlDomUtilities.getAttributeValue(levelRankNode, "additionalHitPoints");

        levelRank.setRank(rank);
        levelRank.setExperiencePointsRequired(xpRequired);
        levelRank.setAdditionalHitpoints(DiceExpression.parse(hitPointExpression));

        return levelRank;
    }
}
