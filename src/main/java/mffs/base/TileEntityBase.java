package mffs.base;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.peripheral.IPeripheral;
import mffs.api.IActivatable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.prefab.implement.IRedstoneReceptor;
import universalelectricity.prefab.implement.IRotatable;
import universalelectricity.prefab.tile.TileEntityDisableable;

public abstract class TileEntityBase extends TileEntityDisableable
    implements IRotatable, IRedstoneReceptor, IActivatable, IPeripheral {
    public boolean isActive;
    public boolean isRedstoneActive;
    public final List<EntityPlayer> playersUsing;
    public float animation;

    public TileEntityBase() {
        this.isActive = false;
        this.isRedstoneActive = false;
        this.playersUsing = new ArrayList<>();
        this.animation = 0.0f;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (super.ticks % 4L == 0L && this.playersUsing.size() > 0) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setBoolean("isActive", this.isActive);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager arg0, S35PacketUpdateTileEntity arg1) {
        NBTTagCompound nbt = arg1.func_148857_g();

        this.isActive = nbt.getBoolean("isActive");
    }

    @SideOnly(Side.CLIENT)
    public void onFxsPacket(NBTTagCompound data) {}

    public void onReceivePacket(final PacketTile.Type type, final NBTTagCompound data) {
        if (type == PacketTile.Type.TOGGLE_ACTIVATION) {
            this.isRedstoneActive = !this.isRedstoneActive;
            this.setActive(this.isRedstoneActive);
        }
    }

    public boolean isPoweredByRedstone() {
        return this.worldObj.isBlockIndirectlyGettingPowered(
            this.xCoord, this.yCoord, this.zCoord
        );
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.isActive = nbt.getBoolean("isActive");
        this.isRedstoneActive = nbt.getBoolean("isRedstoneActive");
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("isActive", this.isActive);
        nbttagcompound.setBoolean("isRedstoneActive", this.isRedstoneActive);
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public void setActive(final boolean flag) {
        this.isActive = flag;
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public ForgeDirection
    getDirection(final IBlockAccess world, final int x, final int y, final int z) {
        return ForgeDirection.getOrientation(this.getBlockMetadata());
    }

    @Override
    public void setDirection(
        final World world,
        final int x,
        final int y,
        final int z,
        final ForgeDirection facingDirection
    ) {
        this.worldObj.setBlockMetadataWithNotify(
            this.xCoord, this.yCoord, this.zCoord, facingDirection.ordinal(), 3
        );
    }

    @Override
    public void onPowerOn() {
        this.setActive(true);
    }

    @Override
    public void onPowerOff() {
        if (!this.isRedstoneActive && !this.worldObj.isRemote) {
            this.setActive(false);
        }
    }

    @Override
    public boolean equals(IPeripheral other) {
        return this == other;
    }
}
