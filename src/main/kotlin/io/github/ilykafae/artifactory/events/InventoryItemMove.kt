package io.github.ilykafae.artifactory.events

import io.github.ilykafae.artifactory.Artifactory
import io.github.ilykafae.artifactory.artifact.ArtifactHelper.getArtifact
import io.github.ilykafae.artifactory.artifact.ArtifactHelper.isArtifact
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryType

class InventoryItemMove : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onMove(event: InventoryMoveItemEvent) {
        if (event.destination.type == InventoryType.HOPPER && !Artifactory.serverConfig.get().allowArtifactInHopper && event.item.isArtifact() && !Artifactory.serverConfig.get().ignoredArtifacts.contains(event.item.getArtifact()!!.id)) {
            event.isCancelled = true
        }
    }

}