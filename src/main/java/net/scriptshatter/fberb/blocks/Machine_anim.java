package net.scriptshatter.fberb.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.components.Machine_anim_int;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Machine_anim extends BlockWithEntity {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public Machine_anim(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new Machine(pos, state);
    }
    private int gen_index(int mult, Double e){
        if(e <= 0.15 && e > -0.15){
            return 3+mult;
        }
        else if(e > 0.15){
            return mult;
        }
        else if(e <= -0.15){
            return 6+mult;
        }
        return 1;
    }

    private int get_cubby(BlockHitResult hit, BlockState state){
        double d = (MathHelper.fractionalPart(hit.getPos().x) - 0.5);
        double e = (MathHelper.fractionalPart(hit.getPos().y) - 0.5);
        double f = (MathHelper.fractionalPart(hit.getPos().z) - 0.5);
        if(state.get(FACING).equals(Direction.SOUTH) || state.get(FACING).equals(Direction.NORTH)){
            if(f <= 0.15 && f > -0.15){
                return gen_index(2, e);
            }
            else if(f > 0.15){
                if(state.get(FACING).equals(Direction.NORTH)){
                    return gen_index(1, e);
                }
                return gen_index(3, e);
            }
            else if(f <= -0.15){
                if(state.get(FACING).equals(Direction.NORTH)){
                    return gen_index(3, e);
                }
                return gen_index(1, e);
            }
        }
        else{
            if(d <= 0.15 && d > -0.15){
                return gen_index(2, e);
            }
            else if(d > 0.15){
                if(state.get(FACING).equals(Direction.WEST)){
                    return gen_index(1, e);
                }
                return gen_index(3, e);
            }
            else if(d <= -0.15){
                if(state.get(FACING).equals(Direction.WEST)){
                    return gen_index(3, e);
                }
                return gen_index(1, e);
            }
        }
        return 1;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient || Phoenix_block_entities.MACHINE.get(world, pos) == null) {
            return ActionResult.SUCCESS;
        }
        if(hit.getSide().equals(state.get(FACING))){
            player.sendMessage(Text.literal("I now know how to use this"));
            return ActionResult.CONSUME;
        }
        if(hit.getSide().equals(state.get(FACING).rotateYClockwise())){
            Machine block = Objects.requireNonNull(Phoenix_block_entities.MACHINE.get(world, pos));
            player.giveItemStack(Bird_parts.INV.get(block).get_item(get_cubby(hit, state)));
            //Machine_funcs.add_item(get_cubby(hit, state), player.getStackInHand(hand), Objects.requireNonNull(Phoenix_block_entities.MACHINE.get(world, pos)));
            Bird_parts.INV.get(block).add_item(get_cubby(hit, state), player.getStackInHand(hand));
            player.getStackInHand(hand).decrement(1);
            return ActionResult.CONSUME;
        }
        return ActionResult.CONSUME_PARTIAL;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
