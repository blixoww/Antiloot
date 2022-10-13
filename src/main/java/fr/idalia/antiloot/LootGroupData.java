package fr.idalia.antiloot;

import org.bukkit.entity.Player;

import java.util.UUID;

public final class LootGroupData {

    // Default cooldown in millis
    public static final String NBT_KEY = "AntiLoot__StackGroup";
    public static final long DEFAULT_COOLDOWN = 30000L;

    private final UUID playerUid;
    private final long droppedAt;
    private final long cooldown;

    private LootGroupData(final long droppedAt, final long cooldown, final UUID playerUid) {
        this.droppedAt = droppedAt;
        this.cooldown = cooldown;
        this.playerUid = playerUid;
    }

    public long droppedAt() {
        return this.droppedAt;
    }

    public long cooldown() {
        return this.cooldown;
    }

    public UUID playerUid() {
        return this.playerUid;
    }

    public boolean ellapsed() {
        return (this.droppedAt + this.cooldown) <= System.currentTimeMillis();
    }

    public long remaining() {
        return this.ellapsed() ? 0L : (this.droppedAt + this.cooldown) - System.currentTimeMillis();
    }

    public long remainingSec() {
        return this.remaining() / 1000L;
    }

    public boolean playerMatch(final Player player) {
        return player.getUniqueId().equals(this.playerUid);
    }

    public boolean canGrab(final Player player) {
        return this.ellapsed() || this.playerMatch(player);
    }

    public static LootGroupData with(final UUID playerUid) {
        return new LootGroupData(System.currentTimeMillis(), DEFAULT_COOLDOWN, playerUid);
    }
}
