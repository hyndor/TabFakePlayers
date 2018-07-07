package ru.hyndo.tabfake.config;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.hyndo.tabfake.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YamlTabRepository implements TabRepository {

    private Plugin plugin;
    private ProtocolManager protocolManager;
    private FileConfiguration yamlCfg;

    public YamlTabRepository(Plugin plugin, ProtocolManager protocolManager) {
        this.plugin = plugin;
        this.protocolManager = protocolManager;
        plugin.saveDefaultConfig();
        this.yamlCfg = plugin.getConfig();
    }

    @Override
    public PlayerCreatorTask loadTask() {
        ConfigurationSection fakePlayersCfg = yamlCfg.getConfigurationSection("fakePlayers");
        FakeTabPopulator fakeTabPopulator = new FakeTabPopulatorImpl(plugin, protocolManager);
        FakePlayerSelector fakePlayerSelector = new RandomPlayerSelector(parsePlayers(fakePlayersCfg.getConfigurationSection("players")), fakePlayersCfg.getInt("minPlayersOnce"), fakePlayersCfg.getInt("maxPlayersOnce"));
        return new PlayerCreatorTask(plugin, fakePlayerSelector, fakeTabPopulator, fakePlayersCfg.getInt("timeoutAdd"), fakePlayersCfg.getInt("timeoutRemove"));
    }

    private List<FakePlayer> parsePlayers(ConfigurationSection playersCfg) {
        return playersCfg.getKeys(false).stream()
                .map(playerCfgKey -> { 
                    ConfigurationSection playerCfg = playersCfg.getConfigurationSection(playerCfgKey);
                    return new FakePlayer(playerCfg.getString("prefix"), playerCfg.getString("name"), playerCfg.getString("suffix")); 
                })
                .collect(Collectors.toList());
    }
}
