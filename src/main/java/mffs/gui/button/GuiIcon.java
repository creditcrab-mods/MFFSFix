package mffs.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class GuiIcon extends GuiButton {
    public static RenderItem itemRenderer;
    public ItemStack[] itemStacks;
    private int index;

    public GuiIcon(
        final int par1, final int par2, final int par3, final ItemStack... itemStacks
    ) {
        super(par1, par2, par3, 20, 20, "");
        this.index = 0;
        this.itemStacks = itemStacks;
    }

    public void setIndex(final int i) {
        if (i >= 0 && i < this.itemStacks.length) {
            this.index = i;
        }
    }

    @Override
    public void
    drawButton(final Minecraft par1Minecraft, final int par2, final int par3) {
        super.drawButton(par1Minecraft, par2, par3);
        if (this.visible && this.itemStacks[this.index] != null) {
            int yDisplacement = 2;
            if (this.itemStacks[this.index].getItem()
                    == Item.getItemFromBlock(Blocks.torch)
                || this.itemStacks[this.index].getItem()
                    == Item.getItemFromBlock(Blocks.redstone_torch)) {
                yDisplacement = 0;
            } else if (this.itemStacks[this.index].getItem() instanceof ItemBlock) {
                yDisplacement = 3;
            }
            this.drawItemStack(
                this.itemStacks[this.index],
                this.xPosition,
                this.yPosition + yDisplacement
            );
        }
    }

    protected void drawItemStack(final ItemStack itemStack, int x, int y) {
        x += 2;
        --y;
        final Minecraft mc = Minecraft.getMinecraft();
        final FontRenderer fontRenderer = mc.fontRenderer;
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glTranslatef(0.0f, 0.0f, 32.0f);
        this.zLevel = 500.0f;
        GuiIcon.itemRenderer.zLevel = 500.0f;
        GuiIcon.itemRenderer.renderItemAndEffectIntoGUI(
            fontRenderer, mc.renderEngine, itemStack, x, y
        );
        GuiIcon.itemRenderer.renderItemOverlayIntoGUI(
            fontRenderer, mc.renderEngine, itemStack, x, y
        );
        this.zLevel = 0.0f;
        GuiIcon.itemRenderer.zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
    }

    static {
        GuiIcon.itemRenderer = new RenderItem();
    }
}
