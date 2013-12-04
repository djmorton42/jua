package ca.quadrilateral.jua.game.eventhandler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IEventHandler;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IOptionRenderer;
import ca.quadrilateral.jua.game.entity.IParty;
import ca.quadrilateral.jua.game.entity.impl.PartyMember;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.Purse;
import ca.quadrilateral.jua.game.impl.event.GiveTreasureEvent;
import ca.quadrilateral.jua.game.item.impl.ItemCollection;

public class GiveTreasureEventHandler extends TextEventHandler {
	private static final Logger log = LoggerFactory.getLogger(GiveTreasureEventHandler.class);
	
	private static final int EVENT_STAGE_BEGIN_GIVE_TREASURE_EVENT = 5;
	private static final int EVENT_STAGE_GIVE_TREASURE_EVENT_MAIN_OPTION = 10;
	
	private static final int EVENT_STAGE_CONFIRM_EXIT = 500;
	
	@Autowired
	private ILevelContext levelContext;
	
    @Autowired
    private IOptionRenderer optionRenderer;
    
    private ItemCollection treasureItems = null;
    private Purse treasurePurse = null;

    private boolean hasItemsToTake = false;
    private boolean hasCurrencyToTake = false;
    
    private boolean hasItemsToTake() {
    	return treasureItems.size() > 0;
    }
    
    private boolean hasCurrencyToTake() {
    	return treasurePurse.getCurrenciesInPurse().size() > 0;
    }
    
    private void updateHasItemsAndCurrencyVariables() {
    	hasItemsToTake = hasItemsToTake();
    	hasCurrencyToTake = hasCurrencyToTake();
    }
    
    private void poolPartyMoney() {
    	final IParty party = levelContext.getParty();
    	//TODO:  Give treasure only to PCs
    	for(int i = 0; i < party.getPartyMemberCount(); i++) {
    		final PartyMember partyMember = party.getPartyMember(i);
    	
    		final Purse partyMemberPurse = partyMember.getPurse();
    		
    		treasurePurse.addPurse(partyMemberPurse);
    		partyMemberPurse.clear();
    	}
    }
    
    private void sharePartyMoney() {
    	final IParty party = levelContext.getParty();
    	//TODO:  Only give treasure to PCs
    	List<Purse> subdividedPurse = this.treasurePurse.subdividePurse(party.getPartyMemberCount(), true);
    	
    	for(int i = 0; i < party.getPartyMemberCount(); i++) {
    		party.getPartyMember(i).getPurse().addPurse(subdividedPurse.get(i));
    	}
    }
    
    private List<String> generateMainGiveTreasureMenuOptions() {
    	final List<String> optionStrings = new ArrayList<String>(0x05);
    	
    	if (hasItemsToTake) {
    		optionStrings.add("Items");        		
    	}	
    	
    	if (hasCurrencyToTake) {
    		optionStrings.add("Money");
    		optionStrings.add("Share");
    	}
    	
    	optionStrings.add("Pool");
    	optionStrings.add("Leave");

    	return optionStrings;
    }
    
    @Override
    public IEvent runEvent() {
        final GiveTreasureEvent giveTreasureEvent = (GiveTreasureEvent) super.event;

        super.runEvent();                
        
        if (eventStage == EVENT_STAGE_BEGIN_GIVE_TREASURE_EVENT) {
        	treasureItems = giveTreasureEvent.getTreasureItems();
        	treasurePurse = giveTreasureEvent.getTreasurePurse();
        	updateHasItemsAndCurrencyVariables();
        	
        	optionRenderer.setOptions(generateMainGiveTreasureMenuOptions());
            super.gameStateMachine.waitForButtonInput();
            eventStage = EVENT_STAGE_GIVE_TREASURE_EVENT_MAIN_OPTION;        	
        }
        
        if (eventStage == EVENT_STAGE_GIVE_TREASURE_EVENT_MAIN_OPTION) {
        	final String input = fetchInput(IGameStateMachine.GAME_STATE_GET_BUTTON_INPUT, false, null);
        	if (input != null) {
        		if (input.equalsIgnoreCase("Items")) {
        			
        		} else if (input.equalsIgnoreCase("Money")) {
        			
        		} else if (input.equalsIgnoreCase("Share")) {
        			sharePartyMoney();
        			updateHasItemsAndCurrencyVariables();
        		} else if (input.equalsIgnoreCase("Pool")) {
        			poolPartyMoney();
        			updateHasItemsAndCurrencyVariables();
        		} else if (input.equalsIgnoreCase("Leave")) {
        			eventStage = IEventHandler.EVENT_STAGE_DONE_EVENT_HAPPENED;
        		}
        	}
        }

        /*
        if (eventStage == EVENT_STAGE_BEGIN_TAVERN_EVENT) {
            final List<String> optionStrings = new ArrayList<String>(0x04);

            if (tavernEvent.getTavernDrink().isAllowDrinks()) {
                optionStrings.add("Drink");
            }
            optionStrings.add("Talk");
            if (tavernEvent.getTavernFight().isAllowFights()) {
                optionStrings.add("Fight");
            }
            optionStrings.add("Leave");

            optionRenderer.setOptions(optionStrings);

            super.gameStateMachine.waitForButtonInput();
            eventStage = EVENT_STAGE_TAVERN_OPTION_QUESTION;
        }

   		if (eventStage == EVENT_STAGE_TAVERN_OPTION_QUESTION) {
			final String input = fetchInput(IGameStateMachine.GAME_STATE_GET_BUTTON_INPUT, true, null);
			if (input != null) {
                if (input.equalsIgnoreCase("Drink")) {
                    eventStage = EVENT_STAGE_DRINK_NAME_OPTION_QUESTION;
                } else if (input.equalsIgnoreCase("Talk")) {
                    eventStage = EVENT_STAGE_TAVERN_TALES_START;
                } else if (input.equalsIgnoreCase("Fight")) {
                    eventStage = EVENT_STAGE_FIGHT_START;
                } else if (input.equalsIgnoreCase("Leave")) {
                    eventStage = IEventHandler.EVENT_STAGE_DONE_EVENT_HAPPENED;
                }
            }
        }

        if (eventStage == EVENT_STAGE_DRINK_NAME_OPTION_QUESTION) {
            if (!tavernEvent.getTavernDrink().getDrinkNames().isEmpty()) {
                optionRenderer.setOptions(tavernEvent.getTavernDrink().getDrinkNames());
                super.gameStateMachine.waitForButtonInput();
                eventStage = 11;
            } else {
                this.textRenderer.addText("The party has a drink.");
                eventStage = 18;
            }
        }
        
        if (eventStage == 11) {
            final String input = fetchInput(IGameStateMachine.GAME_STATE_GET_BUTTON_INPUT, true, 12);
            if (input != null) {
                drinkNameConsumed = input;
                drinkPointsConsumed += tavernEvent.getTavernDrink().getDrinkStrength(input);
            }
        }

        if (eventStage == 12) {
            final Map<String, String> eventBindings = new HashMap<String, String>();
            eventBindings.put("drinkName", this.drinkNameConsumed);

            log.debug("Event Binding: " + eventBindings.get("drinkName"));
            
            if (!StringUtils.isBlank(tavernEvent.getTavernDrink().getDrinkText())) {
                final String resultText = scriptManager.processVariableReplacement(tavernEvent.getTavernDrink().getDrinkText(), eventBindings);
                this.textRenderer.addText(resultText);
                eventStage = 13;
            } else {
                eventStage = 17;
            }
        }

        if (eventStage == 13 && this.textRenderer.isDoneRendering()) {
            eventStage = 14;
        }

        if (eventStage == 14) {
            if (hasHitDrunkThreshold(tavernEvent)) {
                final IEvent fightEvent = tavernEvent.getTavernDrink().getChainedEvent();
                if (fightEvent != null) {
                    this.gameStateMachine.waitForEnterInput();
                    eventStage = 15;
                } else {
                    eventStage = EVENT_STAGE_BEGIN_TAVERN_EVENT;
                }
            } else {
                eventStage = EVENT_STAGE_BEGIN_TAVERN_EVENT;
            }
        }

        if (eventStage == 15) {
   			final String input = fetchInput(IGameStateMachine.GAME_STATE_GET_ENTER_INPUT, true, null);
			if (input != null) {								                
                gameStateMachine.pushQueuedEvent(tavernEvent.getTavernDrink().getChainedEvent());
                this.isDone = true;
            }
        }

        if (eventStage == 17) {
            if (hasHitDrunkThreshold(tavernEvent)) {
                final IEvent fightEvent = tavernEvent.getTavernDrink().getChainedEvent();
                if (fightEvent != null) {
                    gameStateMachine.pushQueuedEvent(fightEvent);
                    this.isDone = true;
                } else {
                    eventStage = EVENT_STAGE_BEGIN_TAVERN_EVENT;
                }
            } else {
                eventStage = EVENT_STAGE_BEGIN_TAVERN_EVENT;
            }
        }


        if (eventStage == 18 && this.textRenderer.isDoneRendering()) {
            eventStage = EVENT_STAGE_BEGIN_TAVERN_EVENT;
        }

        if (eventStage == EVENT_STAGE_TAVERN_TALES_START) {
            textRenderer.clear();
            eventStage = EVENT_STAGE_TAVERN_TALES_SHOW_TAVERN_TALE;
        }

        if (eventStage == EVENT_STAGE_TAVERN_TALES_SHOW_TAVERN_TALE) {
            if (!tavernEvent.getTavernTales().isEmpty()) {
                this.textRenderer.addText(scriptManager.processVariableReplacement(tavernEvent.getNextTavernTale()));
            } else {
                this.textRenderer.addText("You talk to the locals.");
            }
            eventStage = 22;
        }

        if (eventStage == 22 && this.textRenderer.isDoneRendering()) {
            eventStage = 23;
        }

        if (eventStage == 23) {
            this.gameStateMachine.waitForEnterInput();
            eventStage = 24;
        }

        if (eventStage == 24) {
			final String input = fetchInput(IGameStateMachine.GAME_STATE_GET_ENTER_INPUT, true, EVENT_STAGE_BEGIN_TAVERN_EVENT);
        }

        if (eventStage == EVENT_STAGE_FIGHT_START) {
            final IEvent fightEvent = tavernEvent.getTavernFight().getChainedEvent();
            if (fightEvent != null) {
                gameStateMachine.pushQueuedEvent(fightEvent);
                this.isDone = true;
            } else {
                eventStage = 31;
            }
        }

        if (eventStage == 31) {
            this.textRenderer.addText("Everyone runs away.  There's no one to fight!");
            eventStage = 32;
        }

        if (eventStage == 32 && this.textRenderer.isDoneRendering()) {
            eventStage = EVENT_STAGE_BEGIN_TAVERN_EVENT;
        }

        */
        return null;
    }

    
    @Override
    public boolean canHandle(IEvent event) {
        return event.getEventType().equals(EventType.GiveTreasureEvent);
    }
	
    @Override
    protected void setPostTextEventState() {
    	log.debug("Setting Post Text Event State " + EVENT_STAGE_BEGIN_GIVE_TREASURE_EVENT);
        eventStage = EVENT_STAGE_BEGIN_GIVE_TREASURE_EVENT;
    }
	
}
