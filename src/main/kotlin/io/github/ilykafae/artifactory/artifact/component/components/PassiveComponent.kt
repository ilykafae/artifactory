package io.github.ilykafae.artifactory.artifact.component.components

import io.github.ilykafae.artifactory.artifact.component.ArtifactComponent
import io.github.ilykafae.artifactory.artifact.component.ArtifactComponentType
import org.bukkit.entity.Player

class PassiveComponent(
    val icon: String = "",
    val name: String,
    val desc: MutableList<String>,
    val onEquip: (plr: Player) -> Unit,
    val onUnequip: (plr: Player) -> Unit,
) : ArtifactComponent {

    override val type = ArtifactComponentType.PASSIVE

    val loreComponent: LoreComponent by lazy {
        desc.addFirst( "[]$icon $name §6§lPASSIVE")
        LoreComponent(desc)
    }

}