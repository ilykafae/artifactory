package io.github.ilykafae.artifactory.artifact

import io.github.ilykafae.artifactory.artifact.ArtifactRegistry.artifactIdentifer
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ArtifactHelper {
    fun ItemStack.getArtifact(): Artifact? {
       val id: String = this.itemMeta.persistentDataContainer.get(artifactIdentifer, PersistentDataType.STRING) ?: return null
       return ArtifactRegistry.getArtifact(id)
    }

    fun ItemStack.isArtifact(): Boolean {
        return this.itemMeta.persistentDataContainer.has(artifactIdentifer)
    }

    fun Artifact.isRegistered(): Boolean {
        return ArtifactRegistry.getArtifact(this.id) != null
    }

}