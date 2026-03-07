package com.choespacedout.serverWarps.commands;

import com.choespacedout.serverWarps.WarpCache;
import com.choespacedout.serverWarps.arguments.WarpArgument;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Objects;

public class Warp {
    public static LiteralCommandNode<CommandSourceStack> createCommand(final String commandName, File warpFile, WarpCache warpCache) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getExecutor() instanceof Player && sender.getSender().hasPermission("warps.use"))
                .then(Commands.argument("name", new WarpArgument(warpCache))
                        .executes(ctx -> {
                            final Player commandSender = (Player) ctx.getSource().getSender();
                            final String warpName = StringArgumentType.getString(ctx,"name");

                            final YamlConfiguration modifyFile = YamlConfiguration.loadConfiguration(warpFile);

                            try {
                                double pitch = modifyFile.getDouble(warpName + ".rotation.pitch");
                                double yaw = modifyFile.getDouble(warpName + ".rotation.yaw");
                                Location warpLocation = new Location(
                                        Bukkit.getWorld(Objects.requireNonNull(modifyFile.getString(warpName + ".world"))),
                                        modifyFile.getDouble(warpName + ".position.x"),
                                        modifyFile.getDouble(warpName + ".position.y"),
                                        modifyFile.getDouble(warpName + ".position.z"),
                                        (float) yaw,
                                        (float) pitch
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
