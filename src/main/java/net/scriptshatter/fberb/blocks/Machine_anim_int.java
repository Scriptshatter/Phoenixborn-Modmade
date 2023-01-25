package net.scriptshatter.fberb.blocks;

import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public interface Machine_anim_int{
    VoxelShape getOutlineShape(BlockView world, BlockPos pos, ShapeContext context);
}
