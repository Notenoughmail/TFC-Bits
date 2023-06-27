package com.notenoughmail.tfcbits.block;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.IForgeBlockExtension;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.fluids.FluidProperty;
import net.dries007.tfc.common.fluids.IFluidLoggable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SupportedLadderBlock extends Block implements IForgeBlockExtension, IFluidLoggable {

    private final ExtendedProperties extendedProperties;
    public static final FluidProperty FLUID = TFCBlockStateProperties.WATER;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    // Please give us a method to define if a face is solid or not! It used to be three boxes per, but then water didn't flow :(
    private static final VoxelShape POSTS = Shapes.or(
            Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 2.0D),
            Block.box(14.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 14.0D, 2.0D, 16.0D, 16.0D),
            Block.box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D)
    );
    private static final VoxelShape EW_SIDE_BARS = Shapes.or(
            Block.box(0.0D, 3.0D, 2.0D, 2.0D, 5.0D, 14.0D),
            Block.box(0.0D, 11.0D, 2.0D, 2.0D, 13.0D, 14.0D),
            Block.box(14.0D, 3.0D, 2.0D, 16.0D, 5.0D, 14.0D),
            Block.box(14.0D, 11.0D, 2.0D, 16.0D, 13.0D, 14.0D)
    );
    private static final VoxelShape NS_SIDE_BARS = Shapes.or(
            Block.box(2.0D, 3.0D, 0.0D, 14.0D, 5.0D, 2.0D),
            Block.box(2.0D, 11.0D, 0.0D, 14.0D, 13.0D, 2.0D),
            Block.box(2.0D, 3.0D, 14.0D, 14.0D, 5.0D, 16.0D),
            Block.box(2.0D, 11.0D, 14.0D, 14.0D, 13.0D, 16.0D)
    );
    public static final VoxelShape NORTH = Shapes.or(
            POSTS,
            EW_SIDE_BARS,
            Block.box(2.0D, 1.0D, 14.0D, 14.0D, 3.0D, 16.0D),
            Block.box(2.0D, 5.0D, 14.0D, 14.0D, 7.0D, 16.0D),
            Block.box(2.0D, 9.0D, 14.0D, 14.0D, 11.0D, 16.0D),
            Block.box(2.0D, 13.0D, 14.0D, 14.0D, 15.0D, 16.0D)
    );
    public static final VoxelShape SOUTH = Shapes.or(
            POSTS,
            EW_SIDE_BARS,
            Block.box(2.0D, 1.0D, 0.0D, 14.0D, 3.0D, 2.0D),
            Block.box(2.0D, 5.0D, 0.0D, 14.0D, 7.0D, 2.0D),
            Block.box(2.0D, 9.0D, 0.0D, 14.0D, 11.0D, 2.0D),
            Block.box(2.0D, 13.0D, 0.0D, 14.0D, 15.0D, 2.0D)
    );
    public static final VoxelShape WEST = Shapes.or(
            POSTS,
            NS_SIDE_BARS,
            Block.box(14.0D, 1.0D, 2.0D, 16.0D, 3.0D, 14.0D),
            Block.box(14.0D, 5.0D, 2.0D, 16.0D, 7.0D, 14.0D),
            Block.box(14.0D, 9.0D, 2.0D, 16.0D, 11.0D, 14.0D),
            Block.box(14.0D, 13.0D, 2.0D, 16.0D, 15.0D, 14.0D)
    );
    public static final VoxelShape EAST = Shapes.or(
            POSTS,
            NS_SIDE_BARS,
            Block.box(0.0D, 1.0D, 2.0D, 2.0D, 3.0D, 14.0D),
            Block.box(0.0D, 5.0D, 2.0D, 2.0D, 7.0D, 14.0D),
            Block.box(0.0D, 9.0D, 2.0D, 2.0D, 11.0D, 14.0D),
            Block.box(0.0D, 13.0D, 2.0D, 2.0D, 15.0D, 14.0D)
    );

    public SupportedLadderBlock(ExtendedProperties properties) {
        super(properties.properties());
        extendedProperties = properties;
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(getFluidProperty(), getFluidProperty().keyFor(Fluids.EMPTY)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            default -> EAST;
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState state = this.defaultBlockState();
        state = FluidHelpers.fillWithFluid(state, level.getFluidState(clickedPos).getType());

        return state == null ? null : state.setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FLUID);
    }


    @Override
    public ExtendedProperties getExtendedProperties() {
        return extendedProperties;
    }

    @Override
    public FluidProperty getFluidProperty() {
        return FLUID;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return IFluidLoggable.super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos)
    {
        FluidHelpers.tickFluid(level, currentPos, state);
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }
}
