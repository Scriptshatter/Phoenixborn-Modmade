package net.scriptshatter.fberb.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.scriptshatter.fberb.Phoenix;
import org.jetbrains.annotations.Nullable;

public class Dmg_sources {
    public static DamageSource phoenix_axe(Entity axe, @Nullable Entity attacker) {
        return (new ProjectileDamageSource(Phoenix.MOD_ID + ".phoenix_axe", axe, attacker)).setProjectile();
    }

    public static final DamageSource STUPID = (new DamageSource(Phoenix.MOD_ID + ".stupid_thing")).setBypassesArmor();

    public static void register(){
        Phoenix.LOGGER.info("Damage sources registered!");
    }
}
