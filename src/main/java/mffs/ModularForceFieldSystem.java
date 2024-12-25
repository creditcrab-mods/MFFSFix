package mffs;

import java.util.Arrays;
import java.util.logging.Logger;

import calclavia.lib.UniversalRecipes;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import dan200.computercraft.api.ComputerCraftAPI;
import mffs.base.BlockBase;
import mffs.base.BlockMachine;
import mffs.base.ItemBase;
import mffs.base.PacketFxs;
import mffs.base.PacketFxsHandler;
import mffs.base.PacketTile;
import mffs.base.PacketTileHandler;
import mffs.block.BlockBiometricIdentifier;
import mffs.block.BlockCoercionDeriver;
import mffs.block.BlockForceField;
import mffs.block.BlockForceFieldProjector;
import mffs.block.BlockForceManipulator;
import mffs.block.BlockFortronCapacitor;
import mffs.block.BlockInterdictionMatrix;
import mffs.card.ItemCard;
import mffs.fortron.FortronHelper;
import mffs.fortron.FrequencyGrid;
import mffs.item.ItemRemoteController;
import mffs.item.card.ItemCardFrequency;
import mffs.item.card.ItemCardID;
import mffs.item.card.ItemCardInfinite;
import mffs.item.card.ItemCardLink;
import mffs.item.mode.ItemMode;
import mffs.item.mode.ItemModeCube;
import mffs.item.mode.ItemModeSphere;
import mffs.item.mode.ItemModeTube;
import mffs.item.module.ItemModule;
import mffs.item.module.interdiction.ItemModuleAntiFriendly;
import mffs.item.module.interdiction.ItemModuleAntiHostile;
import mffs.item.module.interdiction.ItemModuleAntiPersonnel;
import mffs.item.module.interdiction.ItemModuleConfiscate;
import mffs.item.module.interdiction.ItemModuleInterdictionMatrix;
import mffs.item.module.interdiction.ItemModuleWarn;
import mffs.item.module.projector.ItemModeCustom;
import mffs.item.module.projector.ItemModeCylinder;
import mffs.item.module.projector.ItemModePyramid;
import mffs.item.module.projector.ItemModuleDisintegration;
import mffs.item.module.projector.ItemModuleFusion;
import mffs.item.module.projector.ItemModuleManipulator;
import mffs.item.module.projector.ItemModuleShock;
import mffs.item.module.projector.ItemModuleSponge;
import mffs.item.module.projector.ItemModuleStablize;
import mffs.tileentity.TileEntityBiometricIdentifier;
import mffs.tileentity.TileEntityCoercionDeriver;
import mffs.tileentity.TileEntityForceField;
import mffs.tileentity.TileEntityForceFieldProjector;
import mffs.tileentity.TileEntityForceManipulator;
import mffs.tileentity.TileEntityFortronCapacitor;
import mffs.tileentity.TileEntityInterdictionMatrix;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import universalelectricity.prefab.CustomDamageSource;
import universalelectricity.prefab.TranslationHelper;

@Mod(
    modid = "MFFS",
    name = "Modular Force Field System",
    version = ModularForceFieldSystem.VERSION,
    useMetadata = true,
    dependencies = "required-after:basiccomponents;after:IC2"
)
public class ModularForceFieldSystem {
    public static final String CHANNEL = "MFFS";
    public static final String ID = "MFFS";
    public static final String NAME = "Modular Force Field System";
    public static final String PREFIX = "mffs:";
    public static final String VERSION = "{VERSION}";
    @Mod.Instance("MFFS")
    public static ModularForceFieldSystem instance;
    @Mod.Metadata("MFFS")
    public static ModMetadata metadata;
    @SidedProxy(clientSide = "mffs.ClientProxy", serverSide = "mffs.CommonProxy")
    public static CommonProxy proxy;
    public static final Logger LOGGER;
    public static final String RESOURCE_DIRECTORY = "/mods/mffs/";
    public static final String LANGUAGE_DIRECTORY = "/mods/mffs/languages/";
    public static final String TEXTURE_DIRECTORY = "/mods/mffs/textures/";
    public static final String BLOCK_DIRECTORY = "/mods/mffs/textures/blocks/";
    public static final String ITEM_DIRECTORY = "/mods/mffs/textures/items/";
    public static final String MODEL_DIRECTORY = "/mods/mffs/textures/models/";
    public static final String GUI_DIRECTORY = "/mods/mffs/textures/gui/";
    public static final String GUI_BASE_DIRECTORY
        = "/mods/mffs/textures/gui/gui_base.png";
    public static final String GUI_COMPONENTS
        = "/mods/mffs/textures/gui/gui_components.png";
    public static final String GUI_BUTTON = "/mods/mffs/textures/gui/gui_button.png";
    public static BlockMachine blockCoercionDeriver;
    public static BlockMachine blockFortronCapacitor;
    public static BlockMachine blockForceFieldProjector;
    public static BlockMachine blockBiometricIdentifier;
    public static BlockMachine blockInterdictionMatrix;
    public static BlockMachine blockForceManipulator;
    public static BlockBase blockForceField;
    public static Item itemRemoteController;
    public static Item itemFocusMatrix;
    public static ItemCard itemCardBlank;
    public static ItemCard itemCardInfinite;
    public static ItemCard itemCardFrequency;
    public static ItemCard itemCardID;
    public static ItemCard itemCardLink;
    public static ItemMode itemModeCube;
    public static ItemMode itemModeSphere;
    public static ItemMode itemModeTube;
    public static ItemMode itemModeCylinder;
    public static ItemMode itemModePyramid;
    public static ItemMode itemModeCustom;
    public static ItemModule itemModuleSpeed;
    public static ItemModule itemModuleCapacity;
    public static ItemModule itemModuleTranslate;
    public static ItemModule itemModuleScale;
    public static ItemModule itemModuleRotate;
    public static ItemModule itemModuleCollection;
    public static ItemModule itemModuleInvert;
    public static ItemModule itemModuleSilence;
    public static ItemModule itemModuleFusion;
    public static ItemModule itemModuleManipulator;
    public static ItemModule itemModuleCamouflage;
    public static ItemModule itemModuleDisintegration;
    public static ItemModule itemModuleShock;
    public static ItemModule itemModuleGlow;
    public static ItemModule itemModuleSponge;
    public static ItemModule itemModuleStablize;
    public static ItemModule itemModuleAntiHostile;
    public static ItemModule itemModuleAntiFriendly;
    public static ItemModule itemModuleAntiPersonnel;
    public static ItemModule itemModuleConfiscate;
    public static ItemModule itemModuleWarn;
    public static ItemModule itemModuleBlockAccess;
    public static ItemModule itemModuleBlockAlter;
    public static ItemModule itemModuleAntiSpawn;
    public static DamageSource damagefieldShock;

    public static SimpleNetworkWrapper channel;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(
            (Object) this, (IGuiHandler) ModularForceFieldSystem.proxy
        );
        MinecraftForge.EVENT_BUS.register((Object) new SubscribeEventHandler());
        Settings.load();
        Settings.CONFIGURATION.load();
        ModularForceFieldSystem.blockForceField = new BlockForceField();
        ModularForceFieldSystem.blockCoercionDeriver = new BlockCoercionDeriver();
        ModularForceFieldSystem.blockFortronCapacitor = new BlockFortronCapacitor();
        ModularForceFieldSystem.blockForceFieldProjector = new BlockForceFieldProjector();
        ModularForceFieldSystem.blockBiometricIdentifier = new BlockBiometricIdentifier();
        ModularForceFieldSystem.blockInterdictionMatrix = new BlockInterdictionMatrix();
        ModularForceFieldSystem.blockForceManipulator = new BlockForceManipulator();
        ModularForceFieldSystem.itemRemoteController = new ItemRemoteController();
        ModularForceFieldSystem.itemFocusMatrix = new ItemBase("focusMatrix");
        ModularForceFieldSystem.itemModeCube = new ItemModeCube(Settings.getNextItemID());
        ModularForceFieldSystem.itemModeSphere
            = new ItemModeSphere(Settings.getNextItemID());
        ModularForceFieldSystem.itemModeTube = new ItemModeTube();
        ModularForceFieldSystem.itemModePyramid = new ItemModePyramid();
        ModularForceFieldSystem.itemModeCylinder = new ItemModeCylinder();
        ModularForceFieldSystem.itemModeCustom = new ItemModeCustom();
        ModularForceFieldSystem.itemModuleTranslate
            = new ItemModule("moduleTranslate").setCost(1.6f);
        ModularForceFieldSystem.itemModuleScale
            = new ItemModule("moduleScale").setCost(1.2f);
        ModularForceFieldSystem.itemModuleRotate
            = new ItemModule("moduleRotate").setCost(0.1f);
        ModularForceFieldSystem.itemModuleSpeed
            = new ItemModule("moduleSpeed").setCost(1.0f);
        ModularForceFieldSystem.itemModuleCapacity
            = new ItemModule("moduleCapacity").setCost(0.5f);
        ModularForceFieldSystem.itemModuleFusion = new ItemModuleFusion();
        ModularForceFieldSystem.itemModuleManipulator = new ItemModuleManipulator();
        ModularForceFieldSystem.itemModuleCamouflage
            = new ItemModule("moduleCamouflage").setCost(1.5f).setMaxStackSize(1);
        ModularForceFieldSystem.itemModuleDisintegration = new ItemModuleDisintegration();
        ModularForceFieldSystem.itemModuleShock = new ItemModuleShock();
        ModularForceFieldSystem.itemModuleGlow = new ItemModule("moduleGlow");
        ModularForceFieldSystem.itemModuleSponge = new ItemModuleSponge();
        ModularForceFieldSystem.itemModuleStablize = new ItemModuleStablize();
        ModularForceFieldSystem.itemModuleAntiFriendly = new ItemModuleAntiFriendly();
        ModularForceFieldSystem.itemModuleAntiHostile = new ItemModuleAntiHostile();
        ModularForceFieldSystem.itemModuleAntiPersonnel = new ItemModuleAntiPersonnel();
        ModularForceFieldSystem.itemModuleConfiscate = new ItemModuleConfiscate();
        ModularForceFieldSystem.itemModuleWarn = new ItemModuleWarn();
        ModularForceFieldSystem.itemModuleBlockAccess
            = new ItemModuleInterdictionMatrix("moduleBlockAccess").setCost(10.0f);
        ModularForceFieldSystem.itemModuleBlockAlter
            = new ItemModuleInterdictionMatrix("moduleBlockAlter").setCost(15.0f);
        ModularForceFieldSystem.itemModuleAntiSpawn
            = new ItemModuleInterdictionMatrix("moduleAntiSpawn").setCost(10.0f);
        ModularForceFieldSystem.itemCardBlank = new ItemCard("cardBlank");
        ModularForceFieldSystem.itemCardFrequency = new ItemCardFrequency();
        ModularForceFieldSystem.itemCardLink = new ItemCardLink();
        ModularForceFieldSystem.itemCardID = new ItemCardID();
        ModularForceFieldSystem.itemCardInfinite = new ItemCardInfinite();
        FortronHelper.FLUID_FORTRON = new Fluid("fortron");
        ModularForceFieldSystem.itemModuleCollection
            = new ItemModule("moduleCollection").setMaxStackSize(1).setCost(15.0f);
        ModularForceFieldSystem.itemModuleInvert
            = new ItemModule("moduleInvert").setMaxStackSize(1).setCost(15.0f);
        ModularForceFieldSystem.itemModuleSilence
            = new ItemModule("moduleSilence").setMaxStackSize(1).setCost(1.0f);
        Settings.CONFIGURATION.save();
        GameRegistry.registerBlock(
            (Block) ModularForceFieldSystem.blockForceField, "blockForceField"
        );
        GameRegistry.registerBlock(
            (Block) ModularForceFieldSystem.blockCoercionDeriver,

            "blockCoercionDeriver"
        );
        GameRegistry.registerBlock(
            (Block) ModularForceFieldSystem.blockFortronCapacitor, "blockFortronCapacitor"
        );
        GameRegistry.registerBlock(
            (Block) ModularForceFieldSystem.blockForceFieldProjector,
            "blockForceFieldProjector"
        );
        GameRegistry.registerBlock(
            (Block) ModularForceFieldSystem.blockBiometricIdentifier,
            "blockBiometricIdentifier"
        );
        GameRegistry.registerBlock(
            (Block) ModularForceFieldSystem.blockInterdictionMatrix,
            "blockInterdictionMatrix"
        );
        GameRegistry.registerBlock(
            (Block) ModularForceFieldSystem.blockForceManipulator, "blockForceManipulator"

        );
        GameRegistry.registerTileEntity(TileEntityForceField.class, "tileForceField");
        GameRegistry.registerTileEntity(
            TileEntityCoercionDeriver.class, "tileCoercionDeriver"
        );
        GameRegistry.registerTileEntity(
            TileEntityFortronCapacitor.class, "tileFortronCapacitor"
        );
        GameRegistry.registerTileEntity(
            TileEntityForceFieldProjector.class, "tileForceFieldProjector"
        );
        GameRegistry.registerTileEntity(
            TileEntityBiometricIdentifier.class, "tileBiometricIdentifier"
        );
        GameRegistry.registerTileEntity(
            TileEntityInterdictionMatrix.class, "tileInterdictionMatrix"
        );
        GameRegistry.registerTileEntity(
            TileEntityForceManipulator.class, "tileForceManipulator"
        );

        ModularForceFieldSystem.proxy.preInit();

        FluidRegistry.registerFluid(FortronHelper.FLUID_FORTRON);

        GameRegistry.registerItem(itemRemoteController, "itemRemoteController");
        GameRegistry.registerItem(itemFocusMatrix, "itemFocusMatix");
        GameRegistry.registerItem(itemCardBlank, "itemCardBlank");
        GameRegistry.registerItem(itemCardInfinite, "itemCardInfinite");
        GameRegistry.registerItem(itemCardFrequency, "itemCardFrequency");
        GameRegistry.registerItem(itemCardID, "itemCardID");
        GameRegistry.registerItem(itemCardLink, "itemCardLink");
        GameRegistry.registerItem(itemModeCube, "itemModeCube");
        GameRegistry.registerItem(itemModeSphere, "itemModeSphere");
        GameRegistry.registerItem(itemModeTube, "itemModeTube");
        GameRegistry.registerItem(itemModeCylinder, "itemModeCylinder");
        GameRegistry.registerItem(itemModePyramid, "itemModePyramid");
        GameRegistry.registerItem(itemModeCustom, "itemModeCustom");
        GameRegistry.registerItem(itemModuleSpeed, "itemModuleSpeed");
        GameRegistry.registerItem(itemModuleCapacity, "itemModuleCapacity");
        GameRegistry.registerItem(itemModuleTranslate, "itemModuleTranslate");
        GameRegistry.registerItem(itemModuleScale, "itemModuleScale");
        GameRegistry.registerItem(itemModuleRotate, "itemModuleRotate");
        GameRegistry.registerItem(itemModuleCollection, "itemModuleCollection");
        GameRegistry.registerItem(itemModuleInvert, "itemModuleInvert");
        GameRegistry.registerItem(itemModuleSilence, "itemModuleSilence");
        GameRegistry.registerItem(itemModuleFusion, "itemModuleFusion");
        GameRegistry.registerItem(itemModuleManipulator, "itemModuleManipulator");
        GameRegistry.registerItem(itemModuleCamouflage, "itemModuleCamouflage");
        GameRegistry.registerItem(itemModuleDisintegration, "itemModuleDisintegration");
        GameRegistry.registerItem(itemModuleShock, "itemModuleShock");
        GameRegistry.registerItem(itemModuleGlow, "itemModuleGlow");
        GameRegistry.registerItem(itemModuleSponge, "itemModuleSponge");
        GameRegistry.registerItem(itemModuleStablize, "itemModuleStablize");
        GameRegistry.registerItem(itemModuleAntiHostile, "itemModuleAntiHostile");
        GameRegistry.registerItem(itemModuleAntiFriendly, "itemModuleAntiFriendly");
        GameRegistry.registerItem(itemModuleAntiPersonnel, "itemModuleAntiPersonnel");
        GameRegistry.registerItem(itemModuleConfiscate, "itemModuleConfiscate");
        GameRegistry.registerItem(itemModuleWarn, "itemModuleWarn");
        GameRegistry.registerItem(itemModuleBlockAccess, "itemModuleBlockAccess");
        GameRegistry.registerItem(itemModuleBlockAlter, "itemModuleBlockAlter");
        GameRegistry.registerItem(itemModuleAntiSpawn, "itemModuleAntiSpawn");

        channel = NetworkRegistry.INSTANCE.newSimpleChannel("mffs");

        int pkgDiscriminator = 0;
        channel.registerMessage(
            PacketTileHandler.class, PacketTile.class, pkgDiscriminator++, Side.SERVER
        );
        channel.registerMessage(
            PacketFxsHandler.class, PacketFxs.class, pkgDiscriminator++, Side.CLIENT
        );
    }

    @EventHandler
    public void load(final FMLInitializationEvent evt) {
        ModularForceFieldSystem.LOGGER.fine(
            "Language(s) Loaded: "
            + TranslationHelper.loadLanguages(
                "/assets/mffs/lang/", new String[] { "en_US", "zh_CN", "de_DE" }
            )
        );
        ModularForceFieldSystem.metadata.modId = "MFFS";
        ModularForceFieldSystem.metadata.name = "Modular Force Field System";
        ModularForceFieldSystem.metadata.description
            = "Modular Force Field System is a mod that adds force fields, high tech machinery and defensive measures to Minecraft.";
        ModularForceFieldSystem.metadata.url
            = "http://www.universalelectricity.com/mffs/";
        ModularForceFieldSystem.metadata.logoFile = "mffs_logo.png";
        ModularForceFieldSystem.metadata.version = VERSION;
        ModularForceFieldSystem.metadata.authorList
            = Arrays.asList("Calclavia", "LordMZTE", "tilera");
        ModularForceFieldSystem.metadata.credits = "Please visit the website.";
        ModularForceFieldSystem.metadata.autogenerated = false;

        ComputerCraftAPI.registerPeripheralProvider(new MFFSPeripheralProvider());
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent evt) {
        UniversalRecipes.init();
        Recipes.registerRecipes();
        ModularForceFieldSystem.proxy.init();
    }

    @EventHandler
    public void serverStarting(final FMLServerStartingEvent evt) {
        FrequencyGrid.reinitiate();
    }

    static {
        LOGGER = Logger.getLogger("Modular Force Field System");
        ModularForceFieldSystem.damagefieldShock
            = new CustomDamageSource("fieldShock").setDamageBypassesArmor();
    }
}
