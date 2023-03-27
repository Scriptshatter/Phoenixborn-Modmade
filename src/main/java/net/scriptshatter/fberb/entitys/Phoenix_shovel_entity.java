package net.scriptshatter.fberb.entitys;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.items.Items;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Phoenix_shovel_entity extends PersistentProjectileEntity implements GeoEntity {

    private ItemStack phoenix_stack;
    private int life;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final TrackedData<Boolean> ENCHANTED;

    static {
        ENCHANTED = DataTracker.registerData(Phoenix_shovel_entity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    public Phoenix_shovel_entity(EntityType<? extends Phoenix_shovel_entity> entityType, World world) {
        super(entityType, world);
        this.phoenix_stack = new ItemStack(Items.PHOENIX_SHOVEL);
    }

    public Phoenix_shovel_entity(World world, LivingEntity owner, ItemStack item) {
        super(Entity_registry.PHOENIX_SHOVEL_ENTITY, owner, world);
        this.phoenix_stack = item.copy();
        this.dataTracker.set(ENCHANTED, item.hasGlint());
    }

    @Override
    protected ItemStack asItemStack() {
        return this.phoenix_stack;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_AMETHYST_BLOCK_HIT;
    }

    public boolean isEnchanted() {
        return this.dataTracker.get(ENCHANTED);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ENCHANTED, false);
    }

    @Override
    public void tick() {
        super.tick();
    }

    /*public void age() {
        ++this.life;
        if (this.life >= 1200) {
            this.discard();
        }

    }*/

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Phoenix_shovel", NbtElement.COMPOUND_TYPE)) {
            this.phoenix_stack = ItemStack.fromNbt(nbt.getCompound("Phoenix_shovel"));
        }
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Phoenix_shovel", this.phoenix_stack.writeNbt(new NbtCompound()));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
