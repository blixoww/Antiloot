package fr.idalia.antiloot;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public final class AntiLoot extends JavaPlugin implements Listener {

    private static final String[] ITEM_FORBIDDEN = { "§b❃ Rod Antique ❃", "§b❃ Rod Antique ❃",
            "§b❃ Epée Antique ❃", "§b❃ Pomme Antique ❃", "§b❃ Chausson Antique ❃"};
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getDrops().isEmpty()) return;
        if (event.getEntity() == null || event.getEntity().getKiller() == null) return;

        final Player killer = event.getEntity().getKiller();
        final LootGroupData lootGroupData = LootGroupData.with(killer.getUniqueId());
        final String lootDataId = LootGroupManager.register(lootGroupData);

        for (ItemStack stack : event.getDrops()) {
            if (isItemForbidden(stack)) continue;
            final Item ent = event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), stack);
            if (ent.hasMetadata(LootGroupData.NBT_KEY))
                ent.removeMetadata(LootGroupData.NBT_KEY, this);
            ent.setMetadata(LootGroupData.NBT_KEY, new FixedMetadataValue(this, lootDataId));
        }
//        (new BukkitRunnable() {
//            @Override
//            public void run() {
//                if (lootGroupData.ellapsed()) {
//                    cancel();
//                    return;
//                }
                killer.sendMessage("§aTu as §830s§a pour récupérer le stuff de " + event.getEntity().getName());
//            }
//        }).runTaskTimer(this, 0L, 20L);
        event.getDrops().clear();
    }

    public static boolean isItemForbidden(ItemStack itemStack) {
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            return Arrays.stream(ITEM_FORBIDDEN).anyMatch(items -> Objects.equals(itemStack.getItemMeta().getDisplayName(), items));
        }
        return false;
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        if (!e.getItem().hasMetadata(LootGroupData.NBT_KEY)) return;
        LootGroupManager.ofItem(e.getItem(), this, true).ifPresent(entry -> {
            if (!entry.data().canGrab(e.getPlayer())) {
                e.setCancelled(true);
            }
        });
    }

//    @EventHandler
//    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
//        if (event.getRightClicked() instanceof Item) {
//            final Item ent = (Item) event.getRightClicked();
//
//            LootGroupManager.ofItem(ent, this, false).ifPresent(entry -> {
//                event.getPlayer().sendMessage(
//                        String.format(
//                                "§a%ds§f restante(s) avant de pouvoir ramasser l'objet",
//                                entry.data().remainingSec()));
//            });
//        }
//    }
}
