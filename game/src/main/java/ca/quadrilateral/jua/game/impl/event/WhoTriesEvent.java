package ca.quadrilateral.jua.game.impl.event;

import ca.quadrilateral.jua.game.enums.EntityAbilityType;
import ca.quadrilateral.jua.game.enums.EntityAttributeType;
import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.enums.WhoTriesCheckType;
import ca.quadrilateral.jua.game.impl.DiceExpression;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WhoTriesEvent extends SuccessFailureEvent {
    private EntityAttributeType checkAgainstAttributeType = null;
    private EntityAbilityType checkAgainstAbility = null;
    private DiceExpression diceExpression = null;
    private WhoTriesCheckType whoTriesCheckType = null;
    private int primaryCheckValue = 0;
    private int partialCheckValue = 0;

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendToString(super.toString());
        builder.append("Check Type", whoTriesCheckType);
        if (whoTriesCheckType.equals(WhoTriesCheckType.MustHave)) {
            builder.append("Check Against", getCheckAgainstPropertyString());
            builder.append("Primary Check Value", primaryCheckValue);
            builder.append("Partial Cehck Value", partialCheckValue);
        } else if (whoTriesCheckType.equals(WhoTriesCheckType.DiceRoll)) {
            builder.append("Check Against", getCheckAgainstPropertyString());
            builder.append("Dice Expression", diceExpression.toString());
        }
        return builder.toString();
    }

  	@Override
	public EventType getEventType() {
		return EventType.WhoTriesEvent;
	}


    private String getCheckAgainstPropertyString() {
        if (checkAgainstAttributeType != null) {
            return checkAgainstAttributeType.getText();
        } else if (checkAgainstAbility != null) {
            return checkAgainstAbility.getText();
        } else {
            return "";
        }
    }

    public EntityAbilityType getCheckAgainstAbility() {
        return checkAgainstAbility;
    }

    public void setCheckAgainstAbility(EntityAbilityType checkAgainstAbility) {
        this.checkAgainstAbility = checkAgainstAbility;
    }

    public EntityAttributeType getCheckAgainstAttributeType() {
        return checkAgainstAttributeType;
    }

    public void setCheckAgainstAttributeType(EntityAttributeType checkAgainstAttributeType) {
        this.checkAgainstAttributeType = checkAgainstAttributeType;
    }

    public DiceExpression getDiceExpression() {
        return diceExpression;
    }

    public void setDiceExpression(DiceExpression diceExpression) {
        this.diceExpression = diceExpression;
    }

    public int getPartialCheckValue() {
        return partialCheckValue;
    }

    public void setPartialCheckValue(int partialCheckValue) {
        this.partialCheckValue = partialCheckValue;
    }

    public int getPrimaryCheckValue() {
        return primaryCheckValue;
    }

    public void setPrimaryCheckValue(int primayCheckValue) {
        this.primaryCheckValue = primayCheckValue;
    }

    public WhoTriesCheckType getWhoTriesCheckType() {
        return whoTriesCheckType;
    }

    public void setWhoTriesCheckType(WhoTriesCheckType whoTriesCheckType) {
        this.whoTriesCheckType = whoTriesCheckType;
    }
}
