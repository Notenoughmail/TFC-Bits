package com.notenoughmail.tfcbits.util.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class BitsClientConfig {

    private static ForgeConfigSpec CONFIG_SPEC;

    public static void register() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        registerConfig(builder);
        CONFIG_SPEC = builder.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CONFIG_SPEC, "tfc-bits-client.toml");
    }

    public static ForgeConfigSpec.BooleanValue enableCalendarOverlay;
    public static ForgeConfigSpec.IntValue calendarOverlayVerticalOffset;
    public static ForgeConfigSpec.IntValue calendarOverlayHorizontalOffset;
    public static ForgeConfigSpec.IntValue calendarOverlayRedValue;
    public static ForgeConfigSpec.IntValue calendarOverlayGreenValue;
    public static ForgeConfigSpec.IntValue calendarOverlayBlueValue;
    public static ForgeConfigSpec.IntValue calendarOverlayAlphaValue;

    public static ForgeConfigSpec.BooleanValue enableClimateOverlay;
    public static ForgeConfigSpec.IntValue climateOverlayVerticalOffset;
    public static ForgeConfigSpec.IntValue climateOverlayHorizontalOffset;
    public static ForgeConfigSpec.IntValue climateOverlayRedValue;
    public static ForgeConfigSpec.IntValue climateOverlayGreenValue;
    public static ForgeConfigSpec.IntValue climateOverlayBlueValue;
    public static ForgeConfigSpec.IntValue climateOverlayAlphaValue;

    private static void registerConfig(ForgeConfigSpec.Builder builder) {
        builder.push("overlays");
        builder.push("calendar");
        enableCalendarOverlay = builder
                .comment("Determines if the calendar overlay should render")
                .define("enableCalendarOverlay", true);
        builder.push("config");
        calendarOverlayVerticalOffset = builder
                .comment("Sets the offset from the top of the screen for calendar HUD elements")
                .defineInRange("calendarOverlayVerticalOffset", 1, 0, Integer.MAX_VALUE);
        calendarOverlayHorizontalOffset = builder
                .comment("Sets the offset from the right of the screen for calendar HUD elements")
                .defineInRange("calendarOverlayHorizontalOffset", 1, 0, Integer.MAX_VALUE);
        calendarOverlayRedValue = builder
                .comment("Sets the red component of the calendar overlay")
                .defineInRange("calendarOverlayRedValue", 255, 0, 255);
        calendarOverlayGreenValue = builder
                .comment("Sets the green component of the calendar overlay")
                .defineInRange("calendarOverlayGreenValue", 255, 0, 255);
        calendarOverlayBlueValue = builder
                .comment("Sets blue component of the calendar overlay")
                .defineInRange("calendarOverlayBlueValue", 255, 0, 255);
        calendarOverlayAlphaValue = builder
                .comment("Sets the alpha component of the calendar overlay")
                .defineInRange("calendarOverlayAlphaValue", 255, 0, 255);
        builder.pop(2);
        builder.push("climate");
        enableClimateOverlay = builder
                .comment("Determines if the climate overlay should render")
                .define("enableClimateOverlay", true);
        builder.push("config");
        climateOverlayVerticalOffset = builder
                .comment("Sets the offset from the top of the screen for climate HUD elements")
                .defineInRange("climateOverlayVerticalOffset", 19, 0, Integer.MAX_VALUE);
        climateOverlayHorizontalOffset = builder
                .comment("Sets the offset from the left of the screen for climate HUD elements")
                .defineInRange("climateOverlayHorizontalOffset", 1, 0, Integer.MAX_VALUE);
        climateOverlayRedValue = builder
                .comment("Sets the red component of the climate overlay")
                .defineInRange("climateOverlayRedValue", 255, 0, 255);
        climateOverlayGreenValue = builder
                .comment("Sets the green component of the climate overlay")
                .defineInRange("climateOverlayGreenValue", 255, 0, 255);
        climateOverlayBlueValue = builder
                .comment("Sets the blue component of the climate overlay")
                .defineInRange("climateOverlayBlueValue", 255, 0, 255);
        climateOverlayAlphaValue = builder
                .comment("Sets the alpha component of the climate overlay")
                .defineInRange("climateOverlayAlphaValue", 255, 0, 255);
        builder.pop(3);
    }
}
