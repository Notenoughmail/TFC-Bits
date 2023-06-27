package com.notenoughmail.tfcbits.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.notenoughmail.tfcbits.util.config.BitsClientConfig;
import net.dries007.tfc.client.ClimateRenderCache;
import net.dries007.tfc.client.IngameOverlays;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.Month;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;

import java.awt.*;

public class BitsOverlays {

    public static final IIngameOverlay HUD_CALENDAR = OverlayRegistry.registerOverlayBelow(ForgeIngameGui.PLAYER_LIST_ELEMENT, "tfc bits calendar", BitsOverlays::renderHudCalendar);
    public static final IIngameOverlay HUD_CLIMATE = OverlayRegistry.registerOverlayBelow(ForgeIngameGui.PLAYER_LIST_ELEMENT, "tfc bits climate", BitsOverlays::renderHudClimate);

    public static void reload() {
        OverlayRegistry.enableOverlay(HUD_CALENDAR, BitsClientConfig.enableCalendarOverlay.get());
        OverlayRegistry.enableOverlay(HUD_CLIMATE, BitsClientConfig.enableClimateOverlay.get());
    }

    public static void renderHudCalendar(ForgeIngameGui gui, PoseStack stack, float partialTicks, int width, int height) {
        final Minecraft mc = Minecraft.getInstance();
        if (IngameOverlays.setup(gui, mc) && !mc.options.renderDebug) {
            Color color = new Color(
                    BitsClientConfig.calendarOverlayRedValue.get(),
                    BitsClientConfig.calendarOverlayGreenValue.get(),
                    BitsClientConfig.calendarOverlayBlueValue.get(),
                    BitsClientConfig.calendarOverlayAlphaValue.get()
            );
            // Taken from TFC's CalendarScreen class
            String seasonDay = I18n.get("tfc.tooltip.calendar_season", I18n.get(Calendars.CLIENT.getCalendarMonthOfYear().getTranslationKey(Month.Style.SEASON))) + ", " + I18n.get("tfc.tooltip.calendar_day", Calendars.CLIENT.getCalendarDayOfYear().getString());
            String date = I18n.get("tfc.tooltip.calendar_date", Calendars.CLIENT.getCalendarTimeAndDate().getString());
            final Font font = gui.getFont();
            font.draw(stack, seasonDay, BitsClientConfig.calendarOverlayHorizontalOffset.get(), BitsClientConfig.calendarOverlayVerticalOffset.get(), color.getRGB());
            font.draw(stack, date, BitsClientConfig.calendarOverlayHorizontalOffset.get(), BitsClientConfig.calendarOverlayVerticalOffset.get() + 9, color.getRGB());
        }
    }

    public static void renderHudClimate(ForgeIngameGui gui, PoseStack stack, float partialTicks, int width, int height) {
        final Minecraft mc = Minecraft.getInstance();
        if (IngameOverlays.setup(gui, mc) && !mc.options.renderDebug) {
            Color color = new Color(
                    BitsClientConfig.climateOverlayRedValue.get(),
                    BitsClientConfig.climateOverlayGreenValue.get(),
                    BitsClientConfig.climateOverlayBlueValue.get(),
                    BitsClientConfig.climateOverlayAlphaValue.get()
            );
            // Taken from TFC's ClimateScreen class
            final float averageTemp = ClimateRenderCache.INSTANCE.getAverageTemperature();
            final float currentTemp = ClimateRenderCache.INSTANCE.getTemperature();
            final float rainfall = ClimateRenderCache.INSTANCE.getRainfall();
            String temperature = I18n.get("tfc.tooltip.climate_average_temperature", String.format("%.1f", averageTemp)) + ", " + I18n.get("tfc.tooltip.climate_current_temp", String.format("%.1f", currentTemp));
            String rainString = I18n.get("tfc.tooltip.climate_annual_rainfall", String.format("%.1f", rainfall));
            final Font font = gui.getFont();
            font.draw(stack, temperature, BitsClientConfig.climateOverlayHorizontalOffset.get(), BitsClientConfig.climateOverlayVerticalOffset.get(), color.getRGB());
            font.draw(stack, rainString, BitsClientConfig.climateOverlayHorizontalOffset.get(), BitsClientConfig.climateOverlayVerticalOffset.get() + 9, color.getRGB());
        }
    }
}
