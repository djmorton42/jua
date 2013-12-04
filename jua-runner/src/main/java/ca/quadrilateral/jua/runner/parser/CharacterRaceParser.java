package ca.quadrilateral.jua.runner.parser;

import java.util.HashSet;
import java.util.Set;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ca.quadrilateral.jua.game.entity.impl.CharacterRace;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;
import ca.quadrilateral.jua.game.impl.DiceExpression;
import ca.switchcase.commons.util.XmlDomUtilities;

public class CharacterRaceParser {
    public Set<CharacterRace> parseRacesNode(Node racesNode) {
        try {
            final Set<CharacterRace> raceList = new HashSet<CharacterRace>();
            final XPathFactory factory = XPathFactory.newInstance();
            final NodeList nodeList = (NodeList)factory.newXPath().evaluate("race", racesNode, XPathConstants.NODESET);
            for(int i = 0; i < nodeList.getLength(); i++) {
                final Node raceNode = nodeList.item(i);
                final CharacterRace race = new CharacterRace();
                race.setName(XmlDomUtilities.getAttributeValue(raceNode, "name"));
                race.setBaseAgeUnits(XmlDomUtilities.getAttributeValueAsInteger(raceNode, "baseAgeUnits"));
                race.setAgeVariationExpression(DiceExpression.parse(XmlDomUtilities.getAttributeValue(raceNode, "ageVariation")));
                raceList.add(race);
            }
            return raceList;
        } catch (Exception e) {
            throw new JUARuntimeException(e);
        }
    }
}
