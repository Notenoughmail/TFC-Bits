package com.notenoughmail.tfcbits.block;

import com.notenoughmail.tfcbits.TFCBits;
import com.notenoughmail.tfcbits.block.entity.AirTubeBlockEntity;
import com.notenoughmail.tfcbits.block.entity.BitsBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.Helpers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class BitsBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TFCBits.ID);

    public static final Map<Wood, RegistryObject<Block>> SUPPORTED_LADDERS = Helpers.mapOfKeys(Wood.class, wood ->
            register("wood/supported_ladder/" + wood.name(), () -> new SupportedLadderBlock(ExtendedProperties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER).noOcclusion().flammableLikePlanks()))
    );

    public static final RegistryObject<Block> ROPE = register("rope", () -> new RopeBlock(ExtendedProperties.of(Material.WOOL).strength(0.2F, 0.0F).sound(SoundType.WOOL).flammableLikeLeaves()));

    public static final RegistryObject<Block> AIR_TUBE = register("air_tube", () -> new AirTubeBlock(ExtendedProperties.of(Material.BAMBOO).sound(SoundType.BAMBOO).blockEntity(BitsBlockEntities.AIR_TUBE).serverTicks(AirTubeBlockEntity::serverTick)));

    private static <T extends Block>RegistryObject<T> register(String name, Supplier<T> block) {
        return BLOCKS.register(name.toLowerCase(Locale.ROOT), block);
    }
}
