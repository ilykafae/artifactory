package io.github.ilykafae.artifactory.artifact.component.components

import io.github.ilykafae.artifactory.artifact.Inputs
import io.github.ilykafae.artifactory.artifact.component.ArtifactComponent
import io.github.ilykafae.artifactory.artifact.component.ArtifactComponentType
import io.github.ilykafae.artifactory.artifact.component.components.helpers.AbilityHelper.getString
import io.github.ilykafae.cafelib.libs.Helper.toTimeString
import org.bukkit.entity.Player

class AbilityComponent(
    val icon: String,
    val name: String,
    val desc: MutableList<String>,
    val input: Inputs,
    val cd: Int,
    val ability: (plr: Player) -> Unit
) : ArtifactComponent {

    override val type = ArtifactComponentType.ABILITY

    val loreComponent: LoreComponent by lazy {
        desc.addFirst( "[]$icon $name §e§l${input.getString()}")
        desc.addLast("[]§8 ↳ Cooldown: §a${cd.toTimeString()}")
        LoreComponent(desc)
    }

}