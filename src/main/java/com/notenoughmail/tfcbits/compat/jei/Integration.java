package com.notenoughmail.tfcbits.compat.jei;

import com.notenoughmail.tfcbits.BitsHelper;
import com.notenoughmail.tfcbits.TFCBits;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.dries007.tfc.client.ClientHelpers;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.recipes.BlastFurnaceRecipe;
import net.dries007.tfc.common.recipes.TFCRecipeTypes;
import net.dries007.tfc.util.Helpers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@JeiPlugin
public final class Integration implements IModPlugin {

    public static final RecipeType<BlastFurnaceRecipe> BLAST_FURNACE = create("blast_furnace", BlastFurnaceRecipe.class);

    private static <T> RecipeType<T> create(String name, Class<T> clazz) {
        return RecipeType.create(TFCBits.ID, name, clazz);
    }

    private static <C extends Container, T extends Recipe<C>> List<T> recipes(net.minecraft.world.item.crafting.RecipeType<T> type) {
        return ClientHelpers.getLevelOrThrow().getRecipeManager().getAllRecipesFor(type);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return BitsHelper.identifier("jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        final IGuiHelper gui = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(new BlastFurnaceRecipeCategory(BLAST_FURNACE, gui));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(BLAST_FURNACE, recipes(TFCRecipeTypes.BLAST_FURNACE.get()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(TFCBlocks.BLAST_FURNACE.get()), BLAST_FURNACE);
        Helpers.getAllTagValues(TFCTags.Items.BLAST_FURNACE_FUEL, ForgeRegistries.ITEMS).forEach(item -> registration.addRecipeCatalyst(new ItemStack(item), BLAST_FURNACE));
    }
}