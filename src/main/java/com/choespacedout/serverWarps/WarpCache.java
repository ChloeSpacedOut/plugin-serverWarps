package com.choespacedout.serverWarps;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WarpCache {

    private File warpFile;
    private Set<String> warpCache;

    public WarpCache(File newWarpFile) {
        warpFile = newWarpFile;
    }

    public Set<String> getCache() {
        return warpCache;
    }

    public void updateCache() {

        YamlConfiguration modifyFile = YamlConfiguration.loadConfiguration(warpFile);

        try {
            warpCache = modifyFile.getKeys(false);
        } catch (Exception e) {
            warpCache = new HashSet<>(Arrays.asList());
        }
    }

}
