package net.scriptshatter.fberb.blocks.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.blocks.Machine;
import software.bernie.example.block.entity.FertilizerBlockEntity;
import software.bernie.example.block.entity.GeckoHabitatBlockEntity;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class Machine_model extends DefaultedBlockGeoModel<Machine> {
    /**
     * Create a new instance of this model class.<br>
     * The asset path should be the truncated relative path from the base folder.<br>
     * E.G.
     * <pre>{@code
     * 	new ResourceLocation("myMod", "workbench/sawmill")
     * }</pre>
     */
    public Machine_model() {
        super(new Identifier(Phoenix.MOD_ID, "machine"));
    }

    @Override
    public RenderLayer getRenderType(Machine animatable, Identifier texture) {
        return RenderLayer.getEntityTranslucent(getTextureResource(animatable));
    }
}
