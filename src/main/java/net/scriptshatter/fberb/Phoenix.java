package net.scriptshatter.fberb;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.scriptshatter.fberb.blocks.Phoenix_block_entities;
import net.scriptshatter.fberb.blocks.Phoenix_blocks;
import net.scriptshatter.fberb.command.Set_temp;
import net.scriptshatter.fberb.effects.EffectsRegistery;
import net.scriptshatter.fberb.events.Temp_control;
import net.scriptshatter.fberb.items.Items;
import net.scriptshatter.fberb.items.Phoenix_brooch;
import net.scriptshatter.fberb.sound.Register_sounds;
import net.scriptshatter.fberb.util.Actionfactory;
import net.scriptshatter.fberb.util.Entity_conditions;
import net.scriptshatter.fberb.util.Phoenix_scales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;

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
		Actionfactory.register();
		Entity_conditions.register();
		Phoenix_scales.init();
		Items.werk();
		EffectsRegistery.registerEffects();
		Register_sounds.help();
		Phoenix_block_entities.register_block_entities();
		Phoenix_blocks.i_exist();
	}
}
