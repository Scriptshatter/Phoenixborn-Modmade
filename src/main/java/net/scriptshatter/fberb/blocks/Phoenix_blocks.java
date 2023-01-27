package net.scriptshatter.fberb.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import software.bernie.example.block.FertilizerBlock;
import software.bernie.example.block.GeckoHabitatBlock;
import software.bernie.geckolib.GeckoLib;

public class Phoenix_blocks {

    public static final Machine_anim MACHINE = registerBlockWithoutItem("machine", new Machine_anim(FabricBlockSettings.of(Material.METAL).nonOpaque()));

    private static <B extends Block> B registerBlockWithoutItem(String name, B block){
        return Registry.register(Registries.BLOCK, new Identifier(Phoenix.MOD_ID, name), block);
    }
}
