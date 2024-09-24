package ca.quadrilateral.jua.game.impl.event;

import ca.quadrilateral.jua.game.enums.EventType;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class QuestionButtonEvent extends AbstractEvent {
    private String questionText = null;
    private List<QuestionOption> questionOptions = new ArrayList<QuestionOption>();

    public QuestionButtonEvent() {}

    public QuestionButtonEvent(String questionText, List<QuestionOption> questionOptions) {
        this.questionText = questionText;
        this.questionOptions = questionOptions;
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this)
                                                .appendToString(super.toString())
                                                .append("Question Text", questionText);
        for(QuestionOption option : questionOptions) {
            builder.appendToString(option.toString());
        }
        return builder.toString();
    }

    public QuestionOption getQuestionOptionForLabel(String label) {
        for(QuestionOption option : questionOptions) {
            if (option.getOptionLabel().equals(label)) {
                return option;
            }
        }
        return null;
    }

    public List<QuestionOption> getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(List<QuestionOption> questionOptions) {
        this.questionOptions = questionOptions;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @Override
    public EventType getEventType() {
        return EventType.QuestionButtonEvent;
    }

}
