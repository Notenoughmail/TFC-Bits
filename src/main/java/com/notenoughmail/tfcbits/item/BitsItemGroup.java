package com.notenoughmail.tfcbits.item;

import com.notenoughmail.tfcbits.TFCBits;
import com.notenoughmail.tfcbits.block.BitsBlocks;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public class BitsItemGroup extends CreativeModeTab {

    public static final CreativeModeTab TAB = new BitsItemGroup("itemGroup", () -> new ItemStack(BitsBlocks.SUPPORTED_LADDERS.get(Wood.SEQUOIA).get()));

    private final Lazy<ItemStack> icon;

    public BitsItemGroup(String label, Supplier<ItemStack> icon) {
        super(TFCBits.ID + "." + label);
        this.icon = Lazy.of(icon);
    }

    @Override
    public ItemStack makeIcon() {
        return icon.get();
    }
}
