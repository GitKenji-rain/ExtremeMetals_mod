package com.surubedai.extrememetals.regi;

import com.surubedai.extrememetals.ExtremeMetalsMain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class ExtremeMetalsAddFluids {
    public static class FluidTypes {
        public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(
                ForgeRegistries.Keys.FLUID_TYPES,
                ExtremeMetalsMain.MODID
        );

        public static final RegistryObject<FluidType> STEAM_FLUID_TYPE = FLUID_TYPES.register("steam",
            () -> new FluidType(FluidType.Properties.create()
                .lightLevel(0)
                .density(1500)
                .viscosity(2000)
            ){
            @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        private static final ResourceLocation WATER_STILL = new ResourceLocation("block/water_still");
                        private static final ResourceLocation WATER_FLOW = new ResourceLocation("block/water_flow");

                        @Override
                        public ResourceLocation getStillTexture() { return WATER_STILL; }

                        @Override
                        public ResourceLocation getFlowingTexture() { return WATER_FLOW; }

                        @Override
                        public int getTintColor() {
                            return 0x11E8E8E8;
                        }
                    });
                }
            }
        );
    }

    public static class Fluids {
        public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(
                ForgeRegistries.FLUIDS,
                ExtremeMetalsMain.MODID
        );

        public static final RegistryObject<FlowingFluid> SOURCE_STEAM =
            FLUIDS.register("steam",
                () -> new ForgeFlowingFluid.Source(ExtremeMetalsAddFluids.STEAM_PROPERTIES)
        );
        public static final RegistryObject<FlowingFluid> FLOWING_STEAM =
            FLUIDS.register("flowing_steam",
                () -> new ForgeFlowingFluid.Flowing(ExtremeMetalsAddFluids.STEAM_PROPERTIES)
        );
    }

    public static class FluidBlocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
                ForgeRegistries.BLOCKS,
                ExtremeMetalsMain.MODID
        );

        public static final RegistryObject<LiquidBlock> STEAM_BLOCK =
            BLOCKS.register("steam_block",
                () -> new LiquidBlock(Fluids.SOURCE_STEAM, BlockBehaviour.Properties.copy(Blocks.WATER)
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .pushReaction(PushReaction.DESTROY)
                    .noCollission()
                    .noLootTable()
            )
        );
    }

    public static class BucketItems {
        public static final DeferredRegister<Item> BUCKET_ITEMS = DeferredRegister.create(
                ForgeRegistries.ITEMS,
                ExtremeMetalsMain.MODID
        );

        public static final RegistryObject<Item> STEAM_BUCKET =
            BUCKET_ITEMS.register("steam_bucket",
                () -> new BucketItem(Fluids.SOURCE_STEAM, new Item.Properties()
                    .craftRemainder(Items.BUCKET)
                    .stacksTo(16)
            )
        );
    }

    public static final ForgeFlowingFluid.Properties STEAM_PROPERTIES = new ForgeFlowingFluid.Properties(
        FluidTypes.STEAM_FLUID_TYPE, Fluids.SOURCE_STEAM, Fluids.FLOWING_STEAM)
        .bucket(BucketItems.STEAM_BUCKET)
        .block(FluidBlocks.STEAM_BLOCK)
        .slopeFindDistance(2)
        .levelDecreasePerBlock(10);
}
