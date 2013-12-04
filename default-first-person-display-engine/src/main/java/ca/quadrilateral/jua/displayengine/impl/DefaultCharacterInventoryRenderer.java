package ca.quadrilateral.jua.displayengine.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ca.quadrilateral.jua.displayengine.renderer.ILevelRenderer;
import ca.quadrilateral.jua.game.IGameConfiguration;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IMapCell;
import ca.quadrilateral.jua.game.entity.impl.PartyMember;
import ca.quadrilateral.jua.game.item.impl.Item;
import ca.quadrilateral.jua.game.item.impl.ItemCollection;

public class DefaultCharacterInventoryRenderer implements ILevelRenderer {

    @Autowired
    private IGameConfiguration gameConfigurationManager;

    @Autowired
    private ILevelContext levelContext;

    @Autowired
    private IGameStateMachine gameStateMachine;

    @Override
    public void render(Graphics2D graphics, List<IMapCell> cellsToRender) {
        final Color initialColor = graphics.getColor();
        final Font initialFont = graphics.getFont();


        final PartyMember partyMember = levelContext.getParty().getPartyMember(gameStateMachine.getSelectedPartyMemberIndex());

        graphics.setColor(Color.WHITE);
        graphics.setFont(gameConfigurationManager.getBoldGameFont());

        graphics.drawString(partyMember.getName() + "'s Inventory", 40, 50);

        graphics.drawString("Ready", 40, 100);
        graphics.drawString("Item", 140, 100);

        graphics.setFont(gameConfigurationManager.getGameFont());
        
        ItemCollection inventory = partyMember.getInventory();
        int index = 0;
        for(Item item : inventory) {
            graphics.drawString(item.getName(), 140, 140 + (index * 25));
            graphics.drawString(item.isEquipped() ? "Yes" : "No", 40, 140 + (index * 25));
            index++;
        }


        graphics.setColor(initialColor);
        graphics.setFont(initialFont);
    }

}
