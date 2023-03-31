package net.scriptshatter.fberb.mixin;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.scriptshatter.fberb.blocks.Phoenix_block_entities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Inject(method = "setBlockEntityNbt", at=@At("HEAD"), cancellable = true)
    private static void set_shovel_nbt(ItemStack stack, BlockEntityType<?> blockEntityType, NbtCompound tag, CallbackInfo ci){
        if(blockEntityType.equals(Phoenix_block_entities.SHOVEL)){
            stack.setNbt(tag.getCompound("cardinal_components").getCompound("fberb:shovel").getCompound("shovel").getCompound("tag"));
            ci.cancel();
        }
    }
}
