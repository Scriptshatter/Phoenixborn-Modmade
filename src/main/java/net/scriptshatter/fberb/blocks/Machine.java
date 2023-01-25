package net.scriptshatter.fberb.blocks;


import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.components.Machine_anim_int;
import net.scriptshatter.fberb.components.Machine_parts;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.HashMap;

public class Machine extends BlockEntity implements IAnimatable {

    private final AnimationBuilder loop = new AnimationBuilder().addAnimation("animation.machine.loop", ILoopType.EDefaultLoopTypes.LOOP);
    private final AnimationBuilder start = new AnimationBuilder().addAnimation("animation.machine.start", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);
    private final AnimationBuilder end = new AnimationBuilder().addAnimation("animation.machine.end", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public Machine(BlockPos pos, BlockState state) {
        super(Phoenix_block_entities.MACHINE, pos, state);
        Phoenix.LOGGER.info("RESET");
    }

    public void write(NbtCompound nbtCompound){
        writeNbt(nbtCompound);
    }


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>
                (this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event){
        //event.getController().setAnimation(loop);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
