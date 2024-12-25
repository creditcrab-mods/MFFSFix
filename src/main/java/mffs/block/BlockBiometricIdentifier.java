package mffs.block;

import mffs.tileentity.TileEntityBiometricIdentifier;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBiometricIdentifier extends BlockMachineBlock {
    public BlockBiometricIdentifier() {
        super("biometricIdentifier");
    }

    @Override
    public TileEntity createNewTileEntity(final World world, int meta) {
        return new TileEntityBiometricIdentifier();
    }
}
