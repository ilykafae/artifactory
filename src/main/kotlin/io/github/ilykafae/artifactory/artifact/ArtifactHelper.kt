package io.github.ilykafae.artifactory.artifact

import io.github.ilykafae.artifactory.Artifactory.Companion.profileStore
import io.github.ilykafae.artifactory.artifact.ArtifactRegistry.artifactIdentifer
import io.github.ilykafae.artifactory.artifact.component.ArtifactComponent
import io.github.ilykafae.artifactory.artifact.component.ArtifactComponentType
import io.github.ilykafae.artifactory.artifact.component.components.AbilityComponent
import io.github.ilykafae.artifactory.artifact.component.components.PassiveComponent
import io.github.ilykafae.artifactory.models.Profile
import io.github.ilykafae.cafelib.libs.Helper.toTimeString
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ArtifactHelper {

    fun ItemStack?.getArtifact(): Artifact? {
        if (this == null || this.type.isAir) return null
        val id: String = this.itemMeta.persistentDataContainer.get(artifactIdentifer, PersistentDataType.STRING) ?: return null
        return ArtifactRegistry.getArtifact(id)
    }

    fun ItemStack?.isArtifact(): Boolean {
        if (this == null || this.type.isAir) return false
        return this.itemMeta.persistentDataContainer.has(artifactIdentifer)
    }

    fun Artifact.isRegistered(): Boolean {
        return ArtifactRegistry.getArtifact(this.id) != null
    }

    fun sendArtifactActionBar(plr: Player) {
        val profile: Profile = profileStore.copy(plr.uniqueId.toString()) ?: return

        if (profile.main != null) {
            val artifact: Artifact? = ArtifactRegistry.getArtifact(profile.main!!)
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
        } else {
            plr.sendActionBar(Component.empty())
        }
    }

    fun updateHand(plr: Player, main: ItemStack?, off: ItemStack?) {
        val profile: Profile = profileStore.copy(plr.uniqueId.toString()) ?: return

        if (profile.main != main.getArtifact()?.id) {
            profile.main?.let { oldId ->
                ArtifactRegistry.getArtifact(oldId)?.artifactComponentManager?.get(ArtifactComponentType.PASSIVE)?.forEach {
                    (it as PassiveComponent).onUnequip(plr)
                }
                profile.main = null
            }
        }

        if (profile.off != off.getArtifact()?.id) {
            profile.off?.let { oldId ->
                ArtifactRegistry.getArtifact(oldId)?.artifactComponentManager?.get(ArtifactComponentType.PASSIVE)?.forEach {
                    (it as PassiveComponent).onUnequip(plr)
                }
                profile.off = null
            }
        }

        if (off.getArtifact()?.id != main.getArtifact()?.id && profile.main != main.getArtifact()?.id) {
            if (main.isArtifact()) {
                main.getArtifact()?.let { newArt ->
                    newArt.artifactComponentManager.get(ArtifactComponentType.PASSIVE)?.forEach {
                        (it as PassiveComponent).onEquip(plr)
                    }
                    profile.main = newArt.id
                }
            }
        }

        if (off.getArtifact()?.id != main.getArtifact()?.id && profile.off != off.getArtifact()?.id) {
            if (off.isArtifact()) {
                off.getArtifact()?.let { newArt ->
                    newArt.artifactComponentManager.get(ArtifactComponentType.PASSIVE)?.forEach {
                        (it as PassiveComponent).onEquip(plr)
                    }
                    profile.off = newArt.id
                }
            }
        }

        profileStore.set(plr.uniqueId.toString(), profile)
        sendArtifactActionBar(plr)
    }

}