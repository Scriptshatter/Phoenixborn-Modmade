package net.scriptshatter.fberb.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.items.Items;
import net.scriptshatter.fberb.recipe.Turn_blast_recipe;

public class Category_turnblast implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(
                new Category_other()
        );
        registry.addWorkstations(TURNBLAST_DISPLAY, EntryStacks.of(Items.MACHINE_ITEM));
        registry.configure(TURNBLAST_DISPLAY, config -> config.setQuickCraftingEnabledByDefault(false));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(Turn_blast_recipe.class, Turn_blast_recipe.Type.INSTANCE, Display_turnblast::new);
    }

    public static final CategoryIdentifier<Display_turnblast> TURNBLAST_DISPLAY = CategoryIdentifier.of(Phoenix.MOD_ID, "turnblast_display");
}
