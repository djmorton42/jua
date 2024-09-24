package ca.quadrilateral.jua.game.impl;

import java.text.MessageFormat;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.quadrilateral.jua.game.IDiceRoller;

public class DiceRoller implements IDiceRoller {
    private static final Logger logger = LoggerFactory.getLogger(DiceRoller.class);
    
    private static final Random random = new Random();


    @Override
    public int roll(DiceExpression expression) {
        int result = 0;
        for(int i = 0; i < expression.getNumberOfDice(); i++) {
            result += (random.nextInt(expression.getSidesOfDice()) + 1);
        }

        result += expression.getModifier();

        logger.debug("Dice Roll ({}) => {}", expression.toString(), result);
        
        return result;
    }

}
