package com.surubedai.extrememetals.screen;

import com.surubedai.extrememetals.blockentity.BlockentityPrimitiveAlloyingMachine;
import com.surubedai.extrememetals.regi.ExtremeMetalsAddScreens;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class ScreenPrimitiveAlloyingMachine extends AbstractContainerMenu {
    public final BlockentityPrimitiveAlloyingMachine blockEntity;
    private final Level level;
    protected final ContainerData data; // 進捗バーの同期用データ

    // クライアント側（画面を開く側）が呼び出すコンストラクタ
    public ScreenPrimitiveAlloyingMachine(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    // サーバー側（内部処理側）が呼び出す共通コンストラクタ
    public ScreenPrimitiveAlloyingMachine(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ExtremeMetalsAddScreens.PRIMITIVE_ALLOYING_MENU.get(), containerId);
        checkContainerDataCount(data, 4);
        this.blockEntity = ((BlockentityPrimitiveAlloyingMachine) entity);
        this.level = inv.player.level();
        this.data = data;

        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), 0, 44, 17));
        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), 1, 62, 17));
        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), 2, 53, 53));
        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), 3, 116, 35));

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
    // 炎ゲージの高さ（ピクセル数）を計算
    public int getBurnScaled() {
        int litTime = this.data.get(2); // 現在の燃焼時間
        int maxLitTime = this.data.get(3); // 最大燃焼時間
        if (maxLitTime == 0) return 0;
        return litTime * 13 / maxLitTime; // 炎の画像の高さが13ピクセルの場合
    }

    // 矢印ゲージの横幅（ピクセル数）を計算
    public int getProgressScaled() {
        int progress = this.data.get(0); // 現在の進行度
        int maxProgress = this.data.get(1); // 最大進行度
        if (maxProgress == 0) return 0;
        return progress * 24 / maxProgress; // 矢印の画像の横幅が24ピクセルの場合
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, blockEntity.getBlockState().getBlock());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        // クリックしたスロットが有効で、アイテムを持っている場合
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            // ケースA：機械側のスロット（0〜3）をシフトクリックした場合
            if (index < 4) {
                // プレイヤーのインベントリ（4〜39）へ移動を試みる（逆順で配置）
                if (!this.moveItemStackTo(itemstack1, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            }
            // ケースB：プレイヤー側のスロット（4〜39）をシフトクリックした場合
            else {
                // 1. 燃料になるアイテムかチェック（かまどの燃料判定を利用）
                if (ForgeHooks.getBurnTime(itemstack1, RecipeType.SMELTING) > 0) {
                    // 燃料スロット（2）への移動を試みる
                    if (!this.moveItemStackTo(itemstack1, 2, 3, false)) {
                        // 燃料スロットが満杯なら、入力スロット（0〜2）への移動を試みる
                        if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
                // 2. 燃料以外はすべて入力スロット（0〜2、つまり0と1）への移動を試みる
                else if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
                    // 機械側に入らなければ、インベントリ ↔ ホットバー間で移動
                    if (index < 31) { // メインインベントリからホットバーへ
                        if (!this.moveItemStackTo(itemstack1, 31, 40, false)) return ItemStack.EMPTY;
                    } else { // ホットバーからメインインベントリへ
                        if (!this.moveItemStackTo(itemstack1, 4, 31, false)) return ItemStack.EMPTY;
                    }
                    return ItemStack.EMPTY;
                }
            }

            // アイテムの個数が0になったらスロットを空にする
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            // 移動前後で個数が変わっていなければ処理を終了（移動失敗）
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
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
