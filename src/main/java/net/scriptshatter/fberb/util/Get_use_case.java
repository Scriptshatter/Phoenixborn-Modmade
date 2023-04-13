package net.scriptshatter.fberb.util;

import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.entity.player.PlayerEntity;

public interface Get_use_case {
    void render_processor(float tickDelta);

    Phoenix_use_actions use_actions(PlayerEntity player);
}
