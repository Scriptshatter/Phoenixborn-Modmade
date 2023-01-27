package net.scriptshatter.fberb.blocks;


import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.components.Bird_parts;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.HashMap;

public class Machine extends BlockEntity implements GeoBlockEntity {
    private static final String turn_wheel_controller_name = "turn_wheel_controller";

    private static final String grid_controller_name = "grid_controller";

    private static final RawAnimation TURN_WHEEL = RawAnimation.begin().thenLoop("animation.machine.loop");


    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public Machine(BlockPos pos, BlockState state) {
        super(Phoenix_block_entities.MACHINE, pos, state);
    }

    public void setInventory(HashMap<Integer, ItemStack> inventory){
        for (int i = 0; i < inventory.size(); i++) {
            inventory.put(i, Bird_parts.INV.get(this).get_inv().get(i));
        }
    }

    public void write(NbtCompound nbtCompound){
        writeNbt(nbtCompound);
    }


    // Maybe separate the opening/closing animations and play the spinning animations separately? could save you a lot of headache and just make the thing look better.
    // Namely, always have the spinning animation running and just change the speed with it being spun
    // And for the opening/closing animation, whenever the wheel starts to spin trigger the animation and when the closing animation if finished start a timer that goes down depending on the speed of the turnwheel/gears.
    // Then, when the timer is done, run the open animation and have the result come out of the top.
    // https://github.com/bernie-g/geckolib/wiki/Animation-Basics This should help with any questions.
    // You're welcome, future me! I'm sorry that in the 5 F**KING HOURS I spent trying to get these stupid animations to work I came up with something so much better.
    // OH, also have the speed and timer be NBT values so that you can use tick to change them, and just have the predicate function read them.
    // Good luck!
    // Oh, and register a sound event. For the wheel, furnace sounds, ect.
    // Just worry about getting the animation done. The rest shouldn't be as painful (hopefully)


    public static <E extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, E e) {
        Machine block = Phoenix_block_entities.MACHINE.get(world, pos);

        if(block != null){
            //Bird_parts.INV.get(block).change_being_used(-1);
            Phoenix.LOGGER.info("Is being turned: " + Bird_parts.INV.get(block).being_used());
            if(Bird_parts.INV.get(block).being_used()){
                Bird_parts.INV.get(block).change_speed(0.05);
                Bird_parts.INV.get(block).change_time((int) -(Bird_parts.INV.get(block).get_speed()*10));
            }
            // Put value changing stuff here.
        }
    }

    public static void clientTick(World world, BlockPos blockPos, BlockState blockState, Machine machine) {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<Machine> turn_wheel = new AnimationController<>(this, state -> state.setAndContinue(TURN_WHEEL)).setAnimationSpeed(Bird_parts.INV.get(this).get_speed());
        turn_wheel.setAnimationSpeed(Bird_parts.INV.get(this).get_speed());
        Phoenix.LOGGER.info("Is turning at: " + Bird_parts.INV.get(this).being_used());
        controllerRegistrar.add(turn_wheel);
        controllerRegistrar.add();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }


}
