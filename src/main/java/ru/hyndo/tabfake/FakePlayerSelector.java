package ru.hyndo.tabfake;

import java.util.Collection;
import java.util.Collections;

public interface FakePlayerSelector {

    Collection<FakePlayer> accept();

    void addPlayer(FakePlayer fakePlayer);

    void addAllPlayer(Collection<FakePlayer> fakePlayers);

    boolean hasNext();

}
