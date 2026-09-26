package com.surubedai.extrememetals.regi;

import com.surubedai.extrememetals.ExtremeMetalsMain;
import com.surubedai.extrememetals.recipe.RecipePrimitiveAlloyingMachine;
import com.surubedai.extrememetals.recipe.RecipePrimitiveReductionFurnace;
import com.surubedai.extrememetals.recipe.RecipeSteamLathe;
import com.surubedai.extrememetals.recipe.RecipeSteamRollingMill;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ExtremeMetalsAddRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ExtremeMetalsMain.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, ExtremeMetalsMain.MODID);

    // シリアライザーの登録
    public static final RegistryObject<RecipeSerializer<RecipePrimitiveReductionFurnace>> PRIMITIVE_REDUCTION_SERIALIZER =
            SERIALIZERS.register("primitive_reduction", () -> RecipePrimitiveReductionFurnace.Serializer.INSTANCE);

    // レシピタイプの登録
    public static final RegistryObject<RecipeType<RecipePrimitiveReductionFurnace>> PRIMITIVE_REDUCTION_TYPE =
            TYPES.register("primitive_reduction", () -> RecipePrimitiveReductionFurnace.Type.INSTANCE);


    public static final RegistryObject<RecipeSerializer<RecipePrimitiveAlloyingMachine>> PRIMITIVE_ALLOYING_SERIALIZER =
            SERIALIZERS.register("primitive_alloying", () -> RecipePrimitiveAlloyingMachine.Serializer.INSTANCE);

    public static final RegistryObject<RecipeType<RecipePrimitiveAlloyingMachine>> PRIMITIVE_ALLOYING_TYPE =
            TYPES.register("primitive_alloying", () -> RecipePrimitiveAlloyingMachine.Type.INSTANCE);


    public static final RegistryObject<RecipeSerializer<RecipeSteamRollingMill>> STEAM_ROLLING_SERIALIZER =
            SERIALIZERS.register("steam_rolling", () -> RecipeSteamRollingMill.Serializer.INSTANCE);

    public static final RegistryObject<RecipeType<RecipeSteamRollingMill>> STEAM_ROLLING_TYPE =
            TYPES.register("steam_rolling", () -> RecipeSteamRollingMill.Type.INSTANCE);

    public static final RegistryObject<RecipeSerializer<RecipeSteamLathe>> STEAM_LATHE_SERIALIZER =
            SERIALIZERS.register("steam_lathe", () -> RecipeSteamLathe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeType<RecipeSteamLathe>> STEAM_LATHE_TYPE =
            TYPES.register("steam_lathe", () -> RecipeSteamLathe.Type.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
