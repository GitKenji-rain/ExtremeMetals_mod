package com.surubedai.extrememetals.recipe;

import com.google.gson.JsonObject;
import com.surubedai.extrememetals.ExtremeMetalsMain; // あなたのメインクラス
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RecipePrimitiveReductionFurnace implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient input;

    public RecipePrimitiveReductionFurnace(ResourceLocation id, ItemStack output, Ingredient input) {
        this.id = id;
        this.output = output;
        this.input = input;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if(level.isClientSide()) return false;
        // スロット0のアイテムが、レシピの材料と一致するかチェック
        return input.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    // レシピの種類を定義する内部クラス
    public static class Type implements RecipeType<RecipePrimitiveReductionFurnace> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "primitive_reduction"; // レシピの種類名
    }

    // JSONとJavaコードを相互変換する内部クラス
    public static class Serializer implements RecipeSerializer<RecipePrimitiveReductionFurnace> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ExtremeMetalsMain.MODID, "primitive_reduction");

        @Override
        public RecipePrimitiveReductionFurnace fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
            return new RecipePrimitiveReductionFurnace(recipeId, output, input);
        }

        @Override
        public @Nullable RecipePrimitiveReductionFurnace fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            ItemStack output = buffer.readItem();
            return new RecipePrimitiveReductionFurnace(recipeId, output, input); // ネットワーク経由の生成
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RecipePrimitiveReductionFurnace recipe) {
            recipe.input.toNetwork(buffer);
            buffer.writeItem(recipe.output);
        }

    }
}
