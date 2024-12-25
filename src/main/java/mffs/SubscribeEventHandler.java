package mffs;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.api.security.IInterdictionMatrix;
import mffs.api.security.Permission;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.FluidRegistry;
import universalelectricity.core.vector.Vector3;

public class SubscribeEventHandler {
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void textureHook(final TextureStitchEvent.Pre event) {
        if (event.map.getTextureType() == 1) {
            FluidRegistry.getFluid("fortron").setIcons(
                event.map.registerIcon("mffs:fortron")
            );
        }
    }

    @SubscribeEvent
    public void playerInteractEvent(final PlayerInteractEvent evt) {
        if (evt.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK
            || evt.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            if (evt.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK
                && ((Entity) ((PlayerEvent) evt).entityPlayer)
                        .worldObj.getBlock(evt.x, evt.y, evt.z)
                    == ModularForceFieldSystem.blockForceField) {
                evt.setCanceled(true);
                return;
            }
            if (((PlayerEvent) evt).entityPlayer.capabilities.isCreativeMode) {
                return;
            }
            final Vector3 position = new Vector3(evt.x, evt.y, evt.z);
            final IInterdictionMatrix interdictionMatrix
                = MFFSHelper.getNearestInterdictionMatrix(
                    ((Entity) ((PlayerEvent) evt).entityPlayer).worldObj, position
                );
            if (interdictionMatrix != null) {
                final Block block = position.getBlock(
                    (IBlockAccess) ((Entity) ((PlayerEvent) evt).entityPlayer).worldObj
                );
                if (ModularForceFieldSystem.blockBiometricIdentifier == block
                    && MFFSHelper.isPermittedByInterdictionMatrix(
                        interdictionMatrix,
                        ((PlayerEvent) evt).entityPlayer.getDisplayName(),
                        Permission.SECURITY_CENTER_CONFIGURE
                    )) {
                    return;
                }
                final boolean hasPermission = MFFSHelper.hasPermission(
                    ((Entity) ((PlayerEvent) evt).entityPlayer).worldObj,
                    new Vector3(evt.x, evt.y, evt.z),
                    interdictionMatrix,
                    evt.action,
                    ((PlayerEvent) evt).entityPlayer
                );
                if (!hasPermission) {
                    ((PlayerEvent) evt)
                        .entityPlayer.addChatMessage(new ChatComponentText(
                            "["
                            + ModularForceFieldSystem.blockInterdictionMatrix
                                  .getLocalizedName()
                            + "] You have no permission to do that!"
                        ));
                    evt.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void livingSpawnEvent(final LivingSpawnEvent evt) {
        final IInterdictionMatrix interdictionMatrix
            = MFFSHelper.getNearestInterdictionMatrix(
                evt.world, new Vector3((Entity) evt.entityLiving)
            );
        if (interdictionMatrix != null
            && interdictionMatrix.getModuleCount(
                   ModularForceFieldSystem.itemModuleAntiSpawn, new int[0]
               ) > 0) {
            evt.setResult(Result.DENY);
        }
    }
}
