package net.scriptshatter.fberb.actions;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.blocks.Machine;
import net.scriptshatter.fberb.blocks.Machine_anim;
import net.scriptshatter.fberb.blocks.Phoenix_block_entities;
import net.scriptshatter.fberb.blocks.Phoenix_blocks;
import net.scriptshatter.fberb.components.Bird_parts;

public class Turnwheel_action {

    public static void action(SerializableData.Instance data, Entity entity) {
            Vec3d origin = new Vec3d(entity.getX(), entity.getEyeY(), entity.getZ());
            Vec3d direction = entity.getRotationVec(1);
            Vec3d target = origin.add(direction.multiply(5));

            BlockHitResult blockHit = performBlockRaycast(entity, origin, target);

            Machine block = Phoenix_block_entities.MACHINE.get(entity.world, blockHit.getBlockPos());

            BlockState blockState = entity.world.getBlockState(blockHit.getBlockPos());

            if (block == null) {
                return;
            }

            if(blockHit.getSide().equals(blockState.get(Machine_anim.FACING).rotateYCounterclockwise())){
                Bird_parts.INV.get(block).set_being_used(3);
            }
            // Add crafting here after done with animation
    }

    private static BlockHitResult performBlockRaycast(Entity source, Vec3d origin, Vec3d target) {
        RaycastContext context = new RaycastContext(origin, target, RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.ANY, source);
        return source.world.raycast(context);
    }

    //Makes the action into an action
    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(new Identifier(Phoenix.MOD_ID, "flame_throw"),
                new SerializableData(),
                Turnwheel_action::action
        );
    }
}
