package ca.quadrilateral.jua.game;

import ca.quadrilateral.jua.game.impl.DiceExpression;

public interface IDiceRoller {
    int roll(DiceExpression expression);
}
