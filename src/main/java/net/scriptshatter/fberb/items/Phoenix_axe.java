package net.scriptshatter.fberb.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.loot.context.LootContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.Phoenix_client;
import net.scriptshatter.fberb.entitys.Phoenix_axe_entity;
import net.scriptshatter.fberb.util.Ect;
import net.scriptshatter.fberb.util.Phoenix_use_actions;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Phoenix_axe extends AxeItem implements Birb_item {
    private final int max_temp;
    private double temp;
    private final List<BlockPos> blocks_to_be_broken = new ArrayList<>();

    @Override
    public Phoenix_use_actions get_use_case(PlayerEntity user) {
        return Phoenix_use_actions.PHOENIX_AXE;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.fberb.phoenix_shovel.aoe").formatted(Formatting.DARK_GRAY));
        tooltip.add(Text.translatable("tooltip.fberb.phoenix_shovel.gravel").formatted(Formatting.DARK_GRAY));
        super.appendTooltip(stack, world, tooltip, context);
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
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity){
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (!world.isClient && i >= 10) {
                stack.damage(1, playerEntity, ((p) -> p.sendToolBreakStatus(user.getActiveHand())));
                Phoenix_axe_entity tridentEntity = new Phoenix_axe_entity(world, playerEntity, stack);
                tridentEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2.5F, 1.0F);
                if (playerEntity.getAbilities().creativeMode) {
                    tridentEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                }
                world.spawnEntity(tridentEntity);
                world.playSoundFromEntity(null, tridentEntity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                if (!playerEntity.getAbilities().creativeMode) {
                    playerEntity.getInventory().removeOne(stack);
                }
            }
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(!world.isClient){
            if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
                return TypedActionResult.fail(itemStack);
            } else {
                user.setCurrentHand(hand);
                return TypedActionResult.consume(itemStack);
            }
        }
        return super.use(world, user, hand);
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
    public Phoenix_axe(float attackDamage, float attackSpeed, ToolMaterial material, int max_temp, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
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

    private void check_blocks_around(World world, BlockPos blockPos, Block block, int exclude_check){
        if(exclude_check == 0){
            blocks_to_be_broken.clear();
        }
        if(world.getBlockState(blockPos.up(1)).isOf(block) && exclude_check != 2 && !blocks_to_be_broken.contains(blockPos.up(1))){
            blocks_to_be_broken.add(blockPos.up(1));
            check_blocks_around(world, blockPos.up(1), block, 1);
        }
        if (world.getBlockState(blockPos.down(1)).isOf(block) && exclude_check != 1 && !blocks_to_be_broken.contains(blockPos.down(1))) {
            blocks_to_be_broken.add(blockPos.down(1));
            check_blocks_around(world, blockPos.down(1), block, 2);
        }
        if (world.getBlockState(blockPos.north(1)).isOf(block) && exclude_check != 4 && !blocks_to_be_broken.contains(blockPos.north(1))) {
            blocks_to_be_broken.add(blockPos.north(1));
            check_blocks_around(world, blockPos.north(1), block, 3);
        }
        if (world.getBlockState(blockPos.south(1)).isOf(block) && exclude_check != 3 && !blocks_to_be_broken.contains(blockPos.south(1))) {
            blocks_to_be_broken.add(blockPos.south(1));
            check_blocks_around(world, blockPos.south(1), block, 4);
        }
        if (world.getBlockState(blockPos.west(1)).isOf(block) && exclude_check != 6 && !blocks_to_be_broken.contains(blockPos.west(1))) {
            blocks_to_be_broken.add(blockPos.west(1));
            check_blocks_around(world, blockPos.west(1), block, 5);
        }
        if (world.getBlockState(blockPos.east(1)).isOf(block) && exclude_check != 5 && !blocks_to_be_broken.contains(blockPos.east(1))) {
            blocks_to_be_broken.add(blockPos.east(1));
            check_blocks_around(world, blockPos.east(1), block, 6);
        }

        if(world.getBlockState(blockPos.up().east()).isOf(block) && exclude_check != 8 && !blocks_to_be_broken.contains(blockPos.up().east())){
            blocks_to_be_broken.add(blockPos.up().east());
            check_blocks_around(world, blockPos.up(1).east(), block, 7);
        }
        if (world.getBlockState(blockPos.down(1).west(1)).isOf(block) && exclude_check != 7 && !blocks_to_be_broken.contains(blockPos.down().west())) {
            blocks_to_be_broken.add(blockPos.down(1).west(1));
            check_blocks_around(world, blockPos.down(1).west(), block, 8);
        }

        if(world.getBlockState(blockPos.up(1).west(1)).isOf(block) && exclude_check != 10 && !blocks_to_be_broken.contains(blockPos.up().west())){
            blocks_to_be_broken.add(blockPos.up(1).west(1));
            check_blocks_around(world, blockPos.up(1).west(), block, 9);
        }
        if (world.getBlockState(blockPos.down(1).east(1)).isOf(block) && exclude_check != 9 && !blocks_to_be_broken.contains(blockPos.down().east())) {
            blocks_to_be_broken.add(blockPos.down(1).east(1));
            check_blocks_around(world, blockPos.down(1).east(), block, 10);
        }

        if(world.getBlockState(blockPos.up(1).north(1)).isOf(block) && exclude_check != 12 && !blocks_to_be_broken.contains(blockPos.up().north())){
            blocks_to_be_broken.add(blockPos.up(1).north(1));
            check_blocks_around(world, blockPos.up(1).north(), block, 11);
        }
        if (world.getBlockState(blockPos.down(1).south(1)).isOf(block) && exclude_check != 11 && !blocks_to_be_broken.contains(blockPos.down().south())) {
            blocks_to_be_broken.add(blockPos.down(1).south(1));
            check_blocks_around(world, blockPos.down(1).south(), block, 12);
        }

        if(world.getBlockState(blockPos.up(1).south(1)).isOf(block) && exclude_check != 14 && !blocks_to_be_broken.contains(blockPos.up().south())){
            blocks_to_be_broken.add(blockPos.up(1).south(1));
            check_blocks_around(world, blockPos.up(1).south(), block, 13);
        }
        if (world.getBlockState(blockPos.down(1).north(1)).isOf(block) && exclude_check != 13 && !blocks_to_be_broken.contains(blockPos.down().north())) {
            blocks_to_be_broken.add(blockPos.down(1).north(1));
            check_blocks_around(world, blockPos.down(1).north(), block, 14);
        }

        if(world.getBlockState(blockPos.east(1).south(1)).isOf(block) && exclude_check != 16 && !blocks_to_be_broken.contains(blockPos.east().south())){
            blocks_to_be_broken.add(blockPos.east(1).south(1));
            check_blocks_around(world, blockPos.east(1).south(), block, 15);
        }
        if (world.getBlockState(blockPos.west(1).north(1)).isOf(block) && exclude_check != 15 && !blocks_to_be_broken.contains(blockPos.west().north())) {
            blocks_to_be_broken.add(blockPos.west(1).north(1));
            check_blocks_around(world, blockPos.west(1).north(), block, 16);
        }

        if(world.getBlockState(blockPos.east(1).north(1)).isOf(block) && exclude_check != 18 && !blocks_to_be_broken.contains(blockPos.east().north())){
            blocks_to_be_broken.add(blockPos.east(1).north(1));
            check_blocks_around(world, blockPos.east(1).north(1), block, 17);
        }
        if (world.getBlockState(blockPos.west(1).south(1)).isOf(block) && exclude_check != 17 && !blocks_to_be_broken.contains(blockPos.west().south())) {
            blocks_to_be_broken.add(blockPos.west(1).south(1));
            check_blocks_around(world, blockPos.west(1).south(1), block, 18);
        }

        if(world.getBlockState(blockPos.east(1).north(1).up(1)).isOf(block) && exclude_check != 20 && !blocks_to_be_broken.contains(blockPos.east().north().up())){
            blocks_to_be_broken.add(blockPos.east(1).north(1).up(1));
            check_blocks_around(world, blockPos.east(1).north(1).up(1), block, 19);
        }
        if (world.getBlockState(blockPos.west(1).south(1).down(1)).isOf(block) && exclude_check != 19 && !blocks_to_be_broken.contains(blockPos.west().south().down())) {
            blocks_to_be_broken.add(blockPos.west(1).south(1).down(1));
            check_blocks_around(world, blockPos.west(1).south(1).down(1), block, 20);
        }

        if(world.getBlockState(blockPos.west(1).south(1).up(1)).isOf(block) && exclude_check != 22 && !blocks_to_be_broken.contains(blockPos.west().south().up())){
            blocks_to_be_broken.add(blockPos.west(1).south(1).up(1));
            check_blocks_around(world, blockPos.west(1).south(1).up(1), block, 21);
        }
        if (world.getBlockState(blockPos.east(1).north(1).down(1)).isOf(block) && exclude_check != 21 && !blocks_to_be_broken.contains(blockPos.east().north().down())) {
            blocks_to_be_broken.add(blockPos.east(1).north(1).down(1));
            check_blocks_around(world, blockPos.east(1).north(1).down(1), block, 22);
        }

        if(world.getBlockState(blockPos.east(1).south(1).up(1)).isOf(block) && exclude_check != 24 && !blocks_to_be_broken.contains(blockPos.east().south().up())){
            blocks_to_be_broken.add(blockPos.east(1).south(1).up(1));
            check_blocks_around(world, blockPos.east(1).south(1).up(1), block, 23);
        }
        if (world.getBlockState(blockPos.west(1).north(1).down(1)).isOf(block) && exclude_check != 23 && !blocks_to_be_broken.contains(blockPos.west().north().down())) {
            blocks_to_be_broken.add(blockPos.west(1).north(1).down(1));
            check_blocks_around(world, blockPos.west(1).north(1).down(1), block, 24);
        }

        if(world.getBlockState(blockPos.east(1).south(1).down(1)).isOf(block) && exclude_check != 26 && !blocks_to_be_broken.contains(blockPos.east().south().down())){
            blocks_to_be_broken.add(blockPos.east(1).south(1).down(1));
            check_blocks_around(world, blockPos.east(1).south(1).down(1), block, 25);
        }
        if (world.getBlockState(blockPos.west(1).north(1).up(1)).isOf(block) && exclude_check != 25 && !blocks_to_be_broken.contains(blockPos.west().north().up())) {
            blocks_to_be_broken.add(blockPos.west(1).north(1).up(1));
            check_blocks_around(world, blockPos.west(1).north(1).up(1), block, 26);
        }
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if(!world.isClient() && miner.isSneaking() && state.isIn(BlockTags.AXE_MINEABLE) && miner.isPlayer()){
            if(this.temp(stack) <= 0){
                return super.postMine(stack, world, state, pos, miner);
            }
            check_blocks_around(world, pos, state.getBlock(), 0);
            blocks_to_be_broken.forEach(blockPos -> {
                if(this.temp(stack) <= 0){
                    return;
                }
                this.change_temp(-1, stack);
                state.getBlock().onBreak(world, blockPos, state, (PlayerEntity) miner);
                world.removeBlock(blockPos, false);
                state.getBlock().onBroken(world, blockPos, state);
                state.getBlock().afterBreak(world, (PlayerEntity) miner, blockPos, state, null, stack);
                //world.breakBlock(pos, false);

            });
        }
        return super.postMine(stack, world, state, pos, miner);
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
