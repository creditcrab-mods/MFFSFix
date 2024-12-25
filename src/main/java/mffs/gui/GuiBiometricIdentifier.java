package mffs.gui;

import mffs.ModularForceFieldSystem;
import mffs.api.card.ICardIdentification;
import mffs.api.security.Permission;
import mffs.base.GuiBase;
import mffs.base.PacketTile;
import mffs.container.ContainerBiometricIdentifier;
import mffs.gui.button.GuiButtonPress;
import mffs.tileentity.TileEntityBiometricIdentifier;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import universalelectricity.core.vector.Vector2;
import universalelectricity.core.vector.Vector3;

public class GuiBiometricIdentifier extends GuiBase {
    private TileEntityBiometricIdentifier tileEntity;
    private GuiTextField textFieldUsername;

    public GuiBiometricIdentifier(
        final EntityPlayer player, final TileEntityBiometricIdentifier tileEntity
    ) {
        super(new ContainerBiometricIdentifier(player, tileEntity), tileEntity);
        this.tileEntity = tileEntity;
        tileEntity.canUpdate();
    }

    @Override
    public void initGui() {
        super.textFieldPos = new Vector2(109.0, 92.0);
        super.initGui();
        (this.textFieldUsername = new GuiTextField(this.fontRendererObj, 52, 18, 90, 12))
            .setMaxStringLength(30);
        int x = 0;
        int y = 0;
        for (int i = 0; i < Permission.getPermissions().length; ++i) {
            ++x;
            this.buttonList.add(new GuiButtonPress(
                i + 1,
                this.width / 2 - 50 + 20 * x,
                this.height / 2 - 75 + 20 * y,
                new Vector2(18.0, 18 * i),
                this,
                Permission.getPermissions()[i].name
            ));
            if (i % 3 == 0 && i != 0) {
                x = 0;
                ++y;
            }
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
        this.drawTextWithTooltip("rights", "%1", 8, 32, x, y, 0);
        try {
            if (this.tileEntity.getManipulatingCard() != null) {
                final ICardIdentification idCard
                    = (ICardIdentification) this.tileEntity.getManipulatingCard().getItem(
                    );
                this.textFieldUsername.drawTextBox();
                if (idCard.getUsername(this.tileEntity.getManipulatingCard()) != null) {
                    for (int i = 0; i < this.buttonList.size(); ++i) {
                        if (this.buttonList.get(i) instanceof GuiButtonPress) {
                            final GuiButtonPress button
                                = (GuiButtonPress) this.buttonList.get(i);
                            button.visible = true;
                            final int permissionID = i - 1;
                            if (Permission.getPermission(permissionID) != null) {
                                if (idCard.hasPermission(
                                        this.tileEntity.getManipulatingCard(),
                                        Permission.getPermission(permissionID)
                                    )) {
                                    button.stuck = true;
                                } else {
                                    button.stuck = false;
                                }
                            }
                        }
                    }
                }
            } else {
                for (final Object button2 : this.buttonList) {
                    if (button2 instanceof GuiButtonPress) {
                        ((GuiButtonPress) button2).visible = false;
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        super.textFieldFrequency.drawTextBox();
        this.drawTextWithTooltip(
            "master", 28, 90 + this.fontRendererObj.FONT_HEIGHT / 2, x, y
        );
        super.drawGuiContainerForegroundLayer(x, y);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!this.textFieldUsername.isFocused()
            && this.tileEntity.getManipulatingCard() != null) {
            final ICardIdentification idCard
                = (ICardIdentification) this.tileEntity.getManipulatingCard().getItem();
            if (idCard.getUsername(this.tileEntity.getManipulatingCard()) != null) {
                this.textFieldUsername.setText(
                    idCard.getUsername(this.tileEntity.getManipulatingCard())
                );
            }
        }
    }

    @Override
    protected void
    drawGuiContainerBackgroundLayer(final float f, final int x, final int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.drawSlot(87, 90);
        this.drawSlot(7, 45);
        this.drawSlot(7, 65);
        this.drawSlot(7, 90);
        for (int var4 = 0; var4 < 9; ++var4) {
            this.drawSlot(8 + var4 * 18 - 1, 110);
        }
    }

    @Override
    protected void keyTyped(final char par1, final int par2) {
        if (par1 != 'e' && par1 != 'E') {
            super.keyTyped(par1, par2);
        }
        this.textFieldUsername.textboxKeyTyped(par1, par2);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("username", this.textFieldUsername.getText());
        ModularForceFieldSystem.channel.sendToServer(
            new PacketTile(PacketTile.Type.STRING, new Vector3(this.tileEntity), nbt)
        );
    }

    @Override
    protected void mouseClicked(final int x, final int y, final int par3) {
        super.mouseClicked(x, y, par3);
        this.textFieldUsername.mouseClicked(
            x - super.containerWidth, y - super.containerHeight, par3
        );
    }

    @Override
    protected void actionPerformed(final GuiButton guiButton) {
        super.actionPerformed(guiButton);
        if (guiButton.id > 0) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("buttonId", guiButton.id - 1);
            ModularForceFieldSystem.channel.sendToServer(new PacketTile(
                PacketTile.Type.TOGGLE_MODE, new Vector3(this.tileEntity), nbt
            ));
        }
    }
}
