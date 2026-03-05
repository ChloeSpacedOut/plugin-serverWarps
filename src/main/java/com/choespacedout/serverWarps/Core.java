package com.choespacedout.serverWarps;

import com.choespacedout.serverWarps.commands.Warp;
import com.choespacedout.serverWarps.commands.WarpManage;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin {

    @Override
    public void onEnable() {

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(WarpManage.createCommand("warpManage", this),"Manage the current world's warps");
            commands.registrar().register(Warp.createCommand("warp", this),"Teleport to warp");
        });
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
