package net.scriptshatter.fberb.recipe;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;

public class Phoenix_recipes {
    public static void register_pizza(){
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Phoenix.MOD_ID, Turn_blast_recipe.Serializer.ID),
                Turn_blast_recipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, new Identifier(Phoenix.MOD_ID, Turn_blast_recipe.Type.ID),
                Turn_blast_recipe.Type.INSTANCE);
    }
}
