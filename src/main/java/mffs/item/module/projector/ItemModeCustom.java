package mffs.item.module.projector;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.MFFSHelper;
import mffs.ModularForceFieldSystem;
import mffs.Settings;
import mffs.api.ICache;
import mffs.api.IFieldInteraction;
import mffs.api.IProjector;
import mffs.api.modules.IProjectorMode;
import mffs.item.mode.ItemMode;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemModeCustom extends ItemMode implements ICache {
    private final HashMap<String, Object> cache;

    public ItemModeCustom() {
        super("modeCustom");
        this.cache = new HashMap<>();
    }

    @Override
    public void addInformation(
        final ItemStack itemStack,
        final EntityPlayer par2EntityPlayer,
        final List list,
        final boolean par4
    ) {
        final NBTTagCompound nbt = MFFSHelper.getNBTTagCompound(itemStack);
        list.add("Mode: " + (nbt.getBoolean("mode") ? "Additive" : "Substraction"));
        final Vector3 point1 = Vector3.readFromNBT(nbt.getCompoundTag("point1"));
        list.add(
            "Point 1: " + point1.intX() + ", " + point1.intY() + ", " + point1.intZ()
        );
        final Vector3 point2 = Vector3.readFromNBT(nbt.getCompoundTag("point2"));
        list.add(
            "Point 2: " + point2.intX() + ", " + point2.intY() + ", " + point2.intZ()
        );
        final int modeID = nbt.getInteger("id");
        if (modeID > 0) {
            list.add("Mode ID: " + modeID);
            final int fieldSize = nbt.getInteger("fieldSize");
            if (fieldSize > 0) {
                list.add("Field size: " + fieldSize);
            } else {
                list.add("Field not saved.");
            }
        }
        if (GuiScreen.isShiftKeyDown()) {
            super.addInformation(itemStack, par2EntityPlayer, list, par4);
        } else {
            list.add("Hold shift for more...");
        }
    }

    public ItemStack onItemRightClick(
        final ItemStack itemStack, final World world, final EntityPlayer entityPlayer
    ) {
        if (!world.isRemote) {
            if (entityPlayer.isSneaking()) {
                final NBTTagCompound nbt = MFFSHelper.getNBTTagCompound(itemStack);
                if (nbt != null) {
                    final Vector3 point1
                        = Vector3.readFromNBT(nbt.getCompoundTag("point1"));
                    final Vector3 point2
                        = Vector3.readFromNBT(nbt.getCompoundTag("point2"));
                    if (nbt.hasKey("point1") && nbt.hasKey("point2")
                        && !point1.equals(point2)
                        && point1.distanceTo(point2) < Settings.MAX_FORCE_FIELD_SCALE) {
                        nbt.removeTag("point1");
                        nbt.removeTag("point2");
                        Vector3 midPoint = new Vector3();
                        midPoint.x = (point1.x + point2.x) / 2.0;
                        midPoint.y = (point1.y + point2.y) / 2.0;
                        midPoint.z = (point1.z + point2.z) / 2.0;
                        midPoint = midPoint.floor();
                        point1.subtract(midPoint);
                        point2.subtract(midPoint);
                        final Vector3 minPoint = new Vector3(
                            Math.min(point1.x, point2.x),
                            Math.min(point1.y, point2.y),
                            Math.min(point1.z, point2.z)
                        );
                        final Vector3 maxPoint = new Vector3(
                            Math.max(point1.x, point2.x),
                            Math.max(point1.y, point2.y),
                            Math.max(point1.z, point2.z)
                        );
                        File saveFile = Paths
                                            .get(
                                                this.getSaveDirectory().getPath(),
                                                "custom_mode_" + this.getModeID(itemStack)
                                            )
                                            .toFile();
                        // TODO: WTF happened to NBTFileLoader?!
                        NBTTagCompound saveNBT = null;
                        try {
                            saveNBT = CompressedStreamTools.read(saveFile);
                        } catch (IOException e1) {
                            throw new UncheckedIOException(e1);
                        }

                        // NBTTagCompound saveNBT = NBTFileLoader.loadData(
                        // this.getSaveDirectory(),
                        // "custom_mode_" + this.getModeID(itemStack));

                        if (saveNBT == null) {
                            saveNBT = new NBTTagCompound();
                        }
                        NBTTagList list;
                        if (saveNBT.hasKey("fieldPoints")) {
                            list = (NBTTagList) saveNBT.getTag("fieldPoints");
                        } else {
                            list = new NBTTagList();
                        }
                        for (int x = minPoint.intX(); x <= maxPoint.intX(); ++x) {
                            for (int y = minPoint.intY(); y <= maxPoint.intY(); ++y) {
                                for (int z = minPoint.intZ(); z <= maxPoint.intZ(); ++z) {
                                    final Vector3 position = new Vector3(x, y, z);
                                    final Vector3 targetCheck
                                        = Vector3.add(midPoint, position);
                                    final Block blockID = targetCheck.getBlock(world);
                                    if (blockID != Blocks.air) {
                                        if (nbt.getBoolean("mode")) {
                                            final NBTTagCompound vectorTag
                                                = new NBTTagCompound();
                                            position.writeToNBT(vectorTag);
                                            vectorTag.setInteger(
                                                "blockID", Block.getIdFromBlock(blockID)
                                            );
                                            vectorTag.setInteger(
                                                "blockMetadata",
                                                targetCheck.getBlockMetadata((IBlockAccess
                                                ) world)
                                            );
                                            list.appendTag((NBTBase) vectorTag);
                                        } else {
                                            for (int i = 0; i < list.tagCount(); ++i) {
                                                final Vector3 vector
                                                    = Vector3.readFromNBT((NBTTagCompound
                                                    ) list.getCompoundTagAt(i));
                                                if (vector.equals(position)) {
                                                    list.removeTag(i);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        saveNBT.setTag("fieldPoints", (NBTBase) list);
                        nbt.setInteger("fieldSize", list.tagCount());

                        try {
                            CompressedStreamTools.write(saveNBT, saveFile);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }

                        // NBTFileLoader.saveData(this.getSaveDirectory(),
                        // "custom_mode_" + this.getModeID(itemStack),
                        // saveNBT);
                        this.clearCache();
                        entityPlayer.addChatMessage(
                            new ChatComponentText("Field structure saved.")
                        );
                    }
                }
            } else {
                final NBTTagCompound nbt = MFFSHelper.getNBTTagCompound(itemStack);
                if (nbt != null) {
                    nbt.setBoolean("mode", !nbt.getBoolean("mode"));
                    entityPlayer.addChatMessage(new ChatComponentText(
                        "Changed selection mode to "
                        + (nbt.getBoolean("mode") ? "additive" : "substraction")
                    ));
                }
            }
        }
        return itemStack;
    }

    public boolean onItemUse(
        final ItemStack itemStack,
        final EntityPlayer entityPlayer,
        final World world,
        final int x,
        final int y,
        final int z,
        final int par7,
        final float par8,
        final float par9,
        final float par10
    ) {
        if (!world.isRemote) {
            final NBTTagCompound nbt = MFFSHelper.getNBTTagCompound(itemStack);
            if (nbt != null) {
                final Vector3 point1 = Vector3.readFromNBT(nbt.getCompoundTag("point1"));
                if (!nbt.hasKey("point1") || point1.equals(new Vector3(0.0, 0.0, 0.0))) {
                    nbt.setTag(
                        "point1", new Vector3(x, y, z).writeToNBT(new NBTTagCompound())
                    );
                    entityPlayer.addChatMessage(new ChatComponentText(
                        "Set point 1: " + x + ", " + y + ", " + z + "."
                    ));
                } else {
                    nbt.setTag(
                        "point2", new Vector3(x, y, z).writeToNBT(new NBTTagCompound())
                    );
                    entityPlayer.addChatMessage(new ChatComponentText(
                        "Set point 2: " + x + ", " + y + ", " + z + "."
                    ));
                }
            }
        }
        return true;
    }

    public int getModeID(final ItemStack itemStack) {
        final NBTTagCompound nbt = MFFSHelper.getNBTTagCompound(itemStack);
        int id = nbt.getInteger("id");
        if (id <= 0) {
            nbt.setInteger("id", this.getNextAvaliableID());
            id = nbt.getInteger("id");
        }
        return id;
    }

    public int getNextAvaliableID() {
        int i = 1;
        i += this.getSaveDirectory().list().length;
        return i;
    }

    public File getSaveDirectory() {
        final File saveDirectory =
            // TODO: might be wrong path?
            Paths.get(MinecraftServer.getServer().getFolderName(), "saves").toFile();
        if (!saveDirectory.exists()) {
            saveDirectory.mkdir();
        }
        final File file = new File(saveDirectory, "mffs");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public Set<Vector3>
    getFieldBlocks(final IFieldInteraction projector, final ItemStack itemStack) {
        return this.getFieldBlockMap(projector, itemStack).keySet();
    }

    public HashMap<Vector3, int[]>
    getFieldBlockMap(final IFieldInteraction projector, final ItemStack itemStack) {
        final String cacheID = "itemStack_" + itemStack.hashCode();
        if (Settings.USE_CACHE && this.cache.containsKey(cacheID)
            && this.cache.get(cacheID) instanceof HashMap) {
            return (HashMap<Vector3, int[]>) this.cache.get(cacheID);
        }
        final float scale = projector.getModuleCount(
                                ModularForceFieldSystem.itemModuleScale, new int[0]
                            )
            / 3.0f;
        final HashMap<Vector3, int[]> fieldBlocks = new HashMap<>();
        if (this.getSaveDirectory() != null) {
            NBTTagCompound nbt = null;
            try {
                nbt = CompressedStreamTools.read(
                    Paths
                        .get(
                            this.getSaveDirectory().getPath(),
                            "custom_mode_" + this.getModeID(itemStack)
                        )
                        .toFile()
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
            // final NBTTagCompound nbt = NBTFileLoader.loadData(
            // this.getSaveDirectory(), "custom_mode_" + this.getModeID(itemStack));
            if (nbt != null) {
                final NBTTagList nbtTagList = nbt.getTagList("fieldPoints", 10);
                for (int i = 0; i < nbtTagList.tagCount(); ++i) {
                    final NBTTagCompound vectorTag
                        = (NBTTagCompound) nbtTagList.getCompoundTagAt(i);
                    final Vector3 position = Vector3.readFromNBT(vectorTag);
                    if (scale > 0.0f) {
                        position.multiply(scale);
                    }
                    final int[] blockInfo = { vectorTag.getInteger("blockID"),
                                              vectorTag.getInteger("blockMetadata") };
                    if (position != null) {
                        fieldBlocks.put(position, blockInfo);
                    }
                }
            }
            if (Settings.USE_CACHE) {
                this.cache.put(cacheID, fieldBlocks);
            }
        }
        return fieldBlocks;
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

    public Set<Vector3> getExteriorPoints(final IFieldInteraction projector) {
        return this.getFieldBlocks(projector, projector.getModeStack());
    }

    @Override
    public Set<Vector3> getInteriorPoints(final IFieldInteraction projector) {
        return this.getExteriorPoints(projector);
    }

    @Override
    public boolean isInField(final IFieldInteraction projector, final Vector3 position) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void render(
        final IProjector projector,
        final double x,
        final double y,
        final double z,
        final float f,
        final long ticks
    ) {
        final IProjectorMode[] modes = { ModularForceFieldSystem.itemModeCube,
                                         ModularForceFieldSystem.itemModeSphere,
                                         ModularForceFieldSystem.itemModeTube,
                                         ModularForceFieldSystem.itemModePyramid };
        modes[((TileEntity) projector).getWorldObj().rand.nextInt(modes.length - 1)]
            .render(projector, x, y, z, f, ticks);
    }
}
