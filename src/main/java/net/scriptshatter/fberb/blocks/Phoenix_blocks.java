package net.scriptshatter.fberb.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;

public class Phoenix_blocks {

    public static final Machine_anim MACHINE = registerBlockWithoutItem("machine", new Machine_anim(FabricBlockSettings.of(Material.METAL).strength(5f, 6f).requiresTool().nonOpaque()));
    public static final Phoenix_shovel_block SHOVEL = registerBlockWithoutItem("phoenix_shovel", new Phoenix_shovel_block(FabricBlockSettings.of(Material.METAL).nonOpaque().breakInstantly()));

    private static <B extends Block> B registerBlockWithoutItem(String name, B block){
        return Registry.register(Registries.BLOCK, new Identifier(Phoenix.MOD_ID, name), block);
    }
}
