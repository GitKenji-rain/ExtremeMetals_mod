package com.surubedai.extrememetals.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.surubedai.extrememetals.ExtremeMetalsMain;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ScreenPrimitiveReductionFurnaceDrawing extends AbstractContainerScreen<ScreenPrimitiveReductionFurnace> {
    // 💡 画面の背景に使用するテクスチャ画像のパス（後ほどバニラのかまどの画像を流用するか、自作画像を置きます）
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ExtremeMetalsMain.MODID, "textures/gui/primitive_reduction_furnace.png");

    public ScreenPrimitiveReductionFurnaceDrawing(ScreenPrimitiveReductionFurnace menu, Inventory playerInventory, Component title) {
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

        // 背景画像の描画（全体）
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // 💡 加工の進捗矢印のアニメーション描画
        if(menu.isCrafting()) {
            // menu.getScaledProgress() で計算されたピクセル幅分だけ、矢印の赤い(または染まった)部分を重ねて描画する
            // 引数：(画像, 画面X, 画面Y, 画像内X, 画像内Y, 幅, 高さ)
            guiGraphics.blit(TEXTURE, x + 79, y + 35, 176, 1, menu.getScaledProgress(), 17);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics); // 背景の暗転効果
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY); // アイテムのツールチップ表示
    }
}
