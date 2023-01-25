package net.scriptshatter.fberb.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.data.client.Model;
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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.scriptshatter.fberb.blocks.client.Machine_model;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.lang.annotation.Target;
import java.util.Objects;

public class Machine_anim extends BlockWithEntity {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public Machine_anim(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
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

    private int get_cubby(BlockHitResult hit, BlockState state, PlayerEntity player){
        double d = (MathHelper.fractionalPart(hit.getPos().x) - 0.5);
        double e = (MathHelper.fractionalPart(hit.getPos().y) - 0.5);
        double f = (MathHelper.fractionalPart(hit.getPos().z) - 0.5);
        if(state.get(FACING).equals(Direction.SOUTH) || state.get(FACING).equals(Direction.NORTH)){
            if(f <= 0.15 && f > -0.15){
                player.sendMessage(Text.literal("hello"));
                if(e <= 0.15 && e > -0.15){
                    player.sendMessage(Text.literal("hello"));
                }
                else if(e > 0.15){
                    player.sendMessage(Text.literal("hello"));
                    return 1;
                }
                else if(e <= -0.15){
                    player.sendMessage(Text.literal("hello"));
                }
            }
            else if(f > 0.15){
                player.sendMessage(Text.literal("hello"));

            }
            else if(f <= -0.15){
                player.sendMessage(Text.literal("hello"));
            }
        }
        else{
            return 0;
        }
        player.sendMessage(Text.literal(String.valueOf(d)));
        player.sendMessage(Text.literal(String.valueOf(e)));
        player.sendMessage(Text.literal(String.valueOf(f)));
        return 0;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        if(hit.getSide().equals(state.get(FACING))){
            player.sendMessage(Text.literal("I now know how to use this"));
            return ActionResult.CONSUME;
        }
        if(hit.getSide().equals(state.get(FACING).rotateYClockwise())){
            player.sendMessage(Text.literal("how does one put stuff in here..."));
            get_cubby(hit, state, player);
            return ActionResult.CONSUME;
        }
        return ActionResult.CONSUME_PARTIAL;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
