package com.notenoughmail.tfcbits.item;

import com.notenoughmail.tfcbits.TFCBits;
import com.notenoughmail.tfcbits.block.BitsBlocks;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.Helpers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class BitsItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TFCBits.ID);

    public static final Map<Wood, RegistryObject<Item>> SUPPORTED_LADDERS = Helpers.mapOfKeys(Wood.class, wood ->
            register("wood/supported_ladder/" + wood.name(), () -> new BlockItem(BitsBlocks.SUPPORTED_LADDERS.get(wood).get(), defaultProperties()))
    );

    public static final RegistryObject<Item> ROPE = register("rope", () -> new BlockItem(BitsBlocks.ROPE.get(), defaultProperties()));

    public static final RegistryObject<Item> AIR_TUBE = register("air_tube", () -> new AirTubeBlockItem(BitsBlocks.AIR_TUBE.get(), defaultProperties()));

    private static Item.Properties defaultProperties() {
        return new Item.Properties().tab(BitsItemGroup.TAB);
    }

    private static <T extends Item> RegistryObject<Item> register(String name, Supplier<T> item) {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}
