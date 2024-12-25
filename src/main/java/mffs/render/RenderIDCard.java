package mffs.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.ModularForceFieldSystem;
import mffs.api.card.ICardIdentification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.vector.Vector2;

@SideOnly(Side.CLIENT)
public class RenderIDCard implements IItemRenderer {
    private Minecraft mc;

    public RenderIDCard() {
        this.mc = Minecraft.getMinecraft();
    }

    public void renderItem(
        final IItemRenderer.ItemRenderType type,
        final ItemStack itemStack,
        final Object... data
    ) {
        if (itemStack.getItem() instanceof ICardIdentification) {
            final ICardIdentification card = (ICardIdentification) itemStack.getItem();
            GL11.glPushMatrix();
            GL11.glDisable(2884);
            this.transform(type);
            this.renderItemIcon(ModularForceFieldSystem.itemCardID.getIcon(itemStack, 0));
            if (type != IItemRenderer.ItemRenderType.INVENTORY) {
                GL11.glTranslatef(0.0f, 0.0f, -5.0E-4f);
            }
            this.renderPlayerFace(this.getSkin(card.getUsername(itemStack)));
            if (type != IItemRenderer.ItemRenderType.INVENTORY) {
                GL11.glTranslatef(0.0f, 0.0f, 0.002f);
                this.renderItemIcon(
                    ModularForceFieldSystem.itemCardID.getIcon(itemStack, 0)
                );
            }
            GL11.glEnable(2884);
            GL11.glPopMatrix();
        }
    }

    private void transform(final IItemRenderer.ItemRenderType type) {
        final float scale = 0.0625f;
        if (type != IItemRenderer.ItemRenderType.INVENTORY) {
            GL11.glScalef(scale, -scale, -scale);
            GL11.glTranslatef(20.0f, -16.0f, 0.0f);
            GL11.glRotatef(180.0f, 1.0f, 1.0f, 0.0f);
            GL11.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
        }
        if (type == IItemRenderer.ItemRenderType.ENTITY) {
            GL11.glTranslatef(20.0f, 0.0f, 0.0f);
            GL11.glRotatef(Minecraft.getSystemTime() / 12.0f % 360.0f, 0.0f, 1.0f, 0.0f);
            GL11.glTranslatef(-8.0f, 0.0f, 0.0f);
            GL11.glTranslated(
                0.0, 2.0 * Math.sin(Minecraft.getSystemTime() / 512.0 % 360.0), 0.0
            );
        }
    }

    private int getSkin(final String name) {
        return 0;
        // TODO: DAFUQ
        // try {
        // final String skin = "http://skins.minecraft.net/MinecraftSkins/" + name +
        // ".png";
        // final Minecraft mc = Minecraft.getMinecraft();
        // if (!mc.renderEngine.func_82773_c(skin)) {
        // mc.renderEngine.func_78356_a(skin, (IImageBuffer)new
        // ImageBufferDownload());
        // }
        // return mc.renderEngine.func_78350_a(skin, "/mob/char.png");
        // }
        // catch (final Exception e) {
        // e.printStackTrace();
        // return 0;
        // }
    }

    private void renderPlayerFace(final int texID) {
        final Vector2 translation = new Vector2(9.0, 5.0);
        final int xSize = 4;
        final int ySize = 4;
        final int topLX = translation.intX();
        final int topRX = translation.intX() + xSize;
        final int botLX = translation.intX();
        final int botRX = translation.intX() + xSize;
        final int topLY = translation.intY();
        final int topRY = translation.intY();
        final int botLY = translation.intY() + ySize;
        final int botRY = translation.intY() + ySize;
        GL11.glBindTexture(3553, texID);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.125f, 0.25f);
        GL11.glVertex2f((float) topLX, (float) topLY);
        GL11.glTexCoord2f(0.125f, 0.5f);
        GL11.glVertex2f((float) botLX, (float) botLY);
        GL11.glTexCoord2f(0.25f, 0.5f);
        GL11.glVertex2f((float) botRX, (float) botRY);
        GL11.glTexCoord2f(0.25f, 0.25f);
        GL11.glVertex2f((float) topRX, (float) topRY);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.625f, 0.25f);
        GL11.glVertex2f((float) topLX, (float) topLY);
        GL11.glTexCoord2f(0.625f, 0.5f);
        GL11.glVertex2f((float) botLX, (float) botLY);
        GL11.glTexCoord2f(0.75f, 0.5f);
        GL11.glVertex2f((float) botRX, (float) botRY);
        GL11.glTexCoord2f(0.75f, 0.25f);
        GL11.glVertex2f((float) topRX, (float) topRY);
        GL11.glEnd();
    }

    private void renderItemIcon(final IIcon icon) {
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture
        );
        GL11.glBegin(7);
        GL11.glTexCoord2f(icon.getMinU(), icon.getMinV());
        GL11.glVertex2f(0.0f, 0.0f);
        GL11.glTexCoord2f(icon.getMinU(), icon.getMaxV());
        GL11.glVertex2f(0.0f, 16.0f);
        GL11.glTexCoord2f(icon.getMaxU(), icon.getMaxV());
        GL11.glVertex2f(16.0f, 16.0f);
        GL11.glTexCoord2f(icon.getMaxU(), icon.getMinV());
        GL11.glVertex2f(16.0f, 0.0f);
        GL11.glEnd();
    }

    //TODO: WTF
    //@Override
    private void renderItem3D(
        final EntityLiving par1EntityLiving, final ItemStack par2ItemStack, final int par3
    ) {
        final IIcon icon = par1EntityLiving.getItemIcon(par2ItemStack, par3);
        if (icon == null) {
            GL11.glPopMatrix();
            return;
        }
        if (par2ItemStack.getItemSpriteNumber() == 0) {
            this.mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        } else {
            this.mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
        }
        final Tessellator tessellator = Tessellator.instance;
        final float f = icon.getMinU();
        final float f2 = icon.getMaxU();
        final float f3 = icon.getMinV();
        final float f4 = icon.getMaxV();
        final float f5 = 0.0f;
        final float f6 = 0.3f;
        GL11.glEnable(32826);
        GL11.glTranslatef(-f5, -f6, 0.0f);
        final float f7 = 1.5f;
        GL11.glScalef(f7, f7, f7);
        GL11.glRotatef(50.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(335.0f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(-0.9375f, -0.0625f, 0.0f);
        ItemRenderer.renderItemIn2D(
            tessellator, f2, f3, f, f4, icon.getIconWidth(), icon.getIconHeight(), 0.0625f
        );
        if (par2ItemStack != null && par2ItemStack.hasEffect(0) && par3 == 0) {
            GL11.glDepthFunc(514);
            GL11.glDisable(2896);
            // TODO: WTF
            this.mc.renderEngine.bindTexture(
                new ResourceLocation("%blur%/textures/misc/enchanted_item_glint.png")
            );
            GL11.glEnable(3042);
            GL11.glBlendFunc(768, 1);
            final float f8 = 0.76f;
            GL11.glColor4f(0.5f * f8, 0.25f * f8, 0.8f * f8, 1.0f);
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            final float f9 = 0.125f;
            GL11.glScalef(f9, f9, f9);
            float f10 = Minecraft.getSystemTime() % 3000L / 3000.0f * 8.0f;
            GL11.glTranslatef(f10, 0.0f, 0.0f);
            GL11.glRotatef(-50.0f, 0.0f, 0.0f, 1.0f);
            ItemRenderer.renderItemIn2D(
                tessellator, 0.0f, 0.0f, 1.0f, 1.0f, 256, 256, 0.0625f
            );
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(f9, f9, f9);
            f10 = Minecraft.getSystemTime() % 4873L / 4873.0f * 8.0f;
            GL11.glTranslatef(-f10, 0.0f, 0.0f);
            GL11.glRotatef(10.0f, 0.0f, 0.0f, 1.0f);
            ItemRenderer.renderItemIn2D(
                tessellator, 0.0f, 0.0f, 1.0f, 1.0f, 256, 256, 0.0625f
            );
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glDepthFunc(515);
        }
        GL11.glDisable(32826);
    }

    public boolean
    handleRenderType(final ItemStack item, final IItemRenderer.ItemRenderType type) {
        return true;
    }

    public boolean shouldUseRenderHelper(
        final IItemRenderer.ItemRenderType type,
        final ItemStack item,
        final IItemRenderer.ItemRendererHelper helper
    ) {
        return false;
    }
}
