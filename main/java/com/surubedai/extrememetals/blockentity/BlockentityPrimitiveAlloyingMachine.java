package com.surubedai.extrememetals.blockentity;

import com.surubedai.extrememetals.block.BlockPrimitiveAlloyingMachine;
import com.surubedai.extrememetals.recipe.RecipePrimitiveAlloyingMachine;
import com.surubedai.extrememetals.regi.ExtremeMetalsAddBlockEntities;
import com.surubedai.extrememetals.screen.ScreenPrimitiveAlloyingMachine;
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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BlockentityPrimitiveAlloyingMachine extends BlockEntity implements MenuProvider{
    // 1. アイテムスロットの定義
    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int FUEL_SLOT = 2;
    public static final int OUTPUT_SLOT = 3;

    //内部用
    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
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
            return switch (slot) {
                case OUTPUT_SLOT -> ItemStack.EMPTY;
                default -> itemHandler.insertItem(slot, stack, simulate);
            };
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return switch (slot) {
                case OUTPUT_SLOT -> itemHandler.extractItem(slot, amount, simulate);
                default -> ItemStack.EMPTY;
            };
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

    private LazyOptional<IItemHandler> externalOptional = LazyOptional.of(() -> externalHandler);
    //内部用を返すため
    public ItemStackHandler getInventory() { return this.itemHandler; }

    // 同期・保存するデータ（ContainerData などに入れる変数）
    private int progress = 0;
    private int maxProgress = 50;
    private int litTime = 0;       // 現在の残り燃料時間
    private int maxLitTime = 0;    // 使用した燃料の最大燃料時間

    protected final ContainerData data;

    public BlockentityPrimitiveAlloyingMachine(BlockPos pos, BlockState state) {
        super(ExtremeMetalsAddBlockEntities.PRIMITIVE_ALLOYING_MACHINE.get(), pos, state);
        // 2つのデータ（0: progress, 1: maxProgress）を同期する設定
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BlockentityPrimitiveAlloyingMachine.this.progress;
                    case 1 -> BlockentityPrimitiveAlloyingMachine.this.maxProgress;
                    case 2 -> BlockentityPrimitiveAlloyingMachine.this.litTime;
                    case 3 -> BlockentityPrimitiveAlloyingMachine.this.maxLitTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> BlockentityPrimitiveAlloyingMachine.this.progress = value;
                    case 1 -> BlockentityPrimitiveAlloyingMachine.this.maxProgress = value;
                    case 2 -> BlockentityPrimitiveAlloyingMachine.this.litTime = value;
                    case 3 -> BlockentityPrimitiveAlloyingMachine.this.maxLitTime = value;
                }
            }

            @Override
            public int getCount() {
                return 4; // 同期する変数の総数
            }
        };
    }

    // 外部（パイプやホッパー）からアイテムを出し入れできるようにする設定
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
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
        externalOptional.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("Inventory", itemHandler.serializeNBT());
        nbt.putInt("Progress", this.progress);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("Inventory"));
        this.progress = nbt.getInt("Progress");
    }

    // 毎チック実行される加工ロジック
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        boolean isLitBefore = this.litTime > 0;
        boolean changed = false;

        // 1. 燃料のカウントダウン
        if (this.litTime > 0) {
            this.litTime--;
        }

        // 2. 精錬処理（サーバー側のみで実行）
        if (!pLevel.isClientSide) {
            ItemStack fuelStack = this.itemHandler.getStackInSlot(FUEL_SLOT);

            // 機械が燃えていない、かつ「精錬可能なアイテム」が揃っている場合、燃料を消費する
            if (this.litTime <= 0 && hasRecipe()) {
                int burnTime = ForgeHooks.getBurnTime(fuelStack, RecipeType.SMELTING);
                if (burnTime > 0) {
                    this.litTime = burnTime;
                    this.maxLitTime = burnTime;

                    // 燃料アイテムを1個減らす
                    fuelStack.shrink(1);
                    changed = true;
                }
            }

            // 機械が燃えており、かつ「精錬可能なアイテム」が揃っているなら進行度を進める
            if (this.litTime > 0 && hasRecipe()) {
                this.progress++;

                // 進行度が最大に達したら、合金アイテムを合成する
                if (this.progress >= this.maxProgress) {
                    this.progress = 0;
                    craftItem();
                    changed = true;
                }
            } else {
                // レシピが外されたり燃料が切れたら、進行度をゆっくり戻す（または0にする）
                this.progress = Math.max(0, this.progress - 2);
            }

            // 燃焼状態（見た目）が変わった場合、ブロックの状態を更新（かまどが光るような処理用）
            if (isLitBefore != (this.litTime > 0)) {
                changed = true;
                // 必要に応じてブロックの「LIT」プロパティなどを更新するコードをここに書きます
                // 💡 現在のブロックの見た目（LIT状態）が、実際の動作状況と食い違っているかチェック
                if (pState.getValue(BlockPrimitiveAlloyingMachine.LIT) != hasRecipe()) {
                    level.setBlock(pPos, pState.setValue(BlockPrimitiveAlloyingMachine.LIT, hasRecipe()), 3);
                }
            }
        }

        // データに変更があった場合は保存を要求
        if (changed) {
            setChanged();
        }
    }

    private boolean hasRecipe() {
        // 💡 スロット0(入力1)とスロット1(入力2)だけを詰め込んだサイズ2のコンテナを作る
        SimpleContainer container = new SimpleContainer(2);
        container.setItem(0, this.itemHandler.getStackInSlot(INPUT_SLOT_1));
        container.setItem(1, this.itemHandler.getStackInSlot(INPUT_SLOT_2));

        // 現在登録されている「primitive_alloying」レシピから一致するものを検索
        java.util.Optional<RecipePrimitiveAlloyingMachine> recipe =
                this.level.getRecipeManager().getRecipeFor(RecipePrimitiveAlloyingMachine.Type.INSTANCE, container, this.level);

        if (recipe.isPresent()) {
            ItemStack result = recipe.get().getResultItem(this.level.registryAccess());
            ItemStack outputSlot = this.itemHandler.getStackInSlot(OUTPUT_SLOT); // スロット3

            // 出力スロットが空か、同じアイテムでスタック可能かチェック
            return outputSlot.isEmpty() ||
                    (outputSlot.is(result.getItem()) &&
                            outputSlot.getCount() + result.getCount() <= outputSlot.getMaxStackSize());
        }
        return false;
    }


    private void craftItem() {
        SimpleContainer container = new SimpleContainer(2);
        container.setItem(0, this.itemHandler.getStackInSlot(INPUT_SLOT_1));
        container.setItem(1, this.itemHandler.getStackInSlot(INPUT_SLOT_2));

        java.util.Optional<RecipePrimitiveAlloyingMachine> recipe =
                this.level.getRecipeManager().getRecipeFor(RecipePrimitiveAlloyingMachine.Type.INSTANCE, container, this.level);

        if (recipe.isPresent()) {
            RecipePrimitiveAlloyingMachine currentRecipe = recipe.get();
            ItemStack result = currentRecipe.getResultItem(this.level.registryAccess());

            // 💡 レシピから「スロット0」と「スロット1」のそれぞれの必要個数を自動取得して減らす
            int req1 = currentRecipe.getRequiredCountForSlot(0, container);
            int req2 = currentRecipe.getRequiredCountForSlot(1, container);

            this.itemHandler.getStackInSlot(INPUT_SLOT_1).shrink(req1);
            this.itemHandler.getStackInSlot(INPUT_SLOT_2).shrink(req2);

            // 成果物を出力スロットに追加する
            ItemStack outputSlot = this.itemHandler.getStackInSlot(OUTPUT_SLOT);
            if (outputSlot.isEmpty()) {
                // 💡 スロットが空なら、レシピで指定された個数（resultのcount）を保持したまま丸ごとセットする
                this.itemHandler.setStackInSlot(OUTPUT_SLOT, result.copy());
            } else {
                // 💡 すでにアイテムがある場合は、1個（grow(1)）ではなく、レシピで指定された個数分（result.getCount()）だけ増やす
                outputSlot.grow(result.getCount());
            }

            this.resetProgress();
        }
    }



    @Override
    public Component getDisplayName() {
        return Component.translatable("block.extrememetals.primitive_alloying_machine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ScreenPrimitiveAlloyingMachine(containerId, playerInventory, this, this.data);
    }



    private void resetProgress() {
        this.progress = 0;
    }
}
