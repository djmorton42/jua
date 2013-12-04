package ca.quadrilateral.jua.game.impl;

import ca.quadrilateral.jua.game.IDiceRoller;
import java.text.MessageFormat;
import java.util.Random;

public class DiceRoller implements IDiceRoller {
    private static final Random random = new Random();


    @Override
    public int roll(DiceExpression expression) {
        int result = 0;
        for(int i = 0; i < expression.getNumberOfDice(); i++) {
            result += (random.nextInt(expression.getSidesOfDice()) + 1);
        }

        result += expression.getModifier();

        System.out.println(MessageFormat.format("Dice Roll ({0}) => {1}", expression.toString(), result));
        
        return result;
    }

}
