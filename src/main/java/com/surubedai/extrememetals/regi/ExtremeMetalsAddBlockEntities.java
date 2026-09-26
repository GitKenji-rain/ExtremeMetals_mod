package com.surubedai.extrememetals.regi; // ご自身の環境に合わせて書き換えてください

import com.surubedai.extrememetals.ExtremeMetalsMain;
import com.surubedai.extrememetals.blockentity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ExtremeMetalsAddBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ExtremeMetalsMain.MODID);


    public static final RegistryObject<BlockEntityType<BlockentityPrimitiveReductionFurnace>> PRIMITIVE_REDUCTION_FURNACE =
            BLOCK_ENTITIES.register("primitive_reduction_furnace", () ->
                    BlockEntityType.Builder.of(BlockentityPrimitiveReductionFurnace::new,
                            ExtremeMetalsAddBlocks.Blocks.PRIMITIVE_REDUCTION_FURNACE.get()).build(null));


    public static final RegistryObject<BlockEntityType<BlockentityPrimitiveAlloyingMachine>> PRIMITIVE_ALLOYING_MACHINE =
            BLOCK_ENTITIES.register("primitive_alloying_machine", () ->
                    BlockEntityType.Builder.of(BlockentityPrimitiveAlloyingMachine::new,
                            ExtremeMetalsAddBlocks.Blocks.PRIMITIVE_ALLOYING_MACHINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockentityPrimitiveSteamBoiler>> PRIMITIVE_STEAM_BOILER =
            BLOCK_ENTITIES.register("primitive_steam_boiler", () ->
                    BlockEntityType.Builder.of(BlockentityPrimitiveSteamBoiler::new,
                            ExtremeMetalsAddBlocks.Blocks.PRIMITIVE_STEAM_BOILER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockentitySteamRollingMill>> STEAM_ROLLING_MILL =
            BLOCK_ENTITIES.register("steam_rolling_mill", () ->
                    BlockEntityType.Builder.of(BlockentitySteamRollingMill::new,
                            ExtremeMetalsAddBlocks.Blocks.STEAM_ROLLING_MILL.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockentitySteamLathe>> STEAM_LATHE =
            BLOCK_ENTITIES.register("steam_lathe", () ->
                    BlockEntityType.Builder.of(BlockentitySteamLathe::new,
                            ExtremeMetalsAddBlocks.Blocks.STEAM_LATHE.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockentitySteamAutomaticLoom>> STEAM_AUTOMATIC_LOOM =
            BLOCK_ENTITIES.register("steam_automatic_loom", () ->
                    BlockEntityType.Builder.of(BlockentitySteamAutomaticLoom::new,
                            ExtremeMetalsAddBlocks.Blocks.STEAM_AUTOMATIC_LOOM.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
