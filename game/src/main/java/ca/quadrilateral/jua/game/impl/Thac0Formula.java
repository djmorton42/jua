package ca.quadrilateral.jua.game.impl;

import ca.quadrilateral.jua.game.exception.JUARuntimeException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Thac0Formula {
    private static final Pattern thac0FormulaPattern = Pattern.compile("([0-9])/([0-9])");
    private int pointChange = 0;
    private int everyNumberOfLevels = 0;


    public Thac0Formula(int pointChange, int everyNumberOfLevels) {
        setParameters(pointChange, everyNumberOfLevels);
    }

    public Thac0Formula(String formula) {
        final Matcher matcher = thac0FormulaPattern.matcher(formula);
        if (matcher.matches()) {
            setParameters(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
        } else {
            throw new JUARuntimeException("Illegal Thac0 Formula! " + formula);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("Formula", pointChange + "/" + everyNumberOfLevels).toString();
    }

    private void setParameters(int pointChange, int everyNumberOfLevels) {
        this.pointChange = pointChange;
        this.everyNumberOfLevels = everyNumberOfLevels;
    }

    public int getThac0ForLevel(int level) {
        return 20 - (pointChange * ((level - 1) / everyNumberOfLevels));
    }

}
