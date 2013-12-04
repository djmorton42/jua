package ca.quadrilateral.jua.game.item.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import ca.quadrilateral.jua.game.entity.impl.CharacterClass;

public class ItemDefinition {
    private Collection<CharacterClass> allowedClasses = new ArrayList<CharacterClass>();
    private String identifiedName = null;
    private String baseName = null;
    private String definitionId = null;
    private boolean isMagical = false;
    private boolean usable = false;
    private double weight = 0.0d;
    private int value = 0;
    private int experienceValue = 0;

    @Override
    public String toString() {
        Collection<String> classNames = new ArrayList<String>();
        for(CharacterClass characterClass : allowedClasses) {
            classNames.add(characterClass.getName());
        }

        return new ToStringBuilder(this)
            .append("Id", definitionId)
            .append("Base Name", baseName)
            .append("Identified Name", identifiedName)
            .append("Is Magical", isMagical)
            .append("Is Usable", usable)
            .append("Weight", weight)
            .append("Base Value", value)
            .append("XP Value", experienceValue)
            .append("Allowed Classes", StringUtils.join(classNames, ","))
            .toString();
    }

    public ItemDefinition(String definitionId) {
        this.definitionId = definitionId;
    }

    public String getDefinitionId() {
        return this.definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    public void addAllowedClass(CharacterClass characterClass) {
        this.allowedClasses.add(characterClass);
    }

    public boolean isCharacterClassAllowed(CharacterClass characterClass) {
        for(CharacterClass allowedClass : allowedClasses) {
            if (allowedClass.equals(characterClass)) {
                return true;
            }
        }
        return false;
    }

    public Collection<CharacterClass> getAllowedClasses() {
        return this.allowedClasses;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public boolean isUsable() {
        return this.isUsable();
    }

    public int getExperienceValue() {
        return this.experienceValue;
    }

    public void setExperienceValue(int experienceValue) {
        this.experienceValue = experienceValue;
    }

    public boolean isMagical() {
        return this.isMagical;
    }
    public void setMagical(boolean isMagical) {
        this.isMagical = isMagical;
    }
    public double getWeight() {
        return this.weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public int getValue() {
        return this.value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public String getIdentifiedName() {
        return this.identifiedName;
    }
    public void setIdentifiedName(String identifiedName) {
        this.identifiedName = identifiedName;
    }
    public String getBaseName() {
        return this.baseName;
    }
    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }



}
