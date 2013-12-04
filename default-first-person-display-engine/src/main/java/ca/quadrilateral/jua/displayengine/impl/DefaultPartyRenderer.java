package ca.quadrilateral.jua.displayengine.impl;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ca.quadrilateral.jua.displayengine.renderer.ILevelRenderer;
import ca.quadrilateral.jua.game.IGameConfiguration;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IMapCell;
import ca.quadrilateral.jua.game.entity.impl.PartyMember;

public class DefaultPartyRenderer implements ILevelRenderer {
    private static final int REGULAR_RENDER = 0;
    private static final int CAN_TRAIN_RENDER = 1;
    private static final int DOWN_RENDER = 2;

    private final Color columnHeaderColor = new Color(0x00CC00);
    private final Color partyTextLightGradientColor = Color.WHITE;
    private final Color partyTextDarkGradientColor = Color.WHITE.darker();

    private Paint titlePaint;
    private Paint regularPaint;
    private Paint canTrainPaint;

    private boolean paintersAreInitialized = false;

    private long firstRenderTick = 0;
    private long pulseDuration = 2000;

    @Autowired
    private ILevelContext levelContext;

    @Autowired
    private IGameConfiguration gameConfigurationManager;

    @Autowired
    private IGameStateMachine stateMachine;

    public void createPainters() {
        if (!paintersAreInitialized) {
            if (gameConfigurationManager.useGradientRender()) {
                titlePaint = new GradientPaint(0, 20, columnHeaderColor, 0, 40, columnHeaderColor.darker().darker());
                regularPaint = new GradientPaint(0, 60, partyTextLightGradientColor, 0, 80, partyTextDarkGradientColor);
                canTrainPaint = new GradientPaint(0, 60, Color.GREEN, 0, 80, Color.GREEN.darker().darker());
            } else {
                titlePaint = columnHeaderColor;
                regularPaint = partyTextLightGradientColor;
                canTrainPaint = Color.GREEN;
            }
            paintersAreInitialized = true;
        }
    }

    @Override
    public void render(Graphics2D graphics, List<IMapCell> cellsToRender) {
        createPainters();

        final List<PartyMember> party = levelContext.getParty().getPartyMembers();

        final Color initialColor = graphics.getColor();
        final Font initialFont = graphics.getFont();
        final Font gameFont = gameConfigurationManager.getGameFont();
        final Font boldGameFont = gameConfigurationManager.getBoldGameFont();
        final Paint initialPaint = graphics.getPaint();

        if (firstRenderTick == 0) {
            firstRenderTick = System.currentTimeMillis();
        }

        graphics.setFont(boldGameFont);

        graphics.setPaint(titlePaint);

        graphics.drawString("Name", 460, 40);
        graphics.drawString("HP", 900, 40);
        graphics.drawString("AC", 950, 40);


        graphics.setFont(gameFont);
        graphics.setColor(Color.WHITE);

        for(int i = 0; i < party.size(); i++) {
            final PartyMember character = party.get(i);
            final int yPosition = 80 + (i * 30);

            Paint painter = null;

            if (character.getCanAdvanceLevelFlag()) {
                painter = canTrainPaint;
            } else {
                painter = regularPaint;
            }

            graphics.setPaint(painter);
            renderPartyLine(graphics, character, yPosition);

            if (stateMachine.getSelectedPartyMemberIndex() == i) {
                final Composite initialComposite = graphics.getComposite();

                updateAlphaComposite(graphics);

                graphics.setColor(Color.RED);
                renderPartyLine(graphics, character, yPosition);


                graphics.setComposite(initialComposite);
            }
        }

        graphics.setPaint(initialPaint);
        graphics.setFont(initialFont);
        graphics.setColor(initialColor);
    }

    private void renderPartyLine(Graphics2D graphics, PartyMember character, int yPosition) {
        AffineTransform initialTransform = graphics.getTransform();

        graphics.setTransform(AffineTransform.getTranslateInstance(0, yPosition - 80));

        graphics.drawString(character.getName(), 460, 80);


        graphics.drawString(Integer.toString(character.getCurrentHitPoints()), 900, 80);
        graphics.drawString(Integer.toString(character.getEffectiveArmorClass()), 950, 80);


        graphics.setTransform(initialTransform);
    }

      private void updateAlphaComposite(Graphics2D graphics) {
        float alphaValue = calculateAlphaValue();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    private float calculateAlphaValue() {
        final long ticksSinceRenderStart = System.currentTimeMillis() - firstRenderTick;

        final long halfDuration = pulseDuration / 2;
        final long pulseModulus = ticksSinceRenderStart % (pulseDuration);

        if (pulseModulus > halfDuration) {
            return (float)((halfDuration - (pulseModulus - halfDuration)) / (1.0 * halfDuration));
        } else {
            return (float)(pulseModulus / (1.0 * halfDuration));
        }
    }


}
