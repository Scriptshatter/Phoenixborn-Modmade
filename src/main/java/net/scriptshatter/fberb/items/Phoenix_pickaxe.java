package net.scriptshatter.fberb.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.Phoenix_client;
import net.scriptshatter.fberb.util.Ect;
import net.scriptshatter.fberb.util.Phoenix_use_actions;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Phoenix_pickaxe extends PickaxeItem implements Birb_item{

    private final int max_temp;
    private double temp;

    private Block cur_block = Blocks.COAL_ORE;



    public Phoenix_pickaxe(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings, int max_temp) {
        super(material, attackDamage, attackSpeed, settings);
        this.max_temp = max_temp;
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
        read_nbt(stack);
        return this.temp;
    }

    public List<BlockPos> get_blocks(BlockPos pos, int x, int y, int z){
        List<BlockPos> list = new ArrayList<>();
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                for (int k = 0; k < z; k++) {
                    list.add(pos.up(i).west(j).south(k));
                    list.add(pos.up(i).east(j).south(k));
                    list.add(pos.up(i).west(j).north(k));
                    list.add(pos.up(i).east(j).north(k));
                    list.add(pos.down(i).west(j).south(k));
                    list.add(pos.down(i).east(j).south(k));
                    list.add(pos.down(i).west(j).north(k));
                    list.add(pos.down(i).east(j).north(k));
                }
            }
        }
        return list;
    }



    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(!world.isClient && this.temp(itemStack) >= 25 && !this.cur_block.equals(Blocks.COMMAND_BLOCK)){
            BlockPos og_point = user.getBlockPos();
            read_nbt(itemStack);
            get_blocks(og_point, 25, 25, 25).forEach(blockPos -> {
                if(world.getBlockState(blockPos).getBlock().equals(this.cur_block)){
                    ServerWorld serverWorld = (ServerWorld) world;
                    Vec3d origin = new Vec3d(user.getX() - 0.5, user.getEyeY() - 0.5, user.getZ() - 0.5);
                    Vec3d target = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                    double length = origin.distanceTo(target);
                    Vec3d direction = target.subtract(origin).normalize();
                    for(double current = 0; current < length; current += 1){
                        serverWorld.spawnParticles((ServerPlayerEntity) user, ParticleTypes.HAPPY_VILLAGER, true, origin.add(direction.multiply(current)).x + 0.5, origin.add(direction.multiply(current)).y + 0.5, origin.add(direction.multiply(current)).z + 0.5, 1, 0, 0, 0, 0);
                    }
                }
            });
            this.change_temp(-25, itemStack);
            user.getItemCooldownManager().set(this, 20);
            return TypedActionResult.success(itemStack, false);
        }
        // Vector3f vec = new Vector3f(0, 0.5f, 2);
        // Space.LOCAL.toGlobal(vec, user);
        // user.addVelocity(vec.x, vec.y, vec.z);
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(MinecraftClient.getInstance().player != null && Ect.has_origin(MinecraftClient.getInstance().player, Ect.FIRE_BIRD)){
            if(Screen.hasShiftDown()){
                tooltip.add(Text.translatable("tooltip.fberb.phoenix_pickaxe.scan").formatted(Formatting.DARK_GRAY));
                tooltip.add(Text.translatable("tooltip.fberb.phoenix_pickaxe.smelt").formatted(Formatting.DARK_GRAY));
                tooltip.add(Text.translatable("tooltip.fberb.phoenix_tool.charge").formatted(Formatting.DARK_GRAY).append(Text.keybind(Phoenix_client.power_tool.getTranslationKey().formatted(Formatting.DARK_GRAY))));
            }
            else{
                this.read_nbt(stack);
                if(this.cur_block.equals(Blocks.COMMAND_BLOCK)){
                    tooltip.add(Text.translatable("tooltip.fberb.phoenix_pickaxe.no_scan").formatted(Formatting.DARK_GRAY));
                }
                else{
                    tooltip.add(Text.translatable("tooltip.fberb.phoenix_pickaxe.cur_block").formatted(Formatting.DARK_GRAY).append(Text.translatable(cur_block.getTranslationKey()).formatted(Formatting.DARK_GRAY)));
                }
                tooltip.add(Text.translatable("tooltip.fberb.phoenix_axe.phoenixcrouch").formatted(Formatting.DARK_GRAY));
            }
        }

        super.appendTooltip(stack, world, tooltip, context);
    }

    public final TagKey<Block> PICK_DETECT = TagKey.of(RegistryKeys.BLOCK, new Identifier(Phoenix.MOD_ID, "pickaxe_selectable"));

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        read_nbt(context.getStack());
        if(context.getPlayer() != null && context.getPlayer().getWorld().getBlockState(context.getBlockPos()).isIn(PICK_DETECT)){
            this.cur_block = context.getPlayer().getWorld().getBlockState(context.getBlockPos()).getBlock();
        }
        write_nbt(context.getStack());
        return super.useOnBlock(context);
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
        if(nbtCompound != null) {
            this.temp = nbtCompound.getDouble(TEMP_KEY);
            if(nbtCompound.getString("cur_block").split(":").length <= 1){
                this.cur_block = Blocks.COMMAND_BLOCK;
                return;
            }
            this.cur_block = Registries.BLOCK.get(Identifier.of(nbtCompound.getString("cur_block").split(":")[0], nbtCompound.getString("cur_block").split(":")[1]));
        }
    }

    public void write_nbt(ItemStack stack){
        NbtCompound nbtCompound = stack.getNbt();
        if(nbtCompound != null) {
            nbtCompound.putDouble(TEMP_KEY, this.temp);
            nbtCompound.putString("cur_block", (Registries.BLOCK.getId(this.cur_block).getNamespace()) + ":" + (Registries.BLOCK.getId(this.cur_block).getPath()));
        }
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
