package io.github.ilykafae.artifactory.events

import io.github.ilykafae.artifactory.artifact.ArtifactHelper
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack

internal class PlayerSwapHands : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onSwap(event: PlayerSwapHandItemsEvent) {
        val plr: Player = event.player
        val main: ItemStack = event.mainHandItem
        val off: ItemStack = event.offHandItem
        ArtifactHelper.updateHand(plr, main, off)
    }

}