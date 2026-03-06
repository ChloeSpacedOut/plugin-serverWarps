package com.choespacedout.serverWarps;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WarpCache {

    private static Core pluginInstance;
    private Set<String> warps;

    public WarpCache(Core newPluginInstance) {
        pluginInstance = newPluginInstance;
    }

    public Set<String> getCache() {
        return warps;
    }

    public void updateCache(FileConfiguration config) {

        final ConfigurationSection configSection = config.getConfigurationSection("Warps");

        try {
            warps = configSection.getKeys(false);
        } catch (Exception e) {
            warps = new HashSet<>(Arrays.asList());
        }
    }

}
