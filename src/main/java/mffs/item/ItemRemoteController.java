package mffs.item;

import java.util.List;
import java.util.Set;

import mffs.MFFSHelper;
import mffs.ModularForceFieldSystem;
import mffs.api.card.ICardLink;
import mffs.api.fortron.IFortronFrequency;
import mffs.api.security.Permission;
import mffs.fortron.FrequencyGrid;
import mffs.item.card.ItemCardFrequency;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.core.vector.Vector3;

public class ItemRemoteController extends ItemCardFrequency implements ICardLink {
    public ItemRemoteController() {
        super("remoteController");
    }

    @Override
    public void addInformation(
        final ItemStack itemStack,
        final EntityPlayer player,
        final List list,
        final boolean b
    ) {
        super.addInformation(itemStack, player, list, b);
        final Vector3 position = this.getLink(itemStack);
        if (position != null) {
            final Block blockId = position.getBlock(player.worldObj);
            if (blockId != null) {
                list.add("Linked with: " + blockId.getLocalizedName());
                list.add(
                    position.intX() + ", " + position.intY() + ", " + position.intZ()
                );
                return;
            }
        }
        list.add("Not linked.");
    }

    @Override
    public boolean onItemUse(
        final ItemStack itemStack,
        final EntityPlayer player,
        final World world,
        final int x,
        final int y,
        final int z,
        final int side,
        final float hitX,
        final float hitY,
        final float hitZ
    ) {
        if (player.isSneaking()) {
            if (!world.isRemote) {
                final Vector3 vector = new Vector3(x, y, z);
                this.setLink(itemStack, vector);
                if (vector.getBlock((IBlockAccess) world) != null) {
                    player.addChatMessage(new ChatComponentText(
                        "Linked remote to position: " + x + ", " + y + ", " + z
                        + " with block: " + vector.getBlock(world).getLocalizedName()
                    ));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(
        final ItemStack itemStack, final World world, final EntityPlayer entityPlayer
    ) {
        if (!entityPlayer.isSneaking()) {
            final Vector3 position = this.getLink(itemStack);
            if (position != null) {
                final Block blockId = position.getBlock(world);
                if (blockId != null) {
                    final Chunk chunk
                        = world.getChunkFromBlockCoords(position.intX(), position.intZ());
                    if (chunk != null && chunk.isChunkLoaded
                        && (MFFSHelper.hasPermission(
                                world,
                                position,
                                PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK,
                                entityPlayer
                            )
                            || MFFSHelper.hasPermission(
                                world, position, Permission.REMOTE_CONTROL, entityPlayer
                            ))) {
                        final double requiredEnergy
                            = Vector3.distance(
                                  new Vector3((Entity) entityPlayer), position
                              )
                            * 10.0;
                        int receivedEnergy = 0;
                        final Set<IFortronFrequency> fortronTiles
                            = FrequencyGrid.instance().getFortronTiles(
                                world,
                                new Vector3((Entity) entityPlayer),
                                50,
                                this.getFrequency(itemStack)
                            );
                        for (final IFortronFrequency fortronTile : fortronTiles) {
                            final int consumedEnergy = fortronTile.requestFortron(
                                (int) Math.ceil(requiredEnergy / fortronTiles.size()),
                                true
                            );
                            if (consumedEnergy > 0) {
                                if (world.isRemote) {
                                    ModularForceFieldSystem.proxy.renderBeam(
                                        world,
                                        new Vector3((Entity) entityPlayer)
                                            .add(new Vector3(
                                                0.0,
                                                entityPlayer.getEyeHeight() - 0.2,
                                                0.0
                                            )),
                                        new Vector3((TileEntity) fortronTile).add(0.5),
                                        0.6f,
                                        0.6f,
                                        1.0f,
                                        20
                                    );
                                }
                                receivedEnergy += consumedEnergy;
                            }
                            if (receivedEnergy >= requiredEnergy) {
                                try {
                                    blockId.onBlockActivated(
                                        world,
                                        position.intX(),
                                        position.intY(),
                                        position.intZ(),
                                        entityPlayer,
                                        0,
                                        0.0f,
                                        0.0f,
                                        0.0f
                                    );
                                } catch (final Exception e) {
                                    e.printStackTrace();
                                }
                                return itemStack;
                            }
                        }
                        if (!world.isRemote) {
                            entityPlayer.addChatMessage(new ChatComponentText(
                                "Unable to harness "
                                + UnitDisplay.getDisplay(
                                    requiredEnergy, UnitDisplay.Unit.JOULES
                                )
                                + " from the Fortron field."
                            ));
                        }
                    }
                }
            }
        }
        return itemStack;
    }

    @Override
    public void setLink(final ItemStack itemStack, final Vector3 position) {
        final NBTTagCompound nbt = MFFSHelper.getNBTTagCompound(itemStack);
        nbt.setTag("position", position.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public Vector3 getLink(final ItemStack itemStack) {
        final NBTTagCompound nbt = MFFSHelper.getNBTTagCompound(itemStack);
        return Vector3.readFromNBT(nbt.getCompoundTag("position"));
    }
}
