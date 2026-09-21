package com.surubedai.extrememetals.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.surubedai.extrememetals.ExtremeMetalsMain;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ScreenSteamRollingMillDrawing extends AbstractContainerScreen<ScreenSteamRollingMill> {
    // 💡 画面の背景に使用するテクスチャ画像のパス（後ほどバニラのかまどの画像を流用するか、自作画像を置きます）
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ExtremeMetalsMain.MODID, "textures/gui/primitive_reduction_furnace.png");

    public ScreenSteamRollingMillDrawing(ScreenSteamRollingMill menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2; // タイル文字を中央寄せ
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 79, y + 35, 176, 1, menu.getScaledProgress(), 17);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics GuiGraphics, int MouseX, int MouseY) {
        super.renderLabels(GuiGraphics, MouseX, MouseY);

        int steamAmount = menu.getSteamAmount();
        GuiGraphics.drawString(this.font, "Steam: " + steamAmount + " mB", 8, 60, 0x404040, false);
    }
}
