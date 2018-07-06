package ru.hyndo.tabfake;

import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class PlayerCreatorTask extends BukkitRunnable implements Listener {

    private Plugin plugin;
    private final FakePlayerSelector fakePlayerSelector;
    private final FakeTabPopulator fakeTabPopulator;
    private final int timeoutAdd;
    private final int timeoutRemove;

    private Set<FakePlayer> addedPlayers = Sets.newConcurrentHashSet();
    private AtomicLong nextAdd = new AtomicLong();
    private AtomicLong nextRemove = new AtomicLong();

    public PlayerCreatorTask(Plugin plugin, FakePlayerSelector fakePlayerSelector,
                             FakeTabPopulator fakeTabPopulator,
                             int timeoutAdd,
                             int timeoutRemove) {
        this.plugin = plugin;
        this.fakePlayerSelector = fakePlayerSelector;
        this.fakeTabPopulator = fakeTabPopulator;
        this.timeoutAdd = timeoutAdd;
        this.timeoutRemove = timeoutRemove;
        runTaskTimerAsynchronously(plugin, 0, 1);
    }

    @Override
    public void run() {
        long nextAddValue = nextAdd.get();
        long nextRemoveValue = nextRemove.get();
        if(System.currentTimeMillis() >= nextAddValue) {
            doAddPlayer();
        }
        if (System.currentTimeMillis() >= nextRemoveValue) {
            doRemovePlayer();
        }
    }

    private void doRemovePlayer() {
        if(addedPlayers.isEmpty()) return;
        nextRemove.set(System.currentTimeMillis() + timeoutRemove);
        FakePlayer fakePlayer = addedPlayers.iterator().next();
        fakePlayerSelector.addPlayer(fakePlayer);
        addedPlayers.remove(fakePlayer);
        fakeTabPopulator.hidePlayerToAll(fakePlayer);
    }

    private void doAddPlayer() {
        if(fakePlayerSelector.hasNext()) {
            nextAdd.set(System.currentTimeMillis() + timeoutAdd);
            Collection<FakePlayer> players = fakePlayerSelector.accept();
            addedPlayers.addAll(players);
            players.forEach(fakeTabPopulator::showPlayerToAll);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin,
                () -> addedPlayers.forEach(fakePlayer -> fakeTabPopulator.showPlayer(fakePlayer, Collections.singletonList(event.getPlayer()))));
    }


}
