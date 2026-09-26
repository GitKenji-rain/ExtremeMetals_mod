package com.surubedai.extrememetals.screen;

import com.surubedai.extrememetals.blockentity.BlockentitySteamAutomaticLoom;
import com.surubedai.extrememetals.regi.ExtremeMetalsAddScreens;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

public class ScreenSteamAutomaticLoom extends AbstractContainerMenu {
    public final BlockentitySteamAutomaticLoom blockEntity;
    private final Level level;
    private final ContainerData data; // 進捗バーの同期用データ

    // クライアント側（画面を開く側）が呼び出すコンストラクタ
    public ScreenSteamAutomaticLoom(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }

    // サーバー側（内部処理側）が呼び出す共通コンストラクタ
    public ScreenSteamAutomaticLoom(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ExtremeMetalsAddScreens.STEAM_LOOM_MENU.get(), containerId);
        checkContainerDataCount(data, 3);
        this.blockEntity = ((BlockentitySteamAutomaticLoom) entity);
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
    
    public boolean isCrafting() {
        return data.get(0) > 0;
    }
    
    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1); 
        int progressArrowSize = 24;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
    
    public int getSteamAmount() {
        return data.get(2);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, blockEntity.getBlockState().getBlock());
    }

    //クイック移動
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyStack = sourceStack.copy();

        if (index < 2) {
            if (!moveItemStackTo(sourceStack, 2, 38, true)) {
                return ItemStack.EMPTY;
            }
        } else {
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
