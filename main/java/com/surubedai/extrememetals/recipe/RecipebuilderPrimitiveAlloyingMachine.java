package com.surubedai.extrememetals.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class RecipebuilderPrimitiveAlloyingMachine implements RecipeBuilder {
    private final Ingredient input1;
    private final int input1_count; // 💡 入力1の必要個数
    private final Ingredient input2;
    private final int input2_count; // 💡 入力2の必要個数
    private final Item result;
    private final int result_count;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    // コンストラクタの引数を拡張
    public RecipebuilderPrimitiveAlloyingMachine(Ingredient input1, int input1_count, Ingredient input2, int input2_count, Item result, int result_count) {
        this.input1 = input1;
        this.input1_count = input1_count;
        this.input2 = input2;
        this.input2_count = input2_count;
        this.result = result;
        this.result_count = result_count;
    }

    // createメソッドも引数を拡張（51行目で直した部分）
    public static RecipebuilderPrimitiveAlloyingMachine create(Ingredient input1, int input1_count, Ingredient input2, int input2_count, Item result, int result_count) {
        return new RecipebuilderPrimitiveAlloyingMachine(input1, input1_count, input2, input2_count, result, result_count);
    }

    // 進捗（レシピ解放条件）を追加するメソッド（RecipeBuilderインターフェースの要求）
    @Override
    public RecipebuilderPrimitiveAlloyingMachine unlockedBy(String name, CriterionTriggerInstance criterion) {
        this.advancement.addCriterion(name, criterion);
        return this;
    }

    @Override
    public RecipebuilderPrimitiveAlloyingMachine group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }
    // Data Generatorの出力処理に登録する
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, this.input1, this.input1_count, this.input2, this.input2_count, this.result, this.result_count, this.advancement, id.withPrefix("recipes/primitive_alloying/")));
    }

    // JSONの形に変換する内部クラス
    private static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient input1;
        private final int input1_count;
        private final Ingredient input2;
        private final int input2_count;
        private final Item result;
        private final int result_count;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, Ingredient input1, int input1_count, Ingredient input2, int input2_count, Item result, int result_count, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.input1 = input1;
            this.input1_count = input1_count;
            this.input2_count = input2_count;
            this.input2 = input2;
            this.result = result;
            this.result_count = result_count;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonArray ingredients = new JsonArray();

            // 💡 入力1を「item」と「count」のセットにしてオブジェクトとして追加
            JsonObject ing1Obj = new JsonObject();
            ing1Obj.add("ingredient", input1.toJson());
            ing1Obj.addProperty("count", this.input1_count);
            ingredients.add(ing1Obj);

            // 💡 入力2も同様にオブジェクトとして追加
            JsonObject ing2Obj = new JsonObject();
            ing2Obj.add("ingredient", input2.toJson());
            ing2Obj.addProperty("count", this.input2_count);
            ingredients.add(ing2Obj);

            json.add("ingredients", ingredients);

            // 完成品（ここはそのまま）
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
            resultJson.addProperty("count", this.result_count);
            json.add("result", resultJson);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            // 💡 あなたが登録した合金レシピの「Serializer」を返します
            // 例: ExtremeMetalsAddRecipes.ALLOY_SMELTING_SERIALIZER.get()
            return ForgeRegistries.RECIPE_SERIALIZERS.getValue(new ResourceLocation("extrememetals", "primitive_alloying"));
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}

