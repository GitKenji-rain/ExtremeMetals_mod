package com.surubedai.extrememetals.regi;

import com.surubedai.extrememetals.ExtremeMetalsMain;
import com.surubedai.extrememetals.screen.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ExtremeMetalsAddScreens {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, ExtremeMetalsMain.MODID);

    // MenuTypeの登録（IForgeMenuTypeを使うことで、BlockPosなどの追加データをクライアントに送れる）
    public static final RegistryObject<MenuType<ScreenPrimitiveReductionFurnace>> PRIMITIVE_REDUCTION_MENU =
            registerMenuType("primitive_reduction_menu", ScreenPrimitiveReductionFurnace::new);

    public static final RegistryObject<MenuType<ScreenPrimitiveAlloyingMachine>> PRIMITIVE_ALLOYING_MENU =
            registerMenuType("primitive_alloying_menu", ScreenPrimitiveAlloyingMachine::new);

    public static final RegistryObject<MenuType<ScreenPrimitiveSteamBoiler>> PRIMITIVE_BOILER_MENU =
            registerMenuType("primitive_boiler_menu", ScreenPrimitiveSteamBoiler::new);

    public static final RegistryObject<MenuType<ScreenSteamRollingMill>> STEAM_ROLLING_MENU =
            registerMenuType("steam_rolling_menu", ScreenSteamRollingMill::new);

    public static final RegistryObject<MenuType<ScreenSteamLathe>> STEAM_LATHE_MENU =
            registerMenuType("steam_lathe_menu", ScreenSteamLathe::new);


    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
