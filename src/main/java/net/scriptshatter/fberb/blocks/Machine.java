package net.scriptshatter.fberb.blocks;


import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.networking.packets.Get_machine_inv_S2C_packet;
import net.scriptshatter.fberb.networking.packets.Set_craft_timer_C2S;
import net.scriptshatter.fberb.networking.packets.Set_status_C2S;
import net.scriptshatter.fberb.recipe.Turn_blast_recipe;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;
import java.util.Optional;

public class Machine extends BlockEntity implements GeoBlockEntity{
    private static final String grid_controller_name = "grid_controller";

    private static final RawAnimation CLOSE_GRID = RawAnimation.begin().thenPlay("animation.machine.start");
    private static final RawAnimation LOOP_GRID = RawAnimation.begin().thenLoop("animation.machine.start_loop");
    private static final RawAnimation OPEN_GRID = RawAnimation.begin().thenPlay("animation.machine.end");
    private static final RawAnimation SPIT_ITEM = RawAnimation.begin().thenPlay("animation.machine.spit");

    public void markDerty() {
        super.markDirty();
    }

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public Machine(BlockPos pos, BlockState state) {
        super(Phoenix_block_entities.MACHINE, pos, state);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
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
            Bird_parts.INV.get(block).change_being_used(-1);
            //Phoenix.LOGGER.info("Is being turned: " + Bird_parts.INV.get(block).being_used());
            if(Bird_parts.INV.get(block).being_used()){
                Bird_parts.INV.get(block).change_speed(0.005f);
                if(Bird_parts.INV.get(block).get_status().matches("crafting")){
                    Bird_parts.INV.get(block).change_time(-(Bird_parts.INV.get(block).get_speed()/0.25f));
                    Phoenix.LOGGER.info("Time left: " + Bird_parts.INV.get(block).get_time());
                }
            }
            else{
                Bird_parts.INV.get(block).change_speed(-0.01f);
            }
            // Put value changing stuff here.
            if(Bird_parts.INV.get(block).get_status().matches("idle")){
                if(Bird_parts.INV.get(block).has_items_crafted()) block.spit_item();
                else if(Bird_parts.INV.get(block).get_speed() >= 0.23){
                    Optional<Turn_blast_recipe> match = world.getRecipeManager().getFirstMatch(Turn_blast_recipe.Type.INSTANCE, Bird_parts.INV.get(block), world);
                    if (match.isPresent()) {
                        Bird_parts.INV.get(block).set_status("animating");
                        block.triggerAnim("close_grid_controller", "close_grid");
                    }
                }
            }
            if (Bird_parts.INV.get(block).get_status().matches("crafting") && Bird_parts.INV.get(block).get_time() <= 0){
                Optional<Turn_blast_recipe> match = world.getRecipeManager().getFirstMatch(Turn_blast_recipe.Type.INSTANCE, Bird_parts.INV.get(block), world);
                if(match.isPresent()){
                    ItemStack result = match.get().getOutput().copy();
                    Bird_parts.INV.get(block).clear();
                    Bird_parts.INV.get(block).craft_item(result);
                    Bird_parts.INV.get(block).set_status("animating");
                    block.triggerAnim("open_grid_controller", "open_grid");
                }
                else Bird_parts.INV.get(block).set_status("idle");
            }
        }
    }

    public static void clientTick(World world, BlockPos blockPos, BlockState blockState, Machine machine) {

    }



    public void spit_item(){
        this.triggerAnim("chute", "spit_item");
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<Machine> close_grid = new AnimationController<>(this, "close_grid_controller",  state -> PlayState.STOP).triggerableAnim("close_grid", CLOSE_GRID).setCustomInstructionKeyframeHandler(event -> {
            Set_craft_timer_C2S.timer(this.pos);
            Set_status_C2S.set_status(this.pos, "crafting");
            event.getController().stop();
        });
        AnimationController<Machine> loop_grid = new AnimationController<>(this, "loop_grid_controller", state -> {
            if(Bird_parts.INV.get(this).get_status().matches("crafting")){
                return state.setAndContinue(LOOP_GRID);
            }
           return PlayState.STOP;
        });
        AnimationController<Machine> open_grid = new AnimationController<>(this, "open_grid_controller",  state -> PlayState.STOP).triggerableAnim("open_grid", OPEN_GRID).setCustomInstructionKeyframeHandler(event -> {
            Set_status_C2S.set_status(this.pos, "idle");
            event.getController().stop();
        });
        AnimationController<Machine> spit_item = new AnimationController<>(this, "chute", state -> PlayState.STOP).triggerableAnim("spit_item", SPIT_ITEM).setCustomInstructionKeyframeHandler(spit_event -> Get_machine_inv_S2C_packet.item_spit(this.pos));
        controllerRegistrar.add(close_grid);
        controllerRegistrar.add(open_grid);
        controllerRegistrar.add(spit_item);
        controllerRegistrar.add(loop_grid);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }


}
