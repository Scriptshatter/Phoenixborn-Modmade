package net.scriptshatter.fberb.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.scriptshatter.fberb.blocks.Phoenix_blocks;
import net.scriptshatter.fberb.blocks.Phoenix_shovel_block_entity;
import net.scriptshatter.fberb.components.Bird_parts;
import org.jetbrains.annotations.Nullable;

public class Phoenix_shovel_block_item extends BlockItem {
    public Phoenix_shovel_block_item(Settings settings) {
        super(Phoenix_blocks.SHOVEL, settings);
    }

    @Override
    protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        Phoenix_shovel_block_entity block_entity = (Phoenix_shovel_block_entity) world.getBlockEntity(pos);
        if(block_entity != null){
            Bird_parts.SHOVEL.get(block_entity).set_itemstack(stack);
            if(player != null){
                Bird_parts.SHOVEL.get(block_entity).set_owner(player.getUuid());
            }
        }
        return super.postPlacement(pos, world, player, stack, state);
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        ItemUsageContext usageContext = new ItemUsageContext(context.getWorld(), context.getPlayer(), context.getHand(), context.getStack(), new BlockHitResult(new Vec3d(context.getBlockPos().getX()+0.5, context.getBlockPos().getY()+0.9, context.getBlockPos().getZ()+0.5), Direction.UP, context.getSide() == Direction.UP ? context.getBlockPos() : context.getBlockPos().up().add(-context.getSide().getOffsetX(), 0, -context.getSide().getOffsetZ()), context.hitsInsideBlock()));
        ItemPlacementContext context1 = new ItemPlacementContext(usageContext);
        return super.place(context1);
    }
}
