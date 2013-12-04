package ca.quadrilateral.jua.displayengine.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ca.quadrilateral.jua.displayengine.renderer.ILevelRenderer;
import ca.quadrilateral.jua.game.IGameConfiguration;
import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IMapCell;
import ca.quadrilateral.jua.game.entity.impl.EntityAttribute;
import ca.quadrilateral.jua.game.entity.impl.PartyMember;
import ca.quadrilateral.jua.game.enums.EntityAttributeType;
import ca.quadrilateral.jua.game.impl.Currency;
import ca.quadrilateral.jua.game.impl.Purse;

public class DefaultCharacterInfoRenderer implements ILevelRenderer {
    @Autowired
    private ILevelContext levelContext;

    @Autowired
    private IGameContext gameContext;

    @Autowired
    private IGameStateMachine gameStateMachine;

    @Autowired
    private IGameConfiguration gameConfigurationManager;

    private final Color columnHeaderColor = new Color(0x00CC00);
    private final Paint titlePaint = new GradientPaint(0, 20, columnHeaderColor, 0, 40, columnHeaderColor.darker().darker());
    private final Color partyTextLightGradientColor = Color.WHITE;
    private final Color partyTextDarkGradientColor = Color.WHITE.darker();

    private final Paint regularPaint = new GradientPaint(0, 60, partyTextLightGradientColor, 0, 80, partyTextDarkGradientColor);
    private final Paint canTrainPaint = new GradientPaint(0, 60, Color.GREEN, 0, 80, Color.GREEN.darker().darker());

    private int getAttributeLabelWidth(Graphics2D graphics) {
        int maxWidth = 0;

        final FontMetrics fontMetrics = graphics.getFontMetrics(gameConfigurationManager.getBoldGameFont());

        for (EntityAttributeType attributeType : EntityAttributeType.values()) {
            final Rectangle2D stringBounds =
                                    fontMetrics
                                        .getStringBounds(attributeType.getText(), graphics);
            if ((int)stringBounds.getWidth() > maxWidth) {
                maxWidth = (int)stringBounds.getWidth();
            }
        }

        return maxWidth;
    }


    private void renderAttribute(Graphics2D graphics, PartyMember partyMember, EntityAttributeType attributeType, int xVal, int yVal) {
        final Font initialFont = graphics.getFont();

        graphics.setFont(gameConfigurationManager.getBoldGameFont());

        graphics.drawString(attributeType.getText() + ":", 30, yVal);

        final EntityAttribute entityAttribute = partyMember.getEntityAttribute(attributeType);
        final int effectiveValue = entityAttribute.getEffectiveValue();
        final int partialValue = entityAttribute.getEffectivePartialValue();

        String partialValueString = "";
        if (partialValue > 0) {
            partialValueString = " (" + partialValue + ")";
        }

        String valueToRender = effectiveValue + partialValueString;

        graphics.setFont(gameConfigurationManager.getGameFont());

        graphics.drawString(valueToRender, xVal, yVal);

        graphics.setFont(initialFont);
    }




    @Override
    public void render(Graphics2D graphics, List<IMapCell> cellsToRender) {
        final PartyMember partyMember = levelContext.getParty().getPartyMember(gameStateMachine.getSelectedPartyMemberIndex());

        final Color initialColor = graphics.getColor();
        final Font initialFont = graphics.getFont();
        final Font gameFont = gameConfigurationManager.getGameFont();
        final Font boldGameFont = gameConfigurationManager.getBoldGameFont();
        final Paint initialPaint = graphics.getPaint();

        graphics.setFont(gameFont);
        graphics.setColor(Color.WHITE);

        graphics.drawString(partyMember.getName(), 30, 40);
        graphics.drawString("Status: " + "Some Status", 500, 40);

        graphics.drawString(partyMember.getGender().getText() + " - " + partyMember.getAgeString(), 30, 80);
        graphics.drawString("Hit Points: " + partyMember.getCurrentHitPoints() + "/" + partyMember.getHitPointTotal(), 500, 80);

        graphics.drawString(partyMember.getAlignment().getName(), 30, 100);
        graphics.drawString(partyMember.getActiveCharacterClass().getName(), 30, 120);

        graphics.drawString(partyMember.getCharacterRace().getName(), 500, 100);

        graphics.drawString("Level: " + partyMember.getLevelInCharacterClass(partyMember.getActiveCharacterClass()), 30, 160);

        graphics.drawString(MessageFormat.format("XP: {0}", partyMember.getExperiencePoints()), 500, 160);

        //Stats

        int attributeX = getAttributeLabelWidth(graphics) + 40;

        renderAttribute(graphics, partyMember, EntityAttributeType.Strength, attributeX, 200);
        renderAttribute(graphics, partyMember, EntityAttributeType.Intelligence, attributeX, 225);
        renderAttribute(graphics, partyMember, EntityAttributeType.Wisdom, attributeX, 250);
        renderAttribute(graphics, partyMember, EntityAttributeType.Dexterity, attributeX, 275);
        renderAttribute(graphics, partyMember, EntityAttributeType.Constitution, attributeX, 300);
        renderAttribute(graphics, partyMember, EntityAttributeType.Charisma, attributeX, 325);


        final Purse purse = partyMember.getPurse();
        final Collection<Currency> currenciesInPurse = purse.getCurrenciesInPurse();

        int index = 0;
        for(Currency currency : currenciesInPurse) {
            graphics.setFont(boldGameFont);
            graphics.drawString(currency.getName(), 500, 200 + (index * 25));
            graphics.setFont(gameFont);

            final String currencyAmount = MessageFormat.format("{0}", purse.getCurrencyAmount(currency));
            final Rectangle2D stringBounds = graphics.getFontMetrics().getStringBounds(currencyAmount, graphics);

            graphics.drawString(currencyAmount, 994 - (int)stringBounds.getWidth(), 200 + (index * 25));
            index++;
        }

        String armorClass = Integer.toString(partyMember.getEffectiveArmorClass());
        String thac0 = Integer.toString(partyMember.getEffectiveThac0());
        String damage = "xd10+x";

        graphics.setFont(boldGameFont);

        graphics.drawString("Armor Class:", 30, 425);
        graphics.drawString("Thac0:", 30, 450);
        graphics.drawString("Damage:", 30, 475);

        graphics.drawString("Encumbrance:", 500, 425);
        graphics.drawString("Movement:", 500, 450);

        graphics.setFont(gameFont);

        renderRightJustified(graphics, armorClass, 470, 425);
        renderRightJustified(graphics, thac0, 470, 450);
        renderRightJustified(graphics, damage, 470, 475);

        renderRightJustified(graphics, "Lots", 994, 425);
        renderRightJustified(graphics, "Slow", 994, 450);


        graphics.setPaint(initialPaint);
        graphics.setFont(initialFont);
        graphics.setColor(initialColor);
    }

    private void renderRightJustified(Graphics2D graphics, String textToRender, int base, int yVal) {
        final int width = (int)graphics.getFontMetrics().getStringBounds(textToRender, graphics).getWidth();
        graphics.drawString(textToRender, base - width, yVal);
    }

}
