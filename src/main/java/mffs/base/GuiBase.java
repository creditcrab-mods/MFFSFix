package mffs.base;

import java.util.HashMap;
import java.util.Map;

import icbm.api.IBlockFrequency;
import mffs.MFFSHelper;
import mffs.ModularForceFieldSystem;
import mffs.api.IBiometricIdentifierLink;
import mffs.gui.button.GuiIcon;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.vector.Vector2;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.TranslationHelper;
import universalelectricity.prefab.vector.Region2;

public class GuiBase extends GuiContainer {
    public static final int METER_HEIGHT = 49;
    public static final int METER_WIDTH = 14;
    public static final int METER_END = 68;
    protected GuiTextField textFieldFrequency;
    protected Vector2 textFieldPos;
    public String tooltip;
    protected int containerWidth;
    protected int containerHeight;
    protected IBlockFrequency frequencyTile;
    protected HashMap<Region2, String> tooltips;

    public GuiBase(final Container container) {
        super(container);
        this.textFieldPos = new Vector2();
        this.tooltip = "";
        this.tooltips = new HashMap<>();
        this.ySize = 217;
    }

    public GuiBase(final Container container, final IBlockFrequency frequencyTile) {
        this(container);
        this.frequencyTile = frequencyTile;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(new GuiIcon(
            0,
            this.width / 2 - 82,
            this.height / 2 - 104,
            new ItemStack(Blocks.torch),
            new ItemStack(Blocks.redstone_torch)
        ));
        Keyboard.enableRepeatEvents(true);
        if (this.frequencyTile != null) {
            (this.textFieldFrequency = new GuiTextField(
                 this.fontRendererObj,
                 this.textFieldPos.intX(),
                 this.textFieldPos.intY(),
                 50,
                 12
             ))
                .setMaxStringLength(6);
            this.textFieldFrequency.setText(this.frequencyTile.getFrequency() + "");
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }

    @Override
    protected void keyTyped(final char par1, final int par2) {
        super.keyTyped(par1, par2);
        if (this.textFieldFrequency != null) {
            this.textFieldFrequency.textboxKeyTyped(par1, par2);
            try {
                final int newFrequency
                    = Math.max(0, Integer.parseInt(this.textFieldFrequency.getText()));
                this.frequencyTile.setFrequency(newFrequency);
                this.textFieldFrequency.setText(this.frequencyTile.getFrequency() + "");
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setInteger("frequency", this.frequencyTile.getFrequency());
                ModularForceFieldSystem.channel.sendToServer(new PacketTile(
                    PacketTile.Type.FREQUENCY,
                    new Vector3((TileEntity) this.frequencyTile),
                    nbt
                ));
            } catch (final NumberFormatException ex) {}
        }
    }

    @Override
    protected void actionPerformed(final GuiButton guiButton) {
        super.actionPerformed(guiButton);
        if (this.frequencyTile != null && guiButton.id == 0) {
            ModularForceFieldSystem.channel.sendToServer(new PacketTile(
                PacketTile.Type.TOGGLE_ACTIVATION,
                new Vector3((TileEntity) this.frequencyTile),
                new NBTTagCompound()
            ));
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (this.textFieldFrequency != null && !this.textFieldFrequency.isFocused()) {
            this.textFieldFrequency.setText(this.frequencyTile.getFrequency() + "");
        }
        if (this.frequencyTile instanceof TileEntityBase && this.buttonList.size() > 0
            && this.buttonList.get(0) != null) {
            ((GuiIcon) this.buttonList.get(0))
                .setIndex(((TileEntityBase) this.frequencyTile).isActive() ? 1 : 0);
        }
    }

    @Override
    protected void mouseClicked(final int x, final int y, final int par3) {
        super.mouseClicked(x, y, par3);
        if (this.textFieldFrequency != null) {
            this.textFieldFrequency.mouseClicked(
                x - this.containerWidth, y - this.containerHeight, par3
            );
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        if (this.textFieldFrequency != null
            && this.func_146978_c(
                this.textFieldPos.intX(),
                this.textFieldPos.intY(),
                this.textFieldFrequency.getWidth(),
                12,
                mouseX,
                mouseY
            )) {
            this.tooltip = TranslationHelper.getLocal("gui.frequency.tooltip");
        }
        for (final Map.Entry<Region2, String> entry : this.tooltips.entrySet()) {
            if (entry.getKey().isIn(
                    new Vector2(mouseX - this.guiLeft, mouseY - this.guiTop)
                )) {
                this.tooltip = entry.getValue();
                break;
            }
        }
        if (this.tooltip != null && this.tooltip != "") {
            this.drawTooltip(
                mouseX - this.guiLeft,
                mouseY - this.guiTop,
                MFFSHelper.splitStringPerWord(this.tooltip, 5).toArray(new String[] {})
            );
        }
        this.tooltip = "";
    }

    @Override
    protected void
    drawGuiContainerBackgroundLayer(final float var1, final int x, final int y) {
        this.containerWidth = (this.width - this.xSize) / 2;
        this.containerHeight = (this.height - this.ySize) / 2;
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("mffs", "textures/gui/gui_base.png")
        );
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(
            this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize
        );
        if (this.frequencyTile instanceof IBiometricIdentifierLink) {
            this.drawBulb(
                167,
                4,
                ((IBiometricIdentifierLink) this.frequencyTile).getBiometricIdentifier()
                    != null
            );
        }
    }

    protected void drawBulb(final int x, final int y, final boolean isOn) {
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("mffs", "textures/gui/gui_components.png")
        );
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (isOn) {
            this.drawTexturedModalRect(
                this.containerWidth + x, this.containerHeight + y, 161, 0, 6, 6
            );
        } else {
            this.drawTexturedModalRect(
                this.containerWidth + x, this.containerHeight + y, 161, 4, 6, 6
            );
        }
    }

    protected void drawSlot(final int x, final int y, final ItemStack itemStack) {
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("mffs", "textures/gui/gui_components.png")
        );
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(
            this.containerWidth + x, this.containerHeight + y, 0, 0, 18, 18
        );
        this.drawItemStack(itemStack, this.containerWidth + x, this.containerHeight + y);
    }

    protected void drawItemStack(final ItemStack itemStack, int x, int y) {
        ++x;
        ++y;
        GL11.glTranslatef(0.0f, 0.0f, 32.0f);
        GuiBase.itemRender.renderItemAndEffectIntoGUI(
            this.fontRendererObj, this.mc.renderEngine, itemStack, x, y
        );
    }

    protected void drawTextWithTooltip(
        final String textName,
        final String format,
        final int x,
        final int y,
        final int mouseX,
        final int mouseY
    ) {
        this.drawTextWithTooltip(textName, format, x, y, mouseX, mouseY, 4210752);
    }

    protected void drawTextWithTooltip(
        final String textName,
        final String format,
        final int x,
        final int y,
        final int mouseX,
        final int mouseY,
        final int color
    ) {
        final String name = TranslationHelper.getLocal("gui." + textName + ".name");
        final String text = format.replaceAll("%1", name);
        this.fontRendererObj.drawString(text, x, y, color);
        final String tooltip = TranslationHelper.getLocal("gui." + textName + ".tooltip");
        if (tooltip != null && tooltip != ""
            && this.func_146978_c(
                x, y, (int) (text.length() * 4.8), 12, mouseX, mouseY
            )) {
            this.tooltip = tooltip;
        }
    }

    protected void drawTextWithTooltip(
        final String textName,
        final int x,
        final int y,
        final int mouseX,
        final int mouseY
    ) {
        this.drawTextWithTooltip(textName, "%1", x, y, mouseX, mouseY);
    }

    protected void drawSlot(
        final int x,
        final int y,
        final SlotType type,
        final float r,
        final float g,
        final float b
    ) {
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("mffs", "textures/gui/gui_components.png")
        );
        GL11.glColor4f(r, g, b, 1.0f);
        this.drawTexturedModalRect(
            this.containerWidth + x, this.containerHeight + y, 0, 0, 18, 18
        );
        if (type != SlotType.NONE) {
            this.drawTexturedModalRect(
                this.containerWidth + x,
                this.containerHeight + y,
                0,
                18 * type.ordinal(),
                18,
                18
            );
        }
    }

    protected void drawSlot(final int x, final int y, final SlotType type) {
        this.drawSlot(x, y, type, 1.0f, 1.0f, 1.0f);
    }

    protected void drawSlot(final int x, final int y) {
        this.drawSlot(x, y, SlotType.NONE);
    }

    protected void drawBar(final int x, final int y, final float scale) {
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("mffs", "textures/gui/gui_components.png")
        );
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(
            this.containerWidth + x, this.containerHeight + y, 18, 0, 22, 15
        );
        if (scale > 0.0f) {
            this.drawTexturedModalRect(
                this.containerWidth + x,
                this.containerHeight + y,
                18,
                15,
                22 - (int) (scale * 22.0f),
                15
            );
        }
    }

    protected void drawForce(final int x, final int y, final float scale) {
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("mffs", "textures/gui/gui_components.png")
        );
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(
            this.containerWidth + x, this.containerHeight + y, 54, 0, 107, 11
        );
        if (scale > 0.0f) {
            this.drawTexturedModalRect(
                this.containerWidth + x,
                this.containerHeight + y,
                54,
                11,
                (int) (scale * 107.0f),
                11
            );
        }
    }

    protected void drawElectricity(final int x, final int y, final float scale) {
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("mffs", "textures/gui/gui_components.png")
        );
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(
            this.containerWidth + x, this.containerHeight + y, 54, 0, 107, 11
        );
        if (scale > 0.0f) {
            this.drawTexturedModalRect(
                this.containerWidth + x,
                this.containerHeight + y,
                54,
                22,
                (int) (scale * 107.0f),
                11
            );
        }
    }

    public void drawTooltip(final int x, final int y, final String... toolTips) {
        if (!GuiScreen.isShiftKeyDown()) {
            GL11.glDisable(32826);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            if (toolTips != null) {
                int var5 = 0;
                for (int var6 = 0; var6 < toolTips.length; ++var6) {
                    final int var7 = this.fontRendererObj.getStringWidth(toolTips[var6]);
                    if (var7 > var5) {
                        var5 = var7;
                    }
                }
                int var6 = x + 12;
                int var7 = y - 12;
                int var8 = 8;
                if (toolTips.length > 1) {
                    var8 += 2 + (toolTips.length - 1) * 10;
                }
                if (this.guiTop + var7 + var8 + 6 > this.height) {
                    var7 = this.height - var8 - this.guiTop - 6;
                }
                zLevel = 300.0f;
                final int var9 = -267386864;
                this.drawGradientRect(
                    var6 - 3, var7 - 4, var6 + var5 + 3, var7 - 3, var9, var9
                );
                this.drawGradientRect(
                    var6 - 3,
                    var7 + var8 + 3,
                    var6 + var5 + 3,
                    var7 + var8 + 4,
                    var9,
                    var9
                );
                this.drawGradientRect(
                    var6 - 3, var7 - 3, var6 + var5 + 3, var7 + var8 + 3, var9, var9
                );
                this.drawGradientRect(
                    var6 - 4, var7 - 3, var6 - 3, var7 + var8 + 3, var9, var9
                );
                this.drawGradientRect(
                    var6 + var5 + 3,
                    var7 - 3,
                    var6 + var5 + 4,
                    var7 + var8 + 3,
                    var9,
                    var9
                );
                final int var10 = 1347420415;
                final int var11 = (var10 & 0xFEFEFE) >> 1 | (var10 & 0xFF000000);
                this.drawGradientRect(
                    var6 - 3,
                    var7 - 3 + 1,
                    var6 - 3 + 1,
                    var7 + var8 + 3 - 1,
                    var10,
                    var11
                );
                this.drawGradientRect(
                    var6 + var5 + 2,
                    var7 - 3 + 1,
                    var6 + var5 + 3,
                    var7 + var8 + 3 - 1,
                    var10,
                    var11
                );
                this.drawGradientRect(
                    var6 - 3, var7 - 3, var6 + var5 + 3, var7 - 3 + 1, var10, var10
                );
                this.drawGradientRect(
                    var6 - 3,
                    var7 + var8 + 2,
                    var6 + var5 + 3,
                    var7 + var8 + 3,
                    var11,
                    var11
                );
                for (int var12 = 0; var12 < toolTips.length; ++var12) {
                    final String var13 = toolTips[var12];
                    this.fontRendererObj.drawStringWithShadow(var13, var6, var7, -1);
                    var7 += 10;
                }
                this.zLevel = 0.0f;
                GL11.glEnable(2929);
                GL11.glEnable(2896);
                RenderHelper.enableGUIStandardItemLighting();
                GL11.glEnable(32826);
            }
        }
    }

    public enum SlotType {
        NONE,
        BATTERY,
        LIQUID,
        ARR_UP,
        ARR_DOWN,
        ARR_LEFT,
        ARR_RIGHT,
        ARR_UP_RIGHT,
        ARR_UP_LEFT,
        ARR_DOWN_LEFT,
        ARR_DOWN_RIGHT;
    }
}
