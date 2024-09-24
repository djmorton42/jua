package ca.quadrilateral.jua.game.eventhandler;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IEventHandler;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.IOptionRenderer;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.event.TavernEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TavernEventHandler extends TextEventHandler {
	private static final Logger log = LoggerFactory.getLogger(TavernEventHandler.class);
	
    @Autowired
    private IOptionRenderer optionRenderer;

    private int drinkPointsConsumed = 0;
    private String drinkNameConsumed = null;

    private static final int EVENT_STAGE_BEGIN_TAVERN_EVENT = 5;
    private static final int EVENT_STAGE_TAVERN_OPTION_QUESTION = 6;

    private static final int EVENT_STAGE_DRINK_NAME_OPTION_QUESTION = 10;

    private static final int EVENT_STAGE_TAVERN_TALES_START = 20;
    private static final int EVENT_STAGE_TAVERN_TALES_SHOW_TAVERN_TALE = 21;

    private static final int EVENT_STAGE_FIGHT_START = 30;

    @Override
    public boolean canHandle(IEvent event) {
        return event.getEventType().equals(EventType.TavernEvent);
    }

    @Override
    public IEvent runEvent() {
        final TavernEvent tavernEvent = (TavernEvent) super.event;

        super.runEvent();

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

        
        return null;
    }

    private boolean hasHitDrunkThreshold(TavernEvent tavernEvent) {
        return this.drinkPointsConsumed >= tavernEvent.getTavernDrink().getDrunkThreshold();
    }

    @Override
    protected void setPostTextEventState() {
    	log.debug("Setting Post Text Event State " + EVENT_STAGE_BEGIN_TAVERN_EVENT);
        eventStage = EVENT_STAGE_BEGIN_TAVERN_EVENT;
    }





}
