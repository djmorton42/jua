package ca.quadrilateral.jua.game.entity.impl;

import ca.quadrilateral.jua.game.impl.DiceExpression;
import junit.framework.Assert;
import org.junit.Test;



public class TestCharacterClass {

    private CharacterClass getFighterCharacterClass() {
        final CharacterClass fighterClass = new CharacterClass("Fighter");

        fighterClass.addLevelRank(new LevelRank(1, 0, DiceExpression.parse("1d10")));
        fighterClass.addLevelRank(new LevelRank(2, 2000, DiceExpression.parse("1d10")));
        fighterClass.addLevelRank(new LevelRank(3, 4000, DiceExpression.parse("1d10")));
        fighterClass.addLevelRank(new LevelRank(4, 8000, DiceExpression.parse("1d10")));
        fighterClass.addLevelRank(new LevelRank(5, 16000, DiceExpression.parse("1d10")));
        fighterClass.addLevelRank(new LevelRank(6, 32000, DiceExpression.parse("1d10")));
        fighterClass.addLevelRank(new LevelRank(7, 64000, DiceExpression.parse("1d10")));
        fighterClass.addLevelRank(new LevelRank(8, 125000, DiceExpression.parse("1d10")));
        fighterClass.addLevelRank(new LevelRank(9, 250000, DiceExpression.parse("1d10")));

        fighterClass.setAdvancedRankAdditionalExperience(250000);
        fighterClass.setAdvancedRankAdditionalHitPoints(3);

        return fighterClass;
    }

    @Test
    public void test_getExperiencePointsRequiredForLevel() {
        final CharacterClass fighterClass = getFighterCharacterClass();

        final int[] expectedExperienceAmounts = new int[] {0, 2000, 4000, 8000, 16000, 32000, 64000, 125000, 250000, 500000, 750000,
                                                           1000000, 1250000, 1500000, 1750000, 2000000, 2250000, 2500000, 2750000, 3000000 };

        for(int i = 1; i <= 20; i++) {
            Assert.assertEquals(expectedExperienceAmounts[i - 1], fighterClass.getExperiencePointsRequiredForLevel(i));
        }

    }
}
