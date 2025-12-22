package io.github.ilykafae.artifactory.events

import io.github.ilykafae.artifactory.Artifactory
import io.github.ilykafae.artifactory.artifact.ArtifactHelper.isArtifact
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryPickupItemEvent
import org.bukkit.event.inventory.InventoryType

class InventoryPickupItem : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onInventoryPickupItem(event: InventoryPickupItemEvent) {
        if (event.inventory.type == InventoryType.HOPPER && !Artifactory.serverConfig.get().allowArtifactInDispenser && event.item.itemStack.isArtifact()) {
            event.isCancelled = true
        }
    }

}