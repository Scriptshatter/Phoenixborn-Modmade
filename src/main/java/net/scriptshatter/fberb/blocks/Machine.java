package net.scriptshatter.fberb.blocks;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.ToIntFunction;

public class Machine extends BlockEntity implements IAnimatable {

    private final AnimationBuilder loop = new AnimationBuilder().addAnimation("animation.machine.loop", ILoopType.EDefaultLoopTypes.LOOP);
    private final AnimationBuilder start = new AnimationBuilder().addAnimation("animation.machine.start", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);
    private final AnimationBuilder end = new AnimationBuilder().addAnimation("animation.machine.end", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public Machine(BlockPos pos, BlockState state) {
        super(Phoenix_block_entities.MACHINE, pos, state);
    }


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<Machine>
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
