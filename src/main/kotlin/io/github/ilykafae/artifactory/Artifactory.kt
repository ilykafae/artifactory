package io.github.ilykafae.artifactory

import io.github.ilykafae.artifactory.artifact.Artifact
import io.github.ilykafae.artifactory.artifact.ArtifactRegistry
import io.github.ilykafae.artifactory.artifact.component.ArtifactComponent
import io.github.ilykafae.artifactory.artifact.component.ArtifactComponentType
import io.github.ilykafae.artifactory.artifact.component.components.AbilityComponent
import io.github.ilykafae.artifactory.events.PlayerInteract
import io.github.ilykafae.artifactory.events.PlayerJoin
import io.github.ilykafae.artifactory.events.PlayerQuit
import io.github.ilykafae.artifactory.models.ArtifactoryProfile
import io.github.ilykafae.artifactory.models.ArtifactoryConfig
import io.github.ilykafae.cafelib.libs.Helper.toTimeString
import io.github.ilykafae.cafelib.libs.Logger
import io.github.ilykafae.cafelib.libs.ProfileStore
import io.github.ilykafae.cafelib.libs.ServerConfigStore
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Path

class Artifactory : JavaPlugin() {

    companion object {
        internal const val ID = "artifactory"
        internal const val SPLIT = "::"

        internal lateinit var plugin: JavaPlugin

        internal val log: java.util.logging.Logger = Bukkit.getLogger()
        val profileStore: ProfileStore<ArtifactoryProfile> = ProfileStore(
            Path.of("artifactory/profiles"),
            ArtifactoryProfile(),
            Logger (log, ID, SPLIT, "profilestore"),
        )
        val serverConfig: ServerConfigStore<ArtifactoryConfig> = ServerConfigStore(
            Path.of("artifactory"),
            ArtifactoryConfig(),
            Logger (log, ID, SPLIT, "servercfg"),
        )
    }

    fun sendArtifactActionBar(plr: Player) {
        val profile: ArtifactoryProfile = profileStore.copy(plr.uniqueId.toString()) ?: return

        if (profile.main != null) {
            val artifact: Artifact? = ArtifactRegistry.getArtifact(profile.main)
            if (artifact == null) {
                plr.sendActionBar(Component.empty())
                return
            }

            val abilities: MutableList<ArtifactComponent>? = artifact.artifactComponentManager.get(ArtifactComponentType.ABILITY)
            if (abilities.isNullOrEmpty()) {
                plr.sendActionBar(Component.empty())
                return
            }

            val bar = abilities.joinToString(" ") { ability ->
                val index = abilities.indexOf(ability)
                val cd = profile.cooldowns[artifact.id]?.get(index) ?: 0
                "§f[${(ability as AbilityComponent).icon}§f] " +
                        if (cd == 0) "§aReady" else "§f${cd.toTimeString()}"
            }
            plr.sendActionBar(Component.text(bar))
        }
    }

    override fun onEnable() {
        plugin = this

        // reg events
        server.pluginManager.registerEvents(PlayerJoin(), this)
        server.pluginManager.registerEvents(PlayerQuit(), this)
        server.pluginManager.registerEvents(PlayerInteract(), this)

        serverConfig.load()

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
                sendArtifactActionBar(plr)
            }
        }, 0L, 10L)
    }

    override fun onDisable() {
        serverConfig.save()
    }
}
