package com.surubedai.extrememetals.recipe;

import com.google.gson.JsonObject;
import com.surubedai.extrememetals.regi.ExtremeMetalsAddRecipes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class RecipebuilderSteamAutomaticLoom implements RecipeBuilder {
    private final Item result;
    private final int count;
    private final Ingredient ingredient;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    // クラスを簡単に呼び出すためのスタティックメソッド
    public static RecipebuilderSteamAutomaticLoom create(Ingredient ingredient, Item result, int count) {
        return new RecipebuilderSteamAutomaticLoom(ingredient, result, count);
    }

    public RecipebuilderSteamAutomaticLoom(Ingredient ingredient, Item result, int count) {
        this.ingredient = ingredient;
        this.result = result;
        this.count = count;
    }

    // 進捗（レシピ解放条件）を追加するメソッド（RecipeBuilderインターフェースの要求）
    @Override
    public RecipebuilderSteamAutomaticLoom unlockedBy(String name, CriterionTriggerInstance criterion) {
        this.advancement.addCriterion(name, criterion);
        return this;
    }

    @Override
    public RecipebuilderSteamAutomaticLoom group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    // 最後にこれを呼び出して、データ生成キューに登録する
    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT)
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(RequirementsStrategy.OR);

        consumer.accept(new Result(id, this.ingredient, this.result, this.count,
                this.advancement, id.withPrefix("recipes/steam_loom/")));
    }

    // 実際にJSONを出力する内部クラス
    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final Item result;
        private final int count;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, Ingredient ingredient, Item result, int count, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.ingredient = ingredient;
            this.result = result;
            this.count = count;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        // 💡 ここで以前作成したレシピのJSON構造（ingredient と output）を定義します
        @Override
        public void serializeRecipeData(JsonObject json) {
            // 材料（ingredient）の書き込み
            json.add("ingredient", this.ingredient.toJson());

            // 完成品（output）の書き込み
            JsonObject outputJson = new JsonObject();
            outputJson.addProperty("item", net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                outputJson.addProperty("count", this.count);
            }
            json.add("output", outputJson);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            // 💡 以前登録した独自レシピのシリアライザーを指定
            return ExtremeMetalsAddRecipes.STEAM_LOOM_SERIALIZER.get();
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
