package ca.quadrilateral.jua.game.entity.impl;

import ca.quadrilateral.jua.game.enums.SavingType;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SavingThrowTable {
    private Set<SavingThrowDefinition> savingThrowDefinitions = new TreeSet<SavingThrowDefinition>(new Comparator<SavingThrowDefinition>() {
        @Override
        public int compare(SavingThrowDefinition o1, SavingThrowDefinition o2) {
            return new Integer(o1.getMaxLevel()).compareTo(new Integer(o2.getMaxLevel()));
        }
    });

    private int saveTableId = 0;

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this).append("Table Id", saveTableId);
        for(SavingThrowDefinition definition : savingThrowDefinitions) {
            builder.append("Level", definition.getMaxLevel());
            builder.append(SavingType.ParalyzationPoisonDeath.getText(), definition.getSavingThrowValue(SavingType.ParalyzationPoisonDeath));
            builder.append(SavingType.RodStaffWand.getText(), definition.getSavingThrowValue(SavingType.RodStaffWand));
            builder.append(SavingType.PetrificationPolymorph.getText(), definition.getSavingThrowValue(SavingType.PetrificationPolymorph));
            builder.append(SavingType.BreathWeapon.getText(), definition.getSavingThrowValue(SavingType.BreathWeapon));
            builder.append(SavingType.Spell.getText(), definition.getSavingThrowValue(SavingType.Spell));
        }

        return builder.toString();
    }

    public void setSaveTableId(int saveTableId) {
        this.saveTableId = saveTableId;
    }
    
    public int getSaveTableId() {
        return this.saveTableId;
    }

    public Set<SavingThrowDefinition> getSavingThrowDefinitions() {
        return this.savingThrowDefinitions;
    }

    public void addSavingThrowDefinition(SavingThrowDefinition savingThrowDefinition) {
        this.savingThrowDefinitions.add(savingThrowDefinition);
    }

    public int getSavingValue(SavingType savingType, int classLevel) {
        for(SavingThrowDefinition definition : savingThrowDefinitions) {
            if (classLevel <= definition.getMaxLevel()) {
                return definition.getSavingThrowValue(savingType);
            }
        }

        return 0xff;
    }
}
