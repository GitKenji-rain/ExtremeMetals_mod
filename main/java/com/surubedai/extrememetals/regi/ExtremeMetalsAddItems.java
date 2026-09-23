package com.surubedai.extrememetals.regi;

import com.surubedai.extrememetals.ExtremeMetalsMain;
import com.surubedai.extrememetals.item.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ExtremeMetalsAddItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            ForgeRegistries.ITEMS,
            ExtremeMetalsMain.MODID
    );

    public static final RegistryObject<Item> IRON_COAL_MIXTURE = ITEMS.register(
            "iron_coal_mixture",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> GOLD_COAL_MIXTURE = ITEMS.register(
            "gold_coal_mixture",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> COPPER_COAL_MIXTURE = ITEMS.register(
            "copper_coal_mixture",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> NATIVE_SILVER_COAL_MIXTURE = ITEMS.register(
            "native_silver_coal_mixture",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> CASSITERITE_COAL_MIXTURE = ITEMS.register(
            "cassiterite_coal_mixture",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> ZINC_OXIDE_COAL_MIXTURE = ITEMS.register(
            "zinc_oxide_coal_mixture",
            () -> new ItemCommonNormal(new Item.Properties()));


    public static final RegistryObject<Item> SODIUM_HYDROXIDE = ITEMS.register(
            "sodium_hydroxide",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> ZINC_OXIDE = ITEMS.register(
            "zinc_oxide",
            () -> new ItemCommonNormal(new Item.Properties()));


    public static final RegistryObject<Item> RAW_NATIVE_SILVER = ITEMS.register(
            "raw_native_silver",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> RAW_CASSITERITE = ITEMS.register(
            "raw_cassiterite",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> RAW_SPHALERITE = ITEMS.register(
            "raw_sphalerite",
            () -> new ItemCommonNormal(new Item.Properties()));


    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register(
            "silver_ingot",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> TIN_INGOT = ITEMS.register(
            "tin_ingot",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> ZINC_INGOT = ITEMS.register(
            "zinc_ingot",
            () -> new ItemCommonNormal(new Item.Properties()));


    public static final RegistryObject<Item> BRONZE_INGOT = ITEMS.register(
            "bronze_ingot",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> BRASS_INGOT = ITEMS.register(
            "brass_ingot",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> PIG_IRON_INGOT = ITEMS.register(
            "pig_iron_ingot",
            () -> new ItemCommonNormal(new Item.Properties()));


    public static final RegistryObject<Item> IRON_PLATE = ITEMS.register(
            "iron_plate",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> BRONZE_PLATE = ITEMS.register(
            "bronze_plate",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> BRASS_PLATE = ITEMS.register(
            "brass_plate",
            () -> new ItemCommonNormal(new Item.Properties()));


    public static final RegistryObject<Item> IRON_ROD = ITEMS.register(
            "iron_rod",
            () -> new ItemCommonNormal(new Item.Properties()));


    public static final RegistryObject<Item> UNSHAPED_BRASS_TURBINE_BLADES = ITEMS.register(
            "unshaped_brass_turbine_blades",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> BRASS_TURBINE_BLADES = ITEMS.register(
            "brass_turbine_blades",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> BRASS_GEAR = ITEMS.register(
            "brass_gear",
            () -> new ItemCommonNormal(new Item.Properties()));


    public static final RegistryObject<Item> DISPOSABLE_PLATE_MOLD = ITEMS.register(
            "disposable_plate_mold",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> DISPOSABLE_TURBINE_BLADES_MOLD = ITEMS.register(
            "disposable_turbine_blades_mold",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> DISPOSABLE_ROD_MOLD = ITEMS.register(
            "disposable_rod_mold",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> DISPOSABLE_GEAR_MOLD = ITEMS.register(
            "disposable_gear_mold",
            () -> new ItemCommonNormal(new Item.Properties()));

    public static final RegistryObject<Item> WEIGHT = ITEMS.register(
            "weight",
            () -> new ItemCommonNormal(new Item.Properties()));


    public static final RegistryObject<Item> STEAM_POWER_UNIT = ITEMS.register(
            "steam_power_unit",
            () -> new ItemCommonNormal(new Item.Properties()));
}
