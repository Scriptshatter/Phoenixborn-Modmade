package net.scriptshatter.fberb.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.items.Birb_item;
import net.scriptshatter.fberb.items.TempAPI;
import net.scriptshatter.fberb.util.GetFuel;
import net.scriptshatter.fberb.util.Get_use_case;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
    @Final
    @Shadow
    private MinecraftClient client;

    @Shadow
    protected abstract void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress);

    @Shadow
    protected abstract void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress);

    @Shadow
    public abstract void renderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    private void lower_axe(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
        Birb_item berb_item = TempAPI.ITEM_WITH_TEMP.find(item, null);
        if(berb_item != null){
            float m;
            float f;
            float g;
            float h;
            float j;
            int l;
            matrices.push();
            boolean bl = hand == Hand.MAIN_HAND;
            Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
            boolean bl2 = arm == Arm.RIGHT;

            if (player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getActiveHand() == hand && client.player != null) {
                l = bl2 ? 1 : -1;
                switch (GetFuel.checkStatus((Get_use_case) (Object) item, player)) {
                    case DEFAULT -> matrices.pop();
                    case PHOENIX_AXE -> {
                        this.applyEquipOffset(matrices, arm, equipProgress);
                        matrices.translate((float) l * -0.8F, 0.18344387F, 0.7F);
                        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-13.935F));
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) l * 35.3F));
                        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) l * -9.785F));
                        m = (float) item.getMaxUseTime() - ((float) this.client.player.getItemUseTimeLeft() - tickDelta + 1.0F);
                        f = m / 20.0F;
                        f = (f * f + f * 2.0F) / 3.0F;
                        if (f > 1.0F) {
                            f = 1.0F;
                        }
                        if (f > 0.1F) {
                            g = MathHelper.sin((m - 0.1F) * 1.3F);
                            h = f - 0.1F;
                            j = g * h;
                            matrices.translate(j * 0.0F, j * 0.004F, j * 0.0F);
                        }
                        matrices.translate(f * 0.0F, f * 0.0F + 0.65f, f * 0.04F);
                        matrices.scale(1.0F, 1.0F, 1.0F + f * 0.2F);
                        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((float) l * 45.0F));
                        this.pop_matrix(bl2, swingProgress, equipProgress, arm, matrices, player, item, vertexConsumers, light, ci);
                    }
                    case NONE -> {
                        Phoenix.LOGGER.info("Item is not anything");
                        this.pop_matrix(bl2, swingProgress, equipProgress, arm, matrices, player, item, vertexConsumers, light, ci);
                    }
                }
            }
        }
    }

    private void pop_matrix(boolean bl2, float swingProgress, float equipProgress, Arm arm, MatrixStack matrices, AbstractClientPlayerEntity player, ItemStack item, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
        float n = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * 3.1415927F);
        float m = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * 6.2831855F);
        float f = -0.2F * MathHelper.sin(swingProgress * 3.1415927F);
        int o = bl2 ? 1 : -1;
        matrices.translate((float)o * n, m, f);
        this.applyEquipOffset(matrices, arm, equipProgress);
        this.applySwingOffset(matrices, arm, swingProgress);

        this.renderItem(player, item, bl2 ? ModelTransformation.Mode.FIRST_PERSON_RIGHT_HAND : ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND, !bl2, matrices, vertexConsumers, light);
        matrices.pop();
        ci.cancel();
    }
}
