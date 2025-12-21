package io.github.ilykafae.artifactory.artifact.component.components

import io.github.ilykafae.artifactory.artifact.component.ArtifactComponent
import io.github.ilykafae.artifactory.artifact.component.ArtifactComponentType
import net.kyori.adventure.text.Component

class NameComponent(
    val stringName: String,
) : ArtifactComponent {

    override val type = ArtifactComponentType.NAME

    val name: Component by lazy  {
        return@lazy Component.text(stringName)
    }

}