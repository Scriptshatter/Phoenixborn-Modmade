package net.scriptshatter.fberb.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.scriptshatter.fberb.components.Bird_parts;

import java.util.Collection;
import java.util.Iterator;

public class Set_temp {
    //This was made for testing, the code here is messy and un-optimised. Known as I took the kill command and butchered it to make this.

        public Set_temp() {
        }
        public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
            dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("add_temp").requires((source) -> {
                return source.hasPermissionLevel(2);
            }))
                    .executes((context) -> {
                        return add_dxp((ServerCommandSource)context.getSource(),3, ImmutableList.of(((ServerCommandSource)context.getSource()).getPlayerOrThrow()));
                    })).then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager.argument("amount", IntegerArgumentType.integer()).executes((context) -> {
                return add_dxp((ServerCommandSource)context.getSource(),IntegerArgumentType.getInteger(context, "amount"), EntityArgumentType.getPlayers(context, "targets"));
            }))));
        }

        private static int add_dxp(ServerCommandSource source, int amount, Collection<? extends ServerPlayerEntity> targets){
            Iterator var2 = targets.iterator();

            while(var2.hasNext()) {
                Entity entity = (Entity)var2.next();
                Bird_parts.TEMP.get(entity).change_temp(amount);
                source.sendFeedback(Text.literal(String.valueOf(Bird_parts.TEMP.get(entity).get_temp())), true);
            }

            if (targets.size() == 1) {
                source.sendFeedback(Text.literal("Did 1 target"), true);
            } else {
                source.sendFeedback(Text.literal("Did multiple targets"), true);
            }

            return targets.size();
        }
}
