package com.surubedai.extrememetals.blockentity;

import com.surubedai.extrememetals.block.BlockSteamRollingMill;
import com.surubedai.extrememetals.recipe.RecipeSteamRollingMill;
import com.surubedai.extrememetals.regi.ExtremeMetalsAddBlockEntities;
import com.surubedai.extrememetals.regi.ExtremeMetalsAddFluids;
import com.surubedai.extrememetals.screen.ScreenSteamRollingMill;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BlockentitySteamRollingMill extends BlockEntity implements MenuProvider{
    // 0: 材料, 1: 完成
    //内部用
    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    //外部用
    private final IItemHandlerModifiable externalHandler = new IItemHandlerModifiable() {
        @Override
        public int getSlots() {
            return itemHandler.getSlots();
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            return itemHandler.getStackInSlot(slot);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (slot == 1) return stack;
            return itemHandler.insertItem(slot, stack, simulate);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == 0) return ItemStack.EMPTY;
            return itemHandler.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return itemHandler.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return itemHandler.isItemValid(slot, stack);
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack stack) {
            itemHandler.setStackInSlot(slot, stack);
        }
    };

    private final FluidTank steamTank = new FluidTank(5000) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            //蒸気だけ
            return stack.getFluid() == ExtremeMetalsAddFluids.Fluids.SOURCE_STEAM.get();
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return super.fill(resource, action);
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            return FluidStack.EMPTY;
        }

        @Override
        protected void onContentsChanged() {
            setChanged();
        }

    };

    private LazyOptional<IItemHandler> externalOptional = LazyOptional.empty();
    private final LazyOptional<IFluidHandler> steamOptional = LazyOptional.of(() -> steamTank);

    //内部用を返すため
    public ItemStackHandler getInventory() { return this.itemHandler; }

    // 加工時間（タイマー）の変数
    private int progress = 0;
    private int maxProgress = 50; // 加工時間

    protected final ContainerData data;

    public BlockentitySteamRollingMill(BlockPos pos, BlockState state) {
        super(ExtremeMetalsAddBlockEntities.STEAM_ROLLING_MILL.get(), pos, state);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BlockentitySteamRollingMill.this.progress;
                    case 1 -> BlockentitySteamRollingMill.this.maxProgress;
                    case 2 -> BlockentitySteamRollingMill.this.steamTank.getFluidAmount();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> BlockentitySteamRollingMill.this.progress = value;
                    case 1 -> BlockentitySteamRollingMill.this.maxProgress = value;
                    case 2 -> BlockentitySteamRollingMill.this.steamTank.setFluid(
                            new FluidStack(ExtremeMetalsAddFluids.Fluids.SOURCE_STEAM.get(), value));
                }
            }

            @Override
            public int getCount() {
                return 3; // 同期する変数の総数
            }
        };
    }

    // 外部（パイプやホッパー）からアイテムを出し入れできるようにする設定
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return steamOptional.cast();
        }

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return externalOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.externalOptional = LazyOptional.of(() -> this.externalHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        externalOptional.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("Inventory", itemHandler.serializeNBT());
        nbt.put("SteamTank", steamTank.writeToNBT(new CompoundTag()));
        nbt.putInt("Progress", this.progress);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("Inventory"));
        steamTank.readFromNBT(nbt.getCompound("SteamTank"));
        this.progress = nbt.getInt("Progress");
    }

    private final int steamPerTick = 12;
    // 毎チック実行される加工ロジック
    public static void tick(Level level, BlockPos pos, BlockState state, BlockentitySteamRollingMill pEntity) {
        if (level.isClientSide()) return;

        boolean isCrafting = pEntity.hasRecipe();

        if (state.getValue(BlockSteamRollingMill.LIT) != isCrafting) {
            level.setBlock(pos, state.setValue(BlockSteamRollingMill.LIT, isCrafting), 3);
        }

        if (isCrafting) {
            int currentSteam = pEntity.steamTank.getFluidAmount();
            if (currentSteam > pEntity.steamPerTick) {
                FluidStack steamStack = new FluidStack(ExtremeMetalsAddFluids.Fluids.SOURCE_STEAM.get(), currentSteam - pEntity.steamPerTick);
                pEntity.steamTank.setFluid(steamStack);
                pEntity.progress++;
            } else{
                pEntity.resetProgress();
            }
            setChanged(level, pos, state);

            if (pEntity.progress >= pEntity.maxProgress) {
                pEntity.craftItem();
            }
        } else {
            pEntity.resetProgress();
        }
    }

    private boolean hasRecipe() {
        SimpleContainer container = new SimpleContainer(2);
        container.setItem(0, this.itemHandler.getStackInSlot(0)); // 入力スロットをセット
        container.setItem(1, this.itemHandler.getStackInSlot(1)); // 出力スロットをセット

        // 現在のワールドにある「steam_rolling」タイプのレシピから、条件に合うものを検索
        java.util.Optional<RecipeSteamRollingMill> recipe =
                this.level.getRecipeManager().getRecipeFor(RecipeSteamRollingMill.Type.INSTANCE, container, this.level);

        if (recipe.isPresent()) {
            ItemStack result = recipe.get().getResultItem(this.level.registryAccess());
            ItemStack outputSlot = this.itemHandler.getStackInSlot(1); // 前の機械の出力は必ずスロット1

            // 出力スロットが空か、同じアイテムでスタック可能かチェック
            return outputSlot.isEmpty() ||
                    (outputSlot.is(result.getItem()) && // == ではなく .is() の方が1.20.1では安全です
                            outputSlot.getCount() + result.getCount() <= outputSlot.getMaxStackSize());
        }
        return false;
    }

    // 実際の加工処理（アイテムの消費と生成）
    private void craftItem() {
        // 【修正】ここも同様にサイズ2のコンテナに固定
        SimpleContainer container = new SimpleContainer(2);
        container.setItem(0, this.itemHandler.getStackInSlot(0));
        container.setItem(1, this.itemHandler.getStackInSlot(1));

        java.util.Optional<RecipeSteamRollingMill> recipe =
                this.level.getRecipeManager().getRecipeFor(RecipeSteamRollingMill.Type.INSTANCE, container, this.level);

        if (recipe.isPresent()) {
            ItemStack result = recipe.get().getResultItem(this.level.registryAccess());

            // 【修正】ItemStackHandlerの直接操作に切り替えて、より確実に
            this.itemHandler.getStackInSlot(0).shrink(1); // スロット0の材料を1個減らす

            ItemStack outputSlot = this.itemHandler.getStackInSlot(1);
            if (outputSlot.isEmpty()) {
                this.itemHandler.setStackInSlot(1, result.copy()); // 空ならそのままセット
            } else {
                outputSlot.grow(result.getCount()); // すでにあるなら個数を増やす
            }

            this.resetProgress();
        }
    }


    // 💡 クラス内のどこかに以下の2つのオーバーライドメソッドを追記
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.extrememetals.steam_rolling_mill"); // 画面上部に表示される機械の名前
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        // 先ほど作成したMenuクラスに、自身のBlockEntityと同期データを渡して生成
        return new ScreenSteamRollingMill(containerId, playerInventory, this, this.data);
    }


    private void resetProgress() {
        this.progress = 0;
    }
}
