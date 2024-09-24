package ca.quadrilateral.jua.game.eventhandler;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.quadrilateral.jua.game.IEvent;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.entity.IAbility;
import ca.quadrilateral.jua.game.entity.impl.EntityAttribute;
import ca.quadrilateral.jua.game.entity.impl.PartyMember;
import ca.quadrilateral.jua.game.enums.EntityAbilityType;
import ca.quadrilateral.jua.game.enums.EntityAttributeType;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.enums.WhoTriesCheckType;
import ca.quadrilateral.jua.game.impl.DiceRoller;
import ca.quadrilateral.jua.game.impl.event.PostEventAction;
import ca.quadrilateral.jua.game.impl.event.WhoTriesEvent;

public class WhoTriesEventHandler extends TextEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(WhoTriesEventHandler.class);
    
    private static final int EVENT_STAGE_BEGIN_WHO_TRIES_EVENT = 5;

    private static final int EVENT_STAGE_AFTER_RENDER_HANDLE_FAILURE = 40;
    private static final int EVENT_STAGE_AFTER_RENDER_HANDLE_SUCCESS = 50;
    private static final int EVENT_STAGE_RENDER_RESULT_TEXT = 90;
    private static final int EVENT_STAGE_RENDER_RESULT_TEXT_WAIT_FOR_CONFIRMATION = 91;

    private static final int EVENT_STAGE_RENDER_TRY_AGAIN_TEXT = 100;
    private static final int EVENT_STAGE_WAIT_FOR_ENTER_AFTER_TRY_AGAIN_TEXT = 101;

    private static final int EVENT_STAGE_RENDER_FAILURE_TEXT = 110;
    private static final int EVENT_STAGE_WAIT_FOR_ENTER_AFTER_FAILURE_TEXT = 111;

    private static final int EVENT_STAGE_HANDLE_FAILURE_POST_EVENT = 112;
    private static final int EVENT_STAGE_SET_WAIT_FOR_ENTER_AFTER_FAILURE_TEXT = 113;

    private int afterRenderStageValue = 0;
    private int unsuccessfulAttemptsCounter = 0;

    @Override
    protected void setPostTextEventState() {
        eventStage = EVENT_STAGE_BEGIN_WHO_TRIES_EVENT;
    }

    @Override
    public void initializeEvent(IEvent event) {
        super.initializeEvent(event);
        afterRenderStageValue = 0;
        unsuccessfulAttemptsCounter = 0;
    }

    @Override
    public boolean canHandle(IEvent event) {
        return event.getEventType().equals(EventType.WhoTriesEvent);
    }

    @Override
    public IEvent runEvent() {
        final WhoTriesEvent whoTriesEvent = (WhoTriesEvent) super.event;

        super.runEvent();

        if (eventStage == EVENT_STAGE_BEGIN_WHO_TRIES_EVENT) {
            this.gameStateMachine.waitForSelectInput();
            logger.debug("Waiting for select input");
            eventStage = 6;
        }

        if (eventStage == 6) {
            final String input = fetchInput(IGameStateMachine.GAME_STATE_GET_SELECT_INPUT, true, 7);
        }

        if (eventStage == 7) {
            if (doesActionSucceed(whoTriesEvent)) {
                eventStage = 10;
            } else {
                eventStage = 20;
            }
            textRenderer.clear();
        }

        if (eventStage == 10) {
            logger.debug("Who Tries Event Succeeded");
            final String whoTriesText = whoTriesEvent.getOnSuccessText();
            if (!StringUtils.isBlank(whoTriesText)) {
                textRenderer.addText(scriptManager.processVariableReplacement(whoTriesText));
                afterRenderStageValue = EVENT_STAGE_AFTER_RENDER_HANDLE_SUCCESS;
                eventStage = EVENT_STAGE_RENDER_RESULT_TEXT;
            } else {
                eventStage = EVENT_STAGE_AFTER_RENDER_HANDLE_SUCCESS;
            }
        }

        if (eventStage == 20) {
            logger.debug("Who Tries Event Failed");
            unsuccessfulAttemptsCounter++;
            eventStage = EVENT_STAGE_AFTER_RENDER_HANDLE_FAILURE;
        }

        if (eventStage == EVENT_STAGE_RENDER_RESULT_TEXT && textRenderer.isDoneRendering()) {
            gameStateMachine.waitForEnterInput();
            eventStage = EVENT_STAGE_RENDER_RESULT_TEXT_WAIT_FOR_CONFIRMATION;
        }

        if (eventStage == EVENT_STAGE_RENDER_RESULT_TEXT_WAIT_FOR_CONFIRMATION) {
            final String input = fetchInput(IGameStateMachine.GAME_STATE_GET_ENTER_INPUT, true, afterRenderStageValue);
        }

        if (eventStage == EVENT_STAGE_AFTER_RENDER_HANDLE_SUCCESS) {
            final PostEventAction afterSuccessAction = whoTriesEvent.getAfterSuccessAction();
            handlePostEventAction(afterSuccessAction);
        }

        if (eventStage == EVENT_STAGE_AFTER_RENDER_HANDLE_FAILURE) {
            logger.debug("Running after failure");
            if (unsuccessfulAttemptsCounter >= whoTriesEvent.getNumberOfAttempts()) {
                eventStage = EVENT_STAGE_RENDER_FAILURE_TEXT;
            } else {
                logger.debug("Attempt is failed... should try again");
                textRenderer.clear();
                final String tryAgainText = whoTriesEvent.getTryAgainText();
                if (!StringUtils.isBlank(tryAgainText)) {
                    textRenderer.addText(scriptManager.processVariableReplacement(tryAgainText));
                    eventStage = EVENT_STAGE_RENDER_TRY_AGAIN_TEXT;
                } else {
                    eventStage = EVENT_STAGE_START;
                }
                
            }
        }

        if (eventStage == EVENT_STAGE_RENDER_FAILURE_TEXT) {
            textRenderer.clear();
            final String failureText = whoTriesEvent.getOnFailureText();
            if (!StringUtils.isBlank(failureText)) {
                textRenderer.addText(scriptManager.processVariableReplacement(failureText));
                eventStage = EVENT_STAGE_SET_WAIT_FOR_ENTER_AFTER_FAILURE_TEXT;
            } else {
                eventStage = EVENT_STAGE_HANDLE_FAILURE_POST_EVENT;
            }
        }

        if (eventStage == EVENT_STAGE_SET_WAIT_FOR_ENTER_AFTER_FAILURE_TEXT && textRenderer.isDoneRendering()) {
            gameStateMachine.waitForEnterInput();
            eventStage = EVENT_STAGE_WAIT_FOR_ENTER_AFTER_FAILURE_TEXT;
        }

        if (eventStage == EVENT_STAGE_WAIT_FOR_ENTER_AFTER_FAILURE_TEXT) {
            final String input = fetchInput(IGameStateMachine.GAME_STATE_GET_ENTER_INPUT, true, EVENT_STAGE_HANDLE_FAILURE_POST_EVENT);
        }

        if (eventStage == EVENT_STAGE_HANDLE_FAILURE_POST_EVENT) {
            final PostEventAction afterFailureAction = whoTriesEvent.getAfterFailureAction();
            handlePostEventAction(afterFailureAction);
        }


        if (eventStage == EVENT_STAGE_RENDER_TRY_AGAIN_TEXT && textRenderer.isDoneRendering()) {
            this.gameStateMachine.waitForEnterInput();
            eventStage = EVENT_STAGE_WAIT_FOR_ENTER_AFTER_TRY_AGAIN_TEXT;
        }

        if (eventStage == EVENT_STAGE_WAIT_FOR_ENTER_AFTER_TRY_AGAIN_TEXT) {
            final String input = fetchInput(IGameStateMachine.GAME_STATE_GET_ENTER_INPUT, true, EVENT_STAGE_START);
        }

        return null;
    }

    private void handlePostEventAction(PostEventAction action) {
        final IEvent chainedEvent = action.getChainedEvent();
        logger.debug("Post Event Action Chain: {}", chainedEvent.toString());
        if (action.isBackupOneStepWhenDone()) {
            gameStateMachine.setRevertPartyPositionAfterChain(true);
            gameStateMachine.clearReturnEventStack();
        }
        if (chainedEvent != null) {
            gameStateMachine.pushQueuedEvent(chainedEvent);
        }
        this.isDone = true;
        this.eventStage = EVENT_STAGE_DONE_EVENT_HAPPENED;
    }

    private boolean doesActionSucceed(WhoTriesEvent whoTriesEvent) {
        final int selectedPartyMemberIndex = gameStateMachine.getSelectedPartyMemberIndex();
        final PartyMember partyMember = levelContext.getParty().getPartyMember(selectedPartyMemberIndex);
        
        final WhoTriesCheckType checkType = whoTriesEvent.getWhoTriesCheckType();
        
        if (checkType.equals(WhoTriesCheckType.AlwaysSucceeds)) {
            return true;
        } else if (checkType.equals(WhoTriesCheckType.AlwaysFails)) {
            return false;
        } else if (checkType.equals(WhoTriesCheckType.MustHave)) {
            return doesMustHaveSucceed(whoTriesEvent, partyMember);
        } else if (checkType.equals(WhoTriesCheckType.DiceRoll)) {
            return doesDiceRollSucceed(whoTriesEvent, partyMember);
        }

        return false;
    }

    private boolean doesMustHaveSucceed(WhoTriesEvent whoTriesEvent, PartyMember partyMember) {
        int characterPrimaryValue = getTargetValue(whoTriesEvent, partyMember);
        if (whoTriesEvent.getCheckAgainstAbility() != null) {
            return characterPrimaryValue >= whoTriesEvent.getPrimaryCheckValue();
        } else if (whoTriesEvent.getCheckAgainstAttributeType() != null) {
            int characterPartialValue = getPartialTargetValue(whoTriesEvent, partyMember);
            int checkValue = whoTriesEvent.getPrimaryCheckValue();
            int partialCheckValue = whoTriesEvent.getPartialCheckValue();

            if (characterPrimaryValue > checkValue) {
                return true;
            } else if (characterPrimaryValue < checkValue) {
                return false;
            } else {
                return characterPartialValue >= partialCheckValue;
            }
        }

        assert(false);
        return false;
    }

    private boolean doesDiceRollSucceed(WhoTriesEvent whoTriesEvent, PartyMember partyMember) {
        final int targetValue = getTargetValue(whoTriesEvent, partyMember);
        final int rolledValue = new DiceRoller().roll(whoTriesEvent.getDiceExpression());
        return rolledValue <= targetValue;
    }

    private int getPartialTargetValue(WhoTriesEvent whoTriesEvent, PartyMember partyMember) {
        if (whoTriesEvent.getCheckAgainstAttributeType() != null) {
            final EntityAttributeType checkAgainstAttribute = whoTriesEvent.getCheckAgainstAttributeType();
            final EntityAttribute entityAttribute = partyMember.getEntityAttribute(checkAgainstAttribute);
            return entityAttribute.getEffectivePartialValue();
        } else {
            return 0;
        }
    }

    private int getTargetValue(WhoTriesEvent whoTriesEvent, PartyMember partyMember) {
        if (whoTriesEvent.getCheckAgainstAbility() != null) {
            final EntityAbilityType checkAgainstAbility = whoTriesEvent.getCheckAgainstAbility();
            final IAbility ability = partyMember.getAbility(checkAgainstAbility);
            if (ability == null) {
                return 0;
            } else {
                return ability.getAbilityValue();
            }
        } else if (whoTriesEvent.getCheckAgainstAttributeType() != null) {
            final EntityAttributeType checkAgainstAttribute = whoTriesEvent.getCheckAgainstAttributeType();
            final EntityAttribute entityAttribute = partyMember.getEntityAttribute(checkAgainstAttribute);
            return entityAttribute.getEffectiveValue();
        }

        assert(false);
        return 0;
    }
}
