package net.scriptshatter.fberb.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.util.Ect;

public class Phoenix_axe extends MiningToolItem implements Birb_item {
    public static final String TEMP_KEY = "temp";
    int max_temp;
    double temp;

    @Override
    public int max_temp() {
        return this.max_temp;
    }

    @Override
    public double temp(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if(nbtCompound != null) return nbtCompound.getDouble(TEMP_KEY);
        else return 0;
    }

    @Override
    public int getItemBarColorTemp(ItemStack stack) {
        float f = Math.max(0.0F, ((float)this.max_temp - (float)this.temp(stack)) / (float)this.max_temp);
        return MathHelper.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }
    @Override
    public int getItemBarStepTemp(ItemStack stack){
        return Math.round(((float)this.temp(stack) * 13.0F )/ (float)this.max_temp());
    }
    public Phoenix_axe(float attackDamage, float attackSpeed, ToolMaterial material, TagKey<Block> effectiveBlocks, int max_temp, Settings settings) {
        super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
        this.max_temp = max_temp;
        this.temp = 0;
    }

    public void read_nbt(ItemStack stack){
        NbtCompound nbtCompound = stack.getNbt();
        if(nbtCompound != null) this.temp = nbtCompound.getDouble(TEMP_KEY);
    }

    public void write_nbt(ItemStack stack){
        NbtCompound nbtCompound = stack.getNbt();
        if(nbtCompound != null) nbtCompound.putDouble(TEMP_KEY, this.temp);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        ItemStack itemStack = miner.getStackInHand(miner.getActiveHand());
        if(!world.isClient() && Ect.has_origin(miner, Ect.FIRE_BIRD)){
            read_nbt(itemStack);
            this.change_temp(1);
            write_nbt(itemStack);
            miner.sendMessage(Text.literal(String.valueOf(this.temp)));
        }
        return super.postMine(stack, world, state, pos, miner);
    }

    public void change_temp(double amount) {
        if(this.temp + amount < 0){
            this.temp = 0;
        } else if (this.temp + amount > this.max_temp) {
            this.temp = this.max_temp;
        }
        else this.temp += amount;
    }



}
