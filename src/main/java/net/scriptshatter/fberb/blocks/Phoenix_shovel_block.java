package net.scriptshatter.fberb.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.scriptshatter.fberb.items.Items;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class Phoenix_shovel_block extends BlockWithEntity implements BlockEntityProvider {
    protected Phoenix_shovel_block(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return super.getPickStack(world, pos, state);
    }

    @Override
    public Item asItem() {
        return Items.PHOENIX_SHOVEL;
    }

    private static final VoxelShape SHAPE = Stream.of(
            Block.createCuboidShape(5.275, 0, 7.5, 10.6, 3.1, 8.5),
            Block.createCuboidShape(6.875, 3.0999999999999996, 7.5, 9.025, 10, 8.5),
            Block.createCuboidShape(6.35, 10, 7.5, 9.550000000000008, 12.575, 8.5)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();;

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new Phoenix_shovel_block_entity(pos, state);
    }
}
