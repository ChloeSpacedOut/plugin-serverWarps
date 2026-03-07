package com.choespacedout.serverWarps;

import com.choespacedout.serverWarps.commands.Warp;
import com.choespacedout.serverWarps.commands.WarpManage;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Core extends JavaPlugin {

    @Override
    public void onEnable() {

        File warpFile = new File(getDataFolder(),"warps.yml");
        if (!warpFile.exists()) {
            try {
                warpFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not load warp config file! ServerWarps could not finish loading!");
                return;
            }
        }

        WarpCache warpCache = new WarpCache(warpFile);
        warpCache.updateCache();

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(WarpManage.createCommand("warpManage",warpFile,warpCache),"Manage the current world's warps");
            commands.registrar().register(Warp.createCommand("warp",warpFile,warpCache),"Teleport to warp");
        });

    }
}
