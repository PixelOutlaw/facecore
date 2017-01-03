package io.github.Cnly.BusyInv.BusyInv.listeners;

import io.github.Cnly.BusyInv.BusyInv.holders.BusyHolder;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

public class BusyListener implements Listener {

    private static boolean isPlayer(HumanEntity he) {
        return he instanceof Player;
    }

    private static boolean isBusyHolder(InventoryHolder holder) {
        return holder instanceof BusyHolder;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        if (!isPlayer(e.getWhoClicked()))
            return;
        InventoryHolder holder = e.getInventory().getHolder();
        if (!isBusyHolder(holder))
            return;

        e.setCancelled(true);
        ((BusyHolder) holder).getMenu().onMenuClick(e);

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!isPlayer(e.getPlayer()))
            return;
        InventoryHolder holder = e.getInventory().getHolder();
        if (!isBusyHolder(holder))
            return;

        ((BusyHolder) holder).getMenu().onMenuClose(e);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (!isPlayer(e.getPlayer()))
            return;
        InventoryHolder holder = e.getInventory().getHolder();
        if (!isBusyHolder(holder))
            return;

        ((BusyHolder) holder).getMenu().onMenuOpen(e);
    }

}
