package ca.quadrilateral.jua.game.item.impl;

import java.text.MessageFormat;

public class Item {
    private ItemDefinition itemDefinition = null;

    private boolean isIdentified = false;
    private boolean isEquipped = false;
    private int charges = -1;
    private int bundleSize = 0;

    public Item() {}

    public Item(ItemDefinition itemDefinition) {
        this.itemDefinition = itemDefinition;
    }

    public ItemDefinition getItemDefinition() {
        return this.itemDefinition;
    }

    public void setItemDefinition(ItemDefinition itemDefinition) {
        this.itemDefinition = itemDefinition;
    }

    public String getName() {
        String name = null;
        if (isIdentified) {
            name = itemDefinition.getIdentifiedName();
        } else {
            name = itemDefinition.getBaseName();
        }
        if (bundleSize > 1) {
            return MessageFormat.format("{0} ({1})", name, bundleSize);
        } else {
            return name;
        }
    }

    public boolean isEquipped() {
        return this.isEquipped;
    }

    public void setEquipped(boolean isEquipped) {
        this.isEquipped = isEquipped;
    }

    public void decrementCharges() {
        charges--;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }

    public int getCharges() {
        return this.charges;
    }

    public void setBundleSize(int bundleSize) {
        this.bundleSize = bundleSize;
    }

    public int getBundleSize() {
        return this.bundleSize;
    }

    public boolean hasCharges() {
        return charges > 0 || charges == -1;
    }

    public boolean isIdentified() {
        return this.isIdentified;
    }

    public void setIdentified(boolean isIdentified) {
        this.isIdentified = isIdentified;
    }

    public Item cloneItem() {
        final Item newItem = new Item(this.itemDefinition);
        newItem.setCharges(this.charges);
        newItem.setIdentified(this.isIdentified);
        newItem.setEquipped(this.isEquipped);
        return newItem;
    }
}
