package io.github.ilykafae.artifactory.events

import io.github.ilykafae.artifactory.Artifactory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

internal class PlayerJoin : Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    fun onJoin(event: PlayerJoinEvent) {
        val plr: Player = event.player
        Artifactory.profileStore.load(plr.uniqueId.toString())
    }

}