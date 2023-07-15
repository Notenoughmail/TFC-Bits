package com.notenoughmail.tfcbits.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.notenoughmail.tfcbits.util.mixin.accessor.BlastFurnaceRecipeAccessor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.recipes.BlastFurnaceRecipe;
import net.dries007.tfc.compat.jei.JEIIntegration;
import net.dries007.tfc.compat.jei.category.BaseRecipeCategory;
import net.dries007.tfc.util.Helpers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

// Mostly taken from BloomeryRecipeCategory
public class BlastFurnaceRecipeCategory extends BaseRecipeCategory<BlastFurnaceRecipe> {

    public BlastFurnaceRecipeCategory(RecipeType<BlastFurnaceRecipe> type, IGuiHelper helper) {
        super(type, helper, helper.createBlankDrawable(98, 26), new ItemStack(TFCBlocks.BLAST_FURNACE.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BlastFurnaceRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 5)
                .addItemStacks(Arrays.asList(recipe.getCatalyst().getItems()))
                .setBackground(slot, -1, -1);

        builder.addSlot(RecipeIngredientRole.INPUT, 26, 5)
                .addIngredients(JEIIntegration.FLUID_STACK, collapse(recipe.getInputFluid()))
                .setFluidRenderer(1, false, 16, 16)
                .setBackground(slot, -1, -1);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 5)
                .addIngredient(JEIIntegration.FLUID_STACK, ((BlastFurnaceRecipeAccessor) recipe).accessor$getOutputFluid())
                .setFluidRenderer(1, false, 16, 16)
                .setBackground(slot, -1, -1);
    }

    @Override
    public void draw(BlastFurnaceRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        arrow.draw(stack, 48, 5);
        arrowAnimated.draw(stack, 48, 5);
    }

    @Override
    public Component getTitle() {
        return Helpers.translatable("tfcbits.jei.blast_furnace");
    }
}
