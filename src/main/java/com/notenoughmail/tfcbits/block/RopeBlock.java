package com.notenoughmail.tfcbits.block;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.IForgeBlockExtension;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.fluids.FluidProperty;
import net.dries007.tfc.common.fluids.IFluidLoggable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RopeBlock extends Block implements IForgeBlockExtension, IFluidLoggable {

    public static final FluidProperty FLUID = TFCBlockStateProperties.WATER;

    private final ExtendedProperties extendedProperties;

    public static final VoxelShape SHAPE = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);

    public RopeBlock(ExtendedProperties properties) {
        super(properties.properties());
        extendedProperties = properties;
        registerDefaultState(getStateDefinition().any().setValue(getFluidProperty(), getFluidProperty().keyFor(Fluids.EMPTY)));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() == this.asItem()) {
            if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND && state.getBlock() == this) {
                for (int dist = 1 ; dist < 24 ; dist++) {
                    BlockPos distPos = pos.below(dist);
                    BlockState distState = level.getBlockState(distPos);
                    if (distState.getBlock() != this) {
                        if (distState.getMaterial().isReplaceable()) {
                            BlockState placeState = FluidHelpers.fillWithFluid(this.defaultBlockState(), level.getFluidState(distPos).getType());
                            if (placeState == null) {
                                return InteractionResult.PASS; // This makes it not place if there is flowing water in the way, maybe change that?
                            }
                            level.setBlockAndUpdate(distPos, placeState);
                            if (!player.isCreative()) {
                                stack.shrink(1);
                            }
                            level.playSound(null, distPos, soundType.getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.PASS;
                    }
                }
            }
            return InteractionResult.PASS;
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.above()).getBlock() == this || level.getBlockState(pos.above()).isFaceSturdy(level, pos.above(), Direction.DOWN, SupportType.CENTER);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!state.canSurvive(level, pos)) {
            dropResources(state, level, pos, null);
            level.removeBlock(pos, false);
            level.playSound(null, pos, soundType.getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        level.scheduleTick(pos, state.getFluidState().getType(), state.getFluidState().getType().getTickDelay(level));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        return FluidHelpers.fillWithFluid(this.defaultBlockState(), level.getFluidState(clickedPos).getType());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FLUID);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return IFluidLoggable.super.getFluidState(state);
    }

    @Override
    public FluidProperty getFluidProperty() {
        return FLUID;
    }

    @Override
    public ExtendedProperties getExtendedProperties() {
        return extendedProperties;
    }
}
