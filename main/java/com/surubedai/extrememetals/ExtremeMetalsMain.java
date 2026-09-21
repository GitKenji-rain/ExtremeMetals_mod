package com.surubedai.extrememetals;

import com.surubedai.extrememetals.regi.*;
import com.surubedai.extrememetals.regi.tab.ExtremeMetalsTabs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExtremeMetalsMain.MODID)
public class ExtremeMetalsMain
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "extrememetals";

    public ExtremeMetalsMain()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ExtremeMetalsAddItems.ITEMS.register(bus);
        ExtremeMetalsAddBlocks.Blocks.BLOCKS.register(bus);
        ExtremeMetalsAddBlocks.BlockItems.BLOCK_ITEMS.register(bus);
        ExtremeMetalsTabs.MOD_TABS.register(bus);
        ExtremeMetalsAddBlockEntities.register(bus);
        ExtremeMetalsAddRecipes.register(bus);
        ExtremeMetalsAddScreens.register(bus);
        ExtremeMetalsAddFluids.FluidTypes.FLUID_TYPES.register(bus);
        ExtremeMetalsAddFluids.Fluids.FLUIDS.register(bus);
        ExtremeMetalsAddFluids.FluidBlocks.BLOCKS.register(bus);
        ExtremeMetalsAddFluids.BucketItems.BUCKET_ITEMS.register(bus);
    }
}

