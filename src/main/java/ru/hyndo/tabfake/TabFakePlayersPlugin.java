package ru.hyndo.tabfake;

import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.hyndo.tabfake.config.TabRepository;
import ru.hyndo.tabfake.config.YamlTabRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TabFakePlayersPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        TabRepository tabRepository = new YamlTabRepository(this, protocolManager);
        PlayerCreatorTask playerCreatorTask = tabRepository.loadTask();
        Bukkit.getPluginManager().registerEvents(playerCreatorTask, this);
    }


}
