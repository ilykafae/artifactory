package io.github.ilykafae.artifactory.events

import io.github.ilykafae.artifactory.Artifactory
import io.github.ilykafae.artifactory.artifact.ArtifactHelper
import io.github.ilykafae.artifactory.artifact.ArtifactHelper.isArtifact
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType

internal class InventoryClick : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onInventoryClick(event: InventoryClickEvent) {
        val plr = event.whoClicked as? Player ?: return

        if (event.view.topInventory.type == InventoryType.ENDER_CHEST && !Artifactory.serverConfig.get().allowArtifactInEchest && event.currentItem.isArtifact()) {
            event.isCancelled = true
            return
        }

        if (event.view.topInventory.type == InventoryType.SHULKER_BOX && !Artifactory.serverConfig.get().allowArtifactInShulker && event.currentItem.isArtifact()) {
            event.isCancelled = true
            return
        }

        if (event.view.topInventory.type == InventoryType.CHEST && !Artifactory.serverConfig.get().allowArtifactInChest && event.currentItem.isArtifact()) {
            event.isCancelled = true
            return
        }

        if (event.view.topInventory.type == InventoryType.BARREL && !Artifactory.serverConfig.get().allowArtifactInBarrel && event.currentItem.isArtifact()) {
            event.isCancelled = true
            return
        }

        if (event.view.topInventory.type == InventoryType.HOPPER && !Artifactory.serverConfig.get().allowArtifactInHopper && event.currentItem.isArtifact()) {
            event.isCancelled = true
            return
        }

        if (event.view.topInventory.type == InventoryType.DROPPER && !Artifactory.serverConfig.get().allowArtifactInDropper && event.currentItem.isArtifact()) {
            event.isCancelled = true
            return
        }

        if (event.view.topInventory.type == InventoryType.DISPENSER && !Artifactory.serverConfig.get().allowArtifactInDispenser && event.currentItem.isArtifact()) {
            event.isCancelled = true
            return
        }

        val isOffhand = event.rawSlot == 45 || event.hotbarButton == 45
        val isHotbar = event.slot in 0..8 || event.hotbarButton in 0..8

        if (isOffhand || isHotbar) {
            Bukkit.getScheduler().runTask(Artifactory.plugin, Runnable {
                ArtifactHelper.updateHand(plr, plr.inventory.itemInMainHand, plr.inventory.itemInOffHand)
            })
        }
    }

}