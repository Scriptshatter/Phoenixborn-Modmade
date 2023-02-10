package net.scriptshatter.fberb.entitys;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.scriptshatter.fberb.items.Items;

public class Phoenix_axe_entity extends PersistentProjectileEntity {
    private final ItemStack phoenix_stack;
    private boolean dealtDamage;
    public int returnTimer;

    public Phoenix_axe_entity(EntityType<? extends Phoenix_axe_entity> entityType, World world) {
        super(entityType, world);
        this.phoenix_stack = new ItemStack(Items.PHOENIX_AXE);
    }

    public Phoenix_axe_entity(ItemStack item, LivingEntity owner, World world) {
        super(Entity_registry.PHOENIX_AXE, owner, world);
        this.phoenix_stack = item.copy();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
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
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        Entity entity = this.getOwner();
        if((this.dealtDamage || this.isNoClip()) && entity != null){
            if(!this.isOwnerAlive()){
                if (!this.world.isClient && this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                    this.dropStack(this.asItemStack(), 0.1F);
                }

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
                    this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.returnTimer;
            }
        }
        super.tick();
    }

    @Override
    protected ItemStack asItemStack() {
        return phoenix_stack;
    }
}
