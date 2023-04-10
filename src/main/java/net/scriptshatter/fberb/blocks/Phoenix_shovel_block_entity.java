package net.scriptshatter.fberb.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.util.Ect;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Phoenix_shovel_block_entity extends BlockEntity implements Nameable {
    public Phoenix_shovel_block_entity(BlockPos pos, BlockState state) {
        super(Phoenix_block_entities.SHOVEL, pos, state);
    }

    @Override
    public Text getName() {
        return Bird_parts.SHOVEL.get(this).get_itemstack().getName();
    }



    @Override
    public boolean hasCustomName() {
        return Nameable.super.hasCustomName();
    }

    @Override
    public Text getDisplayName() {
        return Nameable.super.getDisplayName();
    }



    @Nullable
    @Override
    public Text getCustomName() {
        return Bird_parts.SHOVEL.get(this).get_itemstack().getName();
    }

    public static StatusEffect get_effect(Block block){
        if(block.equals(Blocks.SAND)){
            return StatusEffects.GLOWING;
        } else if (block.equals(Blocks.GRAVEL)) {
            return StatusEffects.HASTE;
        } else if (block.equals(Blocks.GRASS_BLOCK)) {
            return StatusEffects.SPEED;
        } else if (block.equals(Blocks.DIRT) || block.equals(Blocks.DIRT_PATH)) {
            return StatusEffects.SATURATION;
        } else if (block.equals(Blocks.CLAY)) {
            return StatusEffects.HERO_OF_THE_VILLAGE;
        } else if (block.equals(Blocks.MYCELIUM)) {
            return StatusEffects.REGENERATION;
        } else if (block.equals(Blocks.MUD)) {
            return StatusEffects.RESISTANCE;
        } else if (block.equals(Blocks.SOUL_SAND) || block.equals(Blocks.SOUL_SOIL)) {
            return StatusEffects.FIRE_RESISTANCE;
        }
        return StatusEffects.LUCK;
    }


    public static <E extends BlockEntity> void tick(World world, BlockPos blockPos, BlockState state, E e) {
        Block block = world.getBlockState(blockPos.down()).getBlock();
        if(Bird_parts.SHOVEL.get(Objects.requireNonNull(Phoenix_block_entities.SHOVEL.get(world, blockPos))).get_temp() > 0){
            Bird_parts.SHOVEL.get(Objects.requireNonNull(Phoenix_block_entities.SHOVEL.get(world, blockPos))).decrease_temp(-0.01);
            applyPlayerEffects(world, blockPos, get_effect(block));
        }
    }

    private static void applyPlayerEffects(World world, BlockPos pos, @Nullable StatusEffect primaryEffect) {
        if (!world.isClient && primaryEffect != null) {
            double d = (3 * 10 + 10);
            int i = 0;

            int j = (9 + 3 * 2) * 20;
            Box box = (new Box(pos)).expand(d).stretch(0.0, world.getHeight(), 0.0);
            List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, box);
            Iterator<PlayerEntity> var11 = list.iterator();

            PlayerEntity playerEntity;
            while(var11.hasNext()) {
                playerEntity = var11.next();
                if(Ect.has_origin(playerEntity, Ect.FIRE_BIRD)){
                    playerEntity.addStatusEffect(new StatusEffectInstance(primaryEffect, j, i, true, true));
                }
            }

        }
    }
}
