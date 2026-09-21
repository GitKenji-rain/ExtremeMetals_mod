package com.surubedai.extrememetals.screen;

import com.surubedai.extrememetals.blockentity.BlockentityPrimitiveReductionFurnace;
import com.surubedai.extrememetals.regi.ExtremeMetalsAddScreens;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class ScreenPrimitiveReductionFurnace extends AbstractContainerMenu {
    public final BlockentityPrimitiveReductionFurnace blockEntity;
    private final Level level;
    private final ContainerData data; // 進捗バーの同期用データ

    // クライアント側（画面を開く側）が呼び出すコンストラクタ
    public ScreenPrimitiveReductionFurnace(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    // サーバー側（内部処理側）が呼び出す共通コンストラクタ
    public ScreenPrimitiveReductionFurnace(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ExtremeMetalsAddScreens.PRIMITIVE_REDUCTION_MENU.get(), containerId);
        checkContainerDataCount(data, 2);
        this.blockEntity = ((BlockentityPrimitiveReductionFurnace) entity);
        this.level = inv.player.level();
        this.data = data;

        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), 0, 56, 35));
        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), 1, 116, 35));
        // プレイヤーのインベントリ（27マス）とホットバー（9マス）のスロットを自動配置
        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        // データの同期（タイマーなど）
        addDataSlots(data);
    }

    // 加工中か判定（Screen側で矢印を動かすのに使う）
    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    // 加工の進捗度を0〜1に換算してピクセル幅（例: 24ピクセル）に変換する
    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1); // maxProgress
        int progressArrowSize = 24; // 矢印の画像サイズ（ピクセル）

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, blockEntity.getBlockState().getBlock());
    }

    // シフトクリック（クイック移動）時にフリーズするのを防ぐ必須メソッド
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyStack = sourceStack.copy();

        if (index < 2) { // 機械のスロットからプレイヤーへ移動
            if (!moveItemStackTo(sourceStack, 2, 38, true)) {
                return ItemStack.EMPTY;
            }
        } else { // プレイヤーから機械のスロット（材料スロット0）へ移動
            if (!moveItemStackTo(sourceStack, 0, 1, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (sourceStack.isEmpty()) sourceSlot.set(ItemStack.EMPTY);
        else sourceSlot.setChanged();

        if (sourceStack.getCount() == copyStack.getCount()) return ItemStack.EMPTY;
        sourceSlot.onTake(playerIn, sourceStack);
        return copyStack;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
