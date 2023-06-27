package com.notenoughmail.tfcbits.util.mixin.accessor;

import net.dries007.tfc.common.recipes.BlastFurnaceRecipe;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = BlastFurnaceRecipe.class, remap = false)
public interface BlastFurnaceRecipeAccessor {

    @Accessor(value = "outputFluid", remap = false)
    FluidStack accessor$getOutputFluid();
}
