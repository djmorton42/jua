package ca.quadrilateral.jua.game.entity.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.entity.IAbility;
import ca.quadrilateral.jua.game.entity.IEntity;
import ca.quadrilateral.jua.game.enums.EntityAbilityType;
import ca.quadrilateral.jua.game.enums.EntityAttributeType;
import ca.quadrilateral.jua.game.enums.Gender;
import ca.quadrilateral.jua.game.enums.SavingType;
import ca.quadrilateral.jua.game.impl.ArmorClassCalculator;
import ca.quadrilateral.jua.game.impl.DiceExpression;
import ca.quadrilateral.jua.game.impl.DiceRoller;
import ca.quadrilateral.jua.game.impl.Effect;
import ca.quadrilateral.jua.game.impl.Purse;
import ca.quadrilateral.jua.game.impl.Thac0Calculator;
import ca.quadrilateral.jua.game.impl.TimeDefinition;
import ca.quadrilateral.jua.game.item.impl.Item;
import ca.quadrilateral.jua.game.item.impl.ItemCollection;

public class Entity implements IEntity {
    private int hitPointTotal = 0;
    private int currentHitPoints = 0;
    private int baseArmorClass = 10;

    private String name = null;
    private Gender gender = null;
    private List<EntityAttribute> entityAttributes;
    private Map<CharacterClass, Integer> classLevels = new HashMap<CharacterClass, Integer>();
    private boolean isMultiClassed = false;
    private boolean isDualClassed = false;
    private CharacterClass activeCharacterClass = null;
    private CharacterRace characterRace = null;
    private Alignment alignment = null;
    private List<Effect> effects = new ArrayList<Effect>(0xff);
    private List<IAbility> abilities = new ArrayList<IAbility>();
    private Purse purse = null;
    private int ageUnits = 0;
    private ItemCollection inventory = new ItemCollection();


    private IGameContext gameContext;

    public Entity(String name, int currentHitPoints, int baseArmorClass, Gender gender, IGameContext gameContext) {
        this.name = name;
        this.currentHitPoints = currentHitPoints;
        this.hitPointTotal = currentHitPoints;
        this.baseArmorClass = baseArmorClass;
        this.gender = gender;
        this.purse = new Purse(gameContext.getCurrenyList());

        this.entityAttributes = new ArrayList<EntityAttribute>();

        this.gameContext = gameContext;
    }

    public ItemCollection getInventory() {
        return this.inventory;
    }

    public boolean addItemToInventory(Item item) {
        inventory.add(item);
        return true;
    }

    public boolean equipItem(int index) {
        final Item itemToEquip = inventory.get(index);
        if (itemToEquip == null) {
            return false;
        }
        return equipItem(itemToEquip);
    }

    public boolean equipItem(Item item) {
        for (Item inventoryItem : inventory) {
            if (item.equals(inventoryItem)) {
                if (canEquip(inventoryItem)) {
                    inventoryItem.setEquipped(true);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean unequipItem(int index) {
        final Item itemToUnequip = inventory.get(index);
        if (itemToUnequip == null) {
            return false;
        }
        return unequipItem(itemToUnequip);
    }

    public boolean unequipItem(Item item) {
        if (item.isEquipped()) {
            item.setEquipped(false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canEquip(Item item) {
        for(CharacterClass characterClass : classLevels.keySet()) {
            if (characterClass.canEquip(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Alignment getAlignment() {
        return this.alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }


    @Override
    public Purse getPurse() {
        return this.purse;
    }

    @Override
    public void addAbility(IAbility ability) {
        abilities.add(ability);
    }

    @Override
    public IAbility getAbility(EntityAbilityType abilityType) {
        for(IAbility ability : abilities) {
            if (ability.getEntityAbilityType().equals(abilityType)) {
                return ability;
            }
        }
        return null;
    }

    public void addEntityAttribute(EntityAttributeType attributeType, int value) {
        this.addEntityAttribute(attributeType, value, 0);
    }

    public void addEntityAttribute(EntityAttributeType attributeType, int value, int partialValue) {
        final EntityAttribute attribute = new EntityAttribute();
        attribute.setEntityAttributeType(attributeType);
        attribute.setBaseValue(value);
        attribute.setBasePartialValue(partialValue);
        this.entityAttributes.add(attribute);
    }

    public void addEffect(Effect effect) {
        this.effects.add(effect);
    }

    public List<Effect> getEffects() {
        return this.effects;
    }

    public String getAgeString() {
        Set<TimeDefinition> timeDefinitions = gameContext.getTimeDefinitions();
        TimeDefinition definition = new LinkedList<TimeDefinition>(timeDefinitions).getLast();
        return MessageFormat.format("{0} {1}", definition.getTimeValue(this.getAgeUnits()), definition.getName());
    }



    @Override
    public int getAgeUnits() {
        return this.ageUnits;
    }

    @Override
    public void setAgeUnits(int ageUnits) {
            this.ageUnits = ageUnits;
    }

    @Override
    public void incrementAge() {
        this.ageUnits++;
    }

    @Override
    public void addAgeUnits(int ageUnits) {
        this.ageUnits += ageUnits;
    }

    @Override
    public Set<CharacterClass> getClasses() {
        return classLevels.keySet();
    }

    public void setCharacterClassLevel(CharacterClass characterClass, int level) {
        classLevels.put(characterClass, level);
        if (activeCharacterClass == null) {
            this.activeCharacterClass = characterClass;
        }
    }

    @Override
    public int getLevelInCharacterClass(CharacterClass characterClass) {
        if (getClasses().contains(characterClass)) {
            return classLevels.get(characterClass);
        } else {
            return 0;
        }
    }

    @Override
    public CharacterClass getActiveCharacterClass() {
        return this.activeCharacterClass;
    }

    public void setActiveCharacterClass(CharacterClass characterClass) {
        this.activeCharacterClass = characterClass;
    }

    @Override
    public CharacterRace getCharacterRace() {
        return this.characterRace;
    }

    public void setCharacterRace(CharacterRace characterRace) {
        this.characterRace = characterRace;
    }


    @Override
    public boolean isDisabled() {
        //TODO add full implementation of this feature to detect character status
        return false;
    }

    @Override
    public int getBaseArmorClass() {
        return this.baseArmorClass;
    }

    @Override
    public int getEffectiveArmorClass() {
        return ArmorClassCalculator.calculate(this);
    }

    @Override
    public int getBaseThac0() {
        if (this.isMultiClassed()) {
            return getBestThac0ForCharacterClasses();
        } else if (this.isDualClassed()) {
            if (this.doesActiveClassExceedOtherClassLevels()) {
                return getBestThac0ForCharacterClasses();
            } else {
                return this.getClassThac0(this.getActiveCharacterClass());
            }
        } else {
            return this.getClassThac0(this.getActiveCharacterClass());
        }
    }

    private int getBestThac0ForCharacterClasses() {
        int bestThac0 = 20;
        for(CharacterClass characterClass : this.getClasses()) {
            int classThac0 = getClassThac0(characterClass);
            if (classThac0 < bestThac0) {
                bestThac0 = classThac0;
            }
        }
        return bestThac0;
    }

    private int getClassThac0(CharacterClass characterClass) {
        return characterClass.getBaseThac0(this.getLevelInCharacterClass(characterClass));
    }

    @Override
    public int getEffectiveThac0() {
        return Thac0Calculator.calculate(this);
    }

    @Override
    public boolean doesActiveClassExceedOtherClassLevels() {
        if (isDualClassed()) {
            for(CharacterClass characterClass : this.getClasses()) {
                if (characterClass != this.getActiveCharacterClass()) {
                    if (this.getLevelInCharacterClass(characterClass) >= this.getLevelInCharacterClass(this.getActiveCharacterClass())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    @Override
    public int getHitPointTotal() {
        return this.hitPointTotal;
    }

    @Override
    public int getCurrentHitPoints() {
        return this.currentHitPoints;
    }

    @Override
    public boolean isDamaged() {
        return this.getCurrentHitPoints() != this.getHitPointTotal();
    }

    @Override
    public boolean isMultiClassed() {
        return this.isMultiClassed;
    }

    @Override
    public boolean isDualClassed() {
        return this.isDualClassed;
    }

    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public Gender getGender() {
        return this.gender;
    }

    @Override
    public EntityAttribute getEntityAttribute(EntityAttributeType type) {
        for(EntityAttribute attribute : entityAttributes) {
            if (attribute.getEntityAttributeType().equals(type)) {
                return attribute;
            }
        }
        return null;
    }

    @Override
    public void takeDamage(int damageAmount) {
        this.currentHitPoints -= damageAmount;
    }

    @Override
    public boolean saveVersus(SavingType saveType) {
        return saveVersus(saveType, 0);
    }

    @Override
    public boolean saveVersus(SavingType saveType, int savingModifier) {
        int saveTarget = getBestSaveValueForClasses(saveType);

        final DiceExpression expression = DiceExpression.D20;
        expression.setModifier(savingModifier);

        return new DiceRoller().roll(expression) >= saveTarget;
    }

    private int getBestSaveValueForClasses(SavingType saveType) {
        int saveValue = 0xff;

        for(CharacterClass characterClass : this.getClasses()) {
            int classValue = characterClass.getSavingValue(this, saveType);
            if (classValue < saveValue) {
                saveValue = classValue;
            }
        }

        return saveValue;
    }




}
