package ca.quadrilateral.jua.game.entity.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang3.builder.ToStringBuilder;
import ca.quadrilateral.jua.game.entity.IEntity;
import ca.quadrilateral.jua.game.enums.EntityAbilityType;
import ca.quadrilateral.jua.game.enums.EntityAttributeType;
import ca.quadrilateral.jua.game.enums.SavingType;
import ca.quadrilateral.jua.game.impl.Thac0Formula;
import ca.quadrilateral.jua.game.item.impl.Item;

public class CharacterClass {
    private static final int PRIME_REQUISITE_EXPERIENCE_POINT_BONUS_THRESHOLD = 16;
    private String name = null;
    private int advancedRankAdditionalHitPoints = 0;
    private int advancedRankAdditionalExperience = 0;
    private Set<LevelRank> levelRankTable = new TreeSet<LevelRank>();
    private Set<EntityAttributeType> primeRequisites = new HashSet<EntityAttributeType>();
    private Map<EntityAttributeType, Integer> abilityMinimums = new HashMap<EntityAttributeType, Integer>();
    private SavingThrowTable savingThrowTable = null;
    private Thac0Formula thac0Formula = null;
    private Map<EntityAbilityType, LevelBasedAbilityDefinition> levelBasedAbilities = new HashMap<EntityAbilityType, LevelBasedAbilityDefinition>();

    public CharacterClass() {}

    public CharacterClass(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("Class Name", name).append("Advanced Rank Additional Hit Points", advancedRankAdditionalHitPoints)
                .append("Advanced Rank Additional Experience", advancedRankAdditionalExperience)
                .appendToString(thac0Formula.toString());

        for(EntityAttributeType primeRequisite : primeRequisites) {
            builder.append("Prime Requisite", primeRequisite.getText());
        }

        for(EntityAttributeType attributeType : abilityMinimums.keySet()) {
            final Integer minimum = abilityMinimums.get(attributeType);
            builder.append("Attribute Minimum", attributeType.getText() + ": " + minimum);
        }

        for(LevelRank levelRank : levelRankTable) {
            builder.appendToString(levelRank.toString());
        }

        return builder.toString();
    }

    public Map<EntityAbilityType, LevelBasedAbilityDefinition> getLevelBasedAbilities() {
        return levelBasedAbilities;
    }

    public void setLevelBasedAbilities(Map<EntityAbilityType, LevelBasedAbilityDefinition> levelBasedAbilities) {
        this.levelBasedAbilities = levelBasedAbilities;
    }

    public void addLevelBasedAbility(LevelBasedAbilityDefinition levelBasedAbility) {
        this.levelBasedAbilities.put(levelBasedAbility.getEntityAbilityType(), levelBasedAbility);
    }

    public void setThac0Formula(Thac0Formula thac0Formula) {
        this.thac0Formula = thac0Formula;
    }

    public Thac0Formula getThac0Formula() {
        return this.thac0Formula;
    }

    public int getBaseThac0(int level) {
        return this.getThac0Formula().getThac0ForLevel(level);
    }

    public int getExperiencePointsRequiredForLevel(int level) {
        int xpThreshold = 0;
        int maxRank = 0;
        for(LevelRank rank : levelRankTable) {
            xpThreshold = rank.getExperiencePointsRequired();
            maxRank = rank.getRank();
            if (rank.getRank() == level) {
               return xpThreshold;
            }
        }

        return xpThreshold + ((level - maxRank) * advancedRankAdditionalExperience);
    }

    public void addLevelRank(LevelRank levelRank) {
        levelRankTable.add(levelRank);
    }

    public void addAbilityMinimum(EntityAttributeType entityAttributeType, Integer minimumValue) {
        abilityMinimums.put(entityAttributeType, minimumValue);
    }

    public void addPrimeRequisite(EntityAttributeType entityAttributeType) {
        primeRequisites.add(entityAttributeType);
    }

    public int getAdvancedRankAdditionalExperience() {
        return advancedRankAdditionalExperience;
    }

    public void setAdvancedRankAdditionalExperience(int advancedRankAdditionalExperience) {
        this.advancedRankAdditionalExperience = advancedRankAdditionalExperience;
    }

    public int getAdvancedRankAdditionalHitPoints() {
        return advancedRankAdditionalHitPoints;
    }

    public void setAdvancedRankAdditionalHitPoints(int advancedRankAdditionalHitPoints) {
        this.advancedRankAdditionalHitPoints = advancedRankAdditionalHitPoints;
    }

    public Map<EntityAttributeType, Integer> getAbilityMinimums() {
        return abilityMinimums;
    }

    protected void setAbilityMinimums(Map<EntityAttributeType, Integer> abilityMinimums) {
        this.abilityMinimums = abilityMinimums;
    }

    public Set<LevelRank> getLevelRankTable() {
        return levelRankTable;
    }

    protected void setLevelRankTable(Set<LevelRank> levelRankTable) {
        this.levelRankTable = levelRankTable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EntityAttributeType> getPrimeRequisites() {
        return primeRequisites;
    }

    protected void setPrimeRequisites(Set<EntityAttributeType> primeRequisites) {
        this.primeRequisites = primeRequisites;
    }

    public void setSavingThrowTable(SavingThrowTable savingThrowTable) {
        this.savingThrowTable = savingThrowTable;
    }

    public SavingThrowTable getSavingThrowTable() {
        return this.savingThrowTable;
    }

    public int getSavingValue(IEntity entity, SavingType savingType) {
        int classLevel = entity.getLevelInCharacterClass(this);
        return this.getSavingThrowTable().getSavingValue(savingType, classLevel);
    }

    public boolean canAdvanceLevel(PartyMember entity) {
        int level = entity.getLevelInCharacterClass(this);
        if (level > 0) {
            final int requiredExperiencePoints = this.getExperiencePointsRequiredForLevel(level + 1);
            return entity.getExperiencePoints() >= requiredExperiencePoints;
        }

        return false;
    }

    public void advanceLevel(PartyMember entity) {
        if (canAdvanceLevel(entity)) {
            final int newLevel = entity.getLevelInCharacterClass(this) + 1;
            //TODO:  Handle level advance
        }
    }

    public boolean meetsPrimeRequisiteMinimum(PartyMember partyMember) {
        for(EntityAttributeType entityAttributeType : this.primeRequisites) {
            if (partyMember.getEntityAttribute(entityAttributeType).getEffectiveValue() < PRIME_REQUISITE_EXPERIENCE_POINT_BONUS_THRESHOLD) {
                return false;
            }
        }
        return true;
    }

    public boolean canEquip(Item item) {
        return item.getItemDefinition().isCharacterClassAllowed(this);
    }
}
