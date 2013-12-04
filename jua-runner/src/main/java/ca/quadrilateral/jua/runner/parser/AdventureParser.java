package ca.quadrilateral.jua.runner.parser;

import java.io.File;
import java.io.FilenameFilter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IGameConfiguration;
import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IMap;
import ca.quadrilateral.jua.game.IMapCell;
import ca.quadrilateral.jua.game.IWall;
import ca.quadrilateral.jua.game.background.BackgroundDefinition;
import ca.quadrilateral.jua.game.background.BackgroundDefinitionItem;
import ca.quadrilateral.jua.game.background.IBackgroundDefinitionManager;
import ca.quadrilateral.jua.game.border.BorderDefinition;
import ca.quadrilateral.jua.game.border.BorderDefinitionItem;
import ca.quadrilateral.jua.game.border.IBorderDefinitionManager;
import ca.quadrilateral.jua.game.entity.impl.Alignment;
import ca.quadrilateral.jua.game.entity.impl.CharacterClass;
import ca.quadrilateral.jua.game.entity.impl.CharacterRace;
import ca.quadrilateral.jua.game.entity.impl.SavingThrowTable;
import ca.quadrilateral.jua.game.enums.BorderStyle;
import ca.quadrilateral.jua.game.enums.Direction;
import ca.quadrilateral.jua.game.enums.Distance;
import ca.quadrilateral.jua.game.enums.FacingEnum;
import ca.quadrilateral.jua.game.enums.LevelType;
import ca.quadrilateral.jua.game.enums.TimeOfDay;
import ca.quadrilateral.jua.game.enums.WallType;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;
import ca.quadrilateral.jua.game.impl.Currency;
import ca.quadrilateral.jua.game.impl.DefaultLevel;
import ca.quadrilateral.jua.game.impl.DefaultMap;
import ca.quadrilateral.jua.game.impl.DiceExpression;
import ca.quadrilateral.jua.game.impl.TimeDefinition;
import ca.quadrilateral.jua.game.item.impl.ItemDefinition;
import ca.quadrilateral.jua.game.level.ILevelDefinitionManager;
import ca.quadrilateral.jua.game.wall.IOverlayDefinitionManager;
import ca.quadrilateral.jua.game.wall.IWallDefinitionManager;
import ca.quadrilateral.jua.game.wall.OverlayDefinition;
import ca.quadrilateral.jua.game.wall.OverlayDefinitionItem;
import ca.quadrilateral.jua.game.wall.WallDefinition;
import ca.quadrilateral.jua.game.wall.WallDefinitionItem;
import ca.quadrilateral.jua.runner.parser.event.IDeferredChainParseTracker;
import ca.quadrilateral.jua.runner.parser.event.IEventParser;
import ca.switchcase.commons.util.XmlDomUtilities;

public class AdventureParser implements IAdventureParser {
    @Autowired
    private ILevelContext levelContext;

    @Autowired
    private IGameContext gameContext;

    @Autowired
    private ILevelDefinitionManager levelDefinitionManager;

    @Autowired
    private IBorderDefinitionManager borderDefinitionManager;

    @Autowired
    private IWallDefinitionManager wallDefinitionManager;

    @Autowired
    private IOverlayDefinitionManager overlayDefinitionManager;

    @Autowired
    private IBackgroundDefinitionManager backgroundDefinitionManager;

    @Autowired
    private IGameConfiguration gameConfigurationManager;

    @Autowired
    private IDeferredChainParseTracker deferredChainParseTracker;

    @Autowired
    private List<IEventParser> eventParsers;

    @Override
    public void parse(File file) {
        try {
            loadAssetDefinitions();

            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            final Document doc = documentBuilder.parse(file);

            final Map<Integer, IEvent> eventIdMap = new HashMap<Integer, IEvent>();

            doc.getDocumentElement().normalize();

            this.parseGameInfo(doc);
            this.parseGameCurrencies(doc);
            this.parseGameTimeDefinitions(doc);
            this.parseCharacterRaces(doc);
            this.parseSavingThrows(doc);
            this.parseCharacterClasses(doc);
            this.parseAlignments(doc);


            loadItemDefinitions();

            for(ItemDefinition itemDefinition : gameContext.getItemDefinitions()) {
                System.out.println(itemDefinition.toString());
            }


            final Node rootNode = doc.getElementsByTagName("jua-adventure").item(0);
            final Node levelsNode = XmlDomUtilities.getChildNode(rootNode, "levels");

            final NodeList levelNodes = levelsNode.getChildNodes();
            for(int i = 0; i < levelNodes.getLength(); i++) {
                final Node levelNode = levelNodes.item(i);
                if (levelNode.getNodeName().equals("level")) {
                    final NamedNodeMap attributeMap = levelNode.getAttributes();

                    final String levelType = attributeMap.getNamedItem("levelType").getTextContent();
                    final String levelId = attributeMap.getNamedItem("id").getTextContent();
                    final int height = Integer.parseInt(attributeMap.getNamedItem("height").getTextContent());
                    final int width = Integer.parseInt(attributeMap.getNamedItem("width").getTextContent());
                    final String levelName = attributeMap.getNamedItem("name").getTextContent();

                    final DefaultLevel level = new DefaultLevel();
                    final DefaultMap map = new DefaultMap(height, width);

                    level.setLevelId(UUID.fromString(levelId));
                    level.setLevelType(LevelType.valueOfString(levelType));
                    level.setLevelName(levelName);
                    level.setLevelMap(map);

                    List<BackgroundParseHolder> backgrounds = null;
                    List<WallParseHolder> walls = null;
                    List<OverlayParseHolder> overlays = null;
                    List<Node> eventNodes = new ArrayList<Node>();
                    List<Node> eventTriggerNodes = new LinkedList<Node>();

                    NodeList levelChildNodes = levelNode.getChildNodes();
                    for(int j = 0; j < levelChildNodes.getLength(); j++) {
                        final Node levelChildNode = levelChildNodes.item(j);
                        if (levelChildNode.getNodeName().equals("backgrounds")) {
                            backgrounds = parseBackgrounds(levelChildNode);
                        } else if (levelChildNode.getNodeName().equals("walls")) {
                            walls = parseWalls(levelChildNode);
                        } else if (levelChildNode.getNodeName().equals("overlays")) {
                            overlays = parseOverlays(levelChildNode);
                        } else if (levelChildNode.getNodeName().equals("events")) {
                            for(int n = 0; n < levelChildNode.getChildNodes().getLength(); n++) {
                                final Node possibleEventNode = levelChildNode.getChildNodes().item(n);
                                if (possibleEventNode != null && possibleEventNode.getNodeName() != null && possibleEventNode.getNodeName().equals("event")) {
                                    eventNodes.add(possibleEventNode);
                                }
                            }
                        } else if (levelChildNode.getNodeName().equals("eventTriggers")) {
                            System.out.println("Event Trigger Child Node Count: " + levelChildNode.getChildNodes().getLength());
                            for(int n = 0; n < levelChildNode.getChildNodes().getLength(); n++) {
                                final Node possibleEventTriggerNode = levelChildNode.getChildNodes().item(n);
                                if (possibleEventTriggerNode != null && possibleEventTriggerNode.getNodeName() != null && possibleEventTriggerNode.getNodeName().contains("Trigger")) {
                                    eventTriggerNodes.add(possibleEventTriggerNode);
                                }
                            }
                        }
                    }

                    for(WallParseHolder wall : walls) {
                        final IMapCell cell = map.getMapCellAt(wall.getX(), wall.getY());
                        final IWall cellWall = cell.getWall(wall.getFacing());

                        cellWall.setWallDefinitionId(wall.getWallDefinitionId());
                        cellWall.setWallType(wall.getWallType());
                    }
                    for(BackgroundParseHolder background : backgrounds) {
                        final IMapCell cell = map.getMapCellAt(background.getX(), background.getY());
                        cell.setBackgroundDefinitionId(background.getBackgroundId());
                    }

                    System.out.println("\n****** Overlay Count: " + overlays.size() + " ******\n");

                    for(OverlayParseHolder overlay : overlays) {
                        final IMapCell cell = map.getMapCellAt(overlay.getX(), overlay.getY());
                        final IWall cellWall = cell.getWall(overlay.getFacing());
                        cellWall.addOverlayDefinition(overlay.getOverlayDefinitionId());
                        System.out.println("Adding overlay definition " + overlay.getOverlayDefinitionId() + " to map cell " + cell.getCellX() + ", " + cell.getCellY() + " - " + cellWall.getWallFacing());
                    }

                    for(Node eventNode : eventNodes) {
                        for(IEventParser eventParser : eventParsers) {
                            if (eventParser.canParse(eventNode)) {
                                final IEvent event = eventParser.parse(eventNode);
                                deferredChainParseTracker.trackEvent(event.getEventId(), event);
                                eventIdMap.put(event.getEventId(), event);
                                break;
                            }
                        }
                    }

                    parseMapTriggers(map, eventTriggerNodes, eventIdMap);

                    deferredChainParseTracker.processDeferredChains();

                    for(IEvent event : eventIdMap.values()) {
                        System.out.println(event.toString());
                    }

                    levelDefinitionManager.addLevel(level);
                }
            }

            levelContext.setCurrentLevel(levelDefinitionManager.getLevel(gameContext.getGameConfiguration().getStartLevelId()));
            levelContext.setPartyPosition(gameConfigurationManager.getStartingPartyPosition());

            for(Currency currency : this.gameContext.getCurrenyList()) {
                System.out.println(currency.toString());
            }

            for(TimeDefinition timeDefinition : this.gameContext.getTimeDefinitions()) {
                System.out.println(timeDefinition.toString());
            }

            gameContext.initializeGroovy();

            System.out.print("Game Loaded: " + gameConfigurationManager.toString());

        } catch (Exception e) {
            throw new JUARuntimeException(e);
        }
    }

    private void parseMapTriggers(IMap levelMap, List<Node> eventTriggerNodes, Map<Integer, IEvent> eventIdMap) {
        for(Node eventTriggerNode : eventTriggerNodes) {
            if (eventTriggerNode.getNodeName().equals("mapTrigger")) {
                final Integer xPos = Integer.parseInt(eventTriggerNode.getAttributes().getNamedItem("x").getTextContent());
                final Integer yPos = Integer.parseInt(eventTriggerNode.getAttributes().getNamedItem("y").getTextContent());
                final Integer eventId = Integer.parseInt(eventTriggerNode.getAttributes().getNamedItem("eventId").getTextContent());

                final IMapCell cell = levelMap.getMapCellAt(xPos, yPos);
                cell.setEventChain(eventIdMap.get(eventId));
            } else {
                System.out.println("Unhandled event trigger node: " + eventTriggerNode.getNodeName());
            }
        }
    }

    private void parseAlignments(Document doc) {
        try {
            Set<Alignment> alignments = new HashSet<Alignment>();

            final XPathFactory factory = XPathFactory.newInstance();

            final NodeList nodeList = (NodeList)factory.newXPath().evaluate("jua-adventure/alignments/alignment", doc, XPathConstants.NODESET);

            for(int i = 0; i < nodeList.getLength(); i++) {
                final Node alignmentNode = nodeList.item(i);
                final Alignment newAlignment = new Alignment(
                    XmlDomUtilities.getAttributeValue(alignmentNode, "name"),
                    XmlDomUtilities.getAttributeValue(alignmentNode, "abbreviation"));

                alignments.add(newAlignment);
            }

            this.gameContext.setAlignments(alignments);
        } catch (Exception e) {
            throw new JUARuntimeException(e);
        }
    }

    private void parseGameCurrencies(Document doc) {
        try {
            List<Currency> gameCurrencies = new LinkedList<Currency>();

            final XPathFactory factory = XPathFactory.newInstance();

            final String baseCurrency = (String)factory.newXPath().evaluate("jua-adventure/currencies/@baseCurrency", doc, XPathConstants.STRING);

            final NodeList nodeList = (NodeList)factory.newXPath().evaluate("jua-adventure/currencies/currency", doc, XPathConstants.NODESET);

            for(int i = 0; i < nodeList.getLength(); i++) {
                final Node currencyNode = nodeList.item(i);
                final Currency newCurrency = new Currency(
                    XmlDomUtilities.getAttributeValue(currencyNode, "name"),
                    DiceExpression.parse(XmlDomUtilities.getAttributeValue(currencyNode, "value")),
                    XmlDomUtilities.getAttributeValueAsBoolean(currencyNode, "canBeChanged"));

                if (newCurrency.getName().equalsIgnoreCase(baseCurrency)) {
                    this.gameContext.setBaseCurrency(newCurrency);
                }

                gameCurrencies.add(newCurrency);
            }

            this.gameContext.setCurrencyList(gameCurrencies);
        } catch (Exception e) {
            throw new JUARuntimeException(e);
        }
    }

    private void parseGameTimeDefinitions(Document doc) {
        try {
            final List<TimeDefinition> timeDefinitions = new LinkedList<TimeDefinition>();
            final XPathFactory factory = XPathFactory.newInstance();
            final NodeList nodeList = (NodeList)factory.newXPath().evaluate("jua-adventure/time/unit", doc, XPathConstants.NODESET);
            for(int i = 0; i < nodeList.getLength(); i++) {
                final Node timeDefinitionNode = nodeList.item(i);
                final TimeDefinition newTimeDefinition = new TimeDefinition(
                    XmlDomUtilities.getAttributeValue(timeDefinitionNode, "name"),
                    XmlDomUtilities.getAttributeValueAsInteger(timeDefinitionNode, "steps"),
                    XmlDomUtilities.getAttributeValueAsInteger(timeDefinitionNode, "offset", 0)
                        );

                timeDefinitions.add(newTimeDefinition);
            }

            this.gameContext.setTimeDefinitions(timeDefinitions);
        } catch (Exception e) {
            throw new JUARuntimeException(e);
        }
    }

    private void parseCharacterRaces(Document doc) {
        final CharacterRaceParser parser = new CharacterRaceParser();
        final Node racesNode = doc.getElementsByTagName("races").item(0);

        final Set<CharacterRace> characterRaces = parser.parseRacesNode(racesNode);

        for(CharacterRace characterRace : characterRaces) {
            System.out.println(characterRace.toString());
        }

        this.gameContext.setCharacterRaces(characterRaces);
    }

    private void parseCharacterClasses(Document doc) {
        assert(this.gameContext.getCharacterRaces() != null && !this.gameContext.getCharacterRaces().isEmpty());

        final CharacterClassParser parser = new CharacterClassParser(this.gameContext.getSavingThrowTables());
        final Node classesNode = doc.getElementsByTagName("classes").item(0);
        final Set<CharacterClass> characterClasses = parser.parseClassesNode(classesNode, this.gameContext.getCharacterRaces());

        for(CharacterClass characterClass : characterClasses) {
            System.out.println(characterClass.toString());
        }

        this.gameContext.setCharacterClasses(characterClasses);
    }

    private void parseSavingThrows(Document doc) {
        final SavingThrowParser parser = new SavingThrowParser();
        final Map<Integer, SavingThrowTable> savingThrowTables = parser.parseSavingThrowTables(doc);
        this.gameContext.setSavingThrowTables(savingThrowTables);
    }

    private void parseGameInfo(Document doc) {
        final Node rootNode = doc.getFirstChild();
        final NamedNodeMap attributeMap = rootNode.getAttributes();

        gameConfigurationManager.setAdventureName(attributeMap.getNamedItem("name").getTextContent());
        gameConfigurationManager.setAdventureVersion(attributeMap.getNamedItem("version").getTextContent());

        final NodeList authorNodes = doc.getElementsByTagName("author");
        for(int i = 0; i < authorNodes.getLength(); i++) {
            final Node authorNode = authorNodes.item(i);
            if (authorNode.getNodeName().equals("author")) {
                gameConfigurationManager.addAuthor(authorNodes.item(i).getTextContent());
            }
        }

        final NodeList defaultBorderNodes = doc.getElementsByTagName("default-border");
        final Node defaultBorderNode = defaultBorderNodes.item(0);
        final NamedNodeMap defaultBorderAttributes = defaultBorderNode.getAttributes();

        final UUID defaultBorderUUID = UUID.fromString(defaultBorderAttributes.getNamedItem("id").getTextContent());

        final NodeList gameStartNodes = doc.getElementsByTagName("game-start");
        final Node gameStartNode = gameStartNodes.item(0);
        final NamedNodeMap gameStartAttributes = gameStartNode.getAttributes();

        final UUID startLevelId = UUID.fromString(gameStartAttributes.getNamedItem("levelId").getTextContent());
        final int startLevelX = Integer.parseInt(gameStartAttributes.getNamedItem("x").getTextContent());
        final int startLevelY = Integer.parseInt(gameStartAttributes.getNamedItem("y").getTextContent());
        final FacingEnum facing = FacingEnum.valueOf(gameStartAttributes.getNamedItem("facing").getTextContent());

        gameConfigurationManager.setStartLevelId(startLevelId);
        gameConfigurationManager.setInitialPartyPosition(startLevelX, startLevelY, facing);
        gameConfigurationManager.setDefaultBorderId(defaultBorderUUID);

        gameContext.setGameConfiguration(gameConfigurationManager);
    }

    private List<BackgroundParseHolder> parseBackgrounds(Node n) {
        final List<BackgroundParseHolder> backgrounds = new ArrayList<BackgroundParseHolder>();

        final NodeList backgroundNodes = n.getChildNodes();
        for(int i = 0; i < backgroundNodes.getLength(); i++) {
            if (backgroundNodes.item(i).getNodeName().equals("background")) {
                backgrounds.add(new BackgroundParseHolder(backgroundNodes.item(i)));
            }
        }

        return backgrounds;
    }

    private List<OverlayParseHolder> parseOverlays(Node n) {
        final List<OverlayParseHolder> overlays = new ArrayList<OverlayParseHolder>();

        final NodeList overlayNodes = n.getChildNodes();
        for(int i = 0; i < overlayNodes.getLength(); i++) {
            if (overlayNodes.item(i).getNodeName().equals("overlay")) {
                overlays.add(new OverlayParseHolder(overlayNodes.item(i)));
            }
        }

        Collections.sort(overlays);

        return overlays;
    }

    private List<WallParseHolder> parseWalls(Node n) {
        final List<WallParseHolder> walls = new ArrayList<WallParseHolder>();

        final NodeList wallNodes = n.getChildNodes();
        for(int i = 0; i < wallNodes.getLength(); i++) {
            if (wallNodes.item(i).getNodeName().equals("wall")) {
                walls.add(new WallParseHolder(wallNodes.item(i)));
            }
        }

        return walls;
    }

    private void loadAssetDefinitions() throws Exception {
        System.out.println("Loading Asset Definitions");
        loadWallDefinitions();
        loadBackgroundDefinitions();
        loadOverlayDefinitions();
        loadBorderDefinitions();
    }

    private void loadItemDefinitions() throws Exception {
        final File itemsDirectory = new File(gameContext.getItemsPath());
        final File[] xmlFiles = itemsDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".xml");
            }
        });
        Collection<ItemDefinition> itemDefinitions = new ArrayList<ItemDefinition>();
        for(File file : xmlFiles) {
            itemDefinitions.addAll(loadItemDefinitionFile(file));
        }

        gameContext.setItemDefinitions(itemDefinitions);
    }

    private Collection<ItemDefinition> loadItemDefinitionFile(File file) throws Exception {
        final ArrayList<ItemDefinition> definitions = new ArrayList<ItemDefinition>();
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final Document doc = documentBuilder.parse(file);

        doc.getDocumentElement().normalize();

        try {
            final XPathFactory factory = XPathFactory.newInstance();
            final NodeList itemNodes = (NodeList)factory.newXPath().evaluate("/items/item", doc, XPathConstants.NODESET);

            for(int i = 0; i < itemNodes.getLength(); i++) {
                Node itemNode = itemNodes.item(i);
                String definitionId = XmlDomUtilities.getAttributeValue(itemNode, "id");
                ItemDefinition definition = new ItemDefinition(definitionId);

                String inheritsId = XmlDomUtilities.getAttributeValue(itemNode, "inherits", null);
                if (inheritsId != null) {
                    continue;
                }
                definition.setBaseName(XmlDomUtilities.getAttributeValue(itemNode, "baseName"));
                definition.setIdentifiedName(XmlDomUtilities.getAttributeValue(itemNode, "identifiedName", definition.getBaseName()));
                definition.setUsable(XmlDomUtilities.getAttributeValueAsBoolean(itemNode, "isUsable", false));
                definition.setMagical(XmlDomUtilities.getAttributeValueAsBoolean(itemNode, "isMagical", false));
                definition.setWeight(XmlDomUtilities.getAttributeValueAsDouble(itemNode, "baseValue", 0.0));
                definition.setValue(XmlDomUtilities.getAttributeValueAsInteger(itemNode, "baseValue", 0));
                definition.setExperienceValue(XmlDomUtilities.getAttributeValueAsInteger(itemNode, "experienceValue", 0));

                final NodeList classesNodes = (NodeList)factory.newXPath().evaluate("classesAllowed/class", itemNode, XPathConstants.NODESET);
                for(int j = 0; j < classesNodes.getLength(); j++) {
                    Node classNode = classesNodes.item(j);
                    String className = XmlDomUtilities.getAttributeValue(classNode, "name");
                    for(CharacterClass characterClass : gameContext.getCharacterClasses()) {
                        if (characterClass.getName().equals(className)) {
                            definition.addAllowedClass(characterClass);
                            break;
                        }
                    }
                }

                definitions.add(definition);
            }

        } catch (Exception e) {
            throw new JUARuntimeException(e);
        }

        return definitions;
    }

    private void loadOverlayDefinitions() throws Exception {
        final File overlaysDirectory = new File(gameContext.getOverlaysPath());
        final File[] directoryContents = overlaysDirectory.listFiles();
        for(File file : directoryContents) {
            if (file.isDirectory()) {
                File[] xmlFiles =
                    file.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.toLowerCase().endsWith(".xml");
                        }
                    });
                if(xmlFiles.length == 0) {
                    System.out.println(MessageFormat.format("No overlay definition xml file found in {0}", file.getCanonicalFile()));
                } else {
                    for(File xmlFile : xmlFiles) {
                        loadOverlayDefinition(xmlFile);
                    }
                }
            }
        }
    }


    private void loadOverlayDefinition(File file) throws Exception {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final Document doc = documentBuilder.parse(file);

        doc.getDocumentElement().normalize();

        final NodeList juaOverlayList = doc.getElementsByTagName("jua_overlay");
        if (juaOverlayList.getLength() == 0) {
            System.out.println(MessageFormat.format("File {0} is not a valid overlay definition file", file.getCanonicalFile()));
        } else if (juaOverlayList.getLength() > 1) {
            System.out.println(MessageFormat.format("File {0} contains multiple jua_overlay tags.  An overlay definition file can only contain one jua_overlay element", file.getCanonicalFile()));
        } else {
            System.out.println(MessageFormat.format("Loading Overlay Definition File {0}", file.getCanonicalFile()));
            final Node rootNode = juaOverlayList.item(0);
            final String overlayName = XmlDomUtilities.getAttributeValue(rootNode, "name");
            final UUID overlayUUID = XmlDomUtilities.getAttributeValueAsUUID(rootNode, "id");

            final OverlayDefinition overlayDefinition = new OverlayDefinition();
            overlayDefinition.setOverlayDefinitionId(overlayUUID);

            final Node overlaysetNode = XmlDomUtilities.getChildNode(rootNode, "overlayset");
            for(int n = 0; n < overlaysetNode.getChildNodes().getLength(); n++) {
                final Node overlayNode = overlaysetNode.getChildNodes().item(n);
                if (overlayNode != null && overlayNode.getNodeName().equals("overlay")) {
                    Direction direction = Direction.valueOfString(XmlDomUtilities.getAttributeValue(overlayNode, "facing"));
                    Distance overlayDistance = Distance.valueOfString(XmlDomUtilities.getAttributeValue(overlayNode, "distance"));
                    String filename = XmlDomUtilities.getAttributeValue(overlayNode, "filename");


                    final OverlayDefinitionItem newWall = new OverlayDefinitionItem(
                            MessageFormat.format("{0}{1}{2}", file.getParentFile().getName(), File.separator, filename),
                            direction,
                            overlayDistance);

                    overlayDefinition.addOverlayDefinitionItem(newWall);
                }
            }

            overlayDefinitionManager.addOverlayDefinition(overlayDefinition);

            System.out.println("Parsed Overlay Definition: " + overlayDefinition.toString());
        }

    }

    private void loadWallDefinitions() throws Exception {
        final File wallsDirectory = new File(gameContext.getWallsPath());
        final File[] directoryContents = wallsDirectory.listFiles();
        for(File file : directoryContents) {
            if (file.isDirectory()) {
                File[] xmlFiles =
                    file.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.toLowerCase().endsWith(".xml");
                        }
                    });
                if(xmlFiles.length == 0) {
                    System.out.println(MessageFormat.format("No wall definition xml file found in {0}", file.getCanonicalFile()));
                } else {
                    for(File xmlFile : xmlFiles) {
                        loadWallDefinition(xmlFile);
                    }
                }
            }
        }
    }


    private void loadWallDefinition(File file) throws Exception {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final Document doc = documentBuilder.parse(file);

        doc.getDocumentElement().normalize();

        final NodeList juaWallList = doc.getElementsByTagName("jua_wall");
        if (juaWallList.getLength() == 0) {
            System.out.println(MessageFormat.format("File {0} is not a valid wall definition file", file.getCanonicalFile()));
        } else if (juaWallList.getLength() > 1) {
            System.out.println(MessageFormat.format("File {0} contains multiple jua_wall tags.  An wall definition file can only contain one jua_wall element", file.getCanonicalFile()));
        } else {
            System.out.println(MessageFormat.format("Loading Wall Definition File {0}", file.getCanonicalFile()));
            final Node rootNode = juaWallList.item(0);
            final String wallsetName = XmlDomUtilities.getAttributeValue(rootNode, "name");
            final UUID wallsetUUID = XmlDomUtilities.getAttributeValueAsUUID(rootNode, "id");

            final WallDefinition wallDefinition = new WallDefinition();
            wallDefinition.setWallDefinitionId(wallsetUUID);

            final Node wallsetNode = XmlDomUtilities.getChildNode(rootNode, "wallset");
            for(int n = 0; n < wallsetNode.getChildNodes().getLength(); n++) {
                final Node wallNode = wallsetNode.getChildNodes().item(n);
                if (wallNode != null && wallNode.getNodeName().equals("wall")) {
                    Direction direction = Direction.valueOfString(XmlDomUtilities.getAttributeValue(wallNode, "facing"));
                    Distance wallDistance = Distance.valueOfString(XmlDomUtilities.getAttributeValue(wallNode, "distance"));
                    String filename = XmlDomUtilities.getAttributeValue(wallNode, "filename");


                    final WallDefinitionItem newWall = new WallDefinitionItem(
                            MessageFormat.format("{0}{1}{2}", file.getParentFile().getName(), File.separator, filename),
                            direction,
                            wallDistance);

                    wallDefinition.addWallDefinitionItem(newWall);
                }
            }

            wallDefinitionManager.addWallDefinition(wallDefinition);

            System.out.println("Parsed Wall Definition: " + wallDefinition.toString());
        }

    }

    private void loadBackgroundDefinitions() throws Exception {
        final File backgroundsDirectory = new File(gameContext.getBackgroundsPath());
        final File[] directoryContents = backgroundsDirectory.listFiles();
        for(File file : directoryContents) {
            if (file.isDirectory()) {
                File[] xmlFiles =
                    file.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.toLowerCase().endsWith(".xml");
                        }
                    });
                if(xmlFiles.length == 0) {
                    System.out.println(MessageFormat.format("No background definition xml file found in {0}", file.getCanonicalFile()));
                } else {
                    for(File xmlFile : xmlFiles) {
                        loadBackgroundDefinition(xmlFile);
                    }
                }
            }
        }
    }

    private void loadBackgroundDefinition(File file) throws Exception {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final Document doc = documentBuilder.parse(file);

        doc.getDocumentElement().normalize();

        final NodeList juaWallList = doc.getElementsByTagName("jua_background");
        if (juaWallList.getLength() == 0) {
            System.out.println(MessageFormat.format("File {0} is not a valid background definition file", file.getCanonicalFile()));
        } else if (juaWallList.getLength() > 1) {
            System.out.println(MessageFormat.format("File {0} contains multiple jua_background tags.  A background definition file can only contain one jua_background element", file.getCanonicalFile()));
        } else {
            System.out.println(MessageFormat.format("Loading Background Definition File {0}", file.getCanonicalFile()));
            final Node rootNode = juaWallList.item(0);
            final String backgroundName = XmlDomUtilities.getAttributeValue(rootNode, "name");
            final UUID backgroundUUID = XmlDomUtilities.getAttributeValueAsUUID(rootNode, "id");

            final BackgroundDefinition backgroundDefinition = new BackgroundDefinition();
            backgroundDefinition.setBackgroundDefinitionId(backgroundUUID);

            final Node backgroundsetNode = XmlDomUtilities.getChildNode(rootNode, "backgroundset");
            for(int n = 0; n < backgroundsetNode.getChildNodes().getLength(); n++) {
                final Node backgroundNode = backgroundsetNode.getChildNodes().item(n);
                if (backgroundNode != null && backgroundNode.getNodeName().equals("background")) {
                    TimeOfDay timeOfDay = TimeOfDay.valueOfString(XmlDomUtilities.getAttributeValue(backgroundNode, "timeOfDay"));

                    String filename = XmlDomUtilities.getAttributeValue(backgroundNode, "filename");


                    final BackgroundDefinitionItem newBackground = new BackgroundDefinitionItem(
                            MessageFormat.format("{0}{1}{2}", file.getParentFile().getName(), File.separator, filename),
                            timeOfDay);

                    backgroundDefinition.addBackgroundDefinitionItem(newBackground);
                }
            }

            backgroundDefinitionManager.addBackgroundDefinition(backgroundDefinition);

            System.out.println("Parsed Background Definition: " + backgroundDefinition.toString());
        }

    }


    private void loadBorderDefinitions() throws Exception {
        final File bordersDirectory = new File(gameContext.getBordersPath());
        final File[] directoryContents = bordersDirectory.listFiles();
        for(File file : directoryContents) {
            if (file.isDirectory()) {
                File[] xmlFiles =
                    file.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.toLowerCase().endsWith(".xml");
                        }
                    });
                if(xmlFiles.length == 0) {
                    System.out.println(MessageFormat.format("No border definition xml file found in {0}", file.getCanonicalFile()));
                } else {
                    for(File xmlFile : xmlFiles) {
                        loadBorderDefinition(xmlFile);
                    }
                }
            }
        }
    }

    private void loadBorderDefinition(File file) throws Exception {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final Document doc = documentBuilder.parse(file);

        doc.getDocumentElement().normalize();

        final NodeList juaWallList = doc.getElementsByTagName("jua_border");
        if (juaWallList.getLength() == 0) {
            System.out.println(MessageFormat.format("File {0} is not a valid border definition file", file.getCanonicalFile()));
        } else if (juaWallList.getLength() > 1) {
            System.out.println(MessageFormat.format("File {0} contains multiple jua_border tags.  A border definition file can only contain one jua_border element", file.getCanonicalFile()));
        } else {
            System.out.println(MessageFormat.format("Loading Border Definition File {0}", file.getCanonicalFile()));
            final Node rootNode = juaWallList.item(0);
            final String borderName = XmlDomUtilities.getAttributeValue(rootNode, "name");
            final UUID borderUUID = XmlDomUtilities.getAttributeValueAsUUID(rootNode, "id");

            final BorderDefinition borderDefinition = new BorderDefinition();
            borderDefinition.setBorderDefinitionId(borderUUID);

            final NodeList bordersets = doc.getElementsByTagName("borderset");

            for(int bordersetIndex = 0; bordersetIndex < bordersets.getLength(); bordersetIndex++) {
                final Node borderset = bordersets.item(bordersetIndex);
                final BorderStyle borderStyle = BorderStyle.valueOfString(XmlDomUtilities.getAttributeValue(borderset, "type"));
                final NodeList borderItemNodes = borderset.getChildNodes();
                for(int n = 0; n < borderItemNodes.getLength(); n++) {
                    final Node borderItemNode = borderItemNodes.item(n);
                    if (borderItemNode.getNodeName().equals("borderItem")) {
                        String fileName = XmlDomUtilities.getAttributeValue(borderItemNode, "filename");
                        Integer zOrder = XmlDomUtilities.getAttributeValueAsInteger(borderItemNode, "zOrder");
                        Integer xOffset = XmlDomUtilities.getAttributeValueAsInteger(borderItemNode, "xOffset");
                        Integer yOffset = XmlDomUtilities.getAttributeValueAsInteger(borderItemNode, "yOffset");

                        final BorderDefinitionItem item = new BorderDefinitionItem(borderStyle, xOffset, yOffset, zOrder,
                                MessageFormat.format("{0}{1}{2}", file.getParentFile().getName(), File.separator, fileName));
                        borderDefinition.addBorderDefinitionItem(borderStyle, item);
                    }
                }
            }
            borderDefinitionManager.addBorderDefinition(borderDefinition);

            System.out.println("Parsed Border Definition: " + borderDefinition.toString());
        }

    }

    private class BackgroundParseHolder {
        private int x = 0;
        private int y = 0;
        private UUID backgroundId = null;

        public BackgroundParseHolder(Node node) {
            final NamedNodeMap map = node.getAttributes();
            x = Integer.parseInt(map.getNamedItem("x").getTextContent());
            y = Integer.parseInt(map.getNamedItem("y").getTextContent());
            backgroundId = UUID.fromString(map.getNamedItem("backgroundDefinitionId").getTextContent());
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public UUID getBackgroundId() {
            return this.backgroundId;
        }
    }

    private class OverlayParseHolder implements Comparable<OverlayParseHolder> {
        private int x = 0;
        private int y = 0;
        private FacingEnum facing = null;
        private Integer zOrder = 0;
        private UUID overlayDefinitionId = null;

        public OverlayParseHolder(Node node) {
            final NamedNodeMap map = node.getAttributes();
            x = Integer.parseInt(map.getNamedItem("x").getTextContent());
            y = Integer.parseInt(map.getNamedItem("y").getTextContent());
            overlayDefinitionId = UUID.fromString(map.getNamedItem("overlayDefinitionId").getTextContent());
            facing = FacingEnum.valueOf(map.getNamedItem("facing").getTextContent());
            zOrder = Integer.parseInt(map.getNamedItem("zOrder").getTextContent());
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public FacingEnum getFacing() {
            return this.facing;
        }

        public int getZOrder() {
            if (this.zOrder == null) {
                return 0;
            }
            return this.zOrder;
        }

        public UUID getOverlayDefinitionId() {
            return this.overlayDefinitionId;
        }

        @Override
        public String toString() {
            return "WallParseHolder [facing=" + this.facing
                    + ", overlayDefinitionId=" + this.overlayDefinitionId
                    + ", zOrder=" + this.zOrder + ", x=" + this.x + ", y="
                    + this.y + "]";
        }

        @Override
        public int compareTo(OverlayParseHolder o) {
            return new Integer(this.getZOrder()).compareTo(o.getZOrder());
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }
    }

    private class WallParseHolder {
        private int x = 0;
        private int y = 0;
        private FacingEnum facing = null;
        private WallType wallType = null;
        private UUID wallDefinitionId = null;

        public WallParseHolder(Node node) {
            final NamedNodeMap map = node.getAttributes();
            x = Integer.parseInt(map.getNamedItem("x").getTextContent());
            y = Integer.parseInt(map.getNamedItem("y").getTextContent());
            wallDefinitionId = UUID.fromString(map.getNamedItem("wallDefinitionId").getTextContent());
            facing = FacingEnum.valueOf(map.getNamedItem("facing").getTextContent());
            wallType = WallType.valueOf(map.getNamedItem("type").getTextContent());
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public FacingEnum getFacing() {
            return this.facing;
        }

        public WallType getWallType() {
            return this.wallType;
        }

        public UUID getWallDefinitionId() {
            return this.wallDefinitionId;
        }

        @Override
        public String toString() {
            return "WallParseHolder [facing=" + this.facing
                    + ", wallDefinitionId=" + this.wallDefinitionId
                    + ", wallType=" + this.wallType + ", x=" + this.x + ", y="
                    + this.y + "]";
        }
    }

}
