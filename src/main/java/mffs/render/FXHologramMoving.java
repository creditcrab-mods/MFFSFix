package mffs.render;

import calclavia.lib.render.CalclaviaRenderHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.ModularForceFieldSystem;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.vector.Vector3;

@SideOnly(Side.CLIENT)
public class FXHologramMoving extends EntityFX {
    public FXHologramMoving(
        final World par1World,
        final Vector3 position,
        final float red,
        final float green,
        final float blue,
        final int age
    ) {
        super(par1World, position.x, position.y, position.z);
        this.setRBGColorF(red, green, blue);
        super.particleMaxAge = age;
        ((Entity) this).noClip = true;
    }

    public void onUpdate() {
        ((Entity) this).prevPosX = ((Entity) this).posX;
        ((Entity) this).prevPosY = ((Entity) this).posY;
        ((Entity) this).prevPosZ = ((Entity) this).posZ;
        if (super.particleAge++ >= super.particleMaxAge) {
            this.setDead();
        }
    }

    public void renderParticle(
        final Tessellator tessellator,
        final float f,
        final float f1,
        final float f2,
        final float f3,
        final float f4,
        final float f5
    ) {
        tessellator.draw();
        GL11.glPushMatrix();
        final float xx = (float
        ) (((Entity) this).prevPosX
           + (((Entity) this).posX - ((Entity) this).prevPosX) * f - EntityFX.interpPosX);
        final float yy = (float
        ) (((Entity) this).prevPosY
           + (((Entity) this).posY - ((Entity) this).prevPosY) * f - EntityFX.interpPosY);
        final float zz = (float
        ) (((Entity) this).prevPosZ
           + (((Entity) this).posZ - ((Entity) this).prevPosZ) * f - EntityFX.interpPosZ);
        GL11.glTranslated((double) xx, (double) yy, (double) zz);
        GL11.glScalef(1.01f, 1.01f, 1.01f);
        final double completion = super.particleAge / (double) super.particleMaxAge;
        GL11.glTranslated(0.0, (completion - 1.0) / 2.0, 0.0);
        GL11.glScaled(1.0, completion, 1.0);
        float op = 0.5f;
        if (super.particleMaxAge - super.particleAge <= 4) {
            op = 0.5f - (5 - (super.particleMaxAge - super.particleAge)) * 0.1f;
        }
        GL11.glColor4d(
            (double) super.particleRed,
            (double) super.particleGreen,
            (double) super.particleBlue,
            (double) (op * 2.0f)
        );
        CalclaviaRenderHelper.disableLighting();
        CalclaviaRenderHelper.enableBlending();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture
        );
        CalclaviaRenderHelper.renderNormalBlockAsItem(
            (Block) ModularForceFieldSystem.blockForceField, 0, new RenderBlocks()
        );
        CalclaviaRenderHelper.disableBlending();
        CalclaviaRenderHelper.enableLighting();
        GL11.glPopMatrix();
        tessellator.startDrawingQuads();
        Minecraft.getMinecraft().renderEngine.bindTexture(
            new ResourceLocation("textures/particle/particles.png")
        );
    }
}
