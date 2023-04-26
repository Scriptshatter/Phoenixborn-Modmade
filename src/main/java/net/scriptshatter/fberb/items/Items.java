package net.scriptshatter.fberb.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.blocks.Phoenix_blocks;

public class Items {
    //Making the icon for the phoenix
    public static final Phoenix_brooch PHOENIX_BROOCH = register_items("phoenix_brooch", new Phoenix_brooch(new FabricItemSettings()), Phoenix.MOD_ID);
    public static final Phoenix_shovel_block_item PHOENIX_BLOCK_SHOVEL = register_items("phoenix_block_shovel", new Phoenix_shovel_block_item(new FabricItemSettings()), Phoenix.MOD_ID);
    public static final Charged_amethyst CHARGED_AMETHYST = register_items("charged_amethyst", new Charged_amethyst(new FabricItemSettings()), Phoenix.MOD_ID);
    public static final Phoenix_pickaxe PHOENIX_PICKAXE = register_API_items("phoenix_pickaxe", new Phoenix_pickaxe(new Charged_amethyst_mat(), 1, -2.5f, new FabricItemSettings(), 150), Phoenix.MOD_ID);
    public static final Phoenix_shovel PHOENIX_SHOVEL = register_API_items("phoenix_shovel", new Phoenix_shovel(new Charged_amethyst_mat(), 1, -3f, new FabricItemSettings(), 150), Phoenix.MOD_ID);
    public static final Machine_item MACHINE_ITEM = register_items("machine", new Machine_item(), Phoenix.MOD_ID);
    public static final Phoenix_axe PHOENIX_AXE = register_API_items("phoenix_axe", new Phoenix_axe(5.5f, -3.0F, new Charged_amethyst_mat(), 150, new FabricItemSettings()), Phoenix.MOD_ID);


    public static <I extends Item> I register_items(String name, I item, String modid){
        return Registry.register(Registries.ITEM, new Identifier(modid, name), item);
    }

    public static <I extends Item> I register_API_items(String name, I item, String modid){
        return Registry.register(Registries.ITEM, new Identifier(modid, name), item);
    }

    public static void werk(){
        Phoenix.LOGGER.info("AHHHH");
    }
}
