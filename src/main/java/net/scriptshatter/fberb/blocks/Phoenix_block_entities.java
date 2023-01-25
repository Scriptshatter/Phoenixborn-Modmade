package net.scriptshatter.fberb.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.scriptshatter.fberb.Phoenix;

public class Phoenix_block_entities {
    public static BlockEntityType<Machine> MACHINE;

    public static void register_block_entities(){
        MACHINE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(Phoenix.MOD_ID, "machine"),
                FabricBlockEntityTypeBuilder.create(Machine::new,
                        Phoenix_blocks.MACHINE).build());
    }
}
