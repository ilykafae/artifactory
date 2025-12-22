package io.github.ilykafae.artifactory.events

import io.github.ilykafae.artifactory.Artifactory
import io.github.ilykafae.artifactory.artifact.ArtifactHelper
import io.github.ilykafae.artifactory.artifact.ArtifactHelper.isArtifact
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemStack

internal class PlayerDropItem : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val plr: Player = event.player

        Bukkit.getScheduler().runTask(Artifactory.plugin, Runnable {
            ArtifactHelper.updateHand(plr, plr.inventory.itemInMainHand, plr.inventory.itemInOffHand)
        })
    }

}