package ca.quadrilateral.jua.game.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Purse {
    protected Map<Currency, Integer> currencyAmounts = new TreeMap<Currency, Integer>();
    private Set<Currency> currencyList = new TreeSet<Currency>();

    public Purse(Collection<Currency> currencyList) {
        this.currencyList.addAll(currencyList);
    }

    public Collection<Currency> getCurrenciesInPurse() {
        final Set<Currency> currencySet = new TreeSet<Currency>();
        for(Currency currency : currencyAmounts.keySet()) {
            if (hasCurrency(currency)) {
                currencySet.add(currency);
            }
        }

        return currencySet;
    }

    public List<Purse> subdividePurse(int shares, boolean clearPurse) {
    	assert(shares > 0);
    	
    	List<Purse> subdividedPurseList = new ArrayList<Purse>(shares);
    	
    	for(int i = 0; i < shares; i++) {
    		subdividedPurseList.add(new Purse(this.currencyList));
    	}
    	
    	for(Currency currency : currencyAmounts.keySet()) {
    		int initialAmountOfCurrency = currencyAmounts.get(currency);
    		int amountPerPurse = (int)Math.round((initialAmountOfCurrency * 1.0) / shares);
    		int currencyBalance = initialAmountOfCurrency;
    		
    		for(int i = 0; i < shares - 1; i++) {
    			subdividedPurseList.get(i).giveCurrency(currency, amountPerPurse);
    			currencyBalance -= amountPerPurse;
    		}
    		subdividedPurseList.get(shares - 1).giveCurrency(currency, currencyBalance);
    	}
    	
    	if (clearPurse) {
    		this.clear();
    	}
    	
    	return subdividedPurseList;
    }
    
    public void clear() {
    	for(Currency currency : currencyAmounts.keySet()) {
    		currencyAmounts.put(currency, 0);
    	}
    }
    
    public void addPurse(Purse purse) {
    	this.giveCurrency(purse.getCurrencyAmounts());
    }
    
    protected Map<Currency, Integer> getCurrencyAmounts() {
    	return new TreeMap<Currency, Integer>(this.currencyAmounts);
    }
    
    public boolean hasCurrency(Currency currency) {
        return getCurrencyAmount(currency) > 0;
    }

    public Purse clonePurse() {
        final Purse purse = new Purse(new ArrayList<Currency>(this.currencyList));

        for(Currency currencyKey : currencyAmounts.keySet()) {
            purse.addCurrency(currencyKey, getCurrencyAmount(currencyKey));
        }

        return purse;
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        for(Currency currency : currencyAmounts.keySet()) {
            builder.append("Currency[" + currency.getName() + ", " + currencyAmounts.get(currency) + "]");
        }

        return builder.toString();
    }

    public int getCurrencyAmount(Currency currency) {
        if (currencyAmounts.containsKey(currency)) {
            return currencyAmounts.get(currency);
        } else {
            return 0;
        }
    }

    public void giveCurrency(Map<Currency, Integer> newCurrency) {
        for(Currency currency : newCurrency.keySet()) {
            addCurrency(currency, newCurrency.get(currency));
        }
    }

    public void giveCurrency(Currency currency, Integer amount) {
        addCurrency(currency, amount);
    }

    public boolean hasSufficientCurrency(Currency currency, Integer amount, boolean includeMoneyChange) {
        int amountInCurrency = getCurrencyAmount(currency);

        if (amountInCurrency >= amount) {
            return true;
        } else {
            if (includeMoneyChange) {
                return getPurseValueInCurrency(currency) >= amount;
            } else {
                return false;
            }
        }
    }

    public boolean hasSufficientCurrency(Currency currency, Integer amount) {
        return hasSufficientCurrency(currency, amount, false);
    }


    public int getPurseValueInCurrency(Currency currency) {
        int total = 0;
        if (!currency.isCanBeChanged()) {
            total = getCurrencyAmount(currency);
        } else {
            DiceRoller roller = new DiceRoller();
            for(Currency purseCurrency : currencyAmounts.keySet()) {
                if (purseCurrency.isCanBeChanged()) {
                    total += getCurrencyAmount(purseCurrency) * roller.roll(purseCurrency.getValueExpression());
                }
            }
            total = total / roller.roll(currency.getValueExpression());
        }
        return total;
    }

    public void takeCurrency(Currency currency, Integer amount) {
        takeCurrency(currency, amount, false);
    }

    public void takeCurrency(Currency currency, Integer amount, boolean allowMoneyChange) {
        DiceRoller diceRoller = new DiceRoller();
        final Map<Currency, Integer> currenciesToSubtract = new HashMap<Currency, Integer>();
        int amountInCurrency = getCurrencyAmount(currency);
        if (amountInCurrency >= amount) {
            subtractCurrency(currency, amount);
        } else if (allowMoneyChange) {
            subtractCurrency(currency, amountInCurrency);
            int balance = amount - amountInCurrency;
            int balanceInBase = balance * diceRoller.roll(currency.getValueExpression());
            for(Currency purseCurrency : currencyAmounts.keySet()) {
                if (purseCurrency != currency && purseCurrency.isCanBeChanged()) {
                    int otherCurrencyAmount = getCurrencyAmount(purseCurrency);
                    int otherCurrencyBaseAmount = otherCurrencyAmount * diceRoller.roll(purseCurrency.getValueExpression());
                    if (otherCurrencyBaseAmount >= balanceInBase) {

                        //int baseAmountToRemove = otherCurrencyBaseAmount - balanceInBase;
                        int amountToRemove = balanceInBase / diceRoller.roll(purseCurrency.getValueExpression());
                        int remainder = balanceInBase % diceRoller.roll(purseCurrency.getValueExpression());
                        if (remainder != 0) {
                            amountToRemove += 1;
                        }

                        this.subtractCurrency(purseCurrency, amountToRemove);

                        if (remainder != 0) {
                            Map<Currency, Integer> change = calculateChange(purseCurrency, amountToRemove, balanceInBase);
                            for(Currency changeCurrency : change.keySet()) {
                                addCurrency(changeCurrency, change.get(changeCurrency));
                            }
                        }
                        break;
                    } else {
                        balanceInBase -= otherCurrencyBaseAmount;
                        currenciesToSubtract.put(purseCurrency, otherCurrencyAmount);
                    }
                }
            }

            //Doing this outside the foreach loop to avoid concurrent modification
            for(Currency currencyToSubtract : currenciesToSubtract.keySet()) {
                subtractCurrency(currencyToSubtract, currenciesToSubtract.get(currencyToSubtract));
            }



        } else {
            assert(false);
        }
    }

    private Map<Currency, Integer> calculateChange(Currency currency, int amountTendered, int amountOwedInBase) {
        final Map<Currency, Integer> resultMap = new HashMap<Currency, Integer>();
        final DiceRoller diceRoller = new DiceRoller();
        int amountTenderedInBase = diceRoller.roll(currency.getValueExpression()) * amountTendered;
        int changeOwedInBase = amountTenderedInBase - amountOwedInBase;

        for(Currency purseCurrency : currencyList) {
            if (purseCurrency.isCanBeChanged() && !currency.equals(purseCurrency)) {
                int amountToGiveAsChange = changeOwedInBase / diceRoller.roll(purseCurrency.getValueExpression());
                resultMap.put(purseCurrency, amountToGiveAsChange);
                if (changeOwedInBase % diceRoller.roll(purseCurrency.getValueExpression()) != 0) {
                    changeOwedInBase -= (amountToGiveAsChange *  diceRoller.roll(purseCurrency.getValueExpression()));
                } else {
                    break;
                }
            }
        }

        return resultMap;
    }

    private void addCurrency(Currency currency, Integer amount) {
        if (!currencyAmounts.containsKey(currency)) {
            currencyAmounts.put(currency, 0);
        }
        currencyAmounts.put(currency, currencyAmounts.get(currency) + amount);
    }

    private void subtractCurrency(Currency currency, Integer amount) {
        int newAmount = getCurrencyAmount(currency) - amount;
        if (newAmount == 0) {
            currencyAmounts.remove(currency);
        } else {
            currencyAmounts.put(currency, getCurrencyAmount(currency) - amount);
        }
    }


}
