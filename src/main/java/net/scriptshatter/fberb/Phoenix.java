package net.scriptshatter.fberb;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.item.ItemGroups;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.blocks.Phoenix_block_entities;
import net.scriptshatter.fberb.blocks.Phoenix_blocks;
import net.scriptshatter.fberb.command.Set_temp;
import net.scriptshatter.fberb.effects.EffectsRegistery;
import net.scriptshatter.fberb.entitys.Entity_registry;
import net.scriptshatter.fberb.events.Rev_temp;
import net.scriptshatter.fberb.events.Temp_control;
import net.scriptshatter.fberb.gui.Tutorial_screen;
import net.scriptshatter.fberb.items.Items;
import net.scriptshatter.fberb.loot_tables.Loot_reg;
import net.scriptshatter.fberb.networking.Youve_got_mail;
import net.scriptshatter.fberb.recipe.Phoenix_recipes;
import net.scriptshatter.fberb.sound.Register_sounds;
import net.scriptshatter.fberb.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Phoenix implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static String MOD_ID = "fberb";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Registers the commands, events, actions, conditions, and the scales.
		CommandRegistrationCallback.EVENT.register(Set_temp::register);
		ServerTickEvents.START_WORLD_TICK.register(new Temp_control());
		ServerPlayerEvents.AFTER_RESPAWN.register(new Rev_temp());
		//Add_loot.onServerStarted();
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (Blocks.GRAVEL.getLootTableId().equals(id)) {
				LootTable nuggets = lootManager.getTable(new Identifier("fberb", "gameplay/phoenix_shovel_nuggets"));
				Arrays.stream(nuggets.pools).toList().forEach(tableBuilder::pool);
			}
		});
		Actionfactory.register();
		Entity_conditions.register();
		Phoenix_scales.init();
		Items.werk();
		Loot_reg.register_loot();
		EffectsRegistery.registerEffects();
		Register_sounds.help();
		Phoenix_block_entities.register_block_entities();
		Phoenix_blocks.werk();
		Phoenix_block_entities.werk();
		new Entity_registry().register();
		Dmg_sources.register();

		Youve_got_mail.registerC2SMail();
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((content) -> {
			content.add(Items.MACHINE_ITEM);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((content) -> {
			content.add(Items.CHARGED_AMETHYST);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((content) -> {
			content.add(Items.PHOENIX_AXE);
			content.add(Items.PHOENIX_PICKAXE);
			content.add(Items.PHOENIX_SHOVEL);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register((content) -> {
			content.add(Items.PHOENIX_BROOCH);
		});
		Phoenix_recipes.register_pizza();


	}
}
