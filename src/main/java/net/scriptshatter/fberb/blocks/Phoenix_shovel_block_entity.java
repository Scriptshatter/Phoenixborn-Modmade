package net.scriptshatter.fberb.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class Phoenix_shovel_block_entity extends BlockEntity implements Nameable {
    public Phoenix_shovel_block_entity(BlockPos pos, BlockState state) {
        super(Phoenix_block_entities.SHOVEL, pos, state);
    }

    @Override
    public Text getName() {
        return Text.literal("placeholder");
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
        return Nameable.super.getCustomName();
    }
}
