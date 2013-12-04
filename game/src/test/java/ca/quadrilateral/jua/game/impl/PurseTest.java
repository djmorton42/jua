package ca.quadrilateral.jua.game.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class PurseTest {
    private Purse purse = null;
    private List<Currency> gameCurrencyList = null;

    @Before
    public void initialize() {
        gameCurrencyList = getGameCurrencyList();
        this.purse = new Purse(gameCurrencyList);
    }
    
    private List<Currency> getGameCurrencyList() {
        final List<Currency> currencyList = new ArrayList<Currency>();
        currencyList.add(new Currency("Platinum", DiceExpression.parse("500"), true));
        currencyList.add(new Currency("Gold", DiceExpression.parse("100"), true));
        currencyList.add(new Currency("Electrum", DiceExpression.parse("50"), true));
        currencyList.add(new Currency("Silver", DiceExpression.parse("10"), true));
        currencyList.add(new Currency("Copper", DiceExpression.parse("1"), true));
        currencyList.add(new Currency("Gems", DiceExpression.parse("20D20"), false));
        currencyList.add(new Currency("Jewelry", DiceExpression.parse("20D100"), false));
        
        return currencyList;
    }

    @Test
    public void getCurrencyAmount_with_one_currency_in_purse_should_return_correct_value() {
        Currency currency = getCurrencyByName("Gold");

        purse.currencyAmounts.put(currency, 10);
        Assert.assertEquals(10, purse.getCurrencyAmount(currency));
    }

    @Test
    public void getCurrencyAmount_with_two_currencies_in_purse_should_return_correct_value() {
        Currency gold = getCurrencyByName("Gold");
        Currency platinum = getCurrencyByName("Platinum");

        purse.currencyAmounts.put(gold, 10);
        purse.currencyAmounts.put(platinum, 15);

        Assert.assertEquals(10, purse.getCurrencyAmount(gold));
        Assert.assertEquals(15, purse.getCurrencyAmount(platinum));
    }

    @Test
    public void giveCurrency_should_add_correct_value_to_purse() {
        Currency gold = getCurrencyByName("Gold");
        purse.currencyAmounts.put(gold, 10);

        purse.giveCurrency(gold, 15);

        Assert.assertEquals(25, purse.currencyAmounts.get(gold).intValue());
    }

    @Test
    public void giveCurrency_run_twice_should_sum_amounts_added() {
        Currency currency = getCurrencyByName("Gold");

        purse.giveCurrency(currency, 15);
        purse.giveCurrency(currency, 10);
        Assert.assertEquals(25, purse.getCurrencyAmount(currency));
    }

    @Test
    public void giveCurrency_with_variable_currency_should_add_correct_value() {
        Currency currency = getCurrencyByName("Gems");
        purse.giveCurrency(currency, 20);

        Assert.assertEquals(20, purse.currencyAmounts.get(currency).intValue());
    }

    @Test
    public void giveCurrency_with_two_variable_currencies_should_add_corretValues() {
        Currency gems = getCurrencyByName("Gems");
        Currency jewelry = getCurrencyByName("Jewelry");
        purse.giveCurrency(gems, 20);
        purse.giveCurrency(jewelry, 30);

        Assert.assertEquals(20, purse.currencyAmounts.get(gems).intValue());
        Assert.assertEquals(30, purse.currencyAmounts.get(jewelry).intValue());
    }

    @Test
    public void giveCurrency_with_multiple_currencies_should_add_correct_values() {
        Currency gold = getCurrencyByName("Gold");
        Currency platinum = getCurrencyByName("Platinum");

        Map<Currency, Integer> currencyMap = new HashMap<Currency, Integer>();

        currencyMap.put(gold, 25);
        currencyMap.put(platinum, 30);

        purse.giveCurrency(currencyMap);

        Assert.assertEquals(25, purse.currencyAmounts.get(gold).intValue());
        Assert.assertEquals(30, purse.currencyAmounts.get(platinum).intValue());
    }

    @Test
    public void hasSufficientCurrency_without_money_changing_should_return_true() {
        Currency gold = getCurrencyByName("Gold");
        purse.giveCurrency(gold, 55);

        Assert.assertTrue(purse.hasSufficientCurrency(gold, 50, false));
    }

    @Test
    public void hasSufficientCurrency_without_money_changing_should_return_false() {
        Currency gold = getCurrencyByName("Gold");
        purse.giveCurrency(gold, 55);

        Assert.assertFalse(purse.hasSufficientCurrency(gold, 56, false));
    }

    @Test
    public void hasSufficientCurrency_with_money_changing_but_sufficient_amount_of_requested_currency_should_return_true() {
        Currency gold = getCurrencyByName("Gold");
        purse.giveCurrency(gold, 55);

        Assert.assertTrue(purse.hasSufficientCurrency(gold, 50, true));
    }

    @Test
    public void hasSufficientCurrency_with_money_changing_requiring_greater_currency_changing_should_return_true() {
        Currency gold = getCurrencyByName("Gold");
        Currency platinum = getCurrencyByName("Platinum");
        purse.giveCurrency(gold, 55);
        purse.giveCurrency(platinum, 1);

        Assert.assertTrue(purse.hasSufficientCurrency(gold, 60, true));
    }

    @Test
    public void hasSufficientCurrency_with_money_changing_requiring_greater_currency_changing_should_return_false() {
        Currency gold = getCurrencyByName("Gold");
        Currency platinum = getCurrencyByName("Platinum");
        purse.giveCurrency(gold, 55);
        purse.giveCurrency(platinum, 1);

        Assert.assertFalse(purse.hasSufficientCurrency(gold, 61, true));
    }

    @Test
    public void hasSufficientCurrency_with_money_changing_requiring_smaller_currency_changing_should_return_true() {
        Currency gold = getCurrencyByName("Gold");
        Currency silver = getCurrencyByName("Silver");
        purse.giveCurrency(gold, 55);
        purse.giveCurrency(silver, 50);

        Assert.assertTrue(purse.hasSufficientCurrency(gold, 60, true));
    }

    @Test
    public void hasSufficientCurrency_with_money_changing_requiring_smaller_currency_changing_should_return_false() {
        Currency gold = getCurrencyByName("Gold");
        Currency silver = getCurrencyByName("Silver");
        purse.giveCurrency(gold, 55);
        purse.giveCurrency(silver, 49);

        Assert.assertFalse(purse.hasSufficientCurrency(gold, 60, true));
    }

    @Test
    public void hasSufficientCurrency_for_changable_amount_does_not_change_unchangable_amounts() {
        Currency gold = getCurrencyByName("Gold");
        Currency gems = getCurrencyByName("Gems");

        purse.giveCurrency(gold, 59);
        purse.giveCurrency(gems, 10);

        Assert.assertFalse(purse.hasSufficientCurrency(gold, 60, true));
    }

    @Test
    public void hasSufficientCurrency_for_unchangable_amount_does_not_change_changable_amounts() {
        Currency gold = getCurrencyByName("Gold");
        Currency gems = getCurrencyByName("Gems");

        purse.giveCurrency(gold, 50000);
        purse.giveCurrency(gems, 1);

        Assert.assertFalse(purse.hasSufficientCurrency(gems, 2, true));
    }

    @Test
    public void getPurseValueInLargeCurrency_should_return_correct_value() {
        Currency gold = getCurrencyByName("Gold");
        Currency platinum = getCurrencyByName("Platinum");
        Currency silver = getCurrencyByName("Silver");
        Currency copper = getCurrencyByName("Copper");

        purse.currencyAmounts.put(platinum, 45);
        purse.currencyAmounts.put(gold, 200);
        purse.currencyAmounts.put(silver, 350);
        purse.currencyAmounts.put(copper, 34);

        Assert.assertEquals(92, purse.getPurseValueInCurrency(platinum));
    }

    @Test
    public void getPurseValueInSmallCurrency_should_return_correct_value() {
        Currency gold = getCurrencyByName("Gold");
        Currency platinum = getCurrencyByName("Platinum");
        Currency silver = getCurrencyByName("Silver");
        Currency copper = getCurrencyByName("Copper");

        purse.currencyAmounts.put(platinum, 45);
        purse.currencyAmounts.put(gold, 200);
        purse.currencyAmounts.put(silver, 350);
        purse.currencyAmounts.put(copper, 34);

        Assert.assertEquals(46034, purse.getPurseValueInCurrency(copper));
    }

    @Test
    public void getPurseValueInMediumCurrency_should_return_correct_value() {
        Currency gold = getCurrencyByName("Gold");
        Currency platinum = getCurrencyByName("Platinum");
        Currency silver = getCurrencyByName("Silver");
        Currency copper = getCurrencyByName("Copper");
        Currency electrum = getCurrencyByName("Electrum");

        purse.currencyAmounts.put(platinum, 45);
        purse.currencyAmounts.put(gold, 200);
        purse.currencyAmounts.put(silver, 350);
        purse.currencyAmounts.put(copper, 34);

        Assert.assertEquals(920, purse.getPurseValueInCurrency(electrum));
    }

    @Test
    public void takeCurrency_not_allowing_change_should_occur_successfully() {
        Currency gold = getCurrencyByName("Gold");
        Currency platinum = getCurrencyByName("Platinum");
        Currency silver = getCurrencyByName("Silver");
        Currency copper = getCurrencyByName("Copper");
        Currency electrum = getCurrencyByName("Electrum");

        purse.currencyAmounts.put(platinum, 45);
        purse.currencyAmounts.put(gold, 200);
        purse.currencyAmounts.put(silver, 350);
        purse.currencyAmounts.put(copper, 34);

        purse.takeCurrency(silver, 50);
        Assert.assertEquals(300, purse.currencyAmounts.get(silver).intValue());
        Assert.assertEquals(45534, purse.getPurseValueInCurrency(copper));
    }

    @Test
    public void takeCurrency_allowing_money_change_should_work_with_large_currencies() {
        Currency gold = getCurrencyByName("Gold");
        Currency platinum = getCurrencyByName("Platinum");
        Currency silver = getCurrencyByName("Silver");
        Currency copper = getCurrencyByName("Copper");
        Currency electrum = getCurrencyByName("Electrum");

        purse.currencyAmounts.put(platinum, 45);
        purse.currencyAmounts.put(gold, 200);
        purse.currencyAmounts.put(silver, 350);
        purse.currencyAmounts.put(copper, 34);

        purse.takeCurrency(gold, 250, true);

        Assert.assertEquals(35, purse.currencyAmounts.get(platinum).intValue());
        Assert.assertNull(purse.currencyAmounts.get(gold));
        Assert.assertEquals(350, purse.currencyAmounts.get(silver).intValue());
        Assert.assertEquals(34, purse.currencyAmounts.get(copper).intValue());
    }


    @Test
    public void takeCurrency_allowing_money_change_should_work_with_small_currencies() {
        Currency gold = getCurrencyByName("Gold");
        Currency platinum = getCurrencyByName("Platinum");
        Currency silver = getCurrencyByName("Silver");
        Currency copper = getCurrencyByName("Copper");
        Currency electrum = getCurrencyByName("Electrum");

        purse.currencyAmounts.put(gold, 10);
        purse.currencyAmounts.put(silver, 10);
        purse.currencyAmounts.put(copper, 5000);

        purse.takeCurrency(gold, 15, true);

        Assert.assertNull(purse.currencyAmounts.get(platinum));
        Assert.assertNull(purse.currencyAmounts.get(gold));
        Assert.assertNull(purse.currencyAmounts.get(silver));
        Assert.assertEquals(4600, purse.currencyAmounts.get(copper).intValue());
    }

    @Test
    public void takeCurrency_allowing_money_change_should_give_correct_change() {
        Currency platinum = getCurrencyByName("Platinum");
        Currency gold = getCurrencyByName("Gold");
        Currency electrum = getCurrencyByName("Electrum");
        Currency silver = getCurrencyByName("Silver");
        Currency copper = getCurrencyByName("Copper");

        purse.currencyAmounts.put(platinum, 1);

        purse.takeCurrency(copper, 1, true);

        Assert.assertNull(purse.currencyAmounts.get(platinum));
        Assert.assertEquals(4, purse.currencyAmounts.get(gold).intValue());
        Assert.assertEquals(1, purse.currencyAmounts.get(electrum).intValue());
        Assert.assertEquals(4, purse.currencyAmounts.get(silver).intValue());
        Assert.assertEquals(9, purse.currencyAmounts.get(copper).intValue());                        
    }

    private Currency getCurrencyByName(String name) {
        for(Currency currency : gameCurrencyList) {
            if (name.equals(currency.getName())) {
                return currency;
            }
        }
        return null;
    }
}
