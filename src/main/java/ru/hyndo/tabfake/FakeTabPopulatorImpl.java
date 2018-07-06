package ru.hyndo.tabfake;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class FakeTabPopulatorImpl implements FakeTabPopulator {

    private Plugin plugin;
    private ProtocolManager protocolManager;

    public FakeTabPopulatorImpl(Plugin plugin, ProtocolManager protocolManager) {
        this.plugin = plugin;
        this.protocolManager = protocolManager;
    }

    @Override
    public void showPlayer(FakePlayer fakePlayer, Collection<? extends Player> players) {
        PacketContainer playerInfo = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        playerInfo.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        WrappedGameProfile gameProfile = new WrappedGameProfile(fakePlayer.getUUID(), fakePlayer.getName());
        PlayerInfoData playerInfoData = new PlayerInfoData(gameProfile, 100, EnumWrappers.NativeGameMode.NOT_SET,
                WrappedChatComponent.fromText(fakePlayer.getName()));
        playerInfo.getPlayerInfoDataLists().write(0, Lists.newArrayList(playerInfoData));

        String prefix = fakePlayer.getPrefix();
        String suffix = fakePlayer.getSuffix();

        PacketContainer setPrefix = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        setPrefix.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME);
        PlayerInfoData playerInfoDataPrefix = new PlayerInfoData(gameProfile, 100, EnumWrappers.NativeGameMode.NOT_SET,
                WrappedChatComponent.fromText(fakePlayer.getPrefix() + " " + fakePlayer.getName() + " " + fakePlayer.getSuffix()));
        setPrefix.getPlayerInfoDataLists().write(0, Lists.newArrayList(playerInfoDataPrefix));

        for (Player player : players) {
            try {
                protocolManager.sendServerPacket(player, playerInfo);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        protocolManager.sendServerPacket(player, setPrefix);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }.runTaskLater(plugin, 7);
        }

    }

    @Override
    public void hidePlayer(FakePlayer fakePlayer, Collection<? extends Player> players) {
        PacketContainer playerInfo = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        playerInfo.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        WrappedGameProfile gameProfile = new WrappedGameProfile(fakePlayer.getUUID(), fakePlayer.getName());
        PlayerInfoData playerInfoData = new PlayerInfoData(gameProfile, 1, EnumWrappers.NativeGameMode.NOT_SET,
                WrappedChatComponent.fromText(fakePlayer.getName()));
        playerInfo.getPlayerInfoDataLists().write(0, Lists.newArrayList(playerInfoData));
        for (Player player : players) {
            try {
                protocolManager.sendServerPacket(player, playerInfo);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
