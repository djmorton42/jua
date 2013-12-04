package ca.quadrilateral.jua.runner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.WeakHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IEventHandler;
import ca.quadrilateral.jua.game.IGameClock;
import ca.quadrilateral.jua.game.IGameConfiguration;
import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.IGameStats;
import ca.quadrilateral.jua.game.IImageRenderer;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IOptionRenderer;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.ITextRenderer;
import ca.quadrilateral.jua.game.IVetoablePositionChangeEvent;
import ca.quadrilateral.jua.game.entity.IParty;
import ca.quadrilateral.jua.game.entity.impl.Alignment;
import ca.quadrilateral.jua.game.entity.impl.CharacterClass;
import ca.quadrilateral.jua.game.entity.impl.Party;
import ca.quadrilateral.jua.game.entity.impl.PartyMember;
import ca.quadrilateral.jua.game.enums.EntityAttributeType;
import ca.quadrilateral.jua.game.enums.GameMode;
import ca.quadrilateral.jua.game.enums.Gender;
import ca.quadrilateral.jua.game.image.IGameImage;
import ca.quadrilateral.jua.game.impl.Purse;
import ca.quadrilateral.jua.game.item.impl.Item;
import ca.quadrilateral.jua.game.item.impl.ItemCollection;
import ca.quadrilateral.jua.game.item.impl.ItemDefinition;
import ca.quadrilateral.jua.runner.parser.IAdventureParser;

public class GameRunner implements IGameRunner {
    public static final long NANOS_PER_SECOND = 1000000000;

    @Autowired
    private IGameWindow gameWindow;

    @Autowired
    private IDisplayManager displayManager;

    @Autowired
    private IAdventureParser adventureParser;

    @Autowired
    private IGameContext gameContext;

    @Autowired
    private ILevelContext levelContext;

    @Autowired
    private IGameConfiguration gameConfigurationManager;

    @Autowired
    private IGameStats gameStats;

    @Autowired
    private IGameStateMachine gameStateMachine;

    @Autowired
    private IOptionRenderer optionRenderer;

    @Autowired
    private IGameClock gameClock;

    @Autowired
    private List<IEventHandler> eventHandlers;

    @Autowired
    private ITextRenderer textRenderer;

    @Autowired
    private IImageRenderer imageRenderer;

    private IEvent currentEvent = null;
    private IEventHandler currentEventHandler = null;

    private List<String> menuOptions = new ArrayList<String>();
    private List<String> characterViewMenuOptions = new ArrayList<String>();
    private List<String> characterInventoryMenuOptions = new ArrayList<String>();

    @Override
    public void initialize() {
        System.out.println("Game Initialized");

        menuOptions.add("Move");
        menuOptions.add("Area");
        menuOptions.add("Cast");
        menuOptions.add("View");
        menuOptions.add("Encamp");
        menuOptions.add("Search");
        menuOptions.add("Look");
        menuOptions.add("Inventory");

        characterViewMenuOptions.add("Items");
        characterViewMenuOptions.add("Trade");
        characterViewMenuOptions.add("Drop");
        characterViewMenuOptions.add("Exit");

        characterInventoryMenuOptions.addAll(Arrays.asList("Ready", "Drop", "Trade", "Use", "Exit"));


        final List<String> commandLineArgs = ArgsContainer.getInstance().getArgs();

        try {
            this.loadGame(commandLineArgs);
        } catch (IOException ioe) {
            System.out.println("An exception occurred loading the adventure...");
            ioe.printStackTrace();
            System.exit(1);
        }

        this.runGame();
    }

    private void loadGame(List<String> args) throws IOException {
        final String adventureHome = args.get(0);

        final File adventureHomeFile = new File(adventureHome);
        if (!adventureHomeFile.exists() || !adventureHomeFile.isDirectory()) {
            System.out.println(MessageFormat.format("The adventure path specified is invalid ({0})", adventureHome));
        }

        final File[] adventureFiles =
            adventureHomeFile.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return  name.endsWith(".adventure.xml");
                }
            });

        if (adventureFiles.length == 0) {
            System.out.println("No adventure file could be found.  Please specify a path containing a file that ends with adventure.xml");
            System.exit(1);
        } else if (adventureFiles.length > 1) {
            System.out.println("Multiple adventure files found at the specified path.  Only one adventure file may be present.");
            System.exit(1);
        }
        final File adventureFile = adventureFiles[0];
        System.out.println(MessageFormat.format("Loading Adventure from {0}", adventureFile.getCanonicalPath()));

        gameContext.setAdventureHome(adventureHome);
        adventureParser.parse(adventureFile);

        final IParty party = createTestParty();
        levelContext.setParty(party);


        final WeakHashMap<String, IGameImage> levelStructureCache = new WeakHashMap<String, IGameImage>();
    }

    private IParty createTestParty() {
        final Random random = new Random();

        final IParty party = new Party();
        PartyMember member = null;

        member = new PartyMember("By-Tor", random.nextInt(100), 10, Gender.Male, gameContext);
        member.setCharacterRace(gameContext.getCharacterRaceByName("Human"));
        member.setAgeUnits(member.getCharacterRace().generateInitialAge());
        member.setCharacterClassLevel(findCharacterClass("Ranger"), 1);
        member.setAlignment(Alignment.getAlignmentByName(gameContext.getAlignments(), "True Neutral"));
        addAttributes(member);
        member.getEntityAttribute(EntityAttributeType.Dexterity).setBaseValue(18);
        member.getEntityAttribute(EntityAttributeType.Charisma).setBaseValue(18);
        member.getEntityAttribute(EntityAttributeType.Strength).setBaseValue(18).setBasePartialValue(100);
        Purse purse = member.getPurse();
        purse.giveCurrency(gameContext.getCurrencyNameMap().get("Gold"), 125);
        purse.giveCurrency(gameContext.getCurrencyNameMap().get("Silver"), 514);
        purse.giveCurrency(gameContext.getCurrencyNameMap().get("Copper"), 2343);

        ItemCollection inventory = member.getInventory();


        Collection<ItemDefinition> itemDefinitions = gameContext.getItemDefinitions();

        for(int i = 0; i < 3; i++) {
            for(ItemDefinition itemDefinition : itemDefinitions) {
                inventory.add(new Item(itemDefinition));
            }
        }


        party.addPartyMember(member);



        member = new PartyMember("Sir Thornhelm", random.nextInt(100), 10, Gender.Male, gameContext);
        member.setCharacterRace(gameContext.getCharacterRaceByName("Human"));
        member.setAgeUnits(member.getCharacterRace().generateInitialAge());
        member.setCharacterClassLevel(findCharacterClass("Paladin"), 1);
        member.setAlignment(Alignment.getAlignmentByName(gameContext.getAlignments(), "Lawful Good"));
        addAttributes(member);

        member.getEntityAttribute(EntityAttributeType.Strength).setBaseValue(17);
        member.getEntityAttribute(EntityAttributeType.Charisma).setBaseValue(17);

        party.addPartyMember(member);


        member = new PartyMember("Ig Stonebeard", random.nextInt(100), 10, Gender.Male, gameContext);
        member.setCharacterRace(gameContext.getCharacterRaceByName("Dwarf"));
        member.setAgeUnits(member.getCharacterRace().generateInitialAge());
        member.setCharacterClassLevel(findCharacterClass("Fighter"), 10);
        member.setAlignment(Alignment.getAlignmentByName(gameContext.getAlignments(), "Chaotic Neutral"));
        addAttributes(member);
        member.getEntityAttribute(EntityAttributeType.Strength).setBaseValue(16);
        member.getEntityAttribute(EntityAttributeType.Charisma).setBaseValue(16);
        member.setExperiencePoints(3000);

        party.addPartyMember(member);



        member = new PartyMember("Wil Greystone", random.nextInt(100), 10, Gender.Male, gameContext);
        member.setCharacterRace(gameContext.getCharacterRaceByName("Elf"));
        member.setAgeUnits(member.getCharacterRace().generateInitialAge());
        member.setCharacterClassLevel(findCharacterClass("Cleric"), 1);
        member.setAlignment(Alignment.getAlignmentByName(gameContext.getAlignments(), "Lawful Neutral"));
        addAttributes(member);
        member.getEntityAttribute(EntityAttributeType.Strength).setBaseValue(15);

        party.addPartyMember(member);



        member = new PartyMember("Alurin", random.nextInt(100), 10, Gender.Female, gameContext);
        member.setCharacterRace(gameContext.getCharacterRaceByName("Half-Elf"));
        member.setAgeUnits(member.getCharacterRace().generateInitialAge());
        member.setCharacterClassLevel(findCharacterClass("Mage"), 1);
        member.setAlignment(Alignment.getAlignmentByName(gameContext.getAlignments(), "Chaotic Neutral"));
        addAttributes(member);
        member.getEntityAttribute(EntityAttributeType.Strength).setBaseValue(7);

        party.addPartyMember(member);

        member = new PartyMember("Asurax", random.nextInt(100), 10, Gender.Male, gameContext);
        member.setCharacterRace(gameContext.getCharacterRaceByName("Human"));
        member.setAgeUnits(member.getCharacterRace().generateInitialAge());
        member.setCharacterClassLevel(findCharacterClass("Mage"), 1);
        member.setAlignment(Alignment.getAlignmentByName(gameContext.getAlignments(), "Chaotic Good"));
        addAttributes(member);

        party.addPartyMember(member);

        for(PartyMember partyMember : party.getPartyMembers()) {
            partyMember.canAdvanceLevel();
        }

        return party;
    }

    private void addAttributes(PartyMember member) {
        member.addEntityAttribute(EntityAttributeType.Strength, 10);
        member.addEntityAttribute(EntityAttributeType.Intelligence, 10);
        member.addEntityAttribute(EntityAttributeType.Wisdom, 10);
        member.addEntityAttribute(EntityAttributeType.Dexterity, 10);
        member.addEntityAttribute(EntityAttributeType.Constitution, 10);
        member.addEntityAttribute(EntityAttributeType.Charisma, 10);

//        Effect dexterityEffect = new Effect("Dexterity Effect");
//        Effect.EffectDetails acAdjustment = new Effect.EffectDetails(EffectType.MODIFY_AC, "dexterityACAdjustment.groovy");
//        dexterityEffect.addEffectDetails(acAdjustment);
//
//        Effect strengthEffect = new Effect("Strength Effect");
//        Effect.EffectDetails toHitAdjustment = new Effect.EffectDetails(EffectType.MODIFY_THAC0, "strengthHitProbabilityAdjustment.groovy");
//        strengthEffect.addEffectDetails(toHitAdjustment);
//
//        member.addEffect(dexterityEffect);
//        member.addEffect(strengthEffect);
    }

    private CharacterClass findCharacterClass(String name) {
        for(CharacterClass characterClass : gameContext.getCharacterClasses()) {
            if (characterClass.getName().equals(name)) {
                return characterClass;
            }
        }
        return null;
    }

    private void runGame() {
        displayManager.initialize();
        try {
            displayManager.setDisplayMode(gameWindow);
            this.gameLoop(displayManager.getBufferStrategy());
        } finally {
            System.out.println("Shutting Down");
            displayManager.restoreOriginalDisplayMode();
        }
    }

    private void renderFPS(Graphics2D g, int fps) {
        final Color initialColor = g.getColor();
        final Font initialFont = g.getFont();


        g.setColor(Color.GREEN);
        g.setFont(this.gameConfigurationManager.getGameFont());
        g.drawString(fps + " fps", 940, 495);


        g.setColor(initialColor);
        g.setFont(initialFont);
    }

    private void clearScreen(Graphics2D g) {
        final Color initialColor = g.getColor();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 1024, 768);
        g.setColor(initialColor);
    }

    private void loadInitialEvent() {
        currentEvent = levelContext
                        .getCurrentLevel()
                        .getLevelMap()
                        .getMapCellAt(levelContext.getPartyPosition()).getEventChain();
    }


    private void gameLoop(BufferStrategy bufferStrategy) {
        long nanoTicks = 0;
        long reportNanoTicks = 0;
        long lastNanoTick = 0;
        long diffTicks = 0;
        int fps = 0;

        long renderTicks = 0;
        long handleGameTicks = 0;
        long loopTicks = 0;

        loadInitialEvent();

        while (true) {
            nanoTicks = System.nanoTime();
            diffTicks = nanoTicks - lastNanoTick;
            lastNanoTick = nanoTicks;

            final Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            this.clearScreen(g);

            renderTicks = System.nanoTime();
            this.gameWindow.render(g);
            gameStats.addRenderTime(System.nanoTime() - renderTicks);

            if (nanoTicks >= reportNanoTicks + NANOS_PER_SECOND) {
                fps = (int) (NANOS_PER_SECOND / diffTicks);
                reportNanoTicks = nanoTicks;
            }

            this.renderFPS(g, fps);

            handleGameTicks = System.nanoTime();
            this.handleGame();
            gameStats.addHandleGameTime(System.nanoTime() - handleGameTicks);

            bufferStrategy.show();
            g.dispose();
            gameStats.addLoopTime(System.nanoTime() - nanoTicks);
        }
    }

    private void handleGame() {
        if ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT) > 0) {
            if ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_GET_YES_NO_INPUT) > 0) {
                if ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_MUST_SET_OPTION) > 0) {
                    gameStateMachine.removeState(IGameStateMachine.GAME_STATE_MUST_SET_OPTION);
                    final List<String> options = new ArrayList<String>(0x2);
                    options.add("Yes");
                    options.add("No");
                    optionRenderer.setOptions(options);
                }
            } else if ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_GET_ENTER_INPUT) > 0) {
                if ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_MUST_SET_OPTION) > 0) {

                    gameStateMachine.removeState(IGameStateMachine.GAME_STATE_MUST_SET_OPTION);
                    final List<String> options = new ArrayList<String>(0x1);
                    options.add("Press ENTER To Continue");
                    optionRenderer.setOptions(options);
                }
            } else if ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_GET_SELECT_INPUT) > 0) {
                if ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_MUST_SET_OPTION) > 0) {
                    gameStateMachine.removeState(IGameStateMachine.GAME_STATE_MUST_SET_OPTION);
                    final List<String> options = new ArrayList<String>(0x1);
                    options.add("Select");
                    optionRenderer.setOptions(options);
                }
            } else if ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_GET_BUTTON_INPUT) > 0) {
                if ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_MUST_SET_OPTION) > 0) {
                    gameStateMachine.removeState(IGameStateMachine.GAME_STATE_MUST_SET_OPTION);
                }
            } else if (gameStateMachine.getCurrentGameMode().equals(GameMode.ThreeDMenu) && ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_MUST_SET_OPTION) > 0)) {
                gameStateMachine.removeState(IGameStateMachine.GAME_STATE_MUST_SET_OPTION);
                gameStateMachine.removeState(IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT);
                optionRenderer.setOptions(menuOptions);
            } else if (gameStateMachine.getCurrentGameMode().equals(GameMode.CharacterInfoView) && ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_MUST_SET_OPTION) > 0)) {
                gameStateMachine.removeState(IGameStateMachine.GAME_STATE_MUST_SET_OPTION);
                gameStateMachine.removeState(IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT);
                optionRenderer.setOptions(characterViewMenuOptions);
            } else if (gameStateMachine.getCurrentGameMode().equals(GameMode.CharacterInventoryView) && ((gameStateMachine.getGameState() & IGameStateMachine.GAME_STATE_MUST_SET_OPTION) > 0)) {
                gameStateMachine.removeState(IGameStateMachine.GAME_STATE_MUST_SET_OPTION);
                gameStateMachine.removeState(IGameStateMachine.GAME_STATE_WAITING_FOR_INPUT);
                optionRenderer.setOptions(characterInventoryMenuOptions);
            }
        } else if (gameStateMachine.isMovementEnabled()) {
            if (gameStateMachine.getCurrentGameMode().equals(GameMode.ThreeD)) {
                final List<String> options = new ArrayList<String>(0x1);
                options.add("Menu");
                optionRenderer.setOptions(options);
            }
        } else {
            this.optionRenderer.clear();
        }
        this.handlePositionChange();

    }

    private void handlePositionChange() {
        final IPartyPosition positionChange = this.gameStateMachine.popPositionChange();
        if (positionChange != null) {
            this.gameStateMachine.updatePartyPosition(positionChange);
            gameClock.incrementTimeOneUnit();
        }
        if (this.gameStateMachine.peekPositionChangeRequest() != null) {
            IPartyPosition peekPositionChange = this.gameStateMachine.peekPositionChangeRequest();

            currentEvent = levelContext
                                .getCurrentLevel()
                                .getLevelMap()
                                .getMapCellAt(
                                        this.gameStateMachine
                                            .peekPositionChangeRequest()).getEventChain();

            this.textRenderer.clear();
            this.imageRenderer.clear();

            if (currentEvent == null) {
                this.gameStateMachine.validatePositionChangeRequest();
            } else {
                if (!(currentEvent instanceof IVetoablePositionChangeEvent)) {
                    this.gameStateMachine.validatePositionChangeRequest();
                }
            }
        }

        if (currentEvent != null) {
            if (currentEventHandler != null) {
                if (!currentEventHandler.isDone()) {
                    currentEventHandler.runEvent();
                } else {
                    System.out.println("Event is done... Clearing variables");
                    currentEvent = null;
                    currentEventHandler = null;

                    System.out.println("Updating Can Advance Level Flag for party members");
                    final List<PartyMember> partyMembers = this.levelContext.getParty().getPartyMembers();
                    for(PartyMember partyMember : partyMembers) {
                        partyMember.canAdvanceLevel();
                    }

                    if (this.gameStateMachine.peekQueuedEvent() != null) {
                        currentEvent = this.gameStateMachine.popQueuedEvent();
                    } else if (this.gameStateMachine.peekReturnEvent() != null) {
                        currentEvent = this.gameStateMachine.popReturnEvent();
                    } else {
                        gameStateMachine.revertAfterChain();
                    }
                }
            } else {
                this.gameStateMachine.setMovementEnabled(false);
                System.out.println("Getting Event Handler");
                currentEventHandler = getEventHandler(currentEvent);
                currentEventHandler.initializeEvent(currentEvent);
            }
        } else {
            this.gameStateMachine.setMovementEnabled(true);
        }
    }

    private IEventHandler getEventHandler(IEvent currentEvent) {
        System.out.println("Event Type: " + currentEvent.toString());

        for(IEventHandler eventHandler : eventHandlers) {
            if (eventHandler.canHandle(currentEvent)) {
                return eventHandler;
            }
        }
        return null;
    }
}
