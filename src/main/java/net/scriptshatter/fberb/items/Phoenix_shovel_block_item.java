package net.scriptshatter.fberb.items;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.ShulkerBoxColoringRecipe;
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
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.Phoenix_client;
import net.scriptshatter.fberb.blocks.Phoenix_blocks;
import net.scriptshatter.fberb.blocks.Phoenix_shovel_block_entity;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.util.Ect;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Phoenix_shovel_block_item extends BlockItem {
    public Phoenix_shovel_block_item(Settings settings) {
        super(Phoenix_blocks.SHOVEL, settings);
    }

    @Override
    protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        Phoenix_shovel_block_entity block_entity = (Phoenix_shovel_block_entity) world.getBlockEntity(pos);
        if(block_entity != null && stack.getItem() instanceof Birb_item){
            Bird_parts.SHOVEL.get(block_entity).set_itemstack(stack);
            if(player != null){
                Bird_parts.SHOVEL.get(block_entity).set_owner(player.getUuid());
            }
        }
        return super.postPlacement(pos, world, player, stack, state);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.fberb.phoenix_shovel.blockitem").formatted(Formatting.DARK_GRAY));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
