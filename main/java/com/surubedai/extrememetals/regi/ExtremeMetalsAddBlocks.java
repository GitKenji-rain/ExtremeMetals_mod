package com.surubedai.extrememetals.regi;

import com.surubedai.extrememetals.ExtremeMetalsMain;
import com.surubedai.extrememetals.block.*;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ExtremeMetalsAddBlocks {

    public static class Blocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
                ForgeRegistries.BLOCKS,
                ExtremeMetalsMain.MODID
        );

        public static final RegistryObject<Block> NATIVE_SILVER_ORE = BLOCKS.register(
                "native_silver_ore",
                () -> new BlockNormal(
                        BlockBehaviour.Properties.of()
                                .strength(3.0F, 3.0F)
                                .sound(SoundType.STONE)
                                .requiresCorrectToolForDrops()
                )
        );

        public static final RegistryObject<Block> CASSITERITE_ORE = BLOCKS.register(
                "cassiterite_ore",
                () -> new BlockNormal(
                        BlockBehaviour.Properties.of()
                                .strength(4.0F, 4.0F)
                                .sound(SoundType.STONE)
                                .requiresCorrectToolForDrops()
                )
        );

        public static final RegistryObject<Block> SPHALERITE_ORE = BLOCKS.register(
                "sphalerite_ore",
                () -> new BlockNormal(
                        BlockBehaviour.Properties.of()
                                .strength(4.0F, 4.0F)
                                .sound(SoundType.STONE)
                                .requiresCorrectToolForDrops()
                )
        );

        public static final RegistryObject<Block> PRIMITIVE_REDUCTION_FURNACE = BLOCKS.register(
                "primitive_reduction_furnace",
                () -> new BlockPrimitiveReductionFurnace(
                        BlockBehaviour.Properties.of()
                                .strength(6.0F, 3.0F)
                                .sound(SoundType.BASALT)
                                .requiresCorrectToolForDrops()
                )
        );

        public static final RegistryObject<Block> PRIMITIVE_ALLOYING_MACHINE = BLOCKS.register(
                "primitive_alloying_machine",
                () -> new BlockPrimitiveAlloyingMachine(BlockBehaviour.Properties.of()
                        .strength(6.0F,3.0F)
                        .sound(SoundType.BASALT)
                        .requiresCorrectToolForDrops()));


        public static final RegistryObject<Block> PRIMITIVE_STEAM_BOILER = BLOCKS.register(
                "primitive_steam_boiler",
                () -> new BlockPrimitiveSteamBoiler(BlockBehaviour.Properties.of()
                        .strength(6.0F,3.0F)
                        .sound(SoundType.BASALT)
                        .requiresCorrectToolForDrops()));

        public static final RegistryObject<Block> STEAM_ROLLING_MILL = BLOCKS.register(
                "steam_rolling_mill",
                () -> new BlockSteamRollingMill(BlockBehaviour.Properties.of()
                        .strength(6.0F,3.0F)
                        .sound(SoundType.BASALT)
                        .requiresCorrectToolForDrops()));

    }

    public static class BlockItems {
        public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(
                ForgeRegistries.ITEMS,
                ExtremeMetalsMain.MODID
        );

        public static final RegistryObject<Item> NATIVE_SILVER_ORE = BLOCK_ITEMS.register(
                "native_silver_ore",
                () -> new BlockItem(
                        Blocks.NATIVE_SILVER_ORE.get(),
                        new Item.Properties()
                )
        );

        public static final RegistryObject<Item> CASSITERITE_ORE = BLOCK_ITEMS.register(
                "cassiterite_ore",
                () -> new BlockItem(
                        Blocks.CASSITERITE_ORE.get(),
                        new Item.Properties()
                )
        );

        public static final RegistryObject<Item> SPHALERITE_ORE = BLOCK_ITEMS.register(
                "sphalerite_ore",
                () -> new BlockItem(
                        Blocks.SPHALERITE_ORE.get(),
                        new Item.Properties()
                )
        );


        public static final RegistryObject<Item> PRIMITIVE_REDUCTION_FURNACE = BLOCK_ITEMS.register(
                "primitive_reduction_furnace",
                () -> new BlockItem(
                        Blocks.PRIMITIVE_REDUCTION_FURNACE.get(),
                        new Item.Properties()
                )
        );

        public static final RegistryObject<Item> PRIMITIVE_ALLOYING_MACHINE = BLOCK_ITEMS.register(
                "primitive_alloying_machine",
                () -> new BlockItem(
                        Blocks.PRIMITIVE_ALLOYING_MACHINE.get(),
                        new Item.Properties()
                )
        );

        public static final RegistryObject<Item> PRIMITIVE_STEAM_BOILER = BLOCK_ITEMS.register(
                "primitive_steam_boiler",
                () -> new BlockItem(
                        Blocks.PRIMITIVE_STEAM_BOILER.get(),
                        new Item.Properties()
                )
        );

        public static final RegistryObject<Item> STEAM_ROLLING_MILL = BLOCK_ITEMS.register(
                "steam_rolling_mill",
                () -> new BlockItem(
                        Blocks.STEAM_ROLLING_MILL.get(),
                        new Item.Properties()
                )
        );
    }
}
