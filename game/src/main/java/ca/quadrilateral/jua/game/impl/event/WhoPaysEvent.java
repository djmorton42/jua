package ca.quadrilateral.jua.game.impl.event;

import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.impl.Currency;
import org.apache.commons.lang.builder.ToStringBuilder;


public class WhoPaysEvent  extends SuccessFailureEvent {
    private Currency requiredCurrency;
    private boolean willChangeMoney = false;


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendToString(super.toString());

        builder.append("Required Currency", requiredCurrency);
        builder.append("Will Change Money", willChangeMoney);

        return builder.toString();
    }

  	@Override
	public EventType getEventType() {
		return EventType.WhoPaysEvent;
	}

    public Currency getRequiredCurrency() {
        return requiredCurrency;
    }

    public void setRequiredCurrency(Currency requiredCurrency) {
        this.requiredCurrency = requiredCurrency;
    }

    public boolean isWillChangeMoney() {
        return willChangeMoney;
    }

    public void setWillChangeMoney(boolean willChangeMoney) {
        this.willChangeMoney = willChangeMoney;
    }

}

