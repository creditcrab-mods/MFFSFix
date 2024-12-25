package mffs.block;

import mffs.base.BlockMachine;
import mffs.tileentity.TileEntityForceManipulator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockForceManipulator extends BlockMachine {
    public BlockForceManipulator() {
        super("manipulator");
    }

    public static int determineOrientation(
        final World world,
        final int x,
        final int y,
        final int z,
        final EntityPlayer entityPlayer
    ) {
        if (MathHelper.abs((float) ((Entity) entityPlayer).posX - x) < 2.0f
            && MathHelper.abs((float) ((Entity) entityPlayer).posZ - z) < 2.0f) {
            final double var5
                = ((Entity) entityPlayer).posY + 1.82 - ((Entity) entityPlayer).yOffset;
            if (var5 - y > 2.0) {
                return 1;
            }
            if (y - var5 > 0.0) {
                return 0;
            }
        }
        final int var6 = MathHelper.floor_double(
                             ((Entity) entityPlayer).rotationYaw * 4.0f / 360.0f + 0.5
                         )
            & 0x3;
        return (var6 == 0)
            ? 2
            : ((var6 == 1) ? 5 : ((var6 == 2) ? 3 : ((var6 == 3) ? 4 : 0)));
    }

    @Override
    public void onBlockPlacedBy(
        final World world,
        final int x,
        final int y,
        final int z,
        final EntityLivingBase par5EntityLiving,
        final ItemStack stack
    ) {
        final int metadata = determineOrientation(
            world,
            x,
            y,
            z,
            (EntityPlayer) par5EntityLiving
        ); // TODO: ClassCastException?
        world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
    }

    @Override
    public boolean onUseWrench(
        final World world,
        final int x,
        final int y,
        final int z,
        final EntityPlayer par5EntityPlayer,
        final int side,
        final float hitX,
        final float hitY,
        final float hitZ
    ) {
        final int mask = 7;
        final int rotMeta = world.getBlockMetadata(x, y, z);
        final int masked = rotMeta & ~mask;
        final ForgeDirection orientation = ForgeDirection.getOrientation(rotMeta & mask);
        final ForgeDirection rotated
            = orientation.getRotation(ForgeDirection.getOrientation(side));
        world.setBlockMetadataWithNotify(x, y, z, (rotated.ordinal() & mask) | masked, 3);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(final World world, int meta) {
        return new TileEntityForceManipulator();
    }
}
