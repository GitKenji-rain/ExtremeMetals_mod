package com.surubedai.extrememetals.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.surubedai.extrememetals.ExtremeMetalsMain; // あなたのメインクラス
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class RecipePrimitiveAlloyingMachine implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<CountedIngredient> recipeItems;

    public RecipePrimitiveAlloyingMachine(ResourceLocation id, ItemStack output, NonNullList<CountedIngredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }


    // 💡 1. 素材の「種類」と「個数」の両方をチェックする判定ロジック
    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide()) return false;

        // 機械の「スロット0」と「スロット1」に入っている実際のアイテムと個数
        ItemStack slot0 = container.getItem(0);
        ItemStack slot1 = container.getItem(1);

        if (recipeItems.size() < 2) return false;

        CountedIngredient ingredientA = recipeItems.get(0);
        CountedIngredient ingredientB = recipeItems.get(1);

        // パターンA：スロット0が材料A、スロット1が材料Bの場合
        boolean patternA = ingredientA.ingredient.test(slot0) && slot0.getCount() >= ingredientA.count
                && ingredientB.ingredient.test(slot1) && slot1.getCount() >= ingredientB.count;

        // パターンB：スロット0が材料B、スロット1が材料Aの場合（順不同に対応）
        boolean patternB = ingredientA.ingredient.test(slot1) && slot1.getCount() >= ingredientA.count
                && ingredientB.ingredient.test(slot0) && slot0.getCount() >= ingredientB.count;

        return patternA || patternB;
    }

    public int getRequiredCountForSlot(int slotIndex, SimpleContainer container) {
        ItemStack stack = container.getItem(slotIndex);
        if (recipeItems.size() < 2) return 1;

        CountedIngredient ingredientA = recipeItems.get(0);
        CountedIngredient ingredientB = recipeItems.get(1);

        // 指定されたスロットのアイテムがどちらの材料に一致するかで、必要な個数を返す
        if (ingredientA.ingredient.test(stack)) {
            return ingredientA.count;
        } else if (ingredientB.ingredient.test(stack)) {
            return ingredientB.count;
        }
        return 1;
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
    public static class Type implements RecipeType<RecipePrimitiveAlloyingMachine> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "primitive_alloying"; // レシピの種類名
    }


    // 💡 3. 個数に対応したシリアライザー（JSON / ネットワーク通信）
    public static class Serializer implements RecipeSerializer<RecipePrimitiveAlloyingMachine> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public RecipePrimitiveAlloyingMachine fromJson(ResourceLocation recipeId, JsonObject json) {
            // 「result」の中身を安全に1つずつ分解して読み込む
            JsonObject resultJson = GsonHelper.getAsJsonObject(json, "result");
            Item resultItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(resultJson, "item")));
            int resultCount = GsonHelper.getAsInt(resultJson, "count", 1);
            ItemStack output = new ItemStack(resultItem, resultCount);

            // 材料の配列（ingredients）を読み込む
            JsonArray ingredientsJson = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<CountedIngredient> inputs = NonNullList.withSize(2, new CountedIngredient(Ingredient.EMPTY, 0));
            for (int i = 0; i < 2; i++) {
                JsonObject obj = ingredientsJson.get(i).getAsJsonObject();
                Ingredient ingredient = Ingredient.fromJson(obj.get("ingredient"));
                int count = GsonHelper.getAsInt(obj, "count", 1);
                inputs.set(i, new CountedIngredient(ingredient, count));
            }
            return new RecipePrimitiveAlloyingMachine(recipeId, output, inputs);
        }


        // 💡 マルチプレイ（サーバーからクライアントへのデータ送信）での読み込み処理
        @Override
        public @Nullable RecipePrimitiveAlloyingMachine fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int size = buffer.readInt();
            NonNullList<CountedIngredient> inputs = NonNullList.withSize(size, new CountedIngredient(Ingredient.EMPTY, 0));
            for (int i = 0; i < size; i++) {
                Ingredient ingredient = Ingredient.fromNetwork(buffer);
                int count = buffer.readInt();
                inputs.set(i, new CountedIngredient(ingredient, count));
            }
            ItemStack output = buffer.readItem();
            return new RecipePrimitiveAlloyingMachine(recipeId, output, inputs);
        }

        // 💡 マルチプレイでの書き出し処理
        @Override
        public void toNetwork(FriendlyByteBuf buffer, RecipePrimitiveAlloyingMachine recipe) {
            buffer.writeInt(recipe.recipeItems.size());
            for (CountedIngredient countedIngredient : recipe.recipeItems) {
                countedIngredient.ingredient.toNetwork(buffer); // 材料の種類を書き込む
                buffer.writeInt(countedIngredient.count);      // 必要個数を書き込む
            }
            buffer.writeItem(recipe.output);
        }
    }

    public record CountedIngredient(Ingredient ingredient, int count) {}
}
