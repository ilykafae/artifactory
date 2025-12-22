package io.github.ilykafae.artifactory.events

import io.github.ilykafae.artifactory.Artifactory
import io.github.ilykafae.artifactory.artifact.ArtifactHelper
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

internal class EntityPickupItem : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onPickup(event: EntityPickupItemEvent) {
        if (event.entity is Player) {
            val plr = event.entity as Player
            Bukkit.getScheduler().runTask(Artifactory.plugin, Runnable {
                ArtifactHelper.updateHand(plr, plr.inventory.itemInMainHand, plr.inventory.itemInOffHand)
            })
        }
    }

}