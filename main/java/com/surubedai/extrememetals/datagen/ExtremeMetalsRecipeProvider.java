package com.surubedai.extrememetals.datagen;

import com.surubedai.extrememetals.ExtremeMetalsMain;
import com.surubedai.extrememetals.recipe.RecipebuilderPrimitiveAlloyingMachine;
import com.surubedai.extrememetals.recipe.RecipebuilderPrimitiveReductionFurnace;
import com.surubedai.extrememetals.recipe.RecipebuilderSteamLathe;
import com.surubedai.extrememetals.recipe.RecipebuilderSteamRollingMill;
import com.surubedai.extrememetals.regi.ExtremeMetalsAddBlocks;
import com.surubedai.extrememetals.regi.ExtremeMetalsAddItems;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class ExtremeMetalsRecipeProvider extends RecipeProvider {

    public ExtremeMetalsRecipeProvider(PackOutput output) {
        super(output);
    }

    //tags
    //ingots
    TagKey<Item> tinIngotTag = ItemTags.create(new ResourceLocation("forge", "ingots/tin"));
    TagKey<Item> zincIngotTag = ItemTags.create(new ResourceLocation("forge", "ingots/zinc"));
    TagKey<Item> silverIngotTag = ItemTags.create(new ResourceLocation("forge", "ingots/silver"));
    TagKey<Item> brassIngotTag = ItemTags.create(new ResourceLocation("forge", "ingots/brass"));
    TagKey<Item> bronzeIngotTag = ItemTags.create(new ResourceLocation("forge", "ingots/bronze"));

    //plates
    TagKey<Item> ironPlateTag = ItemTags.create(new ResourceLocation("forge", "plates/iron"));
    TagKey<Item> brassPlateTag = ItemTags.create(new ResourceLocation("forge", "plates/brass"));
    TagKey<Item> bronzePlateTag = ItemTags.create(new ResourceLocation("forge", "plates/bronze"));

    //raw materials
    TagKey<Item> native_sliverRaw_materialTag = ItemTags.create(new ResourceLocation("forge", "raw_materials/native_silver"));
    TagKey<Item> cassiteriteRaw_materialTag = ItemTags.create(new ResourceLocation("forge", "raw_materials/cassiterite"));
    TagKey<Item> sphaleriteRaw_materialTag = ItemTags.create(new ResourceLocation("forge", "raw_materials/sphalerite"));

    //rods
    TagKey<Item> ironRodTag = ItemTags.create(new ResourceLocation("forge", "rods/iron"));

    //turbine_blades
    TagKey<Item> brassTurbine_bladesTag = ItemTags.create(new ResourceLocation("forge", "turbine_blades/brass"));

    //gears
    TagKey<Item> brassGearTag = ItemTags.create(new ResourceLocation("forge", "gears/brass"));

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        // smelting
        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(native_sliverRaw_materialTag),
                RecipeCategory.MISC,
                ExtremeMetalsAddItems.SILVER_INGOT.get(),
                0.1F,
                200
            )
            .unlockedBy("has_raw_nativesilver", has(ExtremeMetalsAddItems.RAW_NATIVE_SILVER.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "smelting/raw_nativesilver")
        );

        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(sphaleriteRaw_materialTag),
                RecipeCategory.MISC,
                ExtremeMetalsAddItems.ZINC_OXIDE.get(),
                0.1F,
                200
            )
            .unlockedBy("has_raw_sphalerite", has(ExtremeMetalsAddItems.RAW_SPHALERITE.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "smelting/raw_sphalerite")
        );

        // primitive reduction furnace
        RecipebuilderPrimitiveReductionFurnace.create(
                Ingredient.of(ExtremeMetalsAddItems.COPPER_COAL_MIXTURE.get()),
                Items.COPPER_INGOT,
                8
            )
            .unlockedBy("has_raw_copper", has(Items.RAW_COPPER))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "reduction/primitive/copper")
        );

        RecipebuilderPrimitiveReductionFurnace.create(
                Ingredient.of(ExtremeMetalsAddItems.IRON_COAL_MIXTURE.get()),
                Items.IRON_INGOT,
                8
            )
            .unlockedBy("has_raw_iron", has(Items.RAW_IRON))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "reduction/primitive/iron")
        );

        RecipebuilderPrimitiveReductionFurnace.create(
                Ingredient.of(ExtremeMetalsAddItems.GOLD_COAL_MIXTURE.get()),
                Items.GOLD_INGOT,
                8
            )
            .unlockedBy("has_raw_gold", has(Items.RAW_GOLD))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "reduction/primitive/gold")
        );

        RecipebuilderPrimitiveReductionFurnace.create(
                Ingredient.of(ExtremeMetalsAddItems.NATIVE_SILVER_COAL_MIXTURE.get()),
                ExtremeMetalsAddItems.SILVER_INGOT.get(),
                8
            )
            .unlockedBy("has_raw_nativesilver", has(ExtremeMetalsAddItems.RAW_NATIVE_SILVER.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "reduction/primitive/nativesilver")
        );

        RecipebuilderPrimitiveReductionFurnace.create(
                Ingredient.of(ExtremeMetalsAddItems.CASSITERITE_COAL_MIXTURE.get()),
                ExtremeMetalsAddItems.TIN_INGOT.get(),
                8
            )
            .unlockedBy("has_raw_cassiterite", has(ExtremeMetalsAddItems.RAW_CASSITERITE.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "reduction/primitive/cassiterite")
        );

        RecipebuilderPrimitiveReductionFurnace.create(
                Ingredient.of(ExtremeMetalsAddItems.ZINC_OXIDE_COAL_MIXTURE.get()),
                ExtremeMetalsAddItems.ZINC_INGOT.get(),
                8
            )
            .unlockedBy("has_zinc_oxide", has(ExtremeMetalsAddItems.ZINC_OXIDE.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "reduction/primitive/zinc_oxide")
        );

        //shapeless crafting
        //coal mixture
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ExtremeMetalsAddItems.IRON_COAL_MIXTURE.get() , 1)
            .requires(Items.COAL)
            .requires(Ingredient.of(Tags.Items.RAW_MATERIALS_IRON), 8)
            .unlockedBy("has_raw_iron", has(Items.RAW_IRON))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shapeless/iron_coal_mixture")
        );

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ExtremeMetalsAddItems.COPPER_COAL_MIXTURE.get() , 1)
            .requires(Items.COAL)
            .requires(Ingredient.of(Tags.Items.RAW_MATERIALS_COPPER), 8)
            .unlockedBy("has_raw_copper", has(Items.RAW_COPPER))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shapeless/copper_coal_mixture")
        );

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ExtremeMetalsAddItems.GOLD_COAL_MIXTURE.get() , 1)
            .requires(Items.COAL)
            .requires(Ingredient.of(Tags.Items.RAW_MATERIALS_GOLD), 8)
            .unlockedBy("has_raw_gold", has(Items.RAW_GOLD))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shapeless/gold_coal_mixture")
        );

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ExtremeMetalsAddItems.NATIVE_SILVER_COAL_MIXTURE.get() , 1)
            .requires(Items.COAL)
            .requires(Ingredient.of(native_sliverRaw_materialTag), 8)
            .unlockedBy("has_raw_nativesilver", has(ExtremeMetalsAddItems.RAW_NATIVE_SILVER.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shapeless/nativesilver_coal_mixture")
        );

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ExtremeMetalsAddItems.CASSITERITE_COAL_MIXTURE.get() , 1)
            .requires(Items.COAL)
            .requires(Ingredient.of(cassiteriteRaw_materialTag), 8)
            .unlockedBy("has_raw_cassiterite", has(ExtremeMetalsAddItems.RAW_CASSITERITE.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shapeless/cassiterite_coal_mixture")
        );

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ExtremeMetalsAddItems.ZINC_OXIDE_COAL_MIXTURE.get() , 1)
            .requires(Items.COAL)
            .requires(ExtremeMetalsAddItems.ZINC_OXIDE.get(), 8)
            .unlockedBy("has_zinc_oxide", has(ExtremeMetalsAddItems.ZINC_OXIDE.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shapeless/zinc_oxide_coal_mixture")
        );

        //shaped crafting (unshaped turbine blades)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExtremeMetalsAddItems.UNSHAPED_BRASS_TURBINE_BLADES.get(),1) // 完成品
            .pattern(" P ")
            .pattern("PPP")
            .pattern(" P ")
            .define('P', Ingredient.of(brassPlateTag))
            .unlockedBy("has_brass", has(ExtremeMetalsAddItems.BRASS_INGOT.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shaped/unshaped_turbine_blades/brass")
        );

        //shaped crafting (primitive machines)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExtremeMetalsAddBlocks.BlockItems.PRIMITIVE_REDUCTION_FURNACE.get(),1) // 完成品
            .pattern("PSP")
            .pattern("SBS")
            .pattern("SFS")
            .define('S', Items.STONE_BRICKS)
            .define('P', Ingredient.of(ironPlateTag))
            .define('B', Items.BLAST_FURNACE)
            .define('F', Items.FURNACE)
            .unlockedBy("has_iron", has(Items.IRON_INGOT))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shaped/primitive/reduction_furnace")
        );

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExtremeMetalsAddBlocks.BlockItems.PRIMITIVE_ALLOYING_MACHINE.get(),1) // 完成品
            .pattern("SSS")
            .pattern("BPB")
            .pattern("PFP")
            .define('S', Items.STONE_BRICKS)
            .define('P', Ingredient.of(ironPlateTag))
            .define('B', Items.BLAST_FURNACE)
            .define('F', Items.FURNACE)
            .unlockedBy("has_iron", has(Items.IRON_INGOT))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shaped/primitive/alloying_machine")
        );

        //shaped crafting (steam machines)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExtremeMetalsAddBlocks.BlockItems.STEAM_ROLLING_MILL.get(),1) // 完成品
            .pattern("PGI")
            .pattern("PGB")
            .pattern("BBB")
            .define('I', Ingredient.of(ironRodTag))
            .define('P', ExtremeMetalsAddItems.STEAM_POWER_UNIT.get())
            .define('B', Ingredient.of(bronzePlateTag))
            .define('G', Ingredient.of(brassGearTag))
            .unlockedBy("has_steam_power_unit", has(ExtremeMetalsAddItems.STEAM_POWER_UNIT.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shaped/steam/rolling_mill")
        );

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExtremeMetalsAddBlocks.BlockItems.STEAM_LATHE.get(),1) // 完成品
            .pattern("BBB")
            .pattern("PGI")
            .pattern("PGS")
            .define('I', Ingredient.of(ironRodTag))
            .define('P', ExtremeMetalsAddItems.STEAM_POWER_UNIT.get())
            .define('B', Ingredient.of(bronzePlateTag))
            .define('G', Ingredient.of(brassGearTag))
            .define('S', Items.GRINDSTONE)
            .unlockedBy("has_steam_power_unit", has(ExtremeMetalsAddItems.STEAM_POWER_UNIT.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shaped/steam/lathe")
        );

        // shaped crafting (weight and mold)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExtremeMetalsAddItems.WEIGHT.get(),4) // 完成品
            .pattern("   ")
            .pattern(" I ")
            .pattern("IBI")
            .define('B', Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD))
            .define('I', Ingredient.of(Tags.Items.INGOTS_IRON))
            .unlockedBy("has_iron", has(Items.IRON_INGOT))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shaped/weight")
        );

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExtremeMetalsAddItems.DISPOSABLE_PLATE_MOLD.get(),4)
            .pattern("BBB")
            .pattern("B B")
            .pattern("BBB")
            .define('B', Ingredient.of(Tags.Items.INGOTS_BRICK))
            .unlockedBy("has_bricks", has(Items.BRICKS))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shaped/disposable_plate_mold")
        );

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExtremeMetalsAddItems.DISPOSABLE_TURBINE_BLADES_MOLD.get(),4)
            .pattern("BPB")
            .pattern("PPP")
            .pattern("BPB")
            .define('B', Ingredient.of(Tags.Items.INGOTS_BRICK))
            .define('P', ExtremeMetalsAddItems.DISPOSABLE_PLATE_MOLD.get())
            .unlockedBy("has_bricks", has(Items.BRICKS))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shaped/disposable_turbine_blades_mold")
        );

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExtremeMetalsAddItems.DISPOSABLE_ROD_MOLD.get(),4)
            .pattern("B B")
            .pattern("BBB")
            .pattern("BBB")
            .define('B', Ingredient.of(Tags.Items.INGOTS_BRICK))
            .unlockedBy("has_bricks", has(Items.BRICKS))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shaped/disposable_rod_mold")
        );

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExtremeMetalsAddItems.DISPOSABLE_GEAR_MOLD.get(),4)
            .pattern("BBB")
            .pattern("BPB")
            .pattern("BBB")
            .define('B', Ingredient.of(Tags.Items.INGOTS_BRICK))
            .define('P', ExtremeMetalsAddItems.DISPOSABLE_PLATE_MOLD.get())
            .unlockedBy("has_bricks", has(Items.BRICKS))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shaped/disposable_gear_mold")
        );


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExtremeMetalsAddItems.STEAM_POWER_UNIT.get(),1)
            .pattern("PTP")
            .pattern("PRP")
            .pattern("PTP")
            .define('T', Ingredient.of(brassTurbine_bladesTag))
            .define('P', Ingredient.of(bronzePlateTag))
            .define('R', Ingredient.of(ironRodTag))
            .unlockedBy("has_bronze", has(ExtremeMetalsAddItems.BRONZE_INGOT.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "shaped/steam_power_unit")
        );

        // primitive alloyig machine
        RecipebuilderPrimitiveAlloyingMachine.create(
                Ingredient.of(Tags.Items.INGOTS_COPPER), 3,
                Ingredient.of(tinIngotTag), 1,
                ExtremeMetalsAddItems.BRONZE_INGOT.get(), 4
            )
            .unlockedBy("has_tin", has(ExtremeMetalsAddItems.TIN_INGOT.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "alloying/primitive/bronze")
        );

        RecipebuilderPrimitiveAlloyingMachine.create(
                Ingredient.of(Tags.Items.INGOTS_COPPER), 2,
                Ingredient.of(zincIngotTag), 1,
                ExtremeMetalsAddItems.BRASS_INGOT.get(), 3
            )
            .unlockedBy("has_zinc", has(ExtremeMetalsAddItems.ZINC_INGOT.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "alloying/primitive/brass")
        );

        // steam rolling mill
        RecipebuilderSteamRollingMill.create(
            Ingredient.of(bronzeIngotTag),
            ExtremeMetalsAddItems.BRONZE_PLATE.get(),
            1
        )
            .unlockedBy("has_bronze", has(ExtremeMetalsAddItems.BRONZE_INGOT.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "rollingmill/steam/bronze_ingot")
        );

        RecipebuilderSteamRollingMill.create(
            Ingredient.of(brassIngotTag),
            ExtremeMetalsAddItems.BRASS_PLATE.get(),
            1
        )
            .unlockedBy("has_brass", has(ExtremeMetalsAddItems.BRASS_INGOT.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "rollingmill/steam/brass_ingot")
        );

        RecipebuilderSteamRollingMill.create(
            Ingredient.of(Tags.Items.INGOTS_IRON),
            ExtremeMetalsAddItems.IRON_PLATE.get(),
            1
        )
            .unlockedBy("has_iron", has(Items.IRON_INGOT))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "rollingmill/steam/iron_ingot")
        );

        //steam lathe
        RecipebuilderSteamLathe.create(
            Ingredient.of(Tags.Items.INGOTS_IRON),
            ExtremeMetalsAddItems.IRON_ROD.get(),
            1
        )
            .unlockedBy("has_iron", has(Items.IRON_INGOT))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "lathe/steam/iron_ingot")
        );

        //smithing
        SmithingTransformRecipeBuilder.smithing(
            Ingredient.of(ExtremeMetalsAddItems.DISPOSABLE_PLATE_MOLD.get()),
            Ingredient.of(Tags.Items.INGOTS_IRON),
            Ingredient.of(ExtremeMetalsAddItems.WEIGHT.get()),
            RecipeCategory.MISC,
            ExtremeMetalsAddItems.IRON_PLATE.get()
        )
            .unlocks("has_plate_mold", has(ExtremeMetalsAddItems.DISPOSABLE_PLATE_MOLD.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "smithing/plate/iron")
        );

        SmithingTransformRecipeBuilder.smithing(
            Ingredient.of(ExtremeMetalsAddItems.DISPOSABLE_PLATE_MOLD.get()),
            Ingredient.of(bronzeIngotTag),
            Ingredient.of(ExtremeMetalsAddItems.WEIGHT.get()),
            RecipeCategory.MISC,
            ExtremeMetalsAddItems.BRONZE_PLATE.get()
        )
            .unlocks("has_plate_mold", has(ExtremeMetalsAddItems.DISPOSABLE_PLATE_MOLD.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "smithing/plate/bronze")
        );

        SmithingTransformRecipeBuilder.smithing(
            Ingredient.of(ExtremeMetalsAddItems.DISPOSABLE_PLATE_MOLD.get()),
            Ingredient.of(brassIngotTag),
            Ingredient.of(ExtremeMetalsAddItems.WEIGHT.get()),
            RecipeCategory.MISC,
            ExtremeMetalsAddItems.BRASS_PLATE.get()
        )
            .unlocks("has_plate_mold", has(ExtremeMetalsAddItems.DISPOSABLE_PLATE_MOLD.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "smithing/plate/brass")
        );

        SmithingTransformRecipeBuilder.smithing(
            Ingredient.of(ExtremeMetalsAddItems.DISPOSABLE_TURBINE_BLADES_MOLD.get()),
            Ingredient.of(ExtremeMetalsAddItems.UNSHAPED_BRASS_TURBINE_BLADES.get()),
            Ingredient.of(ExtremeMetalsAddItems.WEIGHT.get()),
            RecipeCategory.MISC,
            ExtremeMetalsAddItems.BRASS_TURBINE_BLADES.get()
        )
            .unlocks("has_turbine_blades_mold", has(ExtremeMetalsAddItems.DISPOSABLE_TURBINE_BLADES_MOLD.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "smithing/turbine_blades/brass")
        );

        SmithingTransformRecipeBuilder.smithing(
            Ingredient.of(ExtremeMetalsAddItems.DISPOSABLE_ROD_MOLD.get()),
            Ingredient.of(Tags.Items.INGOTS_IRON),
            Ingredient.of(ExtremeMetalsAddItems.WEIGHT.get()),
            RecipeCategory.MISC,
            ExtremeMetalsAddItems.IRON_ROD.get()
        )
            .unlocks("has_rod_mold", has(ExtremeMetalsAddItems.DISPOSABLE_ROD_MOLD.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "smithing/rod/iron")
        );

        SmithingTransformRecipeBuilder.smithing(
            Ingredient.of(ExtremeMetalsAddItems.DISPOSABLE_GEAR_MOLD.get()),
            Ingredient.of(brassPlateTag),
            Ingredient.of(ExtremeMetalsAddItems.WEIGHT.get()),
            RecipeCategory.MISC,
            ExtremeMetalsAddItems.BRASS_GEAR.get()
        )
            .unlocks("has_gear_mold", has(ExtremeMetalsAddItems.DISPOSABLE_GEAR_MOLD.get()))
            .save(consumer, new ResourceLocation(ExtremeMetalsMain.MODID, "smithing/gear/brass")
        );
    }
}
