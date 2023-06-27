package com.notenoughmail.tfcbits.util.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dries007.tfc.client.IngameOverlays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = IngameOverlays.class, remap = false)
public abstract class IngameOverlaysMixin {

    // Mixing into something you wrote feels... strange
    @Inject(method = "renderJumpMeter(Lnet/minecraftforge/client/gui/ForgeIngameGui;Lcom/mojang/blaze3d/vertex/PoseStack;FII)V", at = @At("HEAD"), cancellable = true, remap = false)
    private static void tfcbits$renderJumpMeter(ForgeIngameGui gui, PoseStack stack, float partialTicks, int width, int height, CallbackInfo ci) {
        LocalPlayer play = Minecraft.getInstance().player;
        if (play != null && play.isRidingJumpable() && play.getVehicle() != null && play.getVehicle().getControllingPassenger() != play) {
            ci.cancel(); // Should return before anything is rendered if not the controlling passenger
        }
    }
}
