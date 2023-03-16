package net.scriptshatter.fberb.items;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.advancement.criterion.CuredZombieVillagerCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.server.tag.VanillaBlockTagProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.*;
import net.minecraft.loot.entry.TagEntry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.util.Phoenix_use_actions;

import java.util.Arrays;
import java.util.List;

public class Phoenix_shovel extends ShovelItem implements Birb_item {


    private final int max_temp;
    private double temp;


    public Phoenix_shovel(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings, int max_temp) {
        super(material, attackDamage, attackSpeed, settings);
        this.max_temp = max_temp;
    }

    @Override
    public Phoenix_use_actions get_use_case(PlayerEntity user) {
        return Phoenix_use_actions.NONE;
    }

    @Override
    public int max_temp() {
        return this.max_temp;
    }

    @Override
    public double temp(ItemStack stack) {
        read_nbt(stack);
        return this.temp;
    }

    @Override
    public int getItemBarColorTemp(ItemStack stack) {
        float f = Math.max(0.0F, ((float)this.max_temp - (float)this.temp(stack)) / (float)this.max_temp);
        return MathHelper.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public int getItemBarStepTemp(ItemStack stack) {
        return Math.round(((float)this.temp(stack) * 13.0F )/ (float)this.max_temp());
    }

    @Override
    public void change_temp(double amount, ItemStack itemStack) {
        read_nbt(itemStack);
        if(this.temp + amount < 0){
            this.temp = 0;
        } else if (this.temp + amount > this.max_temp) {
            this.temp = this.max_temp;
        }
        else this.temp += amount;
        write_nbt(itemStack);
    }

    public void read_nbt(ItemStack stack){
        NbtCompound nbtCompound = stack.getNbt();
        if(nbtCompound != null) this.temp = nbtCompound.getDouble(TEMP_KEY);
    }
    public final TagKey<Block> SHOVEL_MINEABLE = TagKey.of(RegistryKeys.BLOCK, new Identifier("minecraft","mineable/shovel"));

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if(state.isIn(SHOVEL_MINEABLE) && world.getServer() != null && this.temp(stack) > 50){
            Identifier loot_table_id = state.getBlock().getLootTableId();
            LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
                if (loot_table_id.equals(id)) {
                    LootTable nuggets = world.getServer().getLootManager().getTable(new Identifier("fberb", "blocks/spade"));
                    Arrays.stream(nuggets.pools).toList().forEach(tableBuilder::pool);
                }
            });
        }
        return super.postMine(stack, world, state, pos, miner);
    }



    public void write_nbt(ItemStack stack){
        NbtCompound nbtCompound = stack.getNbt();
        if(nbtCompound != null) nbtCompound.putDouble(TEMP_KEY, this.temp);
    }
}
