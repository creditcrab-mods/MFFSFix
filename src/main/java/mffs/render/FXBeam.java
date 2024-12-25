package mffs.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.vector.Vector3;

@SideOnly(Side.CLIENT)
public class FXBeam extends EntityFX {
    double movX;
    double movY;
    double movZ;
    private float length;
    private float rotYaw;
    private float rotPitch;
    private float prevYaw;
    private float prevPitch;
    private Vector3 target;
    private float endModifier;
    private boolean reverse;
    private boolean pulse;
    private int rotationSpeed;
    private float prevSize;

    public FXBeam(
        final World par1World,
        final Vector3 position,
        final Vector3 target,
        final float red,
        final float green,
        final float blue,
        final int age
    ) {
        super(par1World, position.x, position.y, position.z, 0.0, 0.0, 0.0);
        this.movX = 0.0;
        this.movY = 0.0;
        this.movZ = 0.0;
        this.length = 0.0f;
        this.rotYaw = 0.0f;
        this.rotPitch = 0.0f;
        this.prevYaw = 0.0f;
        this.prevPitch = 0.0f;
        this.target = new Vector3();
        this.endModifier = 1.0f;
        this.reverse = false;
        this.pulse = true;
        this.rotationSpeed = 20;
        this.prevSize = 0.0f;
        this.setRGB(red, green, blue);
        this.setSize(0.02f, 0.02f);
        ((Entity) this).noClip = true;
        ((Entity) this).motionX = 0.0;
        ((Entity) this).motionY = 0.0;
        ((Entity) this).motionZ = 0.0;
        this.target = target;
        final float xd = (float) (((Entity) this).posX - this.target.x);
        final float yd = (float) (((Entity) this).posY - this.target.y);
        final float zd = (float) (((Entity) this).posZ - this.target.z);
        this.length = (float) new Vector3((Entity) this).distanceTo(this.target);
        final double var7 = MathHelper.sqrt_double((double) (xd * xd + zd * zd));
        this.rotYaw = (float) (Math.atan2(xd, zd) * 180.0 / 3.141592653589793);
        this.rotPitch = (float) (Math.atan2(yd, var7) * 180.0 / 3.141592653589793);
        this.prevYaw = this.rotYaw;
        this.prevPitch = this.rotPitch;
        super.particleMaxAge = age;
        final EntityLivingBase renderentity = Minecraft.getMinecraft().renderViewEntity;
        int visibleDistance = 50;
        if (!Minecraft.getMinecraft().gameSettings.fancyGraphics) {
            visibleDistance = 25;
        }
        if (renderentity.getDistance(
                ((Entity) this).posX, ((Entity) this).posY, ((Entity) this).posZ
            )
            > visibleDistance) {
            super.particleMaxAge = 0;
        }
    }

    public void onUpdate() {
        ((Entity) this).prevPosX = ((Entity) this).posX;
        ((Entity) this).prevPosY = ((Entity) this).posY;
        ((Entity) this).prevPosZ = ((Entity) this).posZ;
        this.prevYaw = this.rotYaw;
        this.prevPitch = this.rotPitch;
        final float xd = (float) (((Entity) this).posX - this.target.x);
        final float yd = (float) (((Entity) this).posY - this.target.y);
        final float zd = (float) (((Entity) this).posZ - this.target.z);
        this.length = MathHelper.sqrt_float(xd * xd + yd * yd + zd * zd);
        final double var7 = MathHelper.sqrt_double((double) (xd * xd + zd * zd));
        this.rotYaw = (float) (Math.atan2(xd, zd) * 180.0 / 3.141592653589793);
        this.rotPitch = (float) (Math.atan2(yd, var7) * 180.0 / 3.141592653589793);
        if (super.particleAge++ >= super.particleMaxAge) {
            this.setDead();
        }
    }

    public void setRGB(final float r, final float g, final float b) {
        super.particleRed = r;
        super.particleGreen = g;
        super.particleBlue = b;
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
        final float var9 = 1.0f;
        final float slide = (float) ((Entity) this).worldObj.getTotalWorldTime();
        final float rot = ((Entity) this).worldObj.provider.getWorldTime()
                % (360 / this.rotationSpeed) * this.rotationSpeed
            + this.rotationSpeed * f;
        float size = 1.0f;
        if (this.pulse) {
            size = Math.min(super.particleAge / 4.0f, 1.0f);
            size = this.prevSize + (size - this.prevSize) * f;
        }
        float op = 0.5f;
        if (this.pulse && super.particleMaxAge - super.particleAge <= 4) {
            op = 0.5f - (4 - (super.particleMaxAge - super.particleAge)) * 0.1f;
        }
        Minecraft.getMinecraft().renderEngine.bindTexture(
            new ResourceLocation("mffs", "textures/blocks/fortron.png")
        );
        GL11.glTexParameterf(3553, 10242, 10497.0f);
        GL11.glTexParameterf(3553, 10243, 10497.0f);
        GL11.glDisable(2884);
        float var10 = slide + f;
        if (this.reverse) {
            var10 *= -1.0f;
        }
        final float var11 = -var10 * 0.2f - MathHelper.floor_float(-var10 * 0.1f);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        GL11.glDepthMask(false);
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
        final float ry = this.prevYaw + (this.rotYaw - this.prevYaw) * f;
        final float rp = this.prevPitch + (this.rotPitch - this.prevPitch) * f;
        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(180.0f + ry, 0.0f, 0.0f, -1.0f);
        GL11.glRotatef(rp, 1.0f, 0.0f, 0.0f);
        final double var12 = -0.15 * size;
        final double var13 = 0.15 * size;
        final double var44b = -0.15 * size * this.endModifier;
        final double var17b = 0.15 * size * this.endModifier;
        GL11.glRotatef(rot, 0.0f, 1.0f, 0.0f);
        for (int t = 0; t < 3; ++t) {
            final double var14 = this.length * size * var9;
            final double var15 = 0.0;
            final double var16 = 1.0;
            final double var17 = -1.0f + var11 + t / 3.0f;
            final double var18 = this.length * size * var9 + var17;
            GL11.glRotatef(60.0f, 0.0f, 1.0f, 0.0f);
            tessellator.startDrawingQuads();
            tessellator.setBrightness(200);
            tessellator.setColorRGBA_F(
                super.particleRed, super.particleGreen, super.particleBlue, op
            );
            tessellator.addVertexWithUV(var44b, var14, 0.0, var16, var18);
            tessellator.addVertexWithUV(var12, 0.0, 0.0, var16, var17);
            tessellator.addVertexWithUV(var13, 0.0, 0.0, var15, var17);
            tessellator.addVertexWithUV(var17b, var14, 0.0, var15, var18);
            tessellator.draw();
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2884);
        GL11.glPopMatrix();
        tessellator.startDrawingQuads();
        this.prevSize = size;
        Minecraft.getMinecraft().renderEngine.bindTexture(
            new ResourceLocation("textures/particle/particles.png")
        );
    }
}
