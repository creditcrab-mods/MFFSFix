package calclavia.lib.gui;

import java.util.HashMap;
import java.util.Map.Entry;

import calclavia.lib.Calclavia;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.vector.Vector2;
import universalelectricity.prefab.GuiBase;
import universalelectricity.prefab.TranslationHelper;
import universalelectricity.prefab.vector.Region2;

public class GuiScreenBase extends GuiBase {
    // private static final int METER_X = 54;
    public static final int METER_HEIGHT = 49;
    public static final int METER_WIDTH = 14;
    public static final int METER_END = 68;
    public String tooltip = "";
    protected HashMap<Region2, String> tooltips = new HashMap<>();
    protected int containerWidth;
    protected int containerHeight;

    public GuiScreenBase() {
        super.ySize = 217;
    }

    @Override
    protected void drawForegroundLayer(int mouseX, int mouseY, float var1) {
        for (Entry<Region2, String> entry : this.tooltips.entrySet()) {
            if (((Region2) entry.getKey())
                    .isIn(new Vector2(
                        (double) (mouseX - this.guiLeft), (double) (mouseY - this.guiTop)
                    ))) {
                this.tooltip = (String) entry.getValue();
                break;
            }
        }

        if (this.tooltip != null && this.tooltip != "") {
            this.drawTooltip(
                mouseX - this.guiLeft,
                mouseY - this.guiTop,
                (String[]) Calclavia.splitStringPerWord(this.tooltip, 5)
                    .toArray(new String[0])
            );
        }

        this.tooltip = "";
    }

    @Override
    protected void drawBackgroundLayer(int x, int y, float var1) {
        this.containerWidth = (this.width - this.xSize) / 2;
        this.containerHeight = (this.height - this.ySize) / 2;
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("calclavia", "textures/gui/gui_base.png")
        );
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(
            this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize
        );
    }

    protected void drawBulb(int x, int y, boolean isOn) {
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("calclavia", "textures/gui/gui_base.png")
        );
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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

    protected void drawTextWithTooltip(
        String textName, String format, int x, int y, int mouseX, int mouseY
    ) {
        this.drawTextWithTooltip(textName, format, x, y, mouseX, mouseY, 4210752);
    }

    protected void drawTextWithTooltip(
        String textName, String format, int x, int y, int mouseX, int mouseY, int color
    ) {
        String name = TranslationHelper.getLocal("gui." + textName + ".name");
        String text = format.replaceAll("%1", name);
        this.fontRendererObj.drawString(text, x, y, color);
        String tooltip = TranslationHelper.getLocal("gui." + textName + ".tooltip");
        if (tooltip != null && tooltip != ""
            && this.isPointInRegion(
                x, y, (int) ((double) text.length() * 4.8D), 12, mouseX, mouseY
            )) {
            this.tooltip = tooltip;
        }
    }

    protected boolean
    isPointInRegion(int par1, int par2, int par3, int par4, int par5, int par6) {
        int k1 = super.guiLeft;
        int l1 = super.guiTop;
        par5 -= k1;
        par6 -= l1;
        return par5 >= par1 - 1 && par5 < par1 + par3 + 1 && par6 >= par2 - 1
            && par6 < par2 + par4 + 1;
    }

    protected void
    drawTextWithTooltip(String textName, int x, int y, int mouseX, int mouseY) {
        this.drawTextWithTooltip(textName, "%1", x, y, mouseX, mouseY);
    }

    protected void drawSlot(int x, int y, GuiSlotType type, float r, float g, float b) {
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("calclavia", "textures/gui/gui_empty.png")
        );
        GL11.glColor4f(r, g, b, 1.0F);
        this.drawTexturedModalRect(
            this.containerWidth + x, this.containerHeight + y, 0, 0, 18, 18
        );
        if (type != GuiSlotType.NONE) {
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

    protected void drawSlot(int x, int y, GuiSlotType type) {
        this.drawSlot(x, y, type, 1.0F, 1.0F, 1.0F);
    }

    protected void drawSlot(int x, int y) {
        this.drawSlot(x, y, GuiSlotType.NONE);
    }

    protected void drawBar(int x, int y, float scale) {
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("calclavia", "textures/gui/gui_empty.png")
        );
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(
            this.containerWidth + x, this.containerHeight + y, 18, 0, 22, 15
        );
        if (scale > 0.0F) {
            this.drawTexturedModalRect(
                this.containerWidth + x,
                this.containerHeight + y,
                18,
                15,
                22 - (int) (scale * 22.0F),
                15
            );
        }
    }

    protected void drawForce(int x, int y, float scale) {
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("calclavia", "textures/gui/gui_empty.png")
        );
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(
            this.containerWidth + x, this.containerHeight + y, 54, 0, 107, 11
        );
        if (scale > 0.0F) {
            this.drawTexturedModalRect(
                this.containerWidth + x,
                this.containerHeight + y,
                54,
                11,
                (int) (scale * 107.0F),
                11
            );
        }
    }

    protected void drawElectricity(int x, int y, float scale) {
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("calclavia", "textures/gui/gui_empty.png")
        );
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(
            this.containerWidth + x, this.containerHeight + y, 54, 0, 107, 11
        );
        if (scale > 0.0F) {
            this.drawTexturedModalRect(
                this.containerWidth + x,
                this.containerHeight + y,
                54,
                22,
                (int) (scale * 107.0F),
                11
            );
        }
    }

    protected void drawMeter(int x, int y, float scale, FluidStack liquidStack) {
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("calclavia", "textures/gui/gui_empty.png")
        );
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(
            this.containerWidth + x, this.containerHeight + y, 40, 0, 14, 49
        );
        this.displayGauge(
            this.containerWidth + x,
            this.containerHeight + y,
            0,
            0,
            (int) (48.0F * scale),
            liquidStack
        );
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("calclavia", "textures/gui/gui_empty.png")
        );
        this.drawTexturedModalRect(
            this.containerWidth + x, this.containerHeight + y, 40, 98, 14, 49
        );
    }

    public void drawTooltip(int x, int y, String... toolTips) {
        if (!GuiScreen.isShiftKeyDown()) {
            GL11.glDisable('\u803a');
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            if (toolTips != null) {
                int var5 = 0;

                int var6;
                int var7;
                for (var6 = 0; var6 < toolTips.length; ++var6) {
                    var7 = this.fontRendererObj.getStringWidth(toolTips[var6]);
                    if (var7 > var5) {
                        var5 = var7;
                    }
                }

                var6 = x + 12;
                var7 = y - 12;
                int var9 = 8;
                if (toolTips.length > 1) {
                    var9 += 2 + (toolTips.length - 1) * 10;
                }

                if (super.guiTop + var7 + var9 + 6 > this.height) {
                    var7 = this.height - var9 - super.guiTop - 6;
                }

                super.zLevel = 300.0F;
                int var10 = -267386864;
                this.drawGradientRect(
                    var6 - 3, var7 - 4, var6 + var5 + 3, var7 - 3, var10, var10
                );
                this.drawGradientRect(
                    var6 - 3,
                    var7 + var9 + 3,
                    var6 + var5 + 3,
                    var7 + var9 + 4,
                    var10,
                    var10
                );
                this.drawGradientRect(
                    var6 - 3, var7 - 3, var6 + var5 + 3, var7 + var9 + 3, var10, var10
                );
                this.drawGradientRect(
                    var6 - 4, var7 - 3, var6 - 3, var7 + var9 + 3, var10, var10
                );
                this.drawGradientRect(
                    var6 + var5 + 3,
                    var7 - 3,
                    var6 + var5 + 4,
                    var7 + var9 + 3,
                    var10,
                    var10
                );
                int var11 = 1347420415;
                int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
                this.drawGradientRect(
                    var6 - 3,
                    var7 - 3 + 1,
                    var6 - 3 + 1,
                    var7 + var9 + 3 - 1,
                    var11,
                    var12
                );
                this.drawGradientRect(
                    var6 + var5 + 2,
                    var7 - 3 + 1,
                    var6 + var5 + 3,
                    var7 + var9 + 3 - 1,
                    var11,
                    var12
                );
                this.drawGradientRect(
                    var6 - 3, var7 - 3, var6 + var5 + 3, var7 - 3 + 1, var11, var11
                );
                this.drawGradientRect(
                    var6 - 3,
                    var7 + var9 + 2,
                    var6 + var5 + 3,
                    var7 + var9 + 3,
                    var12,
                    var12
                );

                for (int var13 = 0; var13 < toolTips.length; ++var13) {
                    String var14 = toolTips[var13];
                    this.fontRendererObj.drawStringWithShadow(var14, var6, var7, -1);
                    var7 += 10;
                }

                super.zLevel = 0.0F;
                GL11.glEnable(2929);
                GL11.glEnable(2896);
                RenderHelper.enableGUIStandardItemLighting();
                GL11.glEnable('\u803a');
            }
        }
    }

    protected void
    displayGauge(int j, int k, int line, int col, int squaled, FluidStack liquid) {
        if (liquid != null) {
            int start = 0;
            IIcon liquidIcon = liquid.getFluid().getIcon();

            int x1;
            do {
                if (squaled > 16) {
                    x1 = 16;
                    squaled -= 16;
                } else {
                    x1 = squaled;
                    squaled = 0;
                }

                this.drawTexturedModelRectFromIcon(
                    j + col, k + line + 58 - x1 - start, liquidIcon, 16, 16 - (16 - x1)
                );
                start += 16;
            } while (x1 != 0 && squaled != 0);
        }
    }
}
