package ca.quadrilateral.jua.game.context;

import groovy.util.GroovyScriptEngine;

import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ca.quadrilateral.jua.game.IGameConfiguration;
import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.entity.impl.Alignment;
import ca.quadrilateral.jua.game.entity.impl.CharacterClass;
import ca.quadrilateral.jua.game.entity.impl.CharacterRace;
import ca.quadrilateral.jua.game.entity.impl.SavingThrowTable;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;
import ca.quadrilateral.jua.game.impl.Currency;
import ca.quadrilateral.jua.game.impl.TimeDefinition;
import ca.quadrilateral.jua.game.item.impl.ItemDefinition;

public class GameContext implements IGameContext {
    private Window gameWindow = null;
    private IGameConfiguration gameConfiguration;
    private String adventureHome;
    private int gameState = 0;
    private Set<CharacterClass> characterClasses;
    private Set<CharacterRace> characterRaces;
    private GroovyScriptEngine groovyScriptEngine;
    private Map<Integer, SavingThrowTable> savingThrowTables;
    private Map<String, Currency> currencyNameMap;
    private Currency baseCurrency;
    private Set<TimeDefinition> timeDefinitions;
    private Set<Alignment> alignments;
    private Collection<ItemDefinition> itemDefinitions;
    private Map<String, ItemDefinition> itemDefinitionMap;

    @Override
    public void initializeGroovy() {
        try {
            groovyScriptEngine = new GroovyScriptEngine(new String[] { this.getScriptsPath() });
        } catch (Exception e) {
            throw new JUARuntimeException(e);
        }
    }

    @Override
    public GroovyScriptEngine getGroovyScriptEngine() {
        assert(groovyScriptEngine != null);
        return this.groovyScriptEngine;
    }

    public void setTimeDefinitions(List<TimeDefinition> newTimeDefinitions) {
        timeDefinitions = new TreeSet<TimeDefinition>();
        for(TimeDefinition timeDefinition : newTimeDefinitions) {
            timeDefinitions.add(timeDefinition);
        }
    }

    public Set<TimeDefinition> getTimeDefinitions() {
        return this.timeDefinitions;
    }

    public void setAlignments(Set<Alignment> alignments) {
        this.alignments = alignments;
    }

    @Override
    public Set<Alignment> getAlignments() {
        return this.alignments;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Currency getBaseCurrency() {
        return this.baseCurrency;
    }

    @Override
    public void setCurrencyList(List<Currency> currencyList) {
        currencyNameMap = new HashMap<String, Currency>();
        for(Currency currency : currencyList) {
            currencyNameMap.put(currency.getName(), currency);
        }
    }

    @Override
    public List<Currency> getCurrenyList() {
        return new ArrayList<Currency>(currencyNameMap.values());
    }

    @Override
    public Map<String,Currency> getCurrencyNameMap() {
        return currencyNameMap;
    }


    @Override
    public Set<CharacterClass> getCharacterClasses() {
        return characterClasses;
    }

    @Override
    public void setCharacterClasses(Set<CharacterClass> characterClasses) {
        this.characterClasses = characterClasses;
    }

    @Override
    public Set<CharacterRace> getCharacterRaces() {
        return this.characterRaces;
    }

    @Override
    public void setCharacterRaces(Set<CharacterRace> characterRaces) {
        this.characterRaces = characterRaces;
    }

    @Override
    public CharacterRace getCharacterRaceByName(String name) {
        for(CharacterRace race : this.characterRaces) {
            if (race.getName().equalsIgnoreCase(name)) {
                return race;
            }
        }
        return null;
    }

    @Override
    public Window getGameWindow() {
        return this.gameWindow;
    }

    @Override
    public void setGameWindow(Window gameWindow) {
        this.gameWindow = gameWindow;
    }

    @Override
    public IGameConfiguration getGameConfiguration() {
        return this.gameConfiguration;
    }

    @Override
    public void setGameConfiguration(IGameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    @Override
    public String getAdventureHome() {
        return this.adventureHome;
    }

    @Override
    public void setAdventureHome(String adventureHome) {
        this.adventureHome = adventureHome;
        if (!adventureHome.endsWith(File.separator)) {
            this.adventureHome += File.separator;
        }
    }

    @Override
    public String getAssetsPath() {
        return this.getAdventureHome() + "assets" + File.separator;
    }

    @Override
    public String getWallsPath() {
        return this.getAssetsPath() + "walls" + File.separator;
    }

    @Override
    public String getOverlaysPath() {
        return this.getAssetsPath() + "overlays" + File.separator;
    }

    @Override
    public String getBackgroundsPath() {
        return this.getAssetsPath() + "backgrounds" + File.separator;
    }

    @Override
    public int getGameState() {
        return this.gameState;
    }

    @Override
    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    @Override
    public void modifyGameState(int gameState) {
        this.gameState |= gameState;
    }

    @Override
    public String getImagesPath() {
        return this.getAssetsPath() + "images" + File.separator;
    }

    @Override
    public String getBordersPath() {
        return this.getAssetsPath() + "borders" + File.separator;
    }

    @Override
    public String getScriptsPath() {
        return this.getAdventureHome() + "scripts" + File.separator;
    }


    @Override
    public String getItemsPath() {
        return this.getAssetsPath() + "items" + File.separator;
    }

    @Override
    public Map<Integer, SavingThrowTable> getSavingThrowTables() {
        return this.savingThrowTables;
    }

    @Override
    public void setItemDefinitions(Collection<ItemDefinition> itemDefinitions) {
        this.itemDefinitions = itemDefinitions;
        this.itemDefinitionMap = new HashMap<String, ItemDefinition>();
        for(ItemDefinition itemDefinition : itemDefinitions) {
            this.itemDefinitionMap.put(itemDefinition.getDefinitionId(), itemDefinition);
        }
    }

    @Override
    public Collection<ItemDefinition> getItemDefinitions() {
        return this.itemDefinitions;
    }

    @Override
    public ItemDefinition getItemDefinition(String itemDefinitionId) {
        return this.itemDefinitionMap.get(itemDefinitionId);
    }

    @Override
    public void setSavingThrowTables(Map<Integer, SavingThrowTable> savingThrowTables) {
        for(Integer x : savingThrowTables.keySet()) {
            System.out.println("Save Table: " + savingThrowTables.get(x));
        }

        this.savingThrowTables = savingThrowTables;
    }


}
