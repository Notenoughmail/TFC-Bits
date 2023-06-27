package com.notenoughmail.tfcbits.block.entity;

import com.notenoughmail.tfcbits.TFCBits;
import com.notenoughmail.tfcbits.block.BitsBlocks;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BitsBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, TFCBits.ID);

    public static final RegistryObject<BlockEntityType<AirTubeBlockEntity>> AIR_TUBE = register("air_tube", AirTubeBlockEntity::new, BitsBlocks.AIR_TUBE);

    private static <T extends BlockEntity>RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Supplier<? extends Block> block) {
        return RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, block);
    }
}
