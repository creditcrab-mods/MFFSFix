package mffs.gui;

import mffs.ModularForceFieldSystem;
import mffs.base.GuiBase;
import mffs.base.PacketTile;
import mffs.container.ContainerFortronCapacitor;
import mffs.gui.button.GuiButtonPressTransferMode;
import mffs.tileentity.TileEntityCoercionDeriver;
import mffs.tileentity.TileEntityFortronCapacitor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.core.vector.Vector2;
import universalelectricity.core.vector.Vector3;

public class GuiFortronCapacitor extends GuiBase {
    private TileEntityFortronCapacitor tileEntity;

    public GuiFortronCapacitor(
        final EntityPlayer player, final TileEntityFortronCapacitor tileentity
    ) {
        super(new ContainerFortronCapacitor(player, tileentity), tileentity);
        this.tileEntity = tileentity;
    }

    @Override
    public void initGui() {
        super.textFieldPos = new Vector2(50.0, 76.0);
        super.initGui();
        this.buttonList.add(new GuiButtonPressTransferMode(
            1, this.width / 2 + 15, this.height / 2 - 37, this, this.tileEntity
        ));
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
        this.drawTextWithTooltip("upgrade", -95, 140, x, y);
        GL11.glPopMatrix();
        this.drawTextWithTooltip(
            "linkedDevice",
            "%1: " + this.tileEntity.getLinkedDevices().size(),
            8,
            28,
            x,
            y
        );
        this.drawTextWithTooltip(
            "transmissionRate",
            "%1: "
                + UnitDisplay.getDisplayShort(
                    this.tileEntity.getTransmissionRate(), UnitDisplay.Unit.JOULES
                ),
            8,
            40,
            x,
            y
        );
        this.drawTextWithTooltip(
            "range", "%1: " + this.tileEntity.getTransmissionRange(), 8, 52, x, y
        );
        this.drawTextWithTooltip("frequency", "%1:", 8, 63, x, y);
        super.textFieldFrequency.drawTextBox();
        this.drawTextWithTooltip("fortron", "%1:", 8, 95, x, y);
        this.fontRendererObj.drawString(
            UnitDisplay.getDisplayShort(
                this.tileEntity.getFortronEnergy()
                    * TileEntityCoercionDeriver.FORTRON_UE_RATIO,
                UnitDisplay.Unit.JOULES
            ) + "/"
                + UnitDisplay.getDisplayShort(
                    this.tileEntity.getFortronCapacity()
                        * TileEntityCoercionDeriver.FORTRON_UE_RATIO,
                    UnitDisplay.Unit.JOULES
                ),
            8,
            105,
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
        this.drawSlot(8, 73);
        this.drawSlot(26, 73);
        this.drawForce(
            8,
            115,
            Math.min(
                this.tileEntity.getFortronEnergy()
                    / (float) this.tileEntity.getFortronCapacity(),
                1.0f
            )
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
