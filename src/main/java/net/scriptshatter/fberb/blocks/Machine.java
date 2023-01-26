package net.scriptshatter.fberb.blocks;


import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.components.Machine_anim_int;
import net.scriptshatter.fberb.components.Machine_parts;
import net.scriptshatter.fberb.networking.packets.Set_status_C2S_packet;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.HashMap;

public class Machine extends BlockEntity implements IAnimatable, ISyncable{
    private static final String controllerName = "processController";

    private final AnimationBuilder loop = new AnimationBuilder().addAnimation("animation.machine.loop", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
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
        animationData.addAnimationController(new AnimationController<>
                (this, controllerName, 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event){
        if(Bird_parts.INV.get(this).get_status().matches("start")){
            if(world != null && world.isClient()){
                Set_status_C2S_packet.sync_status("loop", pos);
            }
            event.getController().setAnimation(start);
        } else if (Bird_parts.INV.get(this).get_status().matches("loop") && !event.getController().getCurrentAnimation().animationName.matches("animation.machine.start")) {
            if(world != null && world.isClient()){
                Set_status_C2S_packet.sync_status("end", pos);
            }
            for (int i = 0; i < 3; i++) {
                event.getController().setAnimation(loop);
            }
        }
        else if (Bird_parts.INV.get(this).get_status().matches("end") && !event.getController().getCurrentAnimation().animationName.matches("animation.machine.loop")) {
            if(world != null && world.isClient()){
                Set_status_C2S_packet.sync_status("idle", pos);
            }
            event.getController().setAnimation(end);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
        if(state == ANIM_START){
            controller.setAnimation(start);
        }
        else if (state == ANIM_LOOP){
            controller.setAnimation(loop);
        }
        else if(state == ANIM_END){
            controller.setAnimation(end);
        }
    }

    public static <E extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, E e) {
        // Recreate the function in  private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) here
    }
}
