package ca.quadrilateral.jua.game.entity.impl;

import ca.quadrilateral.jua.game.enums.SavingType;
import java.util.HashMap;
import java.util.Map;

public class SavingThrowDefinition {
    private int maxLevel = 0;

    private Map<SavingType, Integer> saveTypeValueMap = new HashMap<SavingType, Integer>();

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }


    public void setBreathWeapon(int breathWeapon) {
        saveTypeValueMap.put(SavingType.BreathWeapon, breathWeapon);
    }

    public void setParalyzationPoisonDeath(int paralyzationPoisonDeath) {
        saveTypeValueMap.put(SavingType.ParalyzationPoisonDeath, paralyzationPoisonDeath);
    }

    public void setPetrificationPolymorph(int petrificationPolymorph) {
        saveTypeValueMap.put(SavingType.PetrificationPolymorph, petrificationPolymorph);
    }

    public void setRodStaffWand(int rodStaffWand) {
        saveTypeValueMap.put(SavingType.RodStaffWand, rodStaffWand);
    }

    public void setSpell(int spell) {
        saveTypeValueMap.put(SavingType.Spell, spell);
    }

    public int getSavingThrowValue(SavingType savingType) {
        return saveTypeValueMap.get(savingType);
    }
}
