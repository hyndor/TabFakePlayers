package ru.hyndo.tabfake;

import java.util.Objects;
import java.util.UUID;

public class FakePlayer {

    private String prefix;
    private String name;
    private String suffix;
    private UUID uuid;

    public FakePlayer(String prefix, String name, String suffix) {
        this.prefix = prefix;
        this.name = name;
        this.suffix = suffix;
        this.uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());
    }

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FakePlayer that = (FakePlayer) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "FakePlayer{" +
                "prefix='" + prefix + '\'' +
                ", name='" + name + '\'' +
                ", suffix='" + suffix + '\'' +
                '}';
    }
}
