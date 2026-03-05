package com.choespacedout.serverWarps.commands;

import com.choespacedout.serverWarps.Core;
import com.choespacedout.serverWarps.arguments.WarpArgument;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Warp {
    public static LiteralCommandNode<CommandSourceStack> createCommand(final String commandName, Core pluginInstance) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getExecutor() instanceof Player && sender.getSender().hasPermission("warps.use"))
                .then(Commands.argument("name", new WarpArgument(pluginInstance.getConfig()))
                        .executes(ctx -> {
                            final Player commandSender = (Player) ctx.getSource().getSender();
                            final String warpName = StringArgumentType.getString(ctx,"name");
                            final FileConfiguration config = pluginInstance.getConfig();

                            try {
                                Double pitch = config.getDouble("Warps." + warpName + ".rotation.pitch");
                                Double yaw = config.getDouble("Warps." + warpName + ".rotation.yaw");
                                Location warpLocation = new Location(
                                        Bukkit.getWorld(config.getString("Warps." + warpName + ".world")),
                                        config.getDouble("Warps." + warpName + ".position.x"),
                                        config.getDouble("Warps." + warpName + ".position.y"),
                                        config.getDouble("Warps." + warpName + ".position.z"),
                                        yaw.floatValue(),
                                        pitch.floatValue()
                                );

                                commandSender.teleport(warpLocation);
                                commandSender.playSound(warpLocation, Sound.ENTITY_PLAYER_TELEPORT,1.0F,1.0F);
                                return Command.SINGLE_SUCCESS;
                            } catch (Exception e) {
                                commandSender.sendRichMessage("<red>Failed teleporting you to \"" + warpName + "\"");
                                return Command.SINGLE_SUCCESS;
                            }
                        }))
                .build();
    }
}
