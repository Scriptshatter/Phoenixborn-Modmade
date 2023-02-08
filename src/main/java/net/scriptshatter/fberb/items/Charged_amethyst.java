package net.scriptshatter.fberb.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.sound.Register_sounds;
import net.scriptshatter.fberb.util.Ect;

import java.util.Random;

public class Charged_amethyst extends Item {
    public Charged_amethyst(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(!world.isClient() && Ect.has_origin(user, Ect.FIRE_BIRD)){
            user.playSound(SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1, 1);
            Bird_parts.TEMP.get(user).change_temp(25);
            itemStack.decrement(1);
            return TypedActionResult.consume(itemStack);
        }
        return super.use(world, user, hand);
    }
}
