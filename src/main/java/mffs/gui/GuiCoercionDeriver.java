package mffs.gui;

import mffs.ModularForceFieldSystem;
import mffs.base.GuiBase;
import mffs.base.PacketTile;
import mffs.container.ContainerCoercionDeriver;
import mffs.tileentity.TileEntityCoercionDeriver;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.vector.Vector2;
import universalelectricity.core.vector.Vector3;

public class GuiCoercionDeriver extends GuiBase {
    private TileEntityCoercionDeriver tileEntity;

    public GuiCoercionDeriver(
        final EntityPlayer player, final TileEntityCoercionDeriver tileentity
    ) {
        super(new ContainerCoercionDeriver(player, tileentity), tileentity);
        this.tileEntity = tileentity;
    }

    @Override
    public void initGui() {
        super.textFieldPos = new Vector2(30.0, 43.0);
        super.initGui();
        this.buttonList.add(
            new GuiButton(1, this.width / 2 - 10, this.height / 2 - 28, 58, 20, "Derive")
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
        this.drawTextWithTooltip("frequency", "%1:", 8, 30, x, y);
        super.textFieldFrequency.drawTextBox();
        GL11.glPushMatrix();
        GL11.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
        this.drawTextWithTooltip("upgrade", -95, 140, x, y);
        GL11.glPopMatrix();
        if (this.buttonList.get(1) instanceof GuiButton) {
            if (!this.tileEntity.isInversed) {
                ((GuiButton) this.buttonList.get(1)).displayString = "Derive";
            } else {
                ((GuiButton) this.buttonList.get(1)).displayString = "Integrate";
            }
        }
        this.fontRendererObj.drawString(
            1000.0 * UniversalElectricity.UE_RF_RATIO + " RF/s", 85, 30, 4210752
        );
        this.fontRendererObj.drawString(
            1000.0 * UniversalElectricity.UE_IC2_RATIO + " EU/s", 85, 40, 4210752
        );
        this.fontRendererObj.drawString(
            UnitDisplay.getDisplayShort(1000.0, UnitDisplay.Unit.WATT), 85, 50, 4210752
        );
        this.fontRendererObj.drawString(
            UnitDisplay.getDisplayShort(
                this.tileEntity.getVoltage(), UnitDisplay.Unit.VOLTAGE
            ),
            85,
            60,
            4210752
        );
        this.drawTextWithTooltip(
            "progress",
            "%1: " + (this.tileEntity.isActive() ? "Running" : "Idle"),
            8,
            70,
            x,
            y
        );
        this.drawTextWithTooltip(
            "fortron",
            "%1: "
                + UnitDisplay.getDisplayShort(
                    this.tileEntity.getFortronEnergy()
                        * TileEntityCoercionDeriver.FORTRON_UE_RATIO,
                    UnitDisplay.Unit.JOULES
                ),
            8,
            105,
            x,
            y
        );
        this.fontRendererObj.drawString(
            "§2+"
                + UnitDisplay.getDisplayShort(
                    this.tileEntity.getProductionRate() * 20, UnitDisplay.Unit.JOULES
                ),
            120,
            117,
            4210752
        );
        super.drawGuiContainerForegroundLayer(x, y);
    }

    @Override
    protected void
    drawGuiContainerBackgroundLayer(final float f, final int x, final int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.drawSlot(153, 46);
        this.drawSlot(153, 66);
        this.drawSlot(153, 86);
        this.drawSlot(8, 40);
        this.drawSlot(8, 82, SlotType.BATTERY);
        this.drawSlot(28, 82);
        this.drawBar(50, 84, 1.0f);
        this.drawForce(
            8,
            115,
            this.tileEntity.getFortronEnergy()
                / (float) this.tileEntity.getFortronCapacity()
        );
    }

    @Override
    protected void actionPerformed(final GuiButton guibutton) {
        super.actionPerformed(guibutton);
        if (guibutton.id == 1) {
            ModularForceFieldSystem.channel.sendToServer(new PacketTile(
                PacketTile.Type.TOGGLE_MODE,
                new Vector3(this.tileEntity),
                new NBTTagCompound()
            ));
        }
    }
}
