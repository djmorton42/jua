package ca.quadrilateral.jua.game;

import groovy.util.GroovyScriptEngine;

import java.awt.Window;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.quadrilateral.jua.game.entity.impl.Alignment;
import ca.quadrilateral.jua.game.entity.impl.CharacterClass;
import ca.quadrilateral.jua.game.entity.impl.CharacterRace;
import ca.quadrilateral.jua.game.entity.impl.SavingThrowTable;
import ca.quadrilateral.jua.game.impl.Currency;
import ca.quadrilateral.jua.game.impl.TimeDefinition;
import ca.quadrilateral.jua.game.item.impl.ItemDefinition;

public interface IGameContext {
    public static final int GAME_STATE_EMPTY = 0x0;
    public static final int GAME_STATE_WAIT_FOR_ENTER = 0x01;
    public static final int GAME_STATE_CLEAR_TEXT_ON_ENTER = 0x02;
    public static final int GAME_STATE_CLEAR_IMAGE_ON_ENTER = 0x04;

    void setCurrencyList(List<Currency> currencyList);
    List<Currency> getCurrenyList();
    Map<String,Currency> getCurrencyNameMap();
    void setBaseCurrency(Currency baseCurrency);
    Currency getBaseCurrency();

    void setTimeDefinitions(List<TimeDefinition> newTimeDefinitions);
    Set<TimeDefinition> getTimeDefinitions();


    void initializeGroovy();
    GroovyScriptEngine getGroovyScriptEngine();

    Set<CharacterClass> getCharacterClasses();
    void setCharacterClasses(Set<CharacterClass> characterClasses);

    Set<CharacterRace> getCharacterRaces();
    void setCharacterRaces(Set<CharacterRace> characterRaces);
    CharacterRace getCharacterRaceByName(String name);

    Map<Integer, SavingThrowTable> getSavingThrowTables();
    void setSavingThrowTables(Map<Integer, SavingThrowTable> savingThrowTables);

    int getGameState();
    void setGameState(int gameState);
    void modifyGameState(int gameState);

    Window getGameWindow();
    void setGameWindow(Window gameWindow);

    IGameConfiguration getGameConfiguration();
    void setGameConfiguration(IGameConfiguration gameConfiguration);

    String getAdventureHome();
    void setAdventureHome(String adventureHome);
    String getAssetsPath();
    String getWallsPath();
    String getBackgroundsPath();
    String getImagesPath();
    String getBordersPath();
    String getScriptsPath();
    String getOverlaysPath();

    Set<Alignment> getAlignments();
    void setAlignments(Set<Alignment> alignments);
    String getItemsPath();
    void setItemDefinitions(Collection<ItemDefinition> itemDefinitions);
    Collection<ItemDefinition> getItemDefinitions();
    ItemDefinition getItemDefinition(String itemDefinitionId);


}
