//
// Decompiled by Procyon v0.6.0
//

package mffs.gui.button;

import mffs.base.GuiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.vector.Vector2;
import universalelectricity.prefab.TranslationHelper;

public class GuiButtonPress extends GuiButton {
    protected Vector2 offset;
    public boolean stuck;
    private GuiBase mainGui;

    public GuiButtonPress(
        final int id,
        final int x,
        final int y,
        final Vector2 offset,
        final GuiBase mainGui,
        final String name
    ) {
        super(id, x, y, 18, 18, name);
        this.offset = new Vector2();
        this.stuck = false;
        this.offset = offset;
        this.mainGui = mainGui;
    }

    public GuiButtonPress(
        final int id,
        final int x,
        final int y,
        final Vector2 offset,
        final GuiBase mainGui
    ) {
        this(id, x, y, offset, mainGui, "");
    }

    public GuiButtonPress(final int id, final int x, final int y, final Vector2 offset) {
        this(id, x, y, offset, null, "");
    }

    public GuiButtonPress(final int id, final int x, final int y) {
        this(id, x, y, new Vector2());
    }

    @Override
    public void drawButton(final Minecraft minecraft, final int x, final int y) {
        if (this.visible) {
            Minecraft.getMinecraft().renderEngine.bindTexture(
                new ResourceLocation("mffs", "textures/gui/gui_button.png")
            );
            if (this.stuck) {
                GL11.glColor4f(0.6f, 0.6f, 0.6f, 1.0f);
            } else if (this.isPointInRegion(
                           this.xPosition, this.yPosition, this.width, this.height, x, y
                       )) {
                GL11.glColor4f(0.85f, 0.85f, 0.85f, 1.0f);
            } else {
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }
            this.drawTexturedModalRect(
                this.xPosition,
                this.yPosition,
                this.offset.intX(),
                this.offset.intY(),
                this.width,
                this.height
            );
            this.mouseDragged(minecraft, x, y);
        }
    }

    @Override
    protected void mouseDragged(final Minecraft minecraft, final int x, final int y) {
        if (this.mainGui != null && this.displayString != null
            && this.displayString.length() > 0
            && this.isPointInRegion(
                this.xPosition, this.yPosition, this.width, this.height, x, y
            )) {
            final String title
                = TranslationHelper.getLocal("gui." + this.displayString + ".name");
            this.mainGui.tooltip
                = TranslationHelper.getLocal("gui." + this.displayString + ".tooltip");
            if (title != null && title.length() > 0) {
                this.mainGui.tooltip = title + ": " + this.mainGui.tooltip;
            }
        }
    }

    protected boolean isPointInRegion(
        final int x,
        final int y,
        final int width,
        final int height,
        int checkX,
        int checkY
    ) {
        final int var7 = 0;
        final int var8 = 0;
        checkX -= var7;
        checkY -= var8;
        return checkX >= x - 1 && checkX < x + width + 1 && checkY >= y - 1
            && checkY < y + height + 1;
    }
}
