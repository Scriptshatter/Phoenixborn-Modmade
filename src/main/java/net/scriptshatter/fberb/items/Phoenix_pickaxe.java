package net.scriptshatter.fberb.items;

import io.github.apace100.apoli.util.Space;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.loot.context.LootContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.events.Temp_control;
import net.scriptshatter.fberb.util.Ect;
import net.scriptshatter.fberb.util.Phoenix_use_actions;
import org.joml.Vector3f;

import java.util.Locale;

public class Phoenix_pickaxe extends PickaxeItem implements Birb_item{

    public static final String TEMP_KEY = "temp";
    private final int max_temp;
    private double temp;

    public Phoenix_pickaxe(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings, int max_temp) {
        super(material, attackDamage, attackSpeed, settings);
        this.max_temp = max_temp;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public Phoenix_use_actions get_use_case(PlayerEntity user) {
        return Phoenix_use_actions.PHOENIX_AXE;
    }

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
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(!world.isClient && user.isSneaking() && Bird_parts.TEMP.get(user).get_temp() > 50 && Ect.has_origin(user, Ect.FIRE_BIRD)){
            if(this.get_temp(itemStack) < this.max_temp()){
                Bird_parts.TEMP.get(user).change_temp(-5);
                this.change_temp(5, itemStack);
                return TypedActionResult.pass(itemStack);
            }
        }
        else if(!world.isClient){
            Bird_parts.TEMP.get(user).set_rage(20);
            BlockPos og_point = user.getBlockPos().offset(user.getHorizontalFacing(), 1);
            Temp_control.destroy_blocks(og_point, user.getHorizontalFacing(), 2, 3, 4).forEach(blockPos -> {
                world.breakBlock(blockPos, true);
            });
        }
        Vector3f vec = new Vector3f(0, 0.5f, 2);
        Space.LOCAL.toGlobal(vec, user);
        user.addVelocity(vec.x, vec.y, vec.z);
        return super.use(world, user, hand);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if(miner.isSneaking() && !world.isClient() && this.temp(stack) >= 5){
            this.change_temp(-5, stack);
        }
        return super.postMine(stack, world, state, pos, miner);
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

    public void read_nbt(ItemStack stack){
        NbtCompound nbtCompound = stack.getNbt();
        if(nbtCompound != null) this.temp = nbtCompound.getDouble(TEMP_KEY);
    }

    public void write_nbt(ItemStack stack){
        NbtCompound nbtCompound = stack.getNbt();
        if(nbtCompound != null) nbtCompound.putDouble(TEMP_KEY, this.temp);
    }

    public double get_temp(ItemStack itemStack){
        read_nbt(itemStack);
        return this.temp;
    }

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
}
