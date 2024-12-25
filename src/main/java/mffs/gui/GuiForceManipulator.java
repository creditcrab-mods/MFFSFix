package mffs.gui;

import mffs.ModularForceFieldSystem;
import mffs.base.GuiBase;
import mffs.base.PacketTile;
import mffs.container.ContainerForceManipulator;
import mffs.gui.button.GuiIcon;
import mffs.tileentity.TileEntityCoercionDeriver;
import mffs.tileentity.TileEntityForceManipulator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.core.vector.Vector2;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.vector.Region2;

public class GuiForceManipulator extends GuiBase {
    private TileEntityForceManipulator tileEntity;

    public GuiForceManipulator(
        final EntityPlayer player, final TileEntityForceManipulator tileEntity
    ) {
        super(new ContainerForceManipulator(player, tileEntity), tileEntity);
        this.tileEntity = tileEntity;
    }

    @Override
    public void initGui() {
        super.textFieldPos = new Vector2(111.0, 93.0);
        super.initGui();
        this.buttonList.add(
            new GuiButton(1, this.width / 2 - 60, this.height / 2 - 22, 40, 20, "Reset")
        );
        this.buttonList.add(new GuiIcon(
            2,
            this.width / 2 - 82,
            this.height / 2 - 82,
            new ItemStack[] { null,
                              new ItemStack(Items.redstone),
                              new ItemStack(Blocks.redstone_block) }
        ));
        this.buttonList.add(new GuiIcon(
            3,
            this.width / 2 - 82,
            this.height / 2 - 60,
            new ItemStack[] { null, new ItemStack(Blocks.anvil) }
        ));
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
            -100,
            10,
            4210752
        );
        GL11.glPopMatrix();
        this.fontRendererObj.drawString("Anchor:", 30, 60, 4210752);
        if (this.tileEntity.anchor != null) {
            this.fontRendererObj.drawString(
                this.tileEntity.anchor.intX() + ", " + this.tileEntity.anchor.intY()
                    + ", " + this.tileEntity.anchor.intZ(),
                30,
                72,
                4210752
            );
        }
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
                    this.tileEntity.getFortronCost(), UnitDisplay.Unit.JOULES
                ),
            120,
            121,
            4210752
        );
        super.drawGuiContainerForegroundLayer(x, y);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        ((GuiIcon) this.buttonList.get(2)).setIndex(this.tileEntity.displayMode);
        ((GuiIcon) this.buttonList.get(3)).setIndex(this.tileEntity.doAnchor ? 1 : 0);
    }

    @Override
    protected void
    drawGuiContainerBackgroundLayer(final float f, final int x, final int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.drawSlot(72, 90);
        this.drawSlot(90, 90);
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
                this.drawSlot(30 + 18 * xSlot, 18 + 18 * ySlot);
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

    @Override
    protected void actionPerformed(final GuiButton guiButton) {
        super.actionPerformed(guiButton);

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("buttonId", guiButton.id);

        if (guiButton.id == 1 || guiButton.id == 2 || guiButton.id == 3) {
            ModularForceFieldSystem.channel.sendToServer(new PacketTile(
                PacketTile.Type.TOGGLE_MODE, new Vector3(this.tileEntity), nbt
            ));
        }
    }
}
