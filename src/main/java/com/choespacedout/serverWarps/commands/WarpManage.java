package com.choespacedout.serverWarps.commands;

import com.choespacedout.serverWarps.WarpCache;
import com.choespacedout.serverWarps.arguments.WarpArgument;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class WarpManage {
    public static LiteralCommandNode<CommandSourceStack> createCommand(final String commandName, File warpFile, WarpCache warpCache) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getExecutor() instanceof Player && sender.getSender().hasPermission("warps.mannage"))
                .then(Commands.literal("add")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    final String warpName = StringArgumentType.getString(ctx,"name");
                                    final Player commandSender = (Player) ctx.getSource().getSender();

                                    final Set<String> warps = warpCache.getCache();

                                    if (warps.contains(warpName)) {
                                        commandSender.sendRichMessage("<red>Could not create warp! \"" + warpName + "\" already exists!");
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    final Location location = commandSender.getLocation();

                                    YamlConfiguration modifyFile = YamlConfiguration.loadConfiguration(warpFile);

                                    modifyFile.set(warpName + ".world",location.getWorld().getName());
                                    modifyFile.set(warpName + ".position.x",location.getX());
                                    modifyFile.set(warpName + ".position.y",location.getY());
                                    modifyFile.set(warpName + ".position.z",location.getZ());
                                    modifyFile.set(warpName + ".rotation.pitch",location.getPitch());
                                    modifyFile.set(warpName + ".rotation.yaw",location.getYaw());

                                    try {
                                        modifyFile.save(warpFile);
                                    } catch (IOException e) {
                                        commandSender.sendRichMessage("<red>Error saving to warp file! Warp could not be created");
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    warpCache.updateCache();

                                    commandSender.sendRichMessage("<gray>Created new warp called \"" + warpName + "\" at your position!");
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("remove")
                        .then(Commands.argument("name",new WarpArgument(warpCache))
                                .executes(ctx -> {
                                    final String warpName = StringArgumentType.getString(ctx,"name");
                                    final Player commandSender = (Player) ctx.getSource().getSender();

                                    YamlConfiguration modifyFile = YamlConfiguration.loadConfiguration(warpFile);

                                    modifyFile.set(warpName,null);
                                    try {
                                        modifyFile.save(warpFile);
                                    } catch (IOException e) {
                                        commandSender.sendRichMessage("<red>Error saving to warp file! Warp could not be removed!");
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    warpCache.updateCache();

                                    commandSender.sendRichMessage("<gray>Removed warp \"" + warpName + "\"");

                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("list")
                        .executes(ctx -> {
                            final Player commandSender = (Player) ctx.getSource().getSender();
                            commandSender.sendRichMessage("<gray>Valid Warps: " + warpCache.cacheToList());
                            return Command.SINGLE_SUCCESS;
                        }))
                .build();
    }
}
