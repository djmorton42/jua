package ca.quadrilateral.jua.game.entity;

import java.util.Set;
import ca.quadrilateral.jua.game.entity.impl.Alignment;
import ca.quadrilateral.jua.game.entity.impl.CharacterClass;
import ca.quadrilateral.jua.game.entity.impl.CharacterRace;
import ca.quadrilateral.jua.game.entity.impl.EntityAttribute;
import ca.quadrilateral.jua.game.enums.EntityAbilityType;
import ca.quadrilateral.jua.game.enums.EntityAttributeType;
import ca.quadrilateral.jua.game.enums.Gender;
import ca.quadrilateral.jua.game.enums.SavingType;
import ca.quadrilateral.jua.game.impl.Purse;
import ca.quadrilateral.jua.game.item.impl.Item;

public interface IEntity {
    String getName();
    CharacterRace getCharacterRace();
    Set<CharacterClass> getClasses();
    Gender getGender();
    int getLevelInCharacterClass(CharacterClass characterClass);

    Purse getPurse();

    void addAbility(IAbility ability);
    IAbility getAbility(EntityAbilityType abilityType);


    boolean doesActiveClassExceedOtherClassLevels();

    Alignment getAlignment();

    int getCurrentHitPoints();
    int getHitPointTotal();
    int getEffectiveArmorClass();
    int getBaseArmorClass();

    boolean isDisabled();

    void takeDamage(int damageAmount);

    boolean saveVersus(SavingType saveType);
    boolean saveVersus(SavingType saveType, int savingModifier);

    int getBaseThac0();
    int getEffectiveThac0();

    EntityAttribute getEntityAttribute(EntityAttributeType type);

    boolean isDamaged();
    boolean isMultiClassed();
    boolean isDualClassed();
    CharacterClass getActiveCharacterClass();

    int getAgeUnits();
    void setAgeUnits(int ageUnits);
    void incrementAge();
    void addAgeUnits(int ageUnits);
    boolean canEquip(Item item);


}
