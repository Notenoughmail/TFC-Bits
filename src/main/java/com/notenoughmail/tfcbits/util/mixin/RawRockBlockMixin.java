package com.notenoughmail.tfcbits.util.mixin;

import com.notenoughmail.tfcbits.util.config.BitsServerConfig;
import com.notenoughmail.tfcbits.world.WaterTable;
import net.dries007.tfc.common.blocks.rock.RawRockBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(value = RawRockBlock.class, remap = false)
public abstract class RawRockBlockMixin {

    @Inject(method = "animateTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Ljava/util/Random;)V", at = @At(value = "RETURN"))
    private void tfc_bits$animateTick(BlockState state, Level level, BlockPos pos, Random random, CallbackInfo ci) {
        if (BitsServerConfig.blocksDripBelowWaterTable.get() && level.getBlockState(pos.below()).isAir() && random.nextInt(BitsServerConfig.belowWaterTableDripRarity.get()) == 0 && WaterTable.isBelowWaterTable(level, pos)) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() - 0.05D;
            double z = pos.getZ() + random.nextDouble();
            level.addParticle(ParticleTypes.DRIPPING_WATER, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
