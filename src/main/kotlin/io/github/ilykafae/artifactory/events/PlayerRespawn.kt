package io.github.ilykafae.artifactory.events

import io.github.ilykafae.artifactory.artifact.ArtifactHelper
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class PlayerRespawn : Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    fun onRespawn(event: PlayerRespawnEvent) {
        ArtifactHelper.updateHand(event.player, event.player.inventory.itemInMainHand, event.player.inventory.itemInOffHand)
    }

}