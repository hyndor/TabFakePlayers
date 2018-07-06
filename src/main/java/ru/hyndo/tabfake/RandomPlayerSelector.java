package ru.hyndo.tabfake;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class RandomPlayerSelector implements FakePlayerSelector {

    private List<FakePlayer> work;
    private final int minPlayersOnce;
    private final int maxPlayersOnce;

    public RandomPlayerSelector(List<FakePlayer> source, int minPlayersOnce, int maxPlayersOnce) {
        this.work = new CopyOnWriteArrayList<>(source);
        this.minPlayersOnce = minPlayersOnce;
        this.maxPlayersOnce = maxPlayersOnce;
    }

    @Override
    public Collection<FakePlayer> accept() {
        if(!hasNext()) {
            throw new IndexOutOfBoundsException("Work collection is empty");
        }
        int toSelect = ThreadLocalRandom.current().nextInt(minPlayersOnce, maxPlayersOnce);
        if (toSelect >= work.size()) {
            toSelect = work.size() - 1;
        }
        List<FakePlayer> fakePlayers = work.subList(0, toSelect);
        List<FakePlayer> copied = new ArrayList<>(fakePlayers);
        work.removeAll(fakePlayers);
        return copied;
    }

    @Override
    public void addPlayer(FakePlayer fakePlayer) {
        work.add(fakePlayer);
    }

    @Override
    public void addAllPlayer(Collection<FakePlayer> fakePlayers) {
        work.addAll(fakePlayers);
    }

    @Override
    public boolean hasNext() {
        return !work.isEmpty();
    }
}
