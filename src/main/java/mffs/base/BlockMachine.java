package mffs.base;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import icbm.api.ICamouflageMaterial;
import mffs.MFFSCreativeTab;
import mffs.ModularForceFieldSystem;
import mffs.api.IBiometricIdentifierLink;
import mffs.api.security.Permission;
import mffs.item.card.ItemCardLink;
import mffs.render.RenderBlockHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.block.BlockRotatable;
import universalelectricity.prefab.implement.IRedstoneReceptor;

public abstract class BlockMachine extends BlockRotatable implements ICamouflageMaterial {
    public BlockMachine(final String name) {
        super(UniversalElectricity.machine);
        this.setBlockName("mffs:" + name);
        this.setHardness(Float.MAX_VALUE);
        this.setResistance(100.0f);
        this.setStepSound(BlockMachine.soundTypeMetal);
        this.setCreativeTab(MFFSCreativeTab.INSTANCE);
    }

    @Override
    public boolean onMachineActivated(
        final World world,
        final int x,
        final int y,
        final int z,
        final EntityPlayer entityPlayer,
        final int side,
        final float hitX,
        final float hitY,
        final float hitZ
    ) {
        if (!world.isRemote) {
            if (entityPlayer.getCurrentEquippedItem() != null
                && entityPlayer.getCurrentEquippedItem().getItem()
                        instanceof ItemCardLink) {
                return false;
            }
            entityPlayer.openGui(
                (Object) ModularForceFieldSystem.instance, 0, world, x, y, z
            );
        }
        return true;
    }

    @Override
    public boolean onSneakMachineActivated(
        final World world,
        final int x,
        final int y,
        final int z,
        final EntityPlayer entityPlayer,
        final int side,
        final float hitX,
        final float hitY,
        final float hitZ
    ) {
        return this.onUseWrench(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean onSneakUseWrench(
        final World world,
        final int x,
        final int y,
        final int z,
        final EntityPlayer entityPlayer,
        final int side,
        final float hitX,
        final float hitY,
        final float hitZ
    ) {
        if (!world.isRemote) {
            final TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity instanceof IBiometricIdentifierLink) {
                if (((IBiometricIdentifierLink) tileEntity).getBiometricIdentifier()
                    == null) {
                    this.dropBlockAsItem(
                        world, x, y, z, world.getBlockMetadata(x, y, z), 0
                    );
                    world.setBlockToAir(x, y, z);
                    return true;
                }
                if (((IBiometricIdentifierLink) tileEntity)
                        .getBiometricIdentifier()
                        .isAccessGranted(
                            entityPlayer.getDisplayName(),
                            Permission.SECURITY_CENTER_CONFIGURE
                        )) {
                    this.dropBlockAsItem(
                        world, x, y, z, world.getBlockMetadata(x, y, z), 0
                    );
                    world.setBlockToAir(x, y, z);
                    return true;
                }
                entityPlayer.addChatMessage(new ChatComponentText(
                    "["
                    + ModularForceFieldSystem.blockBiometricIdentifier.getLocalizedName()
                    + "]"
                    + " Cannot remove machine! Access denied!"
                ));
            }
        }
        return false;
    }

    @Override
    public void onNeighborBlockChange(
        final World world, final int x, final int y, final int z, final Block block
    ) {
        if (!world.isRemote) {
            final TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity instanceof IRedstoneReceptor) {
                if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
                    ((IRedstoneReceptor) tileEntity).onPowerOn();
                } else {
                    ((IRedstoneReceptor) tileEntity).onPowerOff();
                }
            }
        }
    }

    @Override
    public float getExplosionResistance(
        final Entity entity,
        final World world,
        final int i,
        final int j,
        final int k,
        final double d,
        final double d1,
        final double d2
    ) {
        return 100.0f;
    }

    @Override
    public void registerBlockIcons(final IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon("mffs:machine");
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderType() {
        return RenderBlockHandler.ID;
    }
}
