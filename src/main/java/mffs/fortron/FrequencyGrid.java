package mffs.fortron;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import icbm.api.IBlockFrequency;
import mffs.api.fortron.IFortronFrequency;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class FrequencyGrid {
    private static FrequencyGrid CLIENT_INSTANCE;
    private static FrequencyGrid SERVER_INSTANCE;
    private final Set<IBlockFrequency> frequencyGrid;

    public FrequencyGrid() {
        this.frequencyGrid = new HashSet<>();
    }

    public void register(final IBlockFrequency tileEntity) {
        try {
            final Iterator<IBlockFrequency> it = this.frequencyGrid.iterator();
            while (it.hasNext()) {
                final IBlockFrequency frequency = it.next();
                if (frequency == null) {
                    it.remove();
                } else if (((TileEntity) frequency).isInvalid()) {
                    it.remove();
                } else {
                    if (!new Vector3((TileEntity) frequency)
                             .equals(new Vector3((TileEntity) tileEntity))) {
                        continue;
                    }
                    it.remove();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        this.frequencyGrid.add(tileEntity);
    }

    public void unregister(final IBlockFrequency tileEntity) {
        this.frequencyGrid.remove(tileEntity);
        this.cleanUp();
    }

    public Set<IBlockFrequency> get() {
        return this.frequencyGrid;
    }

    public Set<IBlockFrequency> get(final int frequency) {
        final Set<IBlockFrequency> set = new HashSet<>();
        for (final IBlockFrequency tile : this.get()) {
            if (tile != null && !((TileEntity) tile).isInvalid()
                && tile.getFrequency() == frequency) {
                set.add(tile);
            }
        }
        return set;
    }

    public void cleanUp() {
        try {
            final Iterator<IBlockFrequency> it = this.frequencyGrid.iterator();
            while (it.hasNext()) {
                final IBlockFrequency frequency = it.next();
                if (frequency == null) {
                    it.remove();
                } else if (((TileEntity) frequency).isInvalid()) {
                    it.remove();
                } else {
                    if (((TileEntity) frequency)
                            .getWorldObj()
                            .getTileEntity(
                                ((TileEntity) frequency).xCoord,
                                ((TileEntity) frequency).yCoord,
                                ((TileEntity) frequency).zCoord
                            )
                        == (TileEntity) frequency) {
                        continue;
                    }
                    it.remove();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public Set<IBlockFrequency>
    get(final World world, final Vector3 position, final int radius, final int frequency
    ) {
        final Set set = new HashSet();
        for (final IBlockFrequency tileEntity : this.get(frequency)) {
            if (((TileEntity) tileEntity).getWorldObj() == world
                && Vector3.distance(new Vector3((TileEntity) tileEntity), position)
                    <= radius) {
                set.add(tileEntity);
            }
        }
        return set;
    }

    public Set<IFortronFrequency> getFortronTiles(
        final World world, final Vector3 position, final int radius, final int frequency
    ) {
        final Set set = new HashSet();
        for (final IBlockFrequency tileEntity : this.get(frequency)) {
            if (((TileEntity) tileEntity).getWorldObj() == world
                && tileEntity instanceof IFortronFrequency
                && Vector3.distance(new Vector3((TileEntity) tileEntity), position)
                    <= radius) {
                set.add(tileEntity);
            }
        }
        return set;
    }

    public static void reinitiate() {
        FrequencyGrid.CLIENT_INSTANCE = new FrequencyGrid();
        FrequencyGrid.SERVER_INSTANCE = new FrequencyGrid();
    }

    public static FrequencyGrid instance() {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            return FrequencyGrid.SERVER_INSTANCE;
        }
        return FrequencyGrid.CLIENT_INSTANCE;
    }

    static {
        FrequencyGrid.CLIENT_INSTANCE = new FrequencyGrid();
        FrequencyGrid.SERVER_INSTANCE = new FrequencyGrid();
    }
}
