package ca.quadrilateral.jua.game.border;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import ca.quadrilateral.jua.game.enums.BorderStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

public class BorderDefinition implements IBorderDefinition {
    private UUID borderDefinitionId = null;
    private Map<BorderStyle, List<IBorderDefinitionItem>> borderDefinitionItems;

    public BorderDefinition() {
        this.borderDefinitionItems = new HashMap<BorderStyle, List<IBorderDefinitionItem>>();
    }

    public BorderDefinition(UUID borderDefinitionId) {
        this.borderDefinitionId = borderDefinitionId;
        this.borderDefinitionItems = new HashMap<BorderStyle, List<IBorderDefinitionItem>>();
    }

    @Override
    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this).append("Definition ID", borderDefinitionId);
        if (borderDefinitionItems != null) {

            for(List<IBorderDefinitionItem> itemList : borderDefinitionItems.values()) {
                for(IBorderDefinitionItem item : itemList) {
                    toStringBuilder.appendToString(item.toString());
                }
            }
        }
        return toStringBuilder.toString();
    }

    @Override
    public UUID getBorderDefinitionId() {
        return this.borderDefinitionId;
    }

    @Override
    public void setBorderDefinitionId(UUID borderDefinitionId) {
        this.borderDefinitionId = borderDefinitionId;
    }

    @Override
    public List<IBorderDefinitionItem> getBorderDefinitionItems(BorderStyle borderStyle) {
        if (this.borderDefinitionItems.containsKey(borderStyle)) {
            return this.borderDefinitionItems.get(borderStyle);
        }
        return null;
    }

    public void addBorderDefinitionItem(BorderStyle borderStyle, IBorderDefinitionItem borderDefinitionItem) {
        if (!this.borderDefinitionItems.containsKey(borderStyle)) {
            this.borderDefinitionItems.put(borderStyle, new ArrayList<IBorderDefinitionItem>());
        }
        this.borderDefinitionItems.get(borderStyle).add(borderDefinitionItem);
    }

    public void setBorderDefinitionItems(BorderStyle borderStyle, List<IBorderDefinitionItem> newBorderDefinitionItems) {
        this.borderDefinitionItems.put(borderStyle, newBorderDefinitionItems);
    }
}
