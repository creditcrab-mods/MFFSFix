package calclavia.lib.render;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;

@SideOnly(Side.CLIENT)
public abstract class RenderTaggedTile extends TileEntitySpecialRenderer {
    @Override
    public void renderTileEntityAt(TileEntity t, double x, double y, double z, float f) {
        if (t != null && t instanceof ITagRender
            && this.getPlayer().getDistance(
                   (double) t.xCoord, (double) t.yCoord, (double) t.zCoord
               ) <= (double) RendererLivingEntity.NAME_TAG_RANGE) {
            HashMap tags = new HashMap();
            float height = ((ITagRender) t).addInformation(tags, this.getPlayer());
            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            if (player.ridingEntity == null) {
                MovingObjectPosition objectPosition = player.rayTrace(8.0D, 1.0F);
                if (objectPosition != null) {
                    boolean isLooking = false;

                    for (int it = 0; (float) it < height; ++it) {
                        if (objectPosition.blockX == t.xCoord
                            && objectPosition.blockY == t.yCoord + it
                            && objectPosition.blockZ == t.zCoord) {
                            isLooking = true;
                        }
                    }

                    if (isLooking) {
                        Iterator var17 = tags.entrySet().iterator();

                        for (int i = 0; var17.hasNext(); ++i) {
                            Entry entry = (Entry) var17.next();
                            if (entry.getKey() != null) {
                                CalclaviaRenderHelper.renderFloatingText(
                                    (String) entry.getKey(),
                                    (float) x + 0.5F,
                                    (float) y + (float) i * 0.25F - 2.0F + height,
                                    (float) z + 0.5F,
                                    ((Integer) entry.getValue()).intValue()
                                );
                            }
                        }
                    }
                }
            }
        }
    }

    public EntityPlayer getPlayer() {
        EntityLivingBase entity = this.field_147501_a.field_147551_g;
        return entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
    }
}
