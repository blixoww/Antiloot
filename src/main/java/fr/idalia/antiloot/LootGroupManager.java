package fr.idalia.antiloot;

import org.bukkit.entity.Item;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class LootGroupManager {

    private static final Map<String, LootGroupData> LOOTS = new HashMap<String, LootGroupData>();

    public static String register(final LootGroupData data) {
        final String lootGroupId = UUID.randomUUID().toString();
        LOOTS.put(lootGroupId, data);

        return lootGroupId;
    }

    /**
     * Get ItemGroupData matching with the lootDataId
     *
     * @param lootDataId value returned by LootGroupManager.register(ItemGroupData)
     * @return the matching loot data or null
     */
    public static LootGroupData getLootData(final String lootDataId) {
        return LOOTS.get(lootDataId);
    }

    public static void unregister(final String lootDataId) {
        LOOTS.remove(lootDataId);
    }

    public static Optional<LootGroupEntry> ofItem(final Item entity, final Plugin plugin, boolean autoclean) {
        if (entity.hasMetadata(LootGroupData.NBT_KEY)) {
            final String lootDataId = entity.getMetadata(LootGroupData.NBT_KEY).get(0).asString();
            final LootGroupData data = getLootData(lootDataId);

            if (data == null) {
                if (autoclean) entity.removeMetadata(LootGroupData.NBT_KEY, plugin);
                return Optional.empty();
            }

            if (data.ellapsed() && autoclean) {
                entity.removeMetadata(LootGroupData.NBT_KEY, plugin);
                unregister(lootDataId);
            }

            return Optional.of(new LootGroupEntry(lootDataId, data));
        }

        return Optional.empty();
    }
}
