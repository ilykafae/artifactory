package io.github.ilykafae.artifactory.events

import io.github.ilykafae.artifactory.Artifactory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

internal class PlayerQuit : Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    fun onQuit(e: PlayerQuitEvent) {
        val plr: Player = e.player
        Artifactory.profileStore.save(plr.uniqueId.toString())
    }

}