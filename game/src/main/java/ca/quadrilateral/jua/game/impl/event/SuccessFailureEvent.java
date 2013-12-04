package ca.quadrilateral.jua.game.impl.event;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SuccessFailureEvent extends TextEvent {
    private int numberOfAttempts = 1;
    private String onSuccessText = "";
    private String onFailureText = "";
    private String tryAgainText = "";

 	private PostEventAction afterSuccessAction = new PostEventAction();
	private PostEventAction afterFailureAction = new PostEventAction();

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);

        builder.appendToString(super.toString());
        builder.append("Number of Attempts", numberOfAttempts);

        builder.append("On Success Text", onSuccessText).append("On Failure Text", onFailureText).append("Try Again Text", tryAgainText);
        builder.appendToString(afterSuccessAction.toString()).appendToString(afterFailureAction.toString());

        return builder.toString();
    }

    public PostEventAction getAfterFailureAction() {
        return afterFailureAction;
    }

    public void setAfterFailureAction(PostEventAction afterFailureAction) {
        this.afterFailureAction = afterFailureAction;
    }

    public PostEventAction getAfterSuccessAction() {
        return afterSuccessAction;
    }

    public void setAfterSuccessAction(PostEventAction afterSuccessAction) {
        this.afterSuccessAction = afterSuccessAction;
    }

    public int getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public void setNumberOfAttempts(int numberOfAttempts) {
        this.numberOfAttempts = numberOfAttempts;
    }

    public String getOnFailureText() {
        return onFailureText;
    }

    public void setOnFailureText(String onFailureText) {
        this.onFailureText = onFailureText;
    }

    public String getOnSuccessText() {
        return onSuccessText;
    }

    public void setOnSuccessText(String onSuccessText) {
        this.onSuccessText = onSuccessText;
    }

    public String getTryAgainText() {
        return tryAgainText;
    }

    public void setTryAgainText(String tryAgainText) {
        this.tryAgainText = tryAgainText;
    }

}
