package net.scriptshatter.fberb.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.TagEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.items.Phoenix_shovel;

import java.util.ArrayList;
import java.util.List;

public class Add_loot{

    public static void onServerStarted() {
        List<Block> list = new ArrayList<>();
        Registries.BLOCK.forEach(block1 -> {
            Registries.BLOCK.getEntry(block1).getKey().ifPresent(blockRegistryKey -> {
                // BlockTags.SHOVEL_MINEABLE.isOf((RegistryKey<? extends Registry<?>>) blockRegistryKey)

            });
        });
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {

        });
    }
}
