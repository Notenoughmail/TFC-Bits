package com.notenoughmail.tfcbits.util.mixin;

import com.notenoughmail.tfcbits.util.config.BitsServerConfig;
import com.notenoughmail.tfcbits.world.WaterTable;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(value = RockSpikeBlock.class, remap = false)
public abstract class RockSpikeBlockMixin extends Block {

    @Final
    @Shadow(remap = false)
    public static EnumProperty<RockSpikeBlock.Part> PART;

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        if (BitsServerConfig.blocksDripBelowWaterTable.get() && level.getBlockState(pos.below()).isAir() && random.nextInt(BitsServerConfig.belowWaterTableDripRarity.get()) == 0 && WaterTable.isBelowWaterTable(level, pos)) {
            double x = pos.getX() + switch (state.getValue(PART)) {
                case BASE -> 0.125D + random.nextDouble(0.75D);
                case MIDDLE -> 0.25D + random.nextDouble(0.5D);
                case TIP -> 0.375D + random.nextDouble(0.25D);
            };
            double y = pos.getY() - 0.05D;
            double z = pos.getZ() + switch (state.getValue(PART)) {
                case BASE -> 0.125D + random.nextDouble(0.75D);
                case MIDDLE -> 0.25D + random.nextDouble(0.5D);
                case TIP -> 0.375D + random.nextDouble(0.25D);
            };
            level.addParticle(ParticleTypes.DRIPPING_WATER, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    public RockSpikeBlockMixin(Properties pProperties) {
        super(pProperties);
    }
}
