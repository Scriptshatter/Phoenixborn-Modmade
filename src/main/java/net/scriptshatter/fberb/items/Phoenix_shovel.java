package net.scriptshatter.fberb.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.scriptshatter.fberb.util.Phoenix_use_actions;

public class Phoenix_shovel extends ShovelItem implements Birb_item{
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
    TagKey<Block> blocks = BlockTags.SHOVEL_MINEABLE;
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(context.getPlayer() != null && context.getPlayer().isSneaking() && context.getWorld().getBlockState(context.getBlockPos()).isIn(blocks)){
            //ItemUsageContext usageContext = new ItemUsageContext(context.getWorld(), context.getPlayer(), context.getHand(), context.getStack(), new BlockHitResult(new Vec3d(context.getBlockPos().getX()+0.5, context.getBlockPos().getY()+1, context.getBlockPos().getZ()+0.5), context.getSide(), context.getBlockPos().up(), context.hitsInsideBlock()));
            //ItemPlacementContext placementContext = new ItemPlacementContext(usageContext);

            Items.PHOENIX_BLOCK_SHOVEL.useOnBlock(context);
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }


    public void write_nbt(ItemStack stack){
        NbtCompound nbtCompound = stack.getNbt();
        if(nbtCompound != null) nbtCompound.putDouble(TEMP_KEY, this.temp);
    }
}
