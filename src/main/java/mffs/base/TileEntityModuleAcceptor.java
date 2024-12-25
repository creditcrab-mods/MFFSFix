package mffs.base;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import mffs.ModularForceFieldSystem;
import mffs.Settings;
import mffs.api.ICache;
import mffs.api.modules.IModule;
import mffs.api.modules.IModuleAcceptor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class TileEntityModuleAcceptor
    extends TileEntityFortron implements IModuleAcceptor, ICache {
    public final HashMap<String, Object> cache;
    public int startModuleIndex;
    public int endModuleIndex;
    protected int capacityBase;
    protected int capacityBoost;

    public TileEntityModuleAcceptor() {
        this.cache = new HashMap<>();
        this.startModuleIndex = 0;
        this.endModuleIndex = this.getSizeInventory() - 1;
        this.capacityBase = 500;
        this.capacityBoost = 5;
    }

    @Override
    public void initiate() {
        super.initiate();
        super.fortronTank.setCapacity(
            (this.getModuleCount(ModularForceFieldSystem.itemModuleCapacity, new int[0])
                 * this.capacityBoost
             + this.capacityBase)
            * 1000
        );
    }

    public void consumeCost() {
        if (this.getFortronCost() > 0) {
            this.requestFortron(this.getFortronCost(), true);
        }
    }

    @Override
    public ItemStack getModule(final IModule module) {
        final String cacheID = "getModule_" + module.hashCode();
        if (Settings.USE_CACHE && this.cache.containsKey(cacheID)
            && this.cache.get(cacheID) instanceof ItemStack) {
            return (ItemStack) this.cache.get(cacheID);
        }
        final ItemStack returnStack = new ItemStack((Item) module, 0);
        for (final ItemStack comparedModule : this.getModuleStacks(new int[0])) {
            if (comparedModule.getItem() == module) {
                final ItemStack itemStack = returnStack;
                itemStack.stackSize += comparedModule.stackSize;
            }
        }
        if (Settings.USE_CACHE) {
            this.cache.put(cacheID, returnStack.copy());
        }
        return returnStack;
    }

    @Override
    public int getModuleCount(final IModule module, final int... slots) {
        int count = 0;
        if (module != null) {
            String cacheID = "getModuleCount_" + module.hashCode();
            if (slots != null) {
                cacheID = cacheID + "_" + Arrays.hashCode(slots);
            }
            if (Settings.USE_CACHE && this.cache.containsKey(cacheID)
                && this.cache.get(cacheID) instanceof Integer) {
                return (int) this.cache.get(cacheID);
            }
            if (slots != null && slots.length > 0) {
                for (final int slotID : slots) {
                    if (this.getStackInSlot(slotID) != null
                        && this.getStackInSlot(slotID).getItem() == module) {
                        count += this.getStackInSlot(slotID).stackSize;
                    }
                }
            } else {
                for (final ItemStack itemStack : this.getModuleStacks(new int[0])) {
                    if (itemStack.getItem() == module) {
                        count += itemStack.stackSize;
                    }
                }
            }
            if (Settings.USE_CACHE) {
                this.cache.put(cacheID, count);
            }
        }
        return count;
    }

    @Override
    public Set<ItemStack> getModuleStacks(final int... slots) {
        String cacheID = "getModuleStacks_";
        if (slots != null) {
            cacheID += Arrays.hashCode(slots);
        }
        if (Settings.USE_CACHE && this.cache.containsKey(cacheID)
            && this.cache.get(cacheID) instanceof Set<?>) {
            return (Set<ItemStack>) this.cache.get(cacheID);
        }
        final Set<ItemStack> modules = new HashSet<>();
        if (slots == null || slots.length <= 0) {
            for (int slotID = this.startModuleIndex; slotID <= this.endModuleIndex;
                 ++slotID) {
                final ItemStack itemStack = this.getStackInSlot(slotID);
                if (itemStack != null && itemStack.getItem() instanceof IModule) {
                    modules.add(itemStack);
                }
            }
        } else {
            for (final int slotID2 : slots) {
                final ItemStack itemStack2 = this.getStackInSlot(slotID2);
                if (itemStack2 != null && itemStack2.getItem() instanceof IModule) {
                    modules.add(itemStack2);
                }
            }
        }
        if (Settings.USE_CACHE) {
            this.cache.put(cacheID, modules);
        }
        return modules;
    }

    @Override
    public Set<IModule> getModules(final int... slots) {
        String cacheID = "getModules_";
        if (slots != null) {
            cacheID += Arrays.hashCode(slots);
        }
        if (Settings.USE_CACHE && this.cache.containsKey(cacheID)
            && this.cache.get(cacheID) instanceof Set) {
            return (Set<IModule>) this.cache.get(cacheID);
        }
        final Set<IModule> modules = new HashSet<>();
        if (slots == null || slots.length <= 0) {
            for (int slotID = this.startModuleIndex; slotID <= this.endModuleIndex;
                 ++slotID) {
                final ItemStack itemStack = this.getStackInSlot(slotID);
                if (itemStack != null && itemStack.getItem() instanceof IModule) {
                    modules.add((IModule) itemStack.getItem());
                }
            }
        } else {
            for (final int slotID2 : slots) {
                final ItemStack itemStack2 = this.getStackInSlot(slotID2);
                if (itemStack2 != null && itemStack2.getItem() instanceof IModule) {
                    modules.add((IModule) itemStack2.getItem());
                }
            }
        }
        if (Settings.USE_CACHE) {
            this.cache.put(cacheID, modules);
        }
        return modules;
    }

    @Override
    public int getFortronCost() {
        final String cacheID = "getFortronCost";
        if (Settings.USE_CACHE && this.cache.containsKey(cacheID)
            && this.cache.get(cacheID) instanceof Integer) {
            return (int) this.cache.get(cacheID);
        }
        float cost = 0.0f;
        for (final ItemStack itemStack : this.getModuleStacks(new int[0])) {
            if (itemStack != null) {
                cost += itemStack.stackSize
                    * ((IModule) itemStack.getItem()).getFortronCost(this.getAmplifier());
            }
        }
        final int result = Math.round(cost);
        if (Settings.USE_CACHE) {
            this.cache.put(cacheID, result);
        }
        return result;
    }

    protected float getAmplifier() {
        return 1.0f;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        super.fortronTank.setCapacity(
            (this.getModuleCount(ModularForceFieldSystem.itemModuleCapacity, new int[0])
                 * this.capacityBoost
             + this.capacityBase)
            * 1000
        );
        this.clearCache();
    }

    @Override
    public Object getCache(final String cacheID) {
        return this.cache.get(cacheID);
    }

    @Override
    public void clearCache(final String cacheID) {
        this.cache.remove(cacheID);
    }

    @Override
    public void clearCache() {
        this.cache.clear();
    }
}
