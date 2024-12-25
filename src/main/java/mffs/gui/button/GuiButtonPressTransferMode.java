//
// Decompiled by Procyon v0.6.0
//

package mffs.gui.button;

import mffs.base.GuiBase;
import mffs.tileentity.TileEntityFortronCapacitor;
import net.minecraft.client.Minecraft;
import universalelectricity.core.vector.Vector2;

public class GuiButtonPressTransferMode extends GuiButtonPress {
    private TileEntityFortronCapacitor tileEntity;

    public GuiButtonPressTransferMode(
        final int id,
        final int x,
        final int y,
        final GuiBase mainGui,
        final TileEntityFortronCapacitor tileEntity
    ) {
        super(id, x, y, new Vector2(), mainGui);
        this.tileEntity = tileEntity;
    }

    @Override
    public void drawButton(final Minecraft minecraft, final int x, final int y) {
        String transferName = this.tileEntity.getTransferMode().name().toLowerCase();
        final char first = Character.toUpperCase(transferName.charAt(0));
        transferName = first + transferName.substring(1);
        this.displayString = "transferMode" + transferName;
        super.offset.y = 18 * this.tileEntity.getTransferMode().ordinal();
        super.drawButton(minecraft, x, y);
    }
}
