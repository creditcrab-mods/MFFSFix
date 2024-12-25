package mffs.render;

import calclavia.lib.render.CalclaviaRenderHelper;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.tileentity.TileEntityForceField;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

@SideOnly(Side.CLIENT)
public class RenderForceField implements ISimpleBlockRenderingHandler {
    public static final int ID;

    @Override
    public void renderInventoryBlock(
        final Block block,
        final int metadata,
        final int modelID,
        final RenderBlocks renderer
    ) {
        CalclaviaRenderHelper.renderNormalBlockAsItem(block, metadata, renderer);
    }

    @Override
    public boolean renderWorldBlock(
        final IBlockAccess iBlockAccess,
        final int x,
        final int y,
        final int z,
        final Block block,
        final int modelId,
        final RenderBlocks renderer
    ) {
        boolean shouldRender = false;
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (block.shouldSideBeRendered(
                    iBlockAccess,
                    x + dir.offsetX,
                    y + dir.offsetY,
                    z + dir.offsetZ,
                    iBlockAccess.getBlockMetadata(x, y, z)
                )) {
                shouldRender = true;
                break;
            }
        }

        if (!shouldRender)
            return false;

        int renderType = 0;
        final TileEntity tileEntity = iBlockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityForceField) {
            final ItemStack checkStack = ((TileEntityForceField) tileEntity).camoStack;
            if (checkStack != null) {
                final Block checkBlock = Block.getBlockFromItem(checkStack.getItem());
                if (checkBlock != null) {
                    renderType = checkBlock.getRenderType();
                }
            }
        }

        if (renderType >= 0) {
            switch (renderType) {
                case 4: {
                    renderer.renderBlockLiquid(block, x, y, z);
                    break;
                }
                case 31: {
                    renderer.renderBlockLog(block, x, y, z);
                    break;
                }
                case 1: {
                    renderer.renderCrossedSquares(block, x, y, z);
                    break;
                }
                case 20: {
                    renderer.renderBlockVine(block, x, y, z);
                    break;
                }
                case 39: {
                    renderer.renderBlockQuartz(block, x, y, z);
                    break;
                }
                case 5: {
                    renderer.renderBlockRedstoneWire(block, x, y, z);
                    break;
                }
                case 13: {
                    renderer.renderBlockCactus(block, x, y, z);
                    break;
                }
                case 23: {
                    renderer.renderBlockLilyPad(block, x, y, z);
                    break;
                }
                case 6: {
                    renderer.renderBlockCrops(block, x, y, z);
                    break;
                }
                case 8: {
                    renderer.renderBlockLadder(block, x, y, z);
                    break;
                }
                case 7: {
                    renderer.renderBlockDoor(block, x, y, z);
                    break;
                }
                case 12: {
                    renderer.renderBlockLever(block, x, y, z);
                    break;
                }
                case 29: {
                    renderer.renderBlockTripWireSource(block, x, y, z);
                    break;
                }
                case 30: {
                    renderer.renderBlockTripWire(block, x, y, z);
                    break;
                }
                case 14: {
                    renderer.renderBlockBed(block, x, y, z);
                    break;
                }
                case 16: {
                    renderer.renderPistonBase(block, x, y, z, false);
                    break;
                }
                case 17: {
                    renderer.renderPistonExtension(block, x, y, z, true);
                    break;
                }
                default: {
                    renderer.renderStandardBlock(block, x, y, z);
                    break;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int arg0) {
        return true;
    }

    @Override
    public int getRenderId() {
        return RenderForceField.ID;
    }

    static {
        ID = RenderingRegistry.getNextAvailableRenderId();
    }
}
