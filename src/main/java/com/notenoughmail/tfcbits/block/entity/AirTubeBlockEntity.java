package com.notenoughmail.tfcbits.block.entity;

import com.notenoughmail.tfcbits.BitsHelper;
import net.dries007.tfc.common.blockentities.TFCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class AirTubeBlockEntity extends TFCBlockEntity {

    protected AirTubeBlockEntity(BlockPos pos, BlockState state) {
        super(BitsBlockEntities.AIR_TUBE.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AirTubeBlockEntity airTube) {
        boolean canProvideAir = level.getBlockState(pos.above()).isAir() && level.getFluidState(pos).getType() != Fluids.EMPTY;
        if (canProvideAir && level.getGameTime() % 20 == 10) {
            AABB range = new AABB(airTube.worldPosition.offset(-5, -6, -5), airTube.worldPosition.offset(5, -1, 5));
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, range);
            for (LivingEntity entity : entities) {
                if (BitsHelper.entityCanSeeBlock(level, entity, pos, 0.0D, -0.25D, 0.0D) && entity.isAffectedByPotions() && entity.isEyeInFluid(FluidTags.WATER)) {
                    entity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 21, 0, true, false, false));
                }
            }
        }
        airTube.setChanged();
    }
}