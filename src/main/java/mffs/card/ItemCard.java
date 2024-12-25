package mffs.card;

import mffs.api.card.ICard;
import mffs.base.ItemBase;

public class ItemCard extends ItemBase implements ICard {
    public ItemCard(final String name) {
        super(name);
        this.setMaxStackSize(1);
    }
}
