package com.notenoughmail.tfcbits.util.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class BitsServerConfig {

    private static ForgeConfigSpec CONFIG_SPEC;

    public static void register() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        registerConfig(builder);
        CONFIG_SPEC = builder.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CONFIG_SPEC, "tfc-bits-server.toml");
    }

    public static ForgeConfigSpec.BooleanValue largeHorsesCanHoldMultiplePlayers;
    public static ForgeConfigSpec.IntValue largeHorseMultipleRidersSize;
    public static ForgeConfigSpec.BooleanValue largeChestedHorsesCanHoldMultipleWithChest;
    public static ForgeConfigSpec.IntValue largeChestedHorseMultipleRidersSize;

    public static ForgeConfigSpec.BooleanValue blocksDripBelowWaterTable;
    public static ForgeConfigSpec.IntValue belowWaterTableDripRarity;

    private static void registerConfig(ForgeConfigSpec.Builder builder) {
        builder.push("horse.modifications");
        largeHorsesCanHoldMultiplePlayers = builder
                .comment("If true, horses above a certain size may have two passengers")
                .define("largeHorsesCanHoldMultiplePlayers", true);
        largeHorseMultipleRidersSize = builder
                .comment("Sets the minimum genetic size a horse needs to be in order to allow a second rider")
                .defineInRange("largeHorseMultipleRidersSize", 24, 0, 32);
        largeChestedHorsesCanHoldMultipleWithChest = builder
                .comment("If true chested horse will not prevent additional players from riding if they have a chest or barrel")
                .define("largeChestedHorsesCanHoldMultipleWithChest", false);
        largeChestedHorseMultipleRidersSize = builder
                .comment("Sets the minimum genetic size a donkey or mule needs to be in order to allow a second rider")
                .defineInRange("largeChestedHorseMultipleRidersSize", 28, 0, 32);
        builder.pop(2).push("world.watertable");
        blocksDripBelowWaterTable = builder
                .comment("Determines if rock blocks should drip water when below the local water table level")
                .define("blocksDripBelowWaterTable", true);
        belowWaterTableDripRarity = builder
                .comment("Determines how often a block below the water table should drip. A higher value means a higher rarity")
                .defineInRange("belowWaterTableDripRarity", 100, 30, Integer.MAX_VALUE);
        builder.pop(2);
    }
}
