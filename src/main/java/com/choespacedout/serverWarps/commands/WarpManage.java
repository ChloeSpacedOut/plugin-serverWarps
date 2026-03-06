package com.choespacedout.serverWarps.commands;

import com.choespacedout.serverWarps.Core;
import com.choespacedout.serverWarps.WarpCache;
import com.choespacedout.serverWarps.arguments.WarpArgument;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WarpManage {
    public static LiteralCommandNode<CommandSourceStack> createCommand(final String commandName, Core pluginInstance, WarpCache warpCache) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getExecutor() instanceof Player && sender.getSender().hasPermission("warps.mannage"))
                .then(Commands.literal("add")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    final String warpName = StringArgumentType.getString(ctx,"name");
                                    final Player commandSender = (Player) ctx.getSource().getSender();

                                    final Location location = commandSender.getLocation();

                                    pluginInstance.getConfig().set("Warps." + warpName + ".world",location.getWorld().getName());
                                    pluginInstance.getConfig().set("Warps." + warpName + ".position.x",location.getX());
                                    pluginInstance.getConfig().set("Warps." + warpName + ".position.y",location.getY());
                                    pluginInstance.getConfig().set("Warps." + warpName + ".position.z",location.getZ());
                                    pluginInstance.getConfig().set("Warps." + warpName + ".rotation.pitch",location.getPitch());
                                    pluginInstance.getConfig().set("Warps." + warpName + ".rotation.yaw",location.getYaw());

                                    pluginInstance.saveConfig();

                                    warpCache.updateCache(pluginInstance.getConfig());

                                    commandSender.sendRichMessage("<gray>Created new warp called \"" + warpName + "\" at your position!");
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("remove")
                        .then(Commands.argument("name",new WarpArgument(warpCache))
                                .executes(ctx -> {
                                    final String warpName = StringArgumentType.getString(ctx,"name");
                                    final Player commandSender = (Player) ctx.getSource().getSender();

                                    pluginInstance.getConfig().set("Warps." + warpName,null);
                                    pluginInstance.saveConfig();

                                    warpCache.updateCache(pluginInstance.getConfig());

                                    commandSender.sendRichMessage("<gray>Removed warp \"" + warpName + "\"");

                                    return Command.SINGLE_SUCCESS;
                                })))
                .build();
    }
}
