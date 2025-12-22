package io.github.ilykafae.artifactory.events

import io.github.ilykafae.artifactory.artifact.ArtifactHelper
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack

internal class PlayerItemHeld : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onItemHeld(event: PlayerItemHeldEvent) {
        val plr: Player = event.player
        val stack: ItemStack? = plr.inventory.getItem(event.newSlot)
        ArtifactHelper.updateHand(plr, stack, plr.inventory.itemInOffHand)
    }

}