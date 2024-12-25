package mffs.gui;

import mffs.base.GuiBase;
import mffs.container.ContainerForceFieldProjector;
import mffs.tileentity.TileEntityCoercionDeriver;
import mffs.tileentity.TileEntityForceFieldProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.core.vector.Vector2;
import universalelectricity.prefab.vector.Region2;

public class GuiForceFieldProjector extends GuiBase {
    private TileEntityForceFieldProjector tileEntity;

    public GuiForceFieldProjector(
        final EntityPlayer player, final TileEntityForceFieldProjector tileEntity
    ) {
        super(new ContainerForceFieldProjector(player, tileEntity), tileEntity);
        this.tileEntity = tileEntity;
    }

    @Override
    public void initGui() {
        super.textFieldPos = new Vector2(48.0, 91.0);
        super.initGui();
        super.tooltips.put(
            new Region2(new Vector2(117.0, 44.0), new Vector2(117.0, 44.0).add(18.0)),
            "Mode"
        );
        super.tooltips.put(
            new Region2(new Vector2(90.0, 17.0), new Vector2(90.0, 17.0).add(18.0)), "Up"
        );
        super.tooltips.put(
            new Region2(new Vector2(144.0, 17.0), new Vector2(144.0, 17.0).add(18.0)),
            "Up"
        );
        super.tooltips.put(
            new Region2(new Vector2(90.0, 71.0), new Vector2(90.0, 71.0).add(18.0)),
            "Down"
        );
        super.tooltips.put(
            new Region2(new Vector2(144.0, 71.0), new Vector2(144.0, 71.0).add(18.0)),
            "Down"
        );
        super.tooltips.put(
            new Region2(new Vector2(108.0, 17.0), new Vector2(108.0, 17.0).add(18.0)),
            "Front"
        );
        super.tooltips.put(
            new Region2(new Vector2(126.0, 17.0), new Vector2(126.0, 17.0).add(18.0)),
            "Front"
        );
        super.tooltips.put(
            new Region2(new Vector2(108.0, 71.0), new Vector2(108.0, 71.0).add(18.0)),
            "Back"
        );
        super.tooltips.put(
            new Region2(new Vector2(126.0, 71.0), new Vector2(126.0, 71.0).add(18.0)),
            "Back"
        );
        super.tooltips.put(
            new Region2(new Vector2(90.0, 35.0), new Vector2(108.0, 35.0).add(18.0)),
            "Left"
        );
        super.tooltips.put(
            new Region2(new Vector2(90.0, 53.0), new Vector2(108.0, 53.0).add(18.0)),
            "Left"
        );
        super.tooltips.put(
            new Region2(new Vector2(144.0, 35.0), new Vector2(144.0, 35.0).add(18.0)),
            "Right"
        );
        super.tooltips.put(
            new Region2(new Vector2(144.0, 53.0), new Vector2(144.0, 53.0).add(18.0)),
            "Right"
        );
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int x, final int y) {
        this.fontRendererObj.drawString(
            this.tileEntity.getInventoryName(),
            this.xSize / 2
                - this.fontRendererObj.getStringWidth(this.tileEntity.getInventoryName())
                    / 2,
            6,
            4210752
        );
        GL11.glPushMatrix();
        GL11.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
        this.fontRendererObj.drawString(
            this.tileEntity
                .getDirection(
                    (IBlockAccess) this.tileEntity.getWorldObj(),
                    this.tileEntity.xCoord,
                    this.tileEntity.yCoord,
                    this.tileEntity.zCoord
                )
                .name(),
            -63,
            8,
            4210752
        );
        GL11.glPopMatrix();
        this.drawTextWithTooltip("matrix", 32, 20, x, y);
        this.drawTextWithTooltip("frequency", "%1:", 8, 76, x, y);
        super.textFieldFrequency.drawTextBox();
        this.drawTextWithTooltip(
            "fortron",
            "%1: "
                + UnitDisplay.getDisplayShort(
                    this.tileEntity.getFortronEnergy()
                        * TileEntityCoercionDeriver.FORTRON_UE_RATIO,
                    UnitDisplay.Unit.JOULES
                )
                + "/"
                + UnitDisplay.getDisplayShort(
                    this.tileEntity.getFortronCapacity()
                        * TileEntityCoercionDeriver.FORTRON_UE_RATIO,
                    UnitDisplay.Unit.JOULES
                ),
            8,
            110,
            x,
            y
        );
        this.fontRendererObj.drawString(
            "§4-"
                + UnitDisplay.getDisplayShort(
                    this.tileEntity.getFortronCost() * 20, UnitDisplay.Unit.JOULES
                ),
            120,
            121,
            4210752
        );
        super.drawGuiContainerForegroundLayer(x, y);
    }

    @Override
    protected void
    drawGuiContainerBackgroundLayer(final float f, final int x, final int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.drawSlot(9, 88);
        this.drawSlot(27, 88);
        this.drawSlot(117, 44, SlotType.NONE, 1.0f, 0.4f, 0.4f);
        for (int xSlot = 0; xSlot < 4; ++xSlot) {
            for (int ySlot = 0; ySlot < 4; ++ySlot) {
                if ((xSlot != 1 || ySlot != 1) && (xSlot != 2 || ySlot != 2)
                    && (xSlot != 1 || ySlot != 2) && (xSlot != 2 || ySlot != 1)) {
                    SlotType type = SlotType.NONE;
                    if (xSlot == 0 && ySlot == 0) {
                        type = SlotType.ARR_UP_LEFT;
                    } else if (xSlot == 0 && ySlot == 3) {
                        type = SlotType.ARR_DOWN_LEFT;
                    } else if (xSlot == 3 && ySlot == 0) {
                        type = SlotType.ARR_UP_RIGHT;
                    } else if (xSlot == 3 && ySlot == 3) {
                        type = SlotType.ARR_DOWN_RIGHT;
                    } else if (ySlot == 0) {
                        type = SlotType.ARR_UP;
                    } else if (ySlot == 3) {
                        type = SlotType.ARR_DOWN;
                    } else if (xSlot == 0) {
                        type = SlotType.ARR_LEFT;
                    } else if (xSlot == 3) {
                        type = SlotType.ARR_RIGHT;
                    }
                    this.drawSlot(90 + 18 * xSlot, 17 + 18 * ySlot, type);
                }
            }
        }
        for (int xSlot = 0; xSlot < 3; ++xSlot) {
            for (int ySlot = 0; ySlot < 2; ++ySlot) {
                this.drawSlot(18 + 18 * xSlot, 35 + 18 * ySlot);
            }
        }
        this.drawForce(
            8,
            120,
            Math.min(
                this.tileEntity.getFortronEnergy()
                    / (float) this.tileEntity.getFortronCapacity(),
                1.0f
            )
        );
    }
}
