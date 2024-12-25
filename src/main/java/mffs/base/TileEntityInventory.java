package mffs.base;

import java.util.HashSet;
import java.util.Set;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.multiblock.TileEntityMulti;

public abstract class TileEntityInventory extends TileEntityBase implements IInventory {
    protected ItemStack[] inventory;

    public TileEntityInventory() {
        this.inventory = new ItemStack[this.getSizeInventory()];
    }

    // TODO: WTF
    // @Override
    // public List getPacketUpdate() {
    // final List objects = new ArrayList();
    // objects.addAll(super.getPacketUpdate());
    // final NBTTagCompound nbt = new NBTTagCompound();
    // this.writeToNBT(nbt);
    // objects.add(nbt);
    // return objects;
    // }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        this.writeToNBT(nbt);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager arg0, S35PacketUpdateTileEntity arg1) {
        NBTTagCompound nbt = arg1.func_148857_g();

        this.readFromNBT(nbt);
    }

    // TODO: WTF
    // @Override
    // public void sendInventoryToClients() {
    // final NBTTagCompound nbt = new NBTTagCompound();
    // this.writeToNBT(nbt);
    // PacketManager.sendPacketToClients(PacketManager.getPacket("MFFS", this,
    // TilePacketType.INVENTORY.ordinal(), nbt));
    // }

    @Override
    public ItemStack getStackInSlot(final int i) {
        return this.inventory[i];
    }

    @Override
    public String getInventoryName() {
        return this.getBlockType().getLocalizedName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public void setInventorySlotContents(final int i, final ItemStack itemstack) {
        this.inventory[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public ItemStack decrStackSize(final int i, final int j) {
        if (this.inventory[i] == null) {
            return null;
        }
        if (this.inventory[i].stackSize <= j) {
            final ItemStack itemstack = this.inventory[i];
            this.inventory[i] = null;
            return itemstack;
        }
        final ItemStack itemstack2 = this.inventory[i].splitStack(j);
        if (this.inventory[i].stackSize == 0) {
            this.inventory[i] = null;
        }
        return itemstack2;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isUseableByPlayer(final EntityPlayer entityplayer) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(final int slotID) {
        if (this.inventory[slotID] != null) {
            final ItemStack itemstack = this.inventory[slotID];
            this.inventory[slotID] = null;
            return itemstack;
        }
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isItemValidForSlot(final int slotID, final ItemStack itemStack) {
        return true;
    }

    public boolean canIncreaseStack(final int slotID, final ItemStack itemStack) {
        return this.getStackInSlot(slotID) == null
            || (this.getStackInSlot(slotID).stackSize + 1 <= 64
                && this.getStackInSlot(slotID).isItemEqual(itemStack));
    }

    public void incrStackSize(final int slot, final ItemStack itemStack) {
        if (this.getStackInSlot(slot) == null) {
            this.setInventorySlotContents(slot, itemStack.copy());
        } else if (this.getStackInSlot(slot).isItemEqual(itemStack)) {
            final ItemStack stackInSlot = this.getStackInSlot(slot);
            ++stackInSlot.stackSize;
        }
    }

    public Set<ItemStack> getCards() {
        final Set<ItemStack> cards = new HashSet<>();
        cards.add(this.getStackInSlot(0));
        return cards;
    }

    public ItemStack tryPlaceInPosition(
        ItemStack itemStack, final Vector3 position, final ForgeDirection dir
    ) {
        final TileEntity tileEntity
            = position.getTileEntity((IBlockAccess) this.worldObj);
        final ForgeDirection direction = dir.getOpposite();
        if (tileEntity != null && itemStack != null) {
            if (tileEntity instanceof TileEntityMulti) {
                final Vector3 mainBlockPosition
                    = ((TileEntityMulti) tileEntity).mainBlockPosition;
                if (mainBlockPosition != null
                    && !(
                        mainBlockPosition.getTileEntity((IBlockAccess) this.worldObj)
                            instanceof TileEntityMulti
                    )) {
                    return this.tryPlaceInPosition(
                        itemStack, mainBlockPosition, direction
                    );
                }
            } else if (tileEntity instanceof TileEntityChest) {
                final TileEntityChest[] chests = { (TileEntityChest) tileEntity, null };
                for (int i = 2; i < 6; ++i) {
                    final ForgeDirection searchDirection
                        = ForgeDirection.getOrientation(i);
                    final Vector3 searchPosition = position.clone();
                    searchPosition.modifyPositionFromSide(searchDirection);
                    if (searchPosition.getTileEntity((IBlockAccess) this.worldObj) != null
                        && searchPosition.getTileEntity((IBlockAccess) this.worldObj)
                                .getClass()
                            == chests[0].getClass()) {
                        chests[1] = (TileEntityChest
                        ) searchPosition.getTileEntity((IBlockAccess) this.worldObj);
                        break;
                    }
                }
                for (final TileEntityChest chest : chests) {
                    if (chest != null) {
                        for (int j = 0; j < chest.getSizeInventory(); ++j) {
                            itemStack = this.addStackToInventory(
                                j, (IInventory) chest, itemStack
                            );
                            if (itemStack == null) {
                                return null;
                            }
                        }
                    }
                }
            } else if (tileEntity instanceof ISidedInventory) {
                final ISidedInventory inventory = (ISidedInventory) tileEntity;
                final int[] slots
                    = inventory.getAccessibleSlotsFromSide(direction.ordinal());
                for (int k = 0; k < slots.length; ++k) {
                    if (inventory.canInsertItem(
                            slots[k], itemStack, direction.ordinal()
                        )) {
                        itemStack = this.addStackToInventory(
                            slots[k], (IInventory) inventory, itemStack
                        );
                    }
                    if (itemStack == null) {
                        return null;
                    }
                }
            } else if (tileEntity instanceof IInventory) {
                final IInventory inventory2 = (IInventory) tileEntity;
                for (int i = 0; i < inventory2.getSizeInventory(); ++i) {
                    itemStack = this.addStackToInventory(i, inventory2, itemStack);
                    if (itemStack == null) {
                        return null;
                    }
                }
            }
        }
        if (itemStack.stackSize <= 0) {
            return null;
        }
        return itemStack;
    }

    public ItemStack addStackToInventory(
        final int slotIndex, final IInventory inventory, final ItemStack itemStack
    ) {
        if (inventory.getSizeInventory() > slotIndex) {
            ItemStack stackInInventory = inventory.getStackInSlot(slotIndex);
            if (stackInInventory == null) {
                inventory.setInventorySlotContents(slotIndex, itemStack);
                if (inventory.getStackInSlot(slotIndex) == null) {
                    return itemStack;
                }
                return null;
            } else if (stackInInventory.isItemEqual(itemStack) && stackInInventory.isStackable()) {
                stackInInventory = stackInInventory.copy();
                final int stackLim = Math.min(
                    inventory.getInventoryStackLimit(), itemStack.getMaxStackSize()
                );
                final int rejectedAmount = Math.max(
                    stackInInventory.stackSize + itemStack.stackSize - stackLim, 0
                );
                stackInInventory.stackSize = Math.min(
                    Math.max(
                        stackInInventory.stackSize + itemStack.stackSize - rejectedAmount,
                        0
                    ),
                    inventory.getInventoryStackLimit()
                );
                itemStack.stackSize = rejectedAmount;
                inventory.setInventorySlotContents(slotIndex, stackInInventory);
            }
        }
        if (itemStack.stackSize <= 0) {
            return null;
        }
        return itemStack;
    }

    public boolean mergeIntoInventory(ItemStack itemStack) {
        if (!this.worldObj.isRemote) {
            for (final ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                if (itemStack != null) {
                    itemStack = this.tryPlaceInPosition(
                        itemStack,
                        new Vector3(this).modifyPositionFromSide(direction),
                        direction
                    );
                }
            }
            if (itemStack != null) {
                this.worldObj.spawnEntityInWorld((Entity) new EntityItem(
                    this.worldObj,
                    this.xCoord + 0.5,
                    (double) (this.yCoord + 1),
                    this.zCoord + 0.5,
                    itemStack
                ));
            }
        }
        return false;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        final NBTTagList nbtTagList = nbttagcompound.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < nbtTagList.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound2
                = (NBTTagCompound) nbtTagList.getCompoundTagAt(i);
            final byte byte0 = nbttagcompound2.getByte("Slot");
            if (byte0 >= 0 && byte0 < this.inventory.length) {
                this.inventory[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound2);
            }
        }
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        final NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < this.inventory.length; ++i) {
            if (this.inventory[i] != null) {
                final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                nbttagcompound2.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbttagcompound2);
                nbtTagList.appendTag((NBTBase) nbttagcompound2);
            }
        }
        nbttagcompound.setTag("Items", (NBTBase) nbtTagList);
    }

    @Override
    public String getType() {
        return this.getInventoryName();
    }

    @Override
    public String[] getMethodNames() {
        return new String[] { "isActivate", "setActivate" };
    }

    @Override
    public Object[] callMethod(
        IComputerAccess computer, ILuaContext context, int method, Object[] arguments
    ) throws LuaException, InterruptedException {
        switch (method) {
            case 0: {
                return new Object[] { this.isActive() };
            }
            case 1: {
                this.setActive((boolean) arguments[0]);
                return null;
            }
            default: {
                throw new LuaException("Invalid method.");
            }
        }
    }

    @Override
    public void attach(final IComputerAccess computer) {}

    @Override
    public void detach(final IComputerAccess computer) {}
}
