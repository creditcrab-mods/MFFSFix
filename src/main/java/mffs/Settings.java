package mffs;

import java.io.File;

import cpw.mods.fml.common.Loader;
import mffs.api.Blacklist;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Settings {
    public static final Configuration CONFIGURATION;
    public static final int BLOCK_ID_PREFIX = 1680;
    public static final int ITEM_ID_PREFIX = 11130;
    private static int NEXT_BLOCK_ID;
    private static int NEXT_ITEM_ID;
    public static int MAX_FORCE_FIELDS_PER_TICK;
    public static int MAX_FORCE_FIELD_SCALE;
    public static boolean INTERACT_CREATIVE;
    public static boolean LOAD_CHUNKS;
    public static boolean OP_OVERRIDE;
    public static boolean USE_CACHE;
    public static boolean ENABLE_ELECTRICITY;
    public static boolean CONSERVE_PACKETS;
    public static boolean HIGH_GRAPHICS;
    public static int INTERDICTION_MURDER_ENERGY;
    public static final int MAX_FREQUENCY_DIGITS = 6;
    public static boolean ENABLE_MANIPULATOR;

    public static int getNextBlockID() {
        return ++Settings.NEXT_BLOCK_ID;
    }

    public static int getNextItemID() {
        return ++Settings.NEXT_ITEM_ID;
    }

    public static void load() {
        Settings.CONFIGURATION.load();
        Settings.ENABLE_MANIPULATOR
            = Settings.CONFIGURATION
                  .get("general", "Enable Force Manipulator", Settings.ENABLE_MANIPULATOR)
                  .getBoolean(Settings.ENABLE_MANIPULATOR);
        final Property propFieldScale = Settings.CONFIGURATION.get(
            "general", "Max Force Field Scale", Settings.MAX_FORCE_FIELD_SCALE
        );
        Settings.MAX_FORCE_FIELD_SCALE
            = propFieldScale.getInt(Settings.MAX_FORCE_FIELD_SCALE);
        final Property propInterdiction = Settings.CONFIGURATION.get(
            "general",
            "Interdiction Murder Fortron Consumption",
            Settings.INTERDICTION_MURDER_ENERGY
        );
        Settings.INTERDICTION_MURDER_ENERGY
            = propInterdiction.getInt(Settings.INTERDICTION_MURDER_ENERGY);
        final Property propCreative = Settings.CONFIGURATION.get(
            "general", "Effect Creative Players", Settings.INTERACT_CREATIVE
        );
        propCreative.comment
            = "Should the interdiction matrix interact with creative players?.";
        Settings.INTERACT_CREATIVE = propCreative.getBoolean(Settings.INTERACT_CREATIVE);
        final Property propChunkLoading
            = Settings.CONFIGURATION.get("general", "Load Chunks", Settings.LOAD_CHUNKS);
        propChunkLoading.comment
            = "Set this to false to turn off the MFFS Chunkloading capabilities.";
        Settings.LOAD_CHUNKS = propChunkLoading.getBoolean(Settings.LOAD_CHUNKS);
        final Property propOpOverride
            = Settings.CONFIGURATION.get("general", "Op Override", Settings.OP_OVERRIDE);
        propOpOverride.comment
            = "Allow the operator(s) to override security measures created by MFFS?";
        Settings.OP_OVERRIDE = propOpOverride.getBoolean(Settings.OP_OVERRIDE);
        final Property propUseCache
            = Settings.CONFIGURATION.get("general", "Use Cache", Settings.USE_CACHE);
        propUseCache.comment
            = "Cache allows temporary data saving to decrease calculations required.";
        Settings.USE_CACHE = propUseCache.getBoolean(Settings.USE_CACHE);
        final Property maxFFGenPerTick = Settings.CONFIGURATION.get(
            "general", "Field Calculation Per Tick", Settings.MAX_FORCE_FIELDS_PER_TICK
        );
        maxFFGenPerTick.comment
            = "How many force field blocks can be generated per tick? Less reduces lag.";
        Settings.MAX_FORCE_FIELDS_PER_TICK
            = maxFFGenPerTick.getInt(Settings.MAX_FORCE_FIELDS_PER_TICK);
        final Property useElectricity = Settings.CONFIGURATION.get(
            "general", "Require Electricity?", Settings.ENABLE_ELECTRICITY
        );
        useElectricity.comment
            = "Turning this to false will make MFFS run without electricity or energy systems required. Great for vanilla!";
        Settings.ENABLE_ELECTRICITY
            = useElectricity.getBoolean(Settings.ENABLE_ELECTRICITY);
        final Property conservePackets = Settings.CONFIGURATION.get(
            "general", "Conserve Packets?", Settings.CONSERVE_PACKETS
        );
        conservePackets.comment
            = "Turning this to false will enable better client side packet and updates but in the cost of more packets sent.";
        Settings.CONSERVE_PACKETS = conservePackets.getBoolean(Settings.CONSERVE_PACKETS);
        final Property highGraphics = Settings.CONFIGURATION.get(
            "general", "High Graphics", Settings.HIGH_GRAPHICS
        );
        highGraphics.comment
            = "Turning this to false will reduce rendering and client side packet graphical packets.";
        Settings.CONSERVE_PACKETS = highGraphics.getBoolean(Settings.HIGH_GRAPHICS);
        final Property forceManipulatorBlacklist
            = Settings.CONFIGURATION.get("general", "Force Manipulator Blacklist", "");
        highGraphics.comment
            = "Put a list of block IDs to be not-moved by the force manipulator. Separate by commas, no space.";
        final String blackListString = forceManipulatorBlacklist.getString();
        if (blackListString != null) {
            for (final String blockIDString : blackListString.split(",")) {
                Block b = Block.getBlockFromName(blockIDString);
                if (b == null) {
                    ModularForceFieldSystem.LOGGER.severe(
                        "Invalid block blacklist ID \'" + blockIDString + "\'!"
                    );
                    continue;
                }

                Blacklist.forceManipulationBlacklist.add(b);
            }
        }

        final Property blacklist1
            = Settings.CONFIGURATION.get("general", "Stabilization Blacklist", "");
        final String blackListString2 = blacklist1.getString();
        if (blackListString2 != null) {
            for (final String blockIDString2 : blackListString2.split(",")) {
                Block b = Block.getBlockFromName(blockIDString2);
                if (b == null) {
                    ModularForceFieldSystem.LOGGER.severe(
                        "Invalid block blacklist ID \'" + blockIDString2 + "\'!"
                    );
                    continue;
                }

                Blacklist.stabilizationBlacklist.add(b);
            }
        }
        final Property blacklist2
            = Settings.CONFIGURATION.get("general", "Disintegration Blacklist", "");
        final String blackListString3 = blacklist2.getString();
        if (blackListString3 != null) {
            for (final String blockIDString3 : blackListString3.split(",")) {
                if (blockIDString3 != null && !blockIDString3.isEmpty()) {
                    Block b = Block.getBlockFromName(blockIDString3);
                    if (b == null) {
                        ModularForceFieldSystem.LOGGER.severe(
                            "Invalid block blacklist ID \'" + blockIDString3 + "\'!"
                        );
                        continue;
                    }

                    Blacklist.disintegrationBlacklist.add(b);
                }
            }
        }
        Blacklist.stabilizationBlacklist.add(Blocks.water);
        Blacklist.stabilizationBlacklist.add(Blocks.flowing_water);
        Blacklist.stabilizationBlacklist.add(Blocks.lava);
        Blacklist.stabilizationBlacklist.add(Blocks.flowing_lava);
        Blacklist.disintegrationBlacklist.add(Blocks.water);
        Blacklist.disintegrationBlacklist.add(Blocks.flowing_water);
        Blacklist.disintegrationBlacklist.add(Blocks.lava);
        Blacklist.stabilizationBlacklist.add(Blocks.flowing_lava);
        Blacklist.forceManipulationBlacklist.add(Blocks.bedrock);
        Blacklist.forceManipulationBlacklist.add(ModularForceFieldSystem.blockForceField);
        Settings.CONFIGURATION.save();
    }

    static {
        CONFIGURATION = new Configuration(
            new File(Loader.instance().getConfigDir(), "Modular Force Field System.cfg")
        );
        Settings.NEXT_BLOCK_ID = 1680;
        Settings.NEXT_ITEM_ID = 11130;
        Settings.MAX_FORCE_FIELDS_PER_TICK = 1000;
        Settings.MAX_FORCE_FIELD_SCALE = 200;
        Settings.INTERACT_CREATIVE = true;
        Settings.LOAD_CHUNKS = true;
        Settings.OP_OVERRIDE = true;
        Settings.USE_CACHE = true;
        Settings.ENABLE_ELECTRICITY = true;
        Settings.CONSERVE_PACKETS = true;
        Settings.HIGH_GRAPHICS = true;
        Settings.INTERDICTION_MURDER_ENERGY = 0;
        Settings.ENABLE_MANIPULATOR = true;
    }
}
