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
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.HashMap;

public class Machine extends BlockEntity implements IAnimatable{
    private static final String controllerName = "processController";

    private final AnimationBuilder loop = new AnimationBuilder().addAnimation("animation.machine.loop", ILoopType.EDefaultLoopTypes.LOOP);
    private final AnimationBuilder start = new AnimationBuilder().addAnimation("animation.machine.start", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    private final AnimationBuilder end = new AnimationBuilder().addAnimation("animation.machine.end", ILoopType.EDefaultLoopTypes.PLAY_ONCE);

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private final int ANIM_START = 0;

    private final int ANIM_LOOP = 1;

    private final int ANIM_END = 2;

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


    @Override
    public void registerControllers(AnimationData animationData) {
        AnimationController<Machine> controller = new AnimationController<>(this, controllerName, 0, this::predicate);

        animationData.addAnimationController(controller);
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
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event){

        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


    public static <E extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, E e) {
        Machine block = Phoenix_block_entities.MACHINE.get(world, pos);

        if(block != null && !world.isClient()){
            // Put value changing stuff here.
        }
    }

    public static void clientTick(World world, BlockPos blockPos, BlockState blockState, Machine machine) {
    }
}
