package ca.quadrilateral.jua.game.impl;

import org.junit.Assert;
import org.junit.Test;

public class TestThac0Formula {

    @Test
    public void getThac0ForLevel_for_priest_formula_should_return_correct_results() {
        String formula = "2/3";
        int[] expectedResults = {20, 20, 20, 18, 18, 18, 16, 16, 16, 14, 14, 14, 12, 12, 12, 10, 10, 10, 8, 8};

        Thac0Formula thac0Formula = new Thac0Formula(formula);
        for(int i = 1; i <= 20; i++) {
            Assert.assertEquals(expectedResults[i-1], thac0Formula.getThac0ForLevel(i));
        }
    }

    @Test
    public void getThac0ForLevel_for_rogue_formula_should_return_correct_results() {
        String formula = "1/2";
        int[] expectedResults = {20, 20, 19, 19, 18, 18, 17, 17, 16, 16, 15, 15, 14, 14, 13, 13, 12, 12, 11, 11};
        Thac0Formula thac0Formula = new Thac0Formula(formula);
        for(int i = 1; i <= 20; i++) {
            Assert.assertEquals(expectedResults[i-1], thac0Formula.getThac0ForLevel(i));
        }
    }

    @Test
    public void getThac0ForLevel_for_warrior_formula_should_return_correct_results() {
        String formula = "1/1";
        int[] expectedResults = {20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        Thac0Formula thac0Formula = new Thac0Formula(formula);
        for(int i = 1; i <= 20; i++) {
            Assert.assertEquals(expectedResults[i-1], thac0Formula.getThac0ForLevel(i));
        }
    }

    @Test
    public void getThac0ForLevel_for_wizard_formula_should_return_correct_results() {
        String formula = "1/3";
        int[] expectedResults = {20, 20, 20, 19, 19, 19, 18, 18, 18, 17, 17, 17, 16, 16, 16, 15, 15, 15, 14, 14};
        Thac0Formula thac0Formula = new Thac0Formula(formula);
        for(int i = 1; i <= 20; i++) {
            Assert.assertEquals(expectedResults[i-1], thac0Formula.getThac0ForLevel(i));
        }
    }



}
