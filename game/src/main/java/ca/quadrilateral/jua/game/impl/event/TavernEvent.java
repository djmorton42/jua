package ca.quadrilateral.jua.game.impl.event;

import ca.quadrilateral.jua.game.IChainedEventContainer;
import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.enums.EventType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import org.apache.commons.lang.builder.ToStringBuilder;

public class TavernEvent extends TextEvent {
    private List<String> tavernTales = new ArrayList<String>(0xf);
    private boolean displayTavernTalesInOrder = false;
    private int lastTavernTaleIndexDisplayed = -1;
    private TavernFight tavernFight = new TavernFight();
    private TavernDrink tavernDrink = new TavernDrink();
    private Random random = new Random();

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this).appendToString(super.toString());
        builder.appendToString(tavernDrink.toString());
        builder.append("Display Tavern Tales In Order", displayTavernTalesInOrder);
        for(String tavernTale : tavernTales) {
            builder.append("Tavern Tale", tavernTale);
        }
        return builder.toString();
    }

    @Override
    public EventType getEventType() {
        return EventType.TavernEvent;
    }



    public boolean isDisplayTavernTalesInOrder() {
        return displayTavernTalesInOrder;
    }

    public void setDisplayTavernTalesInOrder(boolean displayTavernTalesInOrder) {
        this.displayTavernTalesInOrder = displayTavernTalesInOrder;
    }

    public int getLastTavernTaleIndexDisplayed() {
        return lastTavernTaleIndexDisplayed;
    }

    public void setLastTavernTaleIndexDisplayed(int lastTavernTaleIndexDisplayed) {
        this.lastTavernTaleIndexDisplayed = lastTavernTaleIndexDisplayed;
    }

    public TavernFight getTavernFight() {
        return this.tavernFight;
    }
    
    public TavernDrink getTavernDrink() {
        return this.tavernDrink;
    }
    
    public List<String> getTavernTales() {
        return new ArrayList<String>(this.tavernTales);
    }
    
    public void addTavernTale(String tavernTale) {
        tavernTales.add(tavernTale);
    }
    
    public String getNextTavernTale() {
        if (displayTavernTalesInOrder) {
            int indexToDisplay = lastTavernTaleIndexDisplayed + 1;
            if (indexToDisplay >= this.tavernTales.size()) {
                indexToDisplay = 0;
            }
            lastTavernTaleIndexDisplayed = indexToDisplay;
            return this.tavernTales.get(indexToDisplay);
        } else {
            return this.tavernTales.get(random.nextInt(this.tavernTales.size()));
        }
    }
    
    public boolean hasTavernTales() {
        return !this.tavernTales.isEmpty();
    }

    @Override
    public boolean isClearImageOnEnter() {
        return false;
    }

    @Override
    public boolean isClearTextOnEnter() {
        return true;
    }

    @Override
    public boolean isMustPressReturn() {
        return true;
    }



    public class TavernFight implements IChainedEventContainer {
        private IEvent chainedEvent = null;
        private boolean allowFights = false;

        public boolean isAllowFights() {
            return allowFights;
        }

        public void setAllowFights(boolean allowFights) {
            this.allowFights = allowFights;
        }

        @Override
        public IEvent getChainedEvent() {
            return this.chainedEvent;
        }

        @Override
        public void setChainedEvent(IEvent event) {
            this.chainedEvent = event;
        }
    }

    public class TavernDrink implements IChainedEventContainer {
        private IEvent chainedEvent = null;
        private boolean allowDrinks = false;
        private Map<String, Integer> drinks = new TreeMap<String, Integer>(); // This sorts based on alpha ordering of name, not on order added... shoudl be changed
        private int drunkThreshold = 0;
        private String drinkText = null;

        @Override
        public String toString() {
            final ToStringBuilder builder = new ToStringBuilder(this).append("Drink Text", drinkText).append("Allow Drinks", allowDrinks);
            for(String drinkName : drinks.keySet()) {
                builder.append("Drink", drinkName + "(" + drinks.get(drinkName) + ")");
            }

            return builder.toString();
        }

        public boolean isAllowDrinks() {
            return this.allowDrinks;
        }

        public void setAllowDrinks(boolean allowDrinks) {
            this.allowDrinks = allowDrinks;
        }

        public String getDrinkText() {
            return drinkText;
        }

        public void setDrinkText(String drinkText) {
            this.drinkText = drinkText;
        }

        public int getDrunkThreshold() {
            return drunkThreshold;
        }

        public void setDrunkThreshold(int drunkThreshold) {
            this.drunkThreshold = drunkThreshold;
        }

        public void addDrink(String drinkName, int strength) {
            drinks.put(drinkName, strength);
        }

        public List<String> getDrinkNames() {
            return new ArrayList<String>(drinks.keySet());
        }

        public Integer getDrinkStrength(String drinkName) {
            return drinks.get(drinkName);
        }

        @Override
        public IEvent getChainedEvent() {
            return this.chainedEvent;
        }

        @Override
        public void setChainedEvent(IEvent event) {
            this.chainedEvent = event;
        }
    }


}
