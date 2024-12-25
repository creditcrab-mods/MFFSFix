package mffs.base;

import java.util.HashSet;
import java.util.Set;

import icbm.api.IBlockFrequency;
import mffs.api.IBiometricIdentifierLink;
import mffs.api.card.ICardLink;
import mffs.api.security.IBiometricIdentifier;
import mffs.fortron.FrequencyGrid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import universalelectricity.core.vector.Vector3;

public abstract class TileEntityFrequency
    extends TileEntityInventory implements IBlockFrequency, IBiometricIdentifierLink {
    private int frequency;

    @Override
    public void initiate() {
        FrequencyGrid.instance().register(this);
        super.initiate();
    }

    @Override
    public void invalidate() {
        FrequencyGrid.instance().unregister(this);
        super.invalidate();
    }

    @Override
    public void onReceivePacket(final PacketTile.Type type, final NBTTagCompound data) {
        super.onReceivePacket(type, data);
        if (type == PacketTile.Type.FREQUENCY) {
            this.setFrequency(data.getInteger("frequency"));
        }
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.setFrequency(nbt.getInteger("frequency"));
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("frequency", this.getFrequency());
    }

    @Override
    public int getFrequency() {
        return this.frequency;
    }

    @Override
    public void setFrequency(final int frequency) {
        this.frequency = frequency;
    }

    @Override
    public IBiometricIdentifier getBiometricIdentifier() {
        if (this.getBiometricIdentifiers().size() > 0) {
            return (IBiometricIdentifier) this.getBiometricIdentifiers().toArray()[0];
        }
        return null;
    }

    @Override
    public Set<IBiometricIdentifier> getBiometricIdentifiers() {
        final Set<IBiometricIdentifier> list = new HashSet<>();
        for (final ItemStack itemStack : this.getCards()) {
            if (itemStack != null && itemStack.getItem() instanceof ICardLink) {
                final Vector3 linkedPosition
                    = ((ICardLink) itemStack.getItem()).getLink(itemStack);
                final TileEntity tileEntity
                    = linkedPosition.getTileEntity((IBlockAccess) this.worldObj);
                if (linkedPosition == null
                    || !(tileEntity instanceof IBiometricIdentifier)) {
                    continue;
                }
                list.add((IBiometricIdentifier) tileEntity);
            }
        }
        for (final IBlockFrequency tileEntity2 :
             FrequencyGrid.instance().get(this.getFrequency())) {
            if (tileEntity2 instanceof IBiometricIdentifier) {
                list.add((IBiometricIdentifier) tileEntity2);
            }
        }
        return list;
    }
}
