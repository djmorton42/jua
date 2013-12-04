package ca.quadrilateral.jua.game.impl;

import junit.framework.Assert;
import org.junit.Test;

public class TestDiceExpression {

    @Test
    public void test_DiceExpression_parse_should_handle_basic_single_die_value() {
        final DiceExpression diceExpression = DiceExpression.parse("1d10");
        Assert.assertEquals(1, diceExpression.getNumberOfDice());
        Assert.assertEquals(10, diceExpression.getSidesOfDice());
        Assert.assertEquals(0, diceExpression.getModifier());
    }

    @Test
    public void test_DiceExpression_parse_should_handle_basic_single_die_value_with_positive_modifier() {
        final DiceExpression diceExpression = DiceExpression.parse("1d10+2");
        Assert.assertEquals(1, diceExpression.getNumberOfDice());
        Assert.assertEquals(10, diceExpression.getSidesOfDice());
        Assert.assertEquals(2, diceExpression.getModifier());
    }

    @Test
    public void test_DiceExpression_parse_should_handle_basic_single_die_value_with_negative_modifier() {
        final DiceExpression diceExpression = DiceExpression.parse("1d10-2");
        Assert.assertEquals(1, diceExpression.getNumberOfDice());
        Assert.assertEquals(10, diceExpression.getSidesOfDice());
        Assert.assertEquals(-2, diceExpression.getModifier());
    }

    @Test
    public void test_DiceExpression_parse_should_handle_complex_value() {
        final DiceExpression diceExpression = DiceExpression.parse("3d6+4");
        Assert.assertEquals(3, diceExpression.getNumberOfDice());
        Assert.assertEquals(6, diceExpression.getSidesOfDice());
        Assert.assertEquals(4, diceExpression.getModifier());
    }
}