package net.scriptshatter.fberb.util;

import net.minecraft.entity.player.PlayerEntity;

public class GetFuel {
    //Lets you check if furnace minecarts have fuel.
    public static boolean hasFuel(Getdata furnaceMinecart){
        return furnaceMinecart.has_fuel();
    }

    public static Phoenix_use_actions checkStatus(Get_use_case itemStack, PlayerEntity player){
        return itemStack.use_actions(player);
    }
}
