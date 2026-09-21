package com.surubedai.extrememetals.blockentity;

import com.surubedai.extrememetals.block.BlockPrimitiveSteamBoiler;
import com.surubedai.extrememetals.regi.ExtremeMetalsAddFluids;
import com.surubedai.extrememetals.regi.ExtremeMetalsAddBlockEntities;
import com.surubedai.extrememetals.screen.ScreenPrimitiveSteamBoiler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeHooks;
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
import org.jetbrains.annotations.Nullable;

public class BlockentityPrimitiveSteamBoiler extends BlockEntity implements MenuProvider {
    // 容量：10,000 mB
    private final FluidTank waterTank = new FluidTank(10000) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            // 水のみ受け入れる
            return stack.getFluid() == net.minecraft.world.level.material.Fluids.WATER;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            // 外部（パイプなど）からの搬入を通常通り処理する
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

    private final FluidTank steamTank = new FluidTank(10000) {
        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return 0;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            return super.drain(maxDrain, action);
        }

        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    //内部用
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
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
            return itemHandler.insertItem(slot, stack, simulate);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
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

    //内部用を返すため
    public ItemStackHandler getInventory() { return this.itemHandler; }

    protected final ContainerData data;
    private int burnTime = 0;
    private int maxBurnTime = 0;
    private final int waterPerTick = 10;
    private final int steamPerTick = 20;

    public BlockentityPrimitiveSteamBoiler(BlockPos pPos, BlockState pState) {
        super(ExtremeMetalsAddBlockEntities.PRIMITIVE_STEAM_BOILER.get(), pPos, pState);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> BlockentityPrimitiveSteamBoiler.this.burnTime;
                    case 1 -> BlockentityPrimitiveSteamBoiler.this.maxBurnTime;
                    case 2 -> BlockentityPrimitiveSteamBoiler.this.waterTank.getFluidAmount();
                    case 3 -> BlockentityPrimitiveSteamBoiler.this.steamTank.getFluidAmount();
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> BlockentityPrimitiveSteamBoiler.this.burnTime = pValue;
                    case 1 -> BlockentityPrimitiveSteamBoiler.this.maxBurnTime = pValue;
                    case 2 -> BlockentityPrimitiveSteamBoiler.this.waterTank.setFluid(
                        new FluidStack(Fluids.WATER, pValue));
                    case 3 -> BlockentityPrimitiveSteamBoiler.this.steamTank.setFluid(
                        new FluidStack(ExtremeMetalsAddFluids.Fluids.SOURCE_STEAM.get(), pValue));
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        waterTank.readFromNBT(pTag.getCompound("WaterTank"));
        steamTank.readFromNBT(pTag.getCompound("SteamTank"));
        itemHandler.deserializeNBT(pTag.getCompound("Inventory"));
        burnTime = pTag.getInt("BurnTime");
        maxBurnTime = pTag.getInt("MaxBurnTime");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("WaterTank", waterTank.writeToNBT(new CompoundTag()));
        pTag.put("SteamTank", steamTank.writeToNBT(new CompoundTag()));
        pTag.put("Inventory", itemHandler.serializeNBT());
        pTag.putInt("BurnTime", burnTime);
        pTag.putInt("MaxBurnTime", maxBurnTime);
    }


    // --- クラス内部の変数定義エリア ---
    // 外部公開用に、水タンク用と蒸気タンク用のLazyOptionalを別々に用意
    private final LazyOptional<IFluidHandler> combinedFluidOptional = LazyOptional.of(() -> new IFluidHandler() {
        @Override
        public int getTanks() {
            // 合計のタンク数は2つ（水 + 蒸気）
            return 2;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            // 0番を水、1番を蒸気として扱う
            return tank == 0 ? waterTank.getFluid() : steamTank.getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return tank == 0 ? waterTank.getCapacity() : steamTank.getCapacity();
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return tank == 0 ? waterTank.isFluidValid(stack) : steamTank.isFluidValid(stack);
        }


        @Override
        public int fill(FluidStack resource, FluidAction action) {
            // 外部からの搬入（fill）は、水のタンク（waterTank）のみに許可
            return waterTank.fill(resource, action);
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            // 外部からの指定搬出（drain）は、蒸気のタンク（steamTank）のみに許可
            return steamTank.drain(resource, action);
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            // 外部からの量指定搬出（drain）は、蒸気のタンク（steamTank）のみに許可
            return steamTank.drain(maxDrain, action);
        }
    });
    private LazyOptional<IItemHandler> externalOptional = LazyOptional.of(() -> externalHandler);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        // 流体
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return combinedFluidOptional.cast();
        }

        // アイテム
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

    // メモリリーク防止のため、ブロックが壊れた際などにCapabilityを無効化する処理
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        combinedFluidOptional.invalidate();
        externalOptional.invalidate();
    }

    // 毎Tickの処理
    public static void tick(Level level, BlockPos pos, BlockState state,BlockentityPrimitiveSteamBoiler be) {
        if (level.isClientSide) return;

        boolean isDirty = false;

        if (be.burnTime > 0) {
            be.burnTime--;
            isDirty = true;
        }

        // 燃料補充
        if (be.burnTime <= 0 && be.canWork()) {
            ItemStack fuel = be.itemHandler.getStackInSlot(0);
            int burn = ForgeHooks.getBurnTime(fuel, net.minecraft.world.item.crafting.RecipeType.SMELTING);
            if (burn > 0) {
                be.burnTime = burn;
                be.maxBurnTime = burn;
                fuel.shrink(1);
                isDirty = true;
            }
        }

        if (be.burnTime > 0 && be.canWork()) {

            int currentWater = be.waterTank.getFluidAmount();
            FluidStack waterStack = new FluidStack(Fluids.WATER, currentWater - be.waterPerTick);
            be.waterTank.setFluid(waterStack);

            int currentSteam = be.steamTank.getFluidAmount();
            FluidStack steamStack = new FluidStack(ExtremeMetalsAddFluids.Fluids.SOURCE_STEAM.get(), currentSteam + be.steamPerTick);
            be.steamTank.setFluid(steamStack);

            isDirty = true;
        }

        if (isDirty) {
            be.setChanged();
        }

        if (state.getValue(BlockPrimitiveSteamBoiler.LIT) != be.burnTime > 0) {
            level.setBlock(pos, state.setValue(BlockPrimitiveSteamBoiler.LIT, be.burnTime > 0), 4);
        }
    }

    private boolean canWork() {
        if (waterTank.getFluidAmount() < waterPerTick) return false;
        return steamTank.getFluidAmount() + steamPerTick <= steamTank.getCapacity();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.extrememetals.primitive_steam_boiler");
    }

    @javax.annotation.Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ScreenPrimitiveSteamBoiler(pContainerId, pPlayerInventory, this, this.data);
    }

}
