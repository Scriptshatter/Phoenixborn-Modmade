package net.scriptshatter.fberb.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;

public class Phoenix_block_entities {
    public static BlockEntityType<Machine> MACHINE;

    public static void register_block_entities(){
        MACHINE = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Phoenix.MOD_ID, "machine"),
                FabricBlockEntityTypeBuilder.create(Machine::new, Phoenix_blocks.MACHINE).build(null));
    }
}
