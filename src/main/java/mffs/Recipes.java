package mffs;

import basiccomponents.common.BasicComponents;
import mffs.recipe.RecipeBuilder;
import mffs.recipe.ShapedOreRecipeAdapter;
import mffs.recipe.ShapelessOreRecipeAdapter;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Recipes {
    public static void registerRecipes() {
        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemFocusMatrix, 9)
            .pattern("RMR", "MDM", "RMR")
            .ingredient('M', "ingotSteel")
            .ingredient('D', Items.diamond)
            .ingredient('R', Items.redstone)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemRemoteController)
            .pattern("WWW", "MCM", "MCM")
            .ingredient('W', BasicComponents.blockCopperWire)
            .ingredient('M', "ingotSteel")
            .ingredient('C', BasicComponents.itemBattery)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.blockCoercionDeriver)
            .pattern("M M", "MFM", "MCM")
            .ingredient('C', BasicComponents.itemBattery)
            .ingredient('M', "ingotSteel")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.blockFortronCapacitor)
            .pattern("MFM", "FCF", "MFM")
            .ingredient('C', BasicComponents.itemBattery)
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('M', "ingotSteel")
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.blockForceFieldProjector)
            .pattern(" D ", "FFF", "MCM")
            .ingredient('D', Items.diamond)
            .ingredient('C', BasicComponents.itemBattery)
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('M', "ingotSteel")
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.blockBiometricIdentifier)
            .pattern("FMF", "MCM", "FMF")
            .ingredient('C', ModularForceFieldSystem.itemCardBlank)
            .ingredient('M', "ingotSteel")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.blockInterdictionMatrix)
            .pattern("SSS", "FFF", "FEF")
            .ingredient('S', ModularForceFieldSystem.itemModuleShock)
            .ingredient('E', Blocks.ender_chest)
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.blockForceManipulator)
            .pattern("F F", "FMF", "F F")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('M', BasicComponents.itemMotor)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemCardBlank)
            .pattern("PPP", "PMP", "PPP")
            .ingredient('M', "ingotSteel")
            .ingredient('P', Items.paper)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemCardLink)
            .pattern("BWB")
            .ingredient('B', ModularForceFieldSystem.itemCardBlank)
            .ingredient('W', BasicComponents.blockCopperWire)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemCardLink)
            .pattern("WBW")
            .ingredient('B', ModularForceFieldSystem.itemCardFrequency)
            .ingredient('W', BasicComponents.blockCopperWire)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemCardID)
            .pattern("RBR")
            .ingredient('B', ModularForceFieldSystem.itemCardBlank)
            .ingredient('R', Items.redstone)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModeSphere)
            .pattern(" F ", "FFF", " F ")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModeCube)
            .pattern("FFF", "FFF", "FFF")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModeTube)
            .pattern("FFF", "   ", "FFF")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModePyramid)
            .pattern("F  ", "FF ", "FFF")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModeCylinder)
            .pattern("S", "S", "S")
            .ingredient('S', ModularForceFieldSystem.itemModeSphere)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModeCustom)
            .pattern(" C ", "TFP", " S ")
            .ingredient('S', ModularForceFieldSystem.itemModeSphere)
            .ingredient('C', ModularForceFieldSystem.itemModeCube)
            .ingredient('T', ModularForceFieldSystem.itemModeTube)
            .ingredient('P', ModularForceFieldSystem.itemModePyramid)
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .register();

        new RecipeBuilder(new ShapelessOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModeCustom)
            .ingredient(ModularForceFieldSystem.itemModeCustom)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleSpeed)
            .pattern("FFF", "RRR", "FFF")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('R', Items.redstone)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleCapacity)
            .pattern("FCF")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('C', BasicComponents.itemBattery)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleShock)
            .pattern("FWF")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('W', BasicComponents.blockCopperWire)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleSponge)
            .pattern("BBB", "BFB", "BBB")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('B', Items.water_bucket)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleDisintegration)
            .pattern(" W ", "FBF", " W ")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('W', BasicComponents.blockCopperWire)
            .ingredient('B', BasicComponents.itemBattery)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleManipulator)
            .pattern("F", " ", "F")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleCamouflage)
            .pattern("WFW", "FWF", "WFW")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('W', new ItemStack(Blocks.wool, 1, 32767))
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleFusion)
            .pattern("FJF")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('J', ModularForceFieldSystem.itemModuleShock)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleScale, 2)
            .pattern("FRF")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient(
                'R',
                Items.redstone
            ) // this is a guess, R isnt defined in the original
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleTranslate, 2)
            .pattern("FSF")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('S', ModularForceFieldSystem.itemModuleScale)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleRotate)
            .pattern("F  ", " F ", "  F")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleGlow)
            .pattern("GGG", "GFG", "GGG")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('G', Blocks.glowstone)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleStablize)
            .pattern("FDF", "PSA", "FDF")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('P', Items.diamond_pickaxe)
            .ingredient('S', Items.diamond_shovel)
            .ingredient('A', Items.diamond_axe)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleCollection)
            .pattern("F F", " H ", "F F")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('H', Blocks.hopper)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleInvert)
            .pattern("L", "F", "L")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('L', Blocks.lapis_block)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleSilence)
            .pattern(" N ", "NFN", " N ")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('N', Blocks.noteblock)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleAntiHostile)
            .pattern(" R ", "GFB", " S ")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('G', Items.gunpowder)
            .ingredient('R', Items.rotten_flesh)
            .ingredient('B', Items.bone)
            .ingredient('S', Items.ghast_tear)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleAntiFriendly)
            .pattern(" R ", "GFB", " S ")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('G', Items.cooked_porkchop)
            .ingredient('R', new ItemStack(Blocks.wool, 1, 32767))
            .ingredient('B', Items.leather)
            .ingredient('S', Items.slime_ball)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleAntiPersonnel)
            .pattern("BFG")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('B', ModularForceFieldSystem.itemModuleAntiHostile)
            .ingredient('G', ModularForceFieldSystem.itemModuleAntiFriendly)
            .register();

        // TODO: config option for confiscate module
        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleConfiscate)
            .pattern("PEP", "EFE", "PEP")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('E', Items.ender_eye)
            .ingredient('P', Items.ender_pearl)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleWarn)
            .pattern("NFN")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('N', Blocks.noteblock)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleBlockAccess)
            .pattern(" C ", "BFB", " C ")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('B', Blocks.iron_block)
            .ingredient('C', Blocks.chest)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleBlockAlter)
            .pattern(" G ", "GFG", " G ")
            .ingredient('F', ModularForceFieldSystem.itemFocusMatrix)
            .ingredient('G', Blocks.gold_block)
            .register();

        new RecipeBuilder(new ShapedOreRecipeAdapter())
            .output(ModularForceFieldSystem.itemModuleAntiSpawn)
            .pattern(" H ", "G G", " H ")
            .ingredient('H', ModularForceFieldSystem.itemModuleAntiHostile)
            .ingredient('G', ModularForceFieldSystem.itemModuleAntiFriendly)
            .register();
    }
}
