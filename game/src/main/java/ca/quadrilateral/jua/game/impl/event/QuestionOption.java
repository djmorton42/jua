package ca.quadrilateral.jua.game.impl.event;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class QuestionOption {
    private String optionLabel = null;
    private PostEventAction postQuestionEventAction = null;

    public QuestionOption() {}

    public QuestionOption(String optionLabel, PostEventAction postQuestionEventAction) {
        this.optionLabel = optionLabel;
        this.postQuestionEventAction = postQuestionEventAction;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("Option Label", optionLabel).appendToString(postQuestionEventAction.toString()).toString();
    }

    public String getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public PostEventAction getPostQuestionEventAction() {
        return postQuestionEventAction;
    }

    public void setPostQuestionEventAction(PostEventAction postQuestionEventAction) {
        this.postQuestionEventAction = postQuestionEventAction;
    }

    
}
