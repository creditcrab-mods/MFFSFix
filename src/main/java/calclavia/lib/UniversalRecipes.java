package calclavia.lib;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class UniversalRecipes {
    public static final String PRIMARY_METAL = "ingotSteel";
    public static final String PRIMARY_PLATE = "plateSteel";
    public static final String SECONDARY_METAL = "ingotBronze";
    public static final String SECONDARY_PLATE = "plateBronze";
    public static final String CIRCUIT_T1 = "calclavia:CIRCUIT_T1";
    public static final String CIRCUIT_T2 = "calclavia:CIRCUIT_T2";
    public static final String CIRCUIT_T3 = "calclavia:CIRCUIT_T3";
    public static String ADVANCED_BATTERY = "calclavia:ADVANCED_BATTERY";
    public static String BATTERY = "calclavia:BATTERY";
    public static String BATTERY_BOX = "calclavia:BATTERY_BOX";
    public static final String WRENCH = "calclavia:WRENCH";
    public static final String WIRE = "calclavia:WIRE";
    public static final String MOTOR = "calclavia:MOTOR";
    public static boolean isInit = false;

    public static void init() {
        if (!isInit) {
            // TODO: WTF
            // register("calclavia:CIRCUIT_T1", new Object[]{"circuitBasic",
            // Items.getItem("electronicCircuit"), new
            // ItemStack(Blocks.redstone_torch)}); register("calclavia:CIRCUIT_T2",
            // new Object[]{"circuitAdvanced", Items.getItem("advancedCircuit"), new
            // ItemStack(Items.repeater)}); register("calclavia:CIRCUIT_T3", new
            // Object[]{"circuitElite", Items.getItem("iridiumPlate"), new
            // ItemStack(Block.field_94346_cn)}); register(ADVANCED_BATTERY, new
            // Object[]{"advancedBattery", Items.getItem("energyCrystal"), "battery",
            // new ItemStack(Items.repeater)}); register(BATTERY, new
            // Object[]{"battery", Items.getItem("reBattery"), new
            // ItemStack(Items.repeater)});
            // register(BATTERY_BOX, new Object[]{"batteryBox",
            // Items.getItem("batBox"), new ItemStack(Block.field_72105_ah)});
            // register("calclavia:WRENCH", new Object[]{"wrench",
            // Items.getItem("wrench"), new ItemStack(Item.field_77708_h)});
            // register("calclavia:WIRE", new Object[]{"copperWire",
            // "copperCableBlock", new ItemStack(Item.field_77767_aC)});
            // register("calclavia:MOTOR", new Object[]{"motor",
            // Items.getItem("generator"), new ItemStack(Block.field_71963_Z)});
            isInit = true;
        }
    }

    public static void register(String name, Object... possiblities) {
        Object[] arr$ = possiblities;
        int len$ = possiblities.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            Object possiblity = arr$[i$];
            if (possiblity instanceof ItemStack) {
                if (registerItemStacksToDictionary(
                        name, new ItemStack[] { (ItemStack) possiblity }
                    )) {
                    break;
                }
            } else if (possiblity instanceof String) {
                if (registerItemStacksToDictionary(name, (String) possiblity)) {
                    break;
                }
            } else {
                FMLLog.severe(
                    "Universal Recipes: Error Registering " + name, new Object[0]
                );
            }
        }
    }

    public static boolean registerItemStacksToDictionary(String name, List itemStacks) {
        boolean returnValue = false;
        if (itemStacks != null && itemStacks.size() > 0) {
            Iterator i$ = itemStacks.iterator();

            while (i$.hasNext()) {
                ItemStack stack = (ItemStack) i$.next();
                if (stack != null) {
                    OreDictionary.registerOre(name, stack);
                    returnValue = true;
                }
            }
        }

        return returnValue;
    }

    public static boolean
    registerItemStacksToDictionary(String name, ItemStack... itemStacks) {
        return registerItemStacksToDictionary(name, Arrays.asList(itemStacks));
    }

    public static boolean registerItemStacksToDictionary(String name, String stackName) {
        return registerItemStacksToDictionary(
            name, (List) OreDictionary.getOres(stackName)
        );
    }
}
