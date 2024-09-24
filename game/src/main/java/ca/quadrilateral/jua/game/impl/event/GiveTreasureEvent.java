package ca.quadrilateral.jua.game.impl.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.Currency;
import ca.quadrilateral.jua.game.impl.DiceExpression;
import ca.quadrilateral.jua.game.impl.DiceRoller;
import ca.quadrilateral.jua.game.impl.Purse;
import ca.quadrilateral.jua.game.item.impl.Item;
import ca.quadrilateral.jua.game.item.impl.ItemCollection;

public class GiveTreasureEvent extends TextEvent {
    private boolean giveExperience = false;
    private boolean regeneratePerInstance = false;
    private Purse treasurePurse = null;
    private ItemCollection treasureItems;
    private IGameContext gameContext;

    private Map<Currency, DiceExpression> currencyTreasureSource = new HashMap<Currency, DiceExpression>();
    private List<ItemTreasureBase> itemTreasureSource = new ArrayList<ItemTreasureBase>();

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                        .appendToString(super.toString())
                        .toString();
    }

    @Override
    public EventType getEventType() {
        return EventType.GiveTreasureEvent;
    }
    
    public void addCurrencyTreasureSource(Currency currency, DiceExpression expression) {
        currencyTreasureSource.put(currency, expression);
    }

    public void addItemTreasureSource(String itemDefinitionId, boolean isIdentified, DiceExpression chargesExpression, DiceExpression bundleSizeExpression) {
        final ItemTreasureBase itemTreasureBase = new ItemTreasureBase();
        itemTreasureBase.itemDefinitionId = itemDefinitionId;
        itemTreasureBase.isIdentified = isIdentified;
        if (chargesExpression != null) {
        	itemTreasureBase.chargesExpression = chargesExpression;
        }
        if (bundleSizeExpression != null) {
        	itemTreasureBase.bundleSizeExpression = bundleSizeExpression;
        }
        itemTreasureSource.add(itemTreasureBase);
    }

    public void setGameContext(IGameContext gameContext) {
        this.gameContext = gameContext;
    }

    public void setGiveExperience(boolean giveExperience) {
        this.giveExperience = giveExperience;
    }

    public boolean isGiveExperience() {
        return this.giveExperience;
    }

    public boolean isRegeneratePerInstance() {
        return this.regeneratePerInstance;
    }

    public void setRegeneratePerInstance(boolean regeneratePerInstance) {
        this.regeneratePerInstance = regeneratePerInstance;
    }

    public Purse getTreasurePurse() {
        if (this.treasurePurse == null || this.regeneratePerInstance) {
            this.treasurePurse = generateTreasurePurse();
        }
        return this.treasurePurse.clonePurse();
    }

    public ItemCollection getTreasureItems() {
        if (this.treasureItems == null || this.regeneratePerInstance) {
            this.treasureItems = generateTreasureItems();
        }
        return this.treasureItems.cloneItemCollection();
    }

    private ItemCollection generateTreasureItems() {
        final ItemCollection itemCollection = new ItemCollection();
        final DiceRoller diceRoller = new DiceRoller();

        for(ItemTreasureBase itemTreasureBase : this.itemTreasureSource) {
            final Item item = new Item();
            
            item.setCharges(diceRoller.roll(itemTreasureBase.chargesExpression));
            item.setBundleSize(diceRoller.roll(itemTreasureBase.bundleSizeExpression));
            item.setIdentified(itemTreasureBase.isIdentified);
            item.setItemDefinition(gameContext.getItemDefinition(itemTreasureBase.itemDefinitionId));
            itemCollection.add(item);
        }

        return itemCollection;
    }

    private Purse generateTreasurePurse() {
        final Purse purse = new Purse(this.gameContext.getCurrenyList());
        final DiceRoller diceRoller = new DiceRoller();

        for(Currency currency : currencyTreasureSource.keySet()) {
            purse.giveCurrency(currency, diceRoller.roll(currencyTreasureSource.get(currency)));
        }

        return purse;
    }

    public class ItemTreasureBase {
        public String itemDefinitionId = null;
        public boolean isIdentified = false;
        public DiceExpression chargesExpression = new DiceExpression(0, 0, -1);
        public DiceExpression bundleSizeExpression = new DiceExpression(0, 0, 0);
    }

}
