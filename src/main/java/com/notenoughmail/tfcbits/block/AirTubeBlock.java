package com.notenoughmail.tfcbits.block;

import net.dries007.tfc.client.particle.TFCParticles;
import net.dries007.tfc.common.blocks.EntityBlockExtension;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.IForgeBlockExtension;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.fluids.FluidProperty;
import net.dries007.tfc.common.fluids.IFluidLoggable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class AirTubeBlock extends Block implements IForgeBlockExtension, EntityBlockExtension, IFluidLoggable {

    private final ExtendedProperties extendedProperties;

    public static final FluidProperty FLUID = TFCBlockStateProperties.WATER;

    public static final VoxelShape SHAPE = Block.box(6.0D, 4.0D, 6.0D, 10.0D, 16.0D, 10.0D);

    public AirTubeBlock(ExtendedProperties properties) {
        super(properties.properties());
        extendedProperties = properties;
        registerDefaultState(getStateDefinition().any().setValue(FLUID, FLUID.keyFor(Fluids.EMPTY)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        return FluidHelpers.fillWithFluid(this.defaultBlockState(), level.getFluidState(clickedPos).getType());
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        boolean canProvideAir = level.getBlockState(pos.above()).isAir() && level.getFluidState(pos).getType() != Fluids.EMPTY;
        if (canProvideAir && level.getGameTime() % 10 == random.nextInt(10)) {
            double x = pos.getX() + 0.5D + ((random.nextDouble() - 0.5D) * 0.03D);
            double y = pos.getY() + ((random.nextDouble() - 0.05) * 0.015D);
            double z = pos.getZ() + 0.5D + ((random.nextDouble() - 0.5D) * 0.03D);
            double xSpeed = (random.nextDouble() - 0.5D) * 0.03D;
            double ySpeed = 0.3D + (random.nextDouble() * 0.03D);
            double zSpeed = (random.nextDouble() - 0.5D) * 0.03D;
            level.addParticle(TFCParticles.BUBBLE.get(), false, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        level.scheduleTick(pos, state.getFluidState().getType(), state.getFluidState().getType().getTickDelay(level));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FLUID);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return IFluidLoggable.super.getFluidState(state);
    }

    @Override
    public ExtendedProperties getExtendedProperties() {
        return extendedProperties;
    }

    @Override
    public FluidProperty getFluidProperty() {
        return FLUID;
    }
}