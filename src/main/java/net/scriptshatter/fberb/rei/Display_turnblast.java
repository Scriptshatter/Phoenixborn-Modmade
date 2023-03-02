package net.scriptshatter.fberb.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.common.displays.crafting.*;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.recipe.Turn_blast_recipe;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Display_turnblast extends DefaultCraftingDisplay<Turn_blast_recipe> {

    private final int width;
    private final int height;

    public Display_turnblast(@Nullable Identifier location, @Nullable Turn_blast_recipe possibleRecipe, List<EntryIngredient> input, List<EntryIngredient> output, int width, int height) {
        super(input, output, Optional.ofNullable(location == null && possibleRecipe != null ? possibleRecipe.getId() : location), Optional.ofNullable(possibleRecipe));
        this.width = width;
        this.height = height;
    }

    public static Display_turnblast simple(List<EntryIngredient> input, List<EntryIngredient> output, int width, int height, Optional<Identifier> location) {
        Turn_blast_recipe optionalRecipe = (Turn_blast_recipe) location.flatMap(resourceLocation -> RecipeManagerContext.getInstance().getRecipeManager().get(resourceLocation))
                .orElse(null);
        return new Display_turnblast(location.orElse(null), optionalRecipe, input, output, width, height);
    }

    public <T extends Recipe<?>> Display_turnblast(@Nullable Turn_blast_recipe t) {
        this(null, t, EntryIngredients.ofIngredients(Objects.requireNonNull(t).getIngredients()), Collections.singletonList(EntryIngredients.of(t.getOutput())), 3, 3);

    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return Category_turnblast.TURNBLAST_DISPLAY;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }


    public static BasicDisplay.Serializer<Display_turnblast> cerializer() {
        return BasicDisplay.Serializer.of((input, output, location, tag) ->
                Display_turnblast.simple(input, output, tag.getInt("RecipeWidth"), tag.getInt("RecipeHeight"), location), (display, tag) -> {
            tag.putString("REIRecipeType", "Shaped");
            if (!display.isShapeless()) {
                tag.putInt("RecipeWidth", display.getInputWidth(3, 3));
                tag.putInt("RecipeHeight", display.getInputHeight(3, 3));
            }
        });
    }
}
