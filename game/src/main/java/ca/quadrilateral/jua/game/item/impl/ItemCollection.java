package ca.quadrilateral.jua.game.item.impl;

import java.util.ArrayList;

public class ItemCollection extends ArrayList<Item> {
    private static final long serialVersionUID = 1L;


    public double getItemCollectionWeight() {
        double weight = 0d;
        for(Item item : this) {
            weight += item.getItemDefinition().getWeight();
        }
        return weight;
    }

    public ItemCollection cloneItemCollection() {
        final ItemCollection newItemCollection = new ItemCollection();
        for(Item item : this) {
            newItemCollection.add(item.cloneItem());
        }
        return newItemCollection;
    }
}
