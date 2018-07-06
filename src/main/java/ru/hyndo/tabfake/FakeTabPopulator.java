package ru.hyndo.tabfake;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface FakeTabPopulator {

    default void showPlayerToAll(FakePlayer fakePlayer) {
        showPlayer(fakePlayer, Bukkit.getOnlinePlayers());
    }

    default void hidePlayerToAll(FakePlayer fakePlayer) {
        hidePlayer(fakePlayer, Bukkit.getOnlinePlayers());
    }

    void showPlayer(FakePlayer fakePlayer, Collection<? extends Player> player);

    void hidePlayer(FakePlayer fakePlayer, Collection<? extends Player> player);

}
