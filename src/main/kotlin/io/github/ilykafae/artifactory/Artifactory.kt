package io.github.ilykafae.artifactory

import io.github.ilykafae.artifactory.artifact.ArtifactHelper
import io.github.ilykafae.artifactory.commands.ArtifactCommand
import io.github.ilykafae.artifactory.events.EntityPickupItem
import io.github.ilykafae.artifactory.events.InventoryClick
import io.github.ilykafae.artifactory.events.InventoryItemMove
import io.github.ilykafae.artifactory.events.InventoryPickupItem
import io.github.ilykafae.artifactory.events.PlayerDropItem
import io.github.ilykafae.artifactory.events.PlayerInteract
import io.github.ilykafae.artifactory.events.PlayerItemHeld
import io.github.ilykafae.artifactory.events.PlayerJoin
import io.github.ilykafae.artifactory.events.PlayerQuit
import io.github.ilykafae.artifactory.events.PlayerRespawn
import io.github.ilykafae.artifactory.events.PlayerSwapHands
import io.github.ilykafae.artifactory.models.Profile
import io.github.ilykafae.artifactory.models.Config
import io.github.ilykafae.cafelib.libs.Logger
import io.github.ilykafae.cafelib.libs.ProfileStore
import io.github.ilykafae.cafelib.libs.ServerConfigStore
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Path

internal class Artifactory : JavaPlugin() {

    companion object {
        const val ID = "artifactory"
        const val SPLIT = "::"
        const val COMMAND_PREFIX = "§e[§6Artifactory§e]"

        lateinit var plugin: JavaPlugin

        val log: java.util.logging.Logger = Bukkit.getLogger()
        val profileStore = ProfileStore(
            Path.of("artifactory/profiles"),
            Profile(),
            Logger (log, ID, SPLIT, "profilestore"),
        )
        val serverConfig = ServerConfigStore(
            Path.of("artifactory"),
            Config(),
            Logger (log, ID, SPLIT, "servercfg"),
        )
    }

    override fun onEnable() {
        plugin = this

        // reg events
        server.pluginManager.registerEvents(PlayerJoin(), this)
        server.pluginManager.registerEvents(PlayerQuit(), this)
        server.pluginManager.registerEvents(PlayerInteract(), this)
        server.pluginManager.registerEvents(PlayerItemHeld(), this)
        server.pluginManager.registerEvents(PlayerSwapHands(), this)
        server.pluginManager.registerEvents(InventoryClick(), this)
        server.pluginManager.registerEvents(EntityPickupItem(), this)
        server.pluginManager.registerEvents(PlayerDropItem(), this)
        server.pluginManager.registerEvents(InventoryItemMove(), this)
        server.pluginManager.registerEvents(InventoryPickupItem(), this)
        server.pluginManager.registerEvents(PlayerRespawn(), this)

        serverConfig.load()

        // register commands
        val manager = this.lifecycleManager

        manager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val registrar = event.registrar()

            registrar.register(
                "artifactory",
                "Main command for Artifactory",
                listOf<String>(),
                ArtifactCommand()
            )
        }

        // tasks
        server.scheduler.runTaskTimer(this, Runnable {
            serverConfig.save()
        }, 20L*60L*10L, 20L*60L*10L)

        server.scheduler.runTaskTimer(this, Runnable {
            server.onlinePlayers.forEach { plr ->
                val profile = profileStore.copy(plr.uniqueId.toString()) ?: return@forEach
                profile.cooldowns.forEach { (string, map) ->
                    val mmap = mutableMapOf<Int, Int>()
                    map.forEach { (index, _) ->
                        if (map[index]!! > 0) mmap[index] = map[index]!! - 1
                    }
                    profile.cooldowns[string] = mmap
                }
                profileStore.set(plr.uniqueId.toString(), profile)
            }
        }, 0L, 20L)

        server.scheduler.runTaskTimer(this, Runnable {
            Bukkit.getOnlinePlayers().forEach { plr ->
                ArtifactHelper.sendArtifactActionBar(plr)
            }
        }, 0L, 10L)
    }

    override fun onDisable() {
        serverConfig.save()
    }
}
