package mffs.tileentity;

import mffs.MFFSHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.IBlockAccess;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public class TileEntityForceField extends TileEntityAdvanced {
    private Vector3 projector;
    public ItemStack camoStack;

    public TileEntityForceField() {
        this.projector = null;
        this.camoStack = null;
    }

    public boolean canUpdate() {
        return false;
    }

    @Override
    public Packet getDescriptionPacket() {
        if (this.getProjector() != null) {
            NBTTagCompound nbt = new NBTTagCompound();

            if (this.camoStack != null) {
                nbt.setTag("projector", this.projector.writeToNBT(new NBTTagCompound()));
                nbt.setInteger("itemID", Item.getIdFromItem(this.camoStack.getItem()));
                nbt.setInteger("itemMetadata", this.camoStack.getItemDamage());
            }
            return new S35PacketUpdateTileEntity(
                this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
            );
        }
        return null;
    }

    @Override
    public void onDataPacket(NetworkManager arg0, S35PacketUpdateTileEntity arg1) {
        NBTTagCompound nbt = arg1.func_148857_g();

        this.setProjector(Vector3.readFromNBT(nbt.getCompoundTag("projector")));
        if (nbt.hasKey("itemID") && nbt.hasKey("itemMetadata")) {
            this.camoStack = new ItemStack(
                Item.getItemById(nbt.getInteger("itemID")),
                1,
                nbt.getInteger("itemMetadata")
            );
        }
        this.getWorldObj().markBlockRangeForRenderUpdate(
            this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord
        );
    }

    public void setProjector(final Vector3 position) {
        this.projector = position;
        if (!this.getWorldObj().isRemote) {
            this.refreshCamoBlock();
        }
    }

    public TileEntityForceFieldProjector getProjector() {
        if (this.getProjectorSafe() != null) {
            return this.getProjectorSafe();
        }
        if (!this.getWorldObj().isRemote) {
            this.getWorldObj().setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
        }
        return null;
    }

    public TileEntityForceFieldProjector getProjectorSafe() {
        if (this.projector != null
            && this.projector.getTileEntity((IBlockAccess) this.getWorldObj())
                    instanceof TileEntityForceFieldProjector
            && (this.getWorldObj().isRemote
                || ((TileEntityForceFieldProjector
                    ) this.projector.getTileEntity((IBlockAccess) this.getWorldObj()))
                       .getCalculatedField()
                       .contains(new Vector3(this)))) {
            return (TileEntityForceFieldProjector
            ) this.projector.getTileEntity((IBlockAccess) this.getWorldObj());
        }
        return null;
    }

    public void refreshCamoBlock() {
        if (this.getProjectorSafe() != null) {
            this.camoStack
                = MFFSHelper.getCamoBlock(this.getProjector(), new Vector3(this));
        }
    }

    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.projector = Vector3.readFromNBT(nbt.getCompoundTag("projector"));
    }

    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (this.getProjector() != null) {
            nbt.setTag("projector", this.projector.writeToNBT(new NBTTagCompound()));
        }
    }
}
