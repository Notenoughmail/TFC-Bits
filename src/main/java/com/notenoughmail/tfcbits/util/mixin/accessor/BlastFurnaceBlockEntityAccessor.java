package com.notenoughmail.tfcbits.util.mixin.accessor;

import net.dries007.tfc.common.blockentities.BlastFurnaceBlockEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = BlastFurnaceBlockEntity.class, remap = false)
public interface BlastFurnaceBlockEntityAccessor {

    @Accessor(value = "inputStacks", remap = false)
    List<ItemStack> accessor$getInputStacks();
}
