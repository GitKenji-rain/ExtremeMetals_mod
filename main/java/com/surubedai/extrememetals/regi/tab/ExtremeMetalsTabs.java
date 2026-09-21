package com.surubedai.extrememetals.regi.tab;

import com.surubedai.extrememetals.ExtremeMetalsMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ExtremeMetalsTabs {
    public static final DeferredRegister<CreativeModeTab> MOD_TABS = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB,
            ExtremeMetalsMain.MODID);

    public static final RegistryObject<CreativeModeTab> EXTREME_METALS = MOD_TABS.register(
        "extreme_metals",
        () -> {
            return CreativeModeTab.builder()
                .icon( () -> new ItemStack(Items.IRON_INGOT))
                .title(Component.translatable("itemGroup.extreme_metals"))
                .displayItems((parameter,output) -> {
                    for(Item item:ExtremeMetalsItems.items){
                        output.accept(item);
                    }
                })
                .build();
        });
}
