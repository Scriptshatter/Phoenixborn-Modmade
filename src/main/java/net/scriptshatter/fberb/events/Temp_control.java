package net.scriptshatter.fberb.events;

import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.util.GetFuel;
import net.scriptshatter.fberb.util.Ect;
import net.scriptshatter.fberb.util.Getdata;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;

import java.util.*;
//Doozy time.

public class Temp_control implements ServerTickEvents.StartWorldTick {
    //I don't feel like registering 34 different candles, okay? I'm just gonna use tags.
    private static final TagKey<Block> candles = TagKey.of(RegistryKeys.BLOCK, new Identifier("minecraft", "candles"));
    private static final TagKey<Block> cake_candles = TagKey.of(RegistryKeys.BLOCK, new Identifier("minecraft", "candle_cakes"));
    private static final UUID speedUuid = new UUID((long)23423422.222342, (long)253332342.22);
    private static final UUID atkUuid = new UUID((long)4564625234.222342, (long)3464353543.22);




    @Override
    public void onStartTick(ServerWorld world) {
        //Get the players and on every tick...
        List<ServerPlayerEntity> players = world.getPlayers();
        for (ServerPlayerEntity player: players) {
            //Check if they're a bird
            if(Ect.has_origin(player, Ect.FIRE_BIRD) || Ect.has_origin(player, Ect.FROST_BIRD)){
                //set them to the right size
                ScaleType scaleType = ScaleRegistries.SCALE_TYPES.get(new Identifier(Phoenix.MOD_ID, "change_size"));
                ScaleData data = scaleType.getScaleData(player);
                data.setScale(0.8f);

                //Get colder the higher up you are, warmer the lower down.
                double change = -(((double)player.getBlockY()-65)/1000);
                Bird_parts.TEMP.get(player).change_temp(change);

                //Increase temp if on fire
                if(player.isOnFire()){
                    Bird_parts.TEMP.get(player).change_temp(0.01);
                }
                Bird_parts.TEMP.get(player).change_rage(-1);

                //Check how cold they are and debuff appropriately.
                AttributedEntityAttributeModifier speed = new AttributedEntityAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(speedUuid, "name", (((double)Bird_parts.TEMP.get(player).get_temp()*2)/1000)-1, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
                AttributedEntityAttributeModifier atk = new AttributedEntityAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(atkUuid, "name", (((double)Bird_parts.TEMP.get(player).get_temp()*2)/1000)-1, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
                if(Bird_parts.TEMP.get(player).get_temp() <= 500 && Ect.has_origin(player, Ect.FIRE_BIRD)){
                    Objects.requireNonNull(player.getAttributeInstance(speed.getAttribute())).removeModifier(speed.getModifier());
                    Objects.requireNonNull(player.getAttributeInstance(speed.getAttribute())).addPersistentModifier(speed.getModifier());
                    Objects.requireNonNull(player.getAttributeInstance(atk.getAttribute())).removeModifier(atk.getModifier());
                    Objects.requireNonNull(player.getAttributeInstance(atk.getAttribute())).addPersistentModifier(atk.getModifier());
                }
                else {
                    Objects.requireNonNull(player.getAttributeInstance(speed.getAttribute())).removeModifier(speed.getModifier());
                    Objects.requireNonNull(player.getAttributeInstance(atk.getAttribute())).removeModifier(atk.getModifier());
                }

                //Make them get colder if they're frozen.
                if(player.isFrozen()){
                    Bird_parts.TEMP.get(player).change_temp(0.02);
                }

                //Update temp according to the biomes
                BlockPos playerBlockPos = player.getBlockPos();
                Biome playerBiome = world.getBiome(playerBlockPos).value();
                Bird_parts.TEMP.get(player).change_temp((playerBiome.getTemperature()-Bird_parts.TEMP.get(player).get_internal_temp())/10);

                //Update temp depending on the blocks around them
                getTemp_blocks().forEach((k,s) -> {
                    double h = s.getKey();
                    int r = s.getValue();
                    BlockPos playerblock = player.getBlockPos();
                    for (int x = r; x >= -r; x--) {
                        for (int y = r; y >= -r; y--) {
                            for (int z = r; z >= -r; z--) {
                                if (world.getBlockState(playerblock.south(z).up(y).east(x)).getBlock().equals(k)) {
                                    Bird_parts.TEMP.get(player).change_temp(h);
                                    //Phoenix.LOGGER.info(h + " " + r + " " + k.getName().toString());
                                }
                            }
                        }
                    }
                });

                //Gotta make sure that if the block's not lit it don't give heat.
                getLitBlocks().forEach((k,s) -> {
                    double h = s.getKey();
                    int r = s.getValue();
                    BlockPos playerblock = player.getBlockPos();
                    for (int x = r; x >= -r; x--) {
                        for (int y = r; y >= -r; y--) {
                            for (int z = r; z >= -r; z--) {
                                if (world.getBlockState(playerblock.south(z).up(y).east(x)).getBlock().equals(k) && /* Checks if the block is lit */ isLit(playerblock, x, y, z, world)) {
                                    Bird_parts.TEMP.get(player).change_temp(h);
                                    Phoenix.LOGGER.info(h + " " + r + " " + k.getName().toString());
                                }
                            }
                        }
                    }
                });

                //This was one of the stupidest things I've ever had to program. Honestly, f*ck NBT. Add a way to check NBT easily fabric, PLEASE.
                {
                    List<?> check =  world.getEntitiesByType(EntityType.FURNACE_MINECART, x -> {
                        Getdata d = (Getdata)x;
                        return d.has_fuel();
                    });

                    for(Object i: check){
                        if(GetFuel.hasFuel((Getdata) i) && player.isInRange((Entity) i, 3)){
                            Bird_parts.TEMP.get(player).change_temp(0.003);
                        }
                    }
                }

                //Checks if entities around you are on fire.
                for (EntityType<?> j: Registries.ENTITY_TYPE.stream().toList()) {
                    List<?> check = world.getEntitiesByType(j, Entity::isOnFire);

                    for(Object i: check){
                        if(player.isInRange((Entity) i, 3)){
                            Bird_parts.TEMP.get(player).change_temp(0.005);
                        }
                    }
                }

                //If they're touching rain...
                if(isTouchingRain(player)){
                    Bird_parts.TEMP.get(player).change_temp(-0.015);
                }

                //If it's night...
                if(!player.world.isDay()){
                    Bird_parts.TEMP.get(player).change_temp(-0.01);
                }

                //For each entity listed, effect temp appropriately.
                getEntityList().forEach((k, s) -> {
                    List<?> check =  world.getEntitiesByType(k, x -> true);
                    double h = s.getKey();
                    int r = s.getValue();
                    for (Object i:
                         check) {
                        if(player.isInRange((Entity)i, r)){
                            Bird_parts.TEMP.get(player).change_temp(h);
                            //Phoenix.LOGGER.info(h + " " + r + " " + k.getName().getString());
                        }
                    }
                });

                //For each potion listed, effect temp appropriately.
                getPotionList().forEach((k, s) -> {
                    if(player.hasStatusEffect(k)){
                        Bird_parts.TEMP.get(player).change_temp(s);
                    }
                });

                //Candles! I mean, there 34 of them, so it only makes sense.
                {
                    int radius = 2;
                    BlockPos playerblock = player.getBlockPos();
                    for (int x = radius; x >= -radius; x--) {
                        for (int y = radius; y >= -radius; y--) {
                            for (int z = radius; z >= -radius; z--) {
                                if (world.getBlockState(playerblock.south(z).up(y).east(x)).isIn(candles) && isLit(playerblock, x, y, z, world)) {
                                    Bird_parts.TEMP.get(player).change_temp(0.001 * candleAmount(playerblock, x, y, z, world));
                                }
                                if ( world.getBlockState(playerblock.south(z).up(y).east(x)).isIn(cake_candles) && isLit(playerblock, x, y, z, world)){
                                    Bird_parts.TEMP.get(player).change_temp(0.001);
                                }
                            }
                        }
                    }
                }
            } //End of for loop
        }
    }

    //Blocks that don't have a Lit property. First number is the temp, second is the range.
    public HashMap<Block, Map.Entry<Double, Integer>> getTemp_blocks() {
        HashMap<Block, Map.Entry<Double, Integer>> temp_blocks = new HashMap<>();
        temp_blocks.put(Blocks.TORCH, new AbstractMap.SimpleEntry<>(0.003, 3));
        temp_blocks.put(Blocks.WALL_TORCH, new AbstractMap.SimpleEntry<>(0.003, 3));
        temp_blocks.put(Blocks.SOUL_TORCH, new AbstractMap.SimpleEntry<>(0.002, 2));
        temp_blocks.put(Blocks.SOUL_WALL_TORCH, new AbstractMap.SimpleEntry<>(0.002, 2));
        temp_blocks.put(Blocks.REDSTONE_TORCH, new AbstractMap.SimpleEntry<>(0.001, 1));
        temp_blocks.put(Blocks.REDSTONE_WALL_TORCH, new AbstractMap.SimpleEntry<>(0.001, 1));
        temp_blocks.put(Blocks.LAVA, new AbstractMap.SimpleEntry<>(0.01, 3));
        temp_blocks.put(Blocks.LAVA_CAULDRON, new AbstractMap.SimpleEntry<>(0.1, 2));
        temp_blocks.put(Blocks.FIRE, new AbstractMap.SimpleEntry<>(0.004, 2));
        temp_blocks.put(Blocks.SOUL_FIRE, new AbstractMap.SimpleEntry<>(0.003, 2));
        temp_blocks.put(Blocks.LANTERN, new AbstractMap.SimpleEntry<>(0.002, 2));
        temp_blocks.put(Blocks.SOUL_LANTERN, new AbstractMap.SimpleEntry<>(0.001, 1));
        temp_blocks.put(Blocks.JACK_O_LANTERN, new AbstractMap.SimpleEntry<>(0.003, 3));
        temp_blocks.put(Blocks.ICE, new AbstractMap.SimpleEntry<>(-0.002, 1));
        temp_blocks.put(Blocks.FROSTED_ICE, new AbstractMap.SimpleEntry<>(-0.002, 1));
        temp_blocks.put(Blocks.PACKED_ICE, new AbstractMap.SimpleEntry<>(-0.004, 2));
        temp_blocks.put(Blocks.BLUE_ICE, new AbstractMap.SimpleEntry<>(-0.008, 2));
        temp_blocks.put(Blocks.MAGMA_BLOCK, new AbstractMap.SimpleEntry<>(0.006, 3));
        temp_blocks.put(Blocks.POWDER_SNOW, new AbstractMap.SimpleEntry<>(-0.005, 1));
        temp_blocks.put(Blocks.POWDER_SNOW_CAULDRON, new AbstractMap.SimpleEntry<>(-0.005, 1));
        temp_blocks.put(Blocks.SNOW, new AbstractMap.SimpleEntry<>(-0.003, 0));
        temp_blocks.put(Blocks.SNOW_BLOCK, new AbstractMap.SimpleEntry<>(-0.004, 1));
        temp_blocks.put(Blocks.END_ROD, new AbstractMap.SimpleEntry<>(0.006, 4));
        temp_blocks.put(Blocks.NETHER_PORTAL, new AbstractMap.SimpleEntry<>(0.015, 5));
        temp_blocks.put(Blocks.WATER, new AbstractMap.SimpleEntry<>(-0.05, 0));
        temp_blocks.put(Blocks.WATER_CAULDRON, new AbstractMap.SimpleEntry<>(0.0005, 1));
        return temp_blocks;
    }
    //Same as above, but for blocks that have a lit property.
    public HashMap<Block, Map.Entry<Double, Integer>> getLitBlocks() {
        HashMap<Block, Map.Entry<Double, Integer>> temp_blocks = new HashMap<>();
        temp_blocks.put(Blocks.CAMPFIRE, new AbstractMap.SimpleEntry<>(0.004, 4));
        temp_blocks.put(Blocks.SOUL_CAMPFIRE, new AbstractMap.SimpleEntry<>(0.003, 3));
        temp_blocks.put(Blocks.SMOKER, new AbstractMap.SimpleEntry<>(0.004, 4));
        temp_blocks.put(Blocks.FURNACE, new AbstractMap.SimpleEntry<>(0.003, 3));
        temp_blocks.put(Blocks.BLAST_FURNACE, new AbstractMap.SimpleEntry<>(0.005, 4));
        return temp_blocks;
    }

    //You see, the function in the Entity file for checking if an entity is being rained on or not is private.
    //So I made my own.
    //By copying the original code and putting it here.
    public boolean isTouchingRain(Entity entity){
        BlockPos blockPos = entity.getBlockPos();
        return entity.world.hasRain(blockPos) || entity.world.hasRain(new BlockPos(blockPos.getX(), entity.getBoundingBox().maxY, blockPos.getZ()));
    }

    //Checks if a block in relation to another blocks position is lit up
    private boolean isLit(BlockPos blockPos, int x, int y, int z, World world){
        if (world.getBlockState(blockPos.south(z).up(y).east(x)).getOrEmpty(Properties.LIT).isPresent()){
            return world.getBlockState(blockPos.south(z).up(y).east(x)).getOrEmpty(Properties.LIT).get();
        }
        return false;
    }

    //Checks the amount of candles a block has with position relative to another block
    private int candleAmount(BlockPos blockPos, int x, int y, int z, World world){
        if (world.getBlockState(blockPos.south(z).up(y).east(x)).getOrEmpty(Properties.CANDLES).isPresent()){
            return world.getBlockState(blockPos.south(z).up(y).east(x)).getOrEmpty(Properties.CANDLES).get();
        }
        return 0;
    }

    //List of entities, works same as blocks.
    public HashMap<EntityType<?>, Map.Entry<Double, Integer>> getEntityList() {
        HashMap<EntityType<?>, Map.Entry<Double, Integer>> temp_blocks = new HashMap<>();
        temp_blocks.put(EntityType.BLAZE, new AbstractMap.SimpleEntry<>(0.004, 3));
        temp_blocks.put(EntityType.MAGMA_CUBE, new AbstractMap.SimpleEntry<>(0.004, 2));
        temp_blocks.put(EntityType.FIREBALL, new AbstractMap.SimpleEntry<>(0.01, 2));
        temp_blocks.put(EntityType.SMALL_FIREBALL, new AbstractMap.SimpleEntry<>(0.005, 1));
        temp_blocks.put(EntityType.DRAGON_FIREBALL, new AbstractMap.SimpleEntry<>(0.015, 3));
        temp_blocks.put(EntityType.FIREWORK_ROCKET, new AbstractMap.SimpleEntry<>(0.002, 1));
        temp_blocks.put(EntityType.LIGHTNING_BOLT, new AbstractMap.SimpleEntry<>(0.02, 5));
        temp_blocks.put(EntityType.END_CRYSTAL, new AbstractMap.SimpleEntry<>(0.003, 2));
        temp_blocks.put(EntityType.STRAY, new AbstractMap.SimpleEntry<>(-0.004, 2));
        temp_blocks.put(EntityType.DROWNED, new AbstractMap.SimpleEntry<>(-0.001, 1));
        temp_blocks.put(EntityType.HUSK, new AbstractMap.SimpleEntry<>(0.002, 2));
        temp_blocks.put(EntityType.SHEEP, new AbstractMap.SimpleEntry<>(0.005, 0));
        temp_blocks.put(EntityType.SNOW_GOLEM, new AbstractMap.SimpleEntry<>(-0.003, 1));
        temp_blocks.put(EntityType.SNOWBALL, new AbstractMap.SimpleEntry<>(-0.005, 0));
        return temp_blocks;
    }

    //List of the potions. Works the same, but there's no range cause it's not needed.
    public HashMap<StatusEffect, Double> getPotionList(){
        HashMap<StatusEffect, Double> temp_potions = new HashMap<>();
        temp_potions.put(StatusEffects.FIRE_RESISTANCE, -0.01);
        temp_potions.put(StatusEffects.NAUSEA, 0.005);
        temp_potions.put(StatusEffects.REGENERATION, 0.01);
        return temp_potions;
    }
}
