package mffs.block;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mffs.ModularForceFieldSystem;
import mffs.api.IForceFieldBlock;
import mffs.api.IProjector;
import mffs.api.fortron.IFortronStorage;
import mffs.api.modules.IModule;
import mffs.api.security.IBiometricIdentifier;
import mffs.api.security.Permission;
import mffs.base.BlockBase;
import mffs.render.RenderForceField;
import mffs.tileentity.TileEntityForceField;
import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.CustomDamageSource;

public class BlockForceField
    extends BlockBase implements IForceFieldBlock, IPartialSealableBlock {
    public BlockForceField() {
        super("forceField", Material.glass);
        this.setBlockUnbreakable();
        this.setResistance(999.0f);
        this.setCreativeTab(null);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return RenderForceField.ID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(
        final IBlockAccess par1IBlockAccess,
        final int par2,
        final int par3,
        final int par4,
        final int par5
    ) {
        final Block i1 = par1IBlockAccess.getBlock(par2, par3, par4);
        return i1 != this
            && super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
    }

    @Override
    public void onBlockClicked(
        final World world,
        final int x,
        final int y,
        final int z,
        final EntityPlayer entityPlayer
    ) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityForceField
            && ((TileEntityForceField) tileEntity).getProjector() != null) {
            for (final ItemStack moduleStack :
                 ((TileEntityForceField) tileEntity)
                     .getProjector()
                     .getModuleStacks(((TileEntityForceField) tileEntity)
                                          .getProjector()
                                          .getModuleSlots())) {
                if (((IModule) moduleStack.getItem())
                        .onCollideWithForceField(
                            world, x, y, z, (Entity) entityPlayer, moduleStack
                        )) {
                    return;
                }
            }
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(
        final World world, final int x, final int y, final int z
    ) {
        if (world.isRemote) {
            if (ModularForceFieldSystem.proxy.isSneaking()) {
                return null;
            }
        }
        if (this.getProjector((IBlockAccess) world, x, y, z) != null && !world.isRemote) {
            final IBiometricIdentifier BiometricIdentifier
                = this.getProjector((IBlockAccess) world, x, y, z)
                      .getBiometricIdentifier();
            final List entities = world.getEntitiesWithinAABB(
                EntityPlayer.class,
                AxisAlignedBB.getBoundingBox(
                    (double) (x),
                    (double) y,
                    (double) (z),
                    (double) (x + 1),
                    y + 1,
                    (double) (z + 1)
                )
            );
            for (final EntityPlayer entityPlayer : (List<EntityPlayer>) entities) {
                if (entityPlayer != null && entityPlayer.isSneaking()) {
                    if (entityPlayer.capabilities.isCreativeMode) {
                        return null;
                    }
                    if (BiometricIdentifier != null
                        && BiometricIdentifier.isAccessGranted(
                            entityPlayer.getDisplayName(), Permission.FORCE_FIELD_WARP
                        )) {
                        return null;
                    }
                    continue;
                }
            }
        }
        final float f = 0.0625f;
        return AxisAlignedBB.getBoundingBox(
            (double) (x + f),
            (double) (y + f),
            (double) (z + f),
            (double) (x + 1 - f),
            (double) (y + 1 - f),
            (double) (z + 1 - f)
        );
    }

    @Override
    public void onEntityCollidedWithBlock(
        final World world, final int x, final int y, final int z, final Entity entity
    ) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityForceField
            && this.getProjector((IBlockAccess) world, x, y, z) != null) {
            for (final ItemStack moduleStack :
                 ((TileEntityForceField) tileEntity)
                     .getProjector()
                     .getModuleStacks(((TileEntityForceField) tileEntity)
                                          .getProjector()
                                          .getModuleSlots())) {
                if (((IModule) moduleStack.getItem())
                        .onCollideWithForceField(world, x, y, z, entity, moduleStack)) {
                    return;
                }
            }
            final IBiometricIdentifier biometricIdentifier
                = this.getProjector((IBlockAccess) world, x, y, z)
                      .getBiometricIdentifier();
            if (new Vector3(entity).distanceTo(new Vector3(x, y, z).add(0.4)) < 0.5
                && entity instanceof EntityLiving && !world.isRemote) {
                ((EntityLiving) entity)
                    .addPotionEffect(new PotionEffect(Potion.confusion.id, 80, 3));
                ((EntityLiving) entity)
                    .addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 20, 1));
                boolean hasPermission = false;
                final List entities = world.getEntitiesWithinAABB(
                    EntityPlayer.class,
                    AxisAlignedBB.getBoundingBox(
                        (double) x,
                        (double) y,
                        (double) z,
                        (double) (x + 1),
                        y + 0.9,
                        (double) (z + 1)
                    )
                );
                for (final EntityPlayer entityPlayer : (List<EntityPlayer>) entities) {
                    if (entityPlayer != null && entityPlayer.isSneaking()) {
                        if (entityPlayer.capabilities.isCreativeMode) {
                            hasPermission = true;
                            break;
                        }
                        if (biometricIdentifier == null
                            || !biometricIdentifier.isAccessGranted(
                                entityPlayer.getDisplayName(), Permission.FORCE_FIELD_WARP
                            )) {
                            continue;
                        }
                        hasPermission = true;
                    }
                }
                if (!hasPermission) {
                    entity.attackEntityFrom(
                        (DamageSource) CustomDamageSource.electrocution, Integer.MAX_VALUE
                    );
                }
            }
        }
    }

    @Override
    public IIcon getIcon(
        final IBlockAccess iBlockAccess,
        final int x,
        final int y,
        final int z,
        final int side
    ) {
        final TileEntity tileEntity = iBlockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityForceField) {
            final ItemStack checkStack = ((TileEntityForceField) tileEntity).camoStack;
            if (checkStack != null) {
                try {
                    final Block block = Block.getBlockFromItem(checkStack.getItem());
                    final Integer[] allowedRenderTypes
                        = { 0, 1, 4, 31, 20, 39, 5,  13, 23,
                            6, 8, 7, 12, 29, 30, 14, 16, 17 };
                    if (Arrays.asList(allowedRenderTypes)
                            .contains(block.getRenderType())) {
                        final IIcon icon
                            = block.getIcon(side, checkStack.getItemDamage());
                        if (icon != null) {
                            return icon;
                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return this.getIcon(side, iBlockAccess.getBlockMetadata(x, y, z));
    }

    @Override
    public int colorMultiplier(
        final IBlockAccess iBlockAccess, final int x, final int y, final int z
    ) {
        try {
            final TileEntity tileEntity = iBlockAccess.getTileEntity(x, y, z);
            if (tileEntity instanceof TileEntityForceField) {
                final ItemStack checkStack
                    = ((TileEntityForceField) tileEntity).camoStack;
                if (checkStack != null) {
                    try {
                        return Block.getBlockFromItem(checkStack.getItem())
                            .colorMultiplier(iBlockAccess, x, y, x);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (final Exception e2) {
            e2.printStackTrace();
        }
        return super.colorMultiplier(iBlockAccess, x, y, z);
    }

    @Override
    public int getLightValue(
        final IBlockAccess iBlockAccess, final int x, final int y, final int z
    ) {
        try {
            final TileEntity tileEntity = iBlockAccess.getTileEntity(x, y, z);
            if (tileEntity instanceof TileEntityForceField) {
                final IProjector zhuYao
                    = ((TileEntityForceField) tileEntity).getProjectorSafe();
                if (zhuYao instanceof IProjector) {
                    return (int
                    ) (Math.min(
                           zhuYao.getModuleCount(
                               ModularForceFieldSystem.itemModuleGlow, new int[0]
                           ),
                           64
                       )
                       / 64.0f * 15.0f);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public float getExplosionResistance(
        final Entity entity,
        final World world,
        final int x,
        final int y,
        final int z,
        final double d,
        final double d1,
        final double d2
    ) {
        return 2.1474836E9f;
    }

    @Override
    public TileEntity createNewTileEntity(final World world, int meta) {
        return new TileEntityForceField();
    }

    @Override
    public void weakenForceField(
        final World world, final int x, final int y, final int z, final int joules
    ) {
        final IProjector projector = this.getProjector((IBlockAccess) world, x, y, z);
        if (projector != null) {
            ((IFortronStorage) projector).provideFortron(joules, true);
        }
        world.setBlock(x, y, z, Blocks.air, 0, 3);
    }

    @Override
    public ItemStack getPickBlock(
        final MovingObjectPosition target,
        final World world,
        final int x,
        final int y,
        final int z
    ) {
        return null;
    }

    @Override
    public IProjector
    getProjector(final IBlockAccess iBlockAccess, final int x, final int y, final int z) {
        final TileEntity tileEntity = iBlockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityForceField) {
            return ((TileEntityForceField) tileEntity).getProjector();
        }
        return null;
    }

    @Override
    public boolean isSealed(World world, int x, int y, int z, ForgeDirection direction) {
        return true;
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.blockIcon = p_149651_1_.registerIcon("mffs:forceField");
    }
}
