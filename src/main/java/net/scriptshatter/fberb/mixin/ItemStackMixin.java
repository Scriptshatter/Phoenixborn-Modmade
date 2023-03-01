package net.scriptshatter.fberb.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.scriptshatter.fberb.items.Birb_item;
import net.scriptshatter.fberb.util.Get_use_case;
import net.scriptshatter.fberb.util.Phoenix_use_actions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements Get_use_case {

    @Shadow public abstract Item getItem();

    public Phoenix_use_actions use_actions(PlayerEntity player){
        if(this.getItem() instanceof Birb_item){
            return ((Birb_item) this.getItem()).get_use_case(player);
        }
        else return Phoenix_use_actions.NONE;
    }
}
