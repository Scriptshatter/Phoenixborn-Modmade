package net.scriptshatter.fberb.entitys;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.items.Items;
import net.scriptshatter.fberb.items.Phoenix_axe;
import net.scriptshatter.fberb.networking.packets.Check_axe_entity_temp_S2C;
import net.scriptshatter.fberb.util.Dmg_sources;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Phoenix_axe_entity extends PersistentProjectileEntity implements GeoEntity {
    private ItemStack phoenix_stack;
    private boolean dealtDamage;
    private static final TrackedData<Boolean> ENCHANTED;
    public int returnTimer;

    public Phoenix_axe_entity(EntityType<? extends Phoenix_axe_entity> entityType, World world) {
        super(entityType, world);
        this.phoenix_stack = new ItemStack(Items.PHOENIX_AXE);
    }

    public Phoenix_axe_entity(World world, LivingEntity owner, ItemStack item) {
        super(Entity_registry.PHOENIX_AXE_ENTITY, owner, world);
        this.phoenix_stack = item.copy();
        this.dataTracker.set(ENCHANTED, item.hasGlint());
    }

    public boolean isEnchanted() {
        return this.dataTracker.get(ENCHANTED);
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    public boolean should_spin(){
        return !this.inGround || this.returnTimer > 0;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float atk = 8.0F;
        if (entity instanceof LivingEntity livingEntity) {
            atk += EnchantmentHelper.getAttackDamage(this.phoenix_stack, livingEntity.getGroup());
        }
        Entity owner = this.getOwner();
        DamageSource damageSource = Dmg_sources.phoenix_axe(this, (Entity)(owner == null ? this : owner));
        this.dealtDamage = true;
        // Temp sound effect
        SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_HIT;

        if (entity.damage(damageSource, atk)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity livingEntity) {
                if(Items.PHOENIX_AXE.temp(this.phoenix_stack) >= 5){
                    livingEntity.setOnFireFor(5);
                    Items.PHOENIX_AXE.change_temp(-5, this.phoenix_stack);
                }
                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingEntity, owner);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)owner, livingEntity);
                }

                this.onHit(livingEntity);
            }
        }
        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));

        this.playSound(soundEvent, 1.0F, 1.0F);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ENCHANTED, false);
    }

    private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4 && !(Items.PHOENIX_AXE.temp(this.asItemStack()) < 10 && this.returnTimer == 0)) {
            this.dealtDamage = true;
        }
        Entity entity = this.getOwner();
        if((this.dealtDamage || this.isNoClip()) && entity != null){
            if(!this.world.isClient() && (!this.isOwnerAlive() || (Items.PHOENIX_AXE.temp(this.asItemStack()) < 10 && this.returnTimer == 0))){
                if (!this.world.isClient && this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                    this.dropStack(this.asItemStack(), 0.1F);
                }

                Check_axe_entity_temp_S2C.axe_temp(this.getId(), (ServerPlayerEntity) entity);
                this.discard();
            }
            else {
                this.setNoClip(true);
                Vec3d vec3d = entity.getEyePos().subtract(this.getPos());
                this.setPos(this.getX(), this.getY() + vec3d.y * 0.015 * 2, this.getZ());
                if (this.world.isClient) {
                    this.lastRenderY = this.getY();
                }

                double d = 0.05 * 2;
                this.setVelocity(this.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(d)));
                if (this.returnTimer == 0) {
                    // Temp sound effect
                    this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                    Items.PHOENIX_AXE.change_temp(-10, this.phoenix_stack);
                }

                ++this.returnTimer;
            }
        }
        super.tick();
    }

    @Override
    protected ItemStack asItemStack() {
        return this.phoenix_stack.copy();
    }

    public ItemStack get_item_stack() {
        return this.phoenix_stack.copy();
    }
    protected boolean tryPickup(PlayerEntity player) {
        return super.tryPickup(player) || this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(this.asItemStack());
    }

    protected SoundEvent getHitSound() {
        // Temp sound
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    public void onPlayerCollision(PlayerEntity player) {
        if (this.isOwner(player) || this.getOwner() == null) {
            super.onPlayerCollision(player);
        }

    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Trident", NbtElement.COMPOUND_TYPE)) {
            this.phoenix_stack = ItemStack.fromNbt(nbt.getCompound("Phoenix_axe"));
        }
        this.dealtDamage = nbt.getBoolean("DealtDamage");
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Phoenix_axe", this.phoenix_stack.writeNbt(new NbtCompound()));
        nbt.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void age() {
        if (this.pickupType != PersistentProjectileEntity.PickupPermission.ALLOWED) {
            super.age();
        }

    }
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }
    static {
        ENCHANTED = DataTracker.registerData(Phoenix_axe_entity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
