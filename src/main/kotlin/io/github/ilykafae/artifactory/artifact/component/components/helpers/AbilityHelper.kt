package io.github.ilykafae.artifactory.artifact.component.components.helpers

import io.github.ilykafae.artifactory.Artifactory
import io.github.ilykafae.artifactory.artifact.Artifact
import io.github.ilykafae.artifactory.artifact.Inputs
import io.github.ilykafae.artifactory.artifact.component.ArtifactComponentType
import io.github.ilykafae.artifactory.artifact.component.components.AbilityComponent
import io.github.ilykafae.artifactory.models.ArtifactoryProfile
import io.github.ilykafae.cafelib.libs.Helper.toTimeString
import org.bukkit.entity.Player

object AbilityHelper {

    fun useAbility(plr: Player, artifact: Artifact, index: Int) {
        val profile: ArtifactoryProfile = Artifactory.profileStore.copy(plr.uniqueId.toString()) ?: return
        val abilityComp: AbilityComponent = artifact.artifactComponentManager.get(ArtifactComponentType.ABILITY)?.get(index) as? AbilityComponent ?: return
        if (profile.cooldowns.containsKey(artifact.id)) {
            if (profile.cooldowns[artifact.id]!!.containsKey(index)) {
                if (profile.cooldowns[artifact.id]!![index]!! > 0) {
                    plr.sendMessage("§c[§4❌§c] Ability ${abilityComp.icon} ${abilityComp.name} §cis still on cooldown §f(${profile.cooldowns[artifact.id]!![index]!!.toTimeString()})")
                    return
                }
            }
        }

        abilityComp.ability(plr)
        // update action bar
        plr.sendMessage("§a[§2✅§a] Used ability ${abilityComp.icon} ${abilityComp.name}")
        val currCooldowns = profile.cooldowns[artifact.id]?.toMutableMap() ?: mutableMapOf()
        currCooldowns[index] = abilityComp.cd
        profile.cooldowns[artifact.id] = currCooldowns
        Artifactory.profileStore.set(plr.uniqueId.toString(), profile)
    }

    fun Inputs.getString(): String {
        return when (this) {
            Inputs.RIGHT_CLICK -> "RIGHT CLICK"
            Inputs.LEFT_CLICK -> "LEFT CLICK"
            Inputs.SHIFT_LEFT_CLICK -> "SHIFT LEFT CLICK"
            Inputs.SHIFT_RIGHT_CLICK -> "SHIFT RIGHT CLICK"
        }
    }

}