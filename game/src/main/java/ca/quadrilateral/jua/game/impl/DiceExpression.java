package ca.quadrilateral.jua.game.impl;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ca.quadrilateral.jua.game.exception.JUARuntimeException;

public class DiceExpression {
    private static final Pattern expressionPattern = Pattern.compile("([0-9]{1,2})d([0-9]{1,10})([\\+|\\-][0-9]{1,6})", Pattern.CASE_INSENSITIVE);
    private static final Pattern constantPattern = Pattern.compile("[0-9]{1,6}");

    public static final DiceExpression D100 = DiceExpression.parse("1d100+0");
    public static final DiceExpression D20 = DiceExpression.parse("1d20+0");
    public static final DiceExpression D12 = DiceExpression.parse("1d12+0");
    public static final DiceExpression D10 = DiceExpression.parse("1d10+0");
    public static final DiceExpression D6 = DiceExpression.parse("1d6+0");
    public static final DiceExpression D4 = DiceExpression.parse("1d4+0");


    private int numberOfDice = 0;
    private int sidesOfDice = 0;
    private int modifier = 0;

    public static DiceExpression parse(String expression) {
        String expressionToParse = expression;

        if (constantPattern.matcher(expressionToParse).matches()) {
            return new DiceExpression(0, 0, Integer.parseInt(expressionToParse));
        }

        if (!expression.contains("+") && !expression.contains("-")) {
            expressionToParse = expressionToParse + "+0";
        }

        final Matcher matcher = expressionPattern.matcher(expressionToParse);
        if (!matcher.matches() || matcher.groupCount() != 3) {
            throw new JUARuntimeException(MessageFormat.format("{0} is not a valid dice expression", expression));
        }

        return new DiceExpression(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3).replace("+", "")));
    }

    public DiceExpression(int numberOfDice, int sidesOfDice, int modifier) {
        this.numberOfDice = numberOfDice;
        this.sidesOfDice = sidesOfDice;
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        return MessageFormat.format("DiceExpression: {0}d{1}{2}{3}", numberOfDice, sidesOfDice, modifier >= 0 ? "+" : "" , modifier);
    }

    public boolean isConstantExpression() {
        return numberOfDice == 0 && sidesOfDice == 0;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public int getNumberOfDice() {
        return numberOfDice;
    }

    public void setNumberOfDice(int numberOfDice) {
        this.numberOfDice = numberOfDice;
    }

    public int getSidesOfDice() {
        return sidesOfDice;
    }

    public void setSidesOfDice(int sidesOfDice) {
        this.sidesOfDice = sidesOfDice;
    }



}
