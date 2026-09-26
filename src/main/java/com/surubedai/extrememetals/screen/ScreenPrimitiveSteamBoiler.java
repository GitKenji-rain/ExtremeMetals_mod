package com.surubedai.extrememetals.screen;

import com.surubedai.extrememetals.blockentity.BlockentityPrimitiveSteamBoiler;
import com.surubedai.extrememetals.regi.ExtremeMetalsAddScreens;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class ScreenPrimitiveSteamBoiler extends AbstractContainerMenu {
    public final BlockentityPrimitiveSteamBoiler blockEntity;
    private final ContainerData data;

    // クライアント側から呼ばれるコンストラクタ
    public ScreenPrimitiveSteamBoiler(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    // サーバー側から呼ばれるコンストラクタ
    public ScreenPrimitiveSteamBoiler(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ExtremeMetalsAddScreens.PRIMITIVE_BOILER_MENU.get(), pContainerId);
        this.blockEntity = (BlockentityPrimitiveSteamBoiler) entity;
        this.data = data;

        this.addSlot(new SlotItemHandler(blockEntity.getInventory(), 0, 116, 55));

        // プレイヤーインベントリの配置
        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        // データの同期（burnTime, maxBurnTimeなど）
        addDataSlots(data);
    }

    // データのgetter（GUIやScreenでゲージ描画に使う）
    public int getBurnTime() {
        return this.data.get(0);
    }

    public int getMaxBurnTime() {
        return this.data.get(1);
    }

    public int getWaterAmount() {
        return this.data.get(2);
    }

    public int getSteamAmount() {
        return this.data.get(3);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
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
