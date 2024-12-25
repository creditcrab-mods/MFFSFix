package mffs.gui;

import mffs.ModularForceFieldSystem;
import mffs.base.GuiBase;
import mffs.base.PacketTile;
import mffs.container.ContainerInterdictionMatrix;
import mffs.tileentity.TileEntityCoercionDeriver;
import mffs.tileentity.TileEntityInterdictionMatrix;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.core.vector.Vector2;
import universalelectricity.core.vector.Vector3;

public class GuiInterdictionMatrix extends GuiBase {
    private TileEntityInterdictionMatrix tileEntity;

    public GuiInterdictionMatrix(
        final EntityPlayer player, final TileEntityInterdictionMatrix tileEntity
    ) {
        super(new ContainerInterdictionMatrix(player, tileEntity), tileEntity);
        this.tileEntity = tileEntity;
    }

    @Override
    public void initGui() {
        super.textFieldPos = new Vector2(110.0, 91.0);
        super.initGui();
        this.buttonList.add(
            new GuiButton(1, this.width / 2 - 80, this.height / 2 - 65, 50, 20, "Banned")
        );
    }

    @Override
    protected void actionPerformed(final GuiButton guiButton) {
        super.actionPerformed(guiButton);
        if (guiButton.id == 1) {
            ModularForceFieldSystem.channel.sendToServer(new PacketTile(
                PacketTile.Type.TOGGLE_MODE,
                new Vector3(this.tileEntity),
                new NBTTagCompound()
            ));
        }
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
        this.drawTextWithTooltip(
            "warn", "%1: " + this.tileEntity.getWarningRange(), 35, 19, x, y
        );
        this.drawTextWithTooltip(
            "action", "%1: " + this.tileEntity.getActionRange(), 100, 19, x, y
        );
        this.drawTextWithTooltip("filterMode", "%1:", 9, 32, x, y);
        if (!this.tileEntity.isBanMode()) {
            if (this.buttonList.get(1) instanceof GuiButton) {
                ((GuiButton) this.buttonList.get(1)).displayString = "Allowed";
            }
        } else if (this.buttonList.get(1) instanceof GuiButton) {
            ((GuiButton) this.buttonList.get(1)).displayString = "Banned";
        }
        this.drawTextWithTooltip("frequency", "%1:", 8, 93, x, y);
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
    drawGuiContainerBackgroundLayer(final float var1, final int x, final int y) {
        super.drawGuiContainerBackgroundLayer(var1, x, y);
        for (int var2 = 0; var2 < 2; ++var2) {
            for (int var3 = 0; var3 < 4; ++var3) {
                this.drawSlot(98 + var3 * 18, 30 + var2 * 18);
            }
        }
        for (int var4 = 0; var4 < 9; ++var4) {
            if (this.tileEntity.isBanMode()) {
                this.drawSlot(8 + var4 * 18, 68, SlotType.NONE, 1.0f, 0.8f, 0.8f);
            } else {
                this.drawSlot(8 + var4 * 18, 68, SlotType.NONE, 0.8f, 1.0f, 0.8f);
            }
        }
        this.drawSlot(68, 88);
        this.drawSlot(86, 88);
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
