package io.github.ilykafae.artifactory.events

import io.github.ilykafae.artifactory.artifact.Artifact
import io.github.ilykafae.artifactory.artifact.ArtifactHelper.getArtifact
import io.github.ilykafae.artifactory.artifact.Inputs
import io.github.ilykafae.artifactory.artifact.component.ArtifactComponent
import io.github.ilykafae.artifactory.artifact.component.ArtifactComponentType
import io.github.ilykafae.artifactory.artifact.component.components.AbilityComponent
import io.github.ilykafae.artifactory.artifact.component.components.helpers.AbilityHelper
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

internal class PlayerInteract : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onInteract(event: PlayerInteractEvent) {
        if (event.hand == EquipmentSlot.OFF_HAND) return

        val plr: Player = event.player
        val artifact: Artifact = event.item?.getArtifact() ?: return
        val abilites: MutableList<ArtifactComponent> = artifact.artifactComponentManager.get(ArtifactComponentType.ABILITY) ?: return

        event.isCancelled = true

        if (event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_AIR) {
            if (abilites.isNotEmpty()) {
                abilites.forEachIndexed { index, ability ->
                    if ((ability as AbilityComponent).input == Inputs.RIGHT_CLICK && !plr.isSneaking) {
                        AbilityHelper.useAbility(plr, artifact, index)
                    } else if (ability.input == Inputs.SHIFT_RIGHT_CLICK && plr.isSneaking) {
                        AbilityHelper.useAbility(plr, artifact, index)
                    }
                }
            }
        } else if (event.action == Action.LEFT_CLICK_BLOCK || event.action == Action.LEFT_CLICK_AIR) {
            if (abilites.isNotEmpty()) {
                abilites.forEachIndexed { index, ability ->
                    if ((ability as AbilityComponent).input == Inputs.LEFT_CLICK && !plr.isSneaking) {
                        AbilityHelper.useAbility(plr, artifact, index)
                    } else if (ability.input == Inputs.SHIFT_LEFT_CLICK && plr.isSneaking) {
                        AbilityHelper.useAbility(plr, artifact, index)
                    }
                }
            }
        }
    }

}