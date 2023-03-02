package net.scriptshatter.fberb.recipe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import me.shedaniel.rei.plugin.common.displays.crafting.CraftingRecipeSizeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.scriptshatter.fberb.components.Machine_anim_int;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Turn_blast_recipe implements Recipe<Machine_anim_int> {
    final int width;
    final int height;
    private final Identifier id;
    private final ItemStack output;
    final DefaultedList<Ingredient> recipeItems;
    final int blast_time;

    public DefaultedList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    private boolean matchesPattern(Machine_anim_int inv, int offsetX, int offsetY, boolean flipped) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                int k = i - offsetX;
                int l = j - offsetY;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (flipped) {
                        ingredient = this.recipeItems.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.recipeItems.get(k + l * this.width);
                    }
                }

                if (!ingredient.test(inv.getStack(i + j * 3))) {
                    return false;
                }
            }
        }

        return true;
    }

    public Turn_blast_recipe(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItems, int blast_time) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.width = 3;
        this.height = 3;
        this.blast_time = blast_time;
    }

    public int get_blast_time(){
        return this.blast_time;
    }

    @Override
    public boolean matches(Machine_anim_int inventory, World world) {
        for(int i = 0; i <= 3 - this.width; ++i) {
            for(int j = 0; j <= 3 - this.height; ++j) {
                if (this.matchesPattern(inventory, i, j, true)) {
                    return true;
                }

                if (this.matchesPattern(inventory, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public ItemStack craft(Machine_anim_int inventory) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    static Map<String, Ingredient> readSymbols(JsonObject json) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if ((entry.getKey()).length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put( entry.getKey(), Ingredient.fromJson(entry.getValue()));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    static DefaultedList<Ingredient> createPatternMatrix(String[] pattern, Map<String, Ingredient> symbols, int width, int height) {
        DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(symbols.keySet());
        set.remove(" ");

        for(int i = 0; i < pattern.length; ++i) {
            for(int j = 0; j < pattern[i].length(); ++j) {
                String string = pattern[i].substring(j, j + 1);
                Ingredient ingredient = symbols.get(string);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + string + "' but it's not defined in the key");
                }

                set.remove(string);
                defaultedList.set(j + width * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return defaultedList;
        }
    }

    private static int findFirstSymbol(String line) {
        int i;
        for(i = 0; i < line.length() && line.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int findLastSymbol(String pattern) {
        int i;
        for(i = pattern.length() - 1; i >= 0 && pattern.charAt(i) == ' '; --i) {
        }

        return i;
    }

    static String[] getPattern(JsonArray json) {
        String[] strings = new String[json.size()];
        if (strings.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        } else if (strings.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for(int i = 0; i < strings.length; ++i) {
                String string = JsonHelper.asString(json.get(i), "pattern[" + i + "]");
                if (string.length() > 3) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
                }

                if (i > 0 && strings[0].length() != string.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                strings[i] = string;
            }

            return strings;
        }
    }

    public static ItemStack outputFromJson(JsonObject json) {
        Item item = getItem(json);
        if (json.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        } else {
            int i = JsonHelper.getInt(json, "count", 1);
            if (i < 1) {
                throw new JsonSyntaxException("Invalid output count: " + i);
            } else {
                return new ItemStack(item, i);
            }
        }
    }

    public static Item getItem(JsonObject json) {
        String string = JsonHelper.getString(json, "item");
        Item item = Registries.ITEM.getOrEmpty(new Identifier(string)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + string + "'"));
        if (item == Items.AIR) {
            throw new JsonSyntaxException("Invalid item: " + string);
        } else {
            return item;
        }
    }

    private static String[] removePadding(String... pattern) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for(int m = 0; m < pattern.length; ++m) {
            String string = pattern[m];
            i = Math.min(i, findFirstSymbol(string));
            int n = findLastSymbol(string);
            j = Math.max(j, n);
            if (n < 0) {
                if (k == m) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (pattern.length == l) {
            return new String[0];
        } else {
            String[] strings = new String[pattern.length - l - k];

            for(int o = 0; o < strings.length; ++o) {
                strings[o] = pattern[o + k].substring(i, j + 1);
            }

            return strings;
        }
    }

    public static class Type implements RecipeType<Turn_blast_recipe>{
        private Type(){}
        public static final Type INSTANCE = new Type();
        public static final String ID = "turnblast";
    }

    public static class Serializer implements RecipeSerializer<Turn_blast_recipe>{
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "turnblast";

        @Override
        public Turn_blast_recipe read(Identifier id, JsonObject json) {

            Map<String, Ingredient> map = Turn_blast_recipe.readSymbols(JsonHelper.getObject(json, "key"));
            String[] strings = Turn_blast_recipe.removePadding(Turn_blast_recipe.getPattern(JsonHelper.getArray(json, "pattern")));
            int i = strings[0].length();
            int j = strings.length;
            DefaultedList<Ingredient> defaultedList = Turn_blast_recipe.createPatternMatrix(strings, map, i, j);
            ItemStack itemStack = Turn_blast_recipe.outputFromJson(JsonHelper.getObject(json, "result"));
            int blast_time = JsonHelper.getInt(json, "blast_time");

            return new Turn_blast_recipe(id, itemStack, defaultedList, blast_time);
        }

        @Override
        public Turn_blast_recipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(9, Ingredient.EMPTY);

            defaultedList.replaceAll(ignored -> Ingredient.fromPacket(buf));

            ItemStack output = buf.readItemStack();
            int blast_time = buf.readInt();
            return new Turn_blast_recipe(id, output, defaultedList, blast_time);
        }

        @Override
        public void write(PacketByteBuf buf, Turn_blast_recipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
            buf.writeInt(recipe.get_blast_time());
        }
    }
}
