package com.notenoughmail.tfcbits.util.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.notenoughmail.tfcbits.BitsHelper;
import com.notenoughmail.tfcbits.util.config.BitsClientConfig;
import com.notenoughmail.tfcbits.util.mixin.accessor.BlastFurnaceBlockEntityAccessor;
import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.client.screen.BlastFurnaceScreen;
import net.dries007.tfc.client.screen.BlockEntityScreen;
import net.dries007.tfc.common.blockentities.BlastFurnaceBlockEntity;
import net.dries007.tfc.common.container.BlastFurnaceContainer;
import net.dries007.tfc.util.Helpers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = BlastFurnaceScreen.class, remap = false)
public abstract class BlastFurnaceScreenMixin extends BlockEntityScreen<BlastFurnaceBlockEntity, BlastFurnaceContainer> {

    @Inject(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;II)V", at = @At(value = "RETURN"), remap = false)
    private void renderItemHeats(PoseStack poseStack, int mouseX, int mouseY, CallbackInfo ci) {
        if (BitsClientConfig.enableBlastFurnaceTemperatureTooltip.get() && RenderHelpers.isInside(mouseX, mouseY, leftPos + 42, topPos + 22, 10, 66)) {
            List<Component> components = new ArrayList<>();
            List<ItemStack> itemList = ((BlastFurnaceBlockEntityAccessor) blockEntity).accessor$getInputStacks();
            int listLimit = BitsClientConfig.blastFurnaceTemperatureListLimit.get();
            if (listLimit == 0) {
                for (ItemStack stack : itemList) {
                    BitsHelper.addItemHeatToComponentList(stack, components);
                }
            } else {
                int k = 0;
                for (int i = 0 ; i < Math.min(listLimit, itemList.size()) ; i++) {
                    BitsHelper.addItemHeatToComponentList(itemList.get(i), components);
                    k++;
                }
                if (k < itemList.size()) {
                    components.add(Helpers.translatable("container.shulkerBox.more", itemList.size() - k).withStyle(ChatFormatting.ITALIC));
                }
            }
            renderComponentTooltip(poseStack, components, mouseX, mouseY + 20);
        }
    }

    public BlastFurnaceScreenMixin(BlastFurnaceContainer container, Inventory playerInventory, Component name, ResourceLocation texture) {
        super(container, playerInventory, name, texture);
    }
}
