package com.notenoughmail.tfcbits.client;

import com.notenoughmail.tfcbits.block.BitsBlocks;
import net.dries007.tfc.common.entities.livestock.horse.TFCChestedHorse;
import net.dries007.tfc.common.entities.livestock.horse.TFCHorse;
import net.minecraft.client.player.Input;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class BitsClientEvents {

    public static void init() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(BitsClientEvents::clientSetup);
        bus.addListener(BitsClientEvents::onConfigReload);
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        BitsOverlays.reload();

        MinecraftForge.EVENT_BUS.addListener(BitsClientEvents::onMovementInputUpdated); // Blindly following https://github.com/SlimeKnights/TinkersConstruct/blob/2f0552ec8944577907acf8b7c6c6332ed2452483/src/main/java/slimeknights/tconstruct/tools/ToolClientEvents.java#L127

        BitsBlocks.SUPPORTED_LADDERS.values().forEach(ladder -> ItemBlockRenderTypes.setRenderLayer(ladder.get(), RenderType.cutout()));
    }

    public static void onConfigReload(ModConfigEvent.Reloading event) {
        BitsOverlays.reload();
    }

    public static void onMovementInputUpdated(MovementInputUpdateEvent event) {
        Input input = event.getInput();
        Player player = event.getPlayer();
        Entity riding = player.getVehicle();
        if ((riding instanceof TFCHorse || riding instanceof TFCChestedHorse) && riding.getControllingPassenger() != player && input.jumping) {
            input.jumping = false;
        }
    }
}