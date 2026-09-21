package com.surubedai.extrememetals;// メインクラス（YourModMain.java など）の内部に追記、または既存の @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD) があればそこに追記します

import com.surubedai.extrememetals.regi.ExtremeMetalsAddScreens;

import com.surubedai.extrememetals.screen.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ExtremeMetalsMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ExtremeMetalsClientEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Menu と Screen を紐付ける公式の登録処理
            MenuScreens.register(ExtremeMetalsAddScreens.PRIMITIVE_REDUCTION_MENU.get(), ScreenPrimitiveReductionFurnaceDrawing::new);
            MenuScreens.register(ExtremeMetalsAddScreens.PRIMITIVE_ALLOYING_MENU.get(), ScreenPrimitiveAlloyingMachineDrawing::new);
            MenuScreens.register(ExtremeMetalsAddScreens.PRIMITIVE_BOILER_MENU.get(), ScreenPrimitiveSteamBoilerDrawing::new);
            MenuScreens.register(ExtremeMetalsAddScreens.STEAM_ROLLING_MENU.get(), ScreenSteamRollingMillDrawing::new);
        });
    }

}
