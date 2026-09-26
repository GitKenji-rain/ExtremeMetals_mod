package com.surubedai.extrememetals.screen;

;
import com.mojang.blaze3d.systems.RenderSystem;
import com.surubedai.extrememetals.ExtremeMetalsMain;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ScreenPrimitiveSteamBoilerDrawing extends AbstractContainerScreen<ScreenPrimitiveSteamBoiler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ExtremeMetalsMain.MODID, "textures/gui/primitive_steam_boiler.png");

    public ScreenPrimitiveSteamBoilerDrawing(ScreenPrimitiveSteamBoiler pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // 背景テクスチャの描画
        pGuiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // 燃焼ゲージ（炎）の描画例
        if (menu.getBurnTime() > 0) {
            int k = (int) Math.ceil((double) menu.getBurnTime() / menu.getMaxBurnTime() * 13);
            pGuiGraphics.blit(TEXTURE, x + 119, y + 38 + 14 - k, 176, 14 - k, 14, k);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);

        int waterAmount = menu.getWaterAmount();
        int steamAmount = menu.getSteamAmount();
        pGuiGraphics.drawString(this.font, "Steam: " + steamAmount + " mB", 8, 60, 0x404040, false);
        pGuiGraphics.drawString(this.font, "Water: " + waterAmount + " mB", 8, 40, 0x404040, false);
    }


    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

}
