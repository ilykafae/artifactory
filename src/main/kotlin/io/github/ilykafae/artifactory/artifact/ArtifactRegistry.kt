package io.github.ilykafae.artifactory.artifact

import io.github.ilykafae.artifactory.Artifactory
import io.github.ilykafae.artifactory.artifact.component.ArtifactComponentType
import io.github.ilykafae.cafelib.libs.Logger
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ArtifactRegistry {

    val artifactIdentifer = NamespacedKey(Artifactory.ID, "artiface_id")

    private val logger: Logger = Logger(
        Artifactory.log,
        Artifactory.ID,
        Artifactory.SPLIT,
        "artifactregistry"
    )

    private val artifacts = mutableMapOf<String, Artifact>()

    /**
     * Add an artifact to the registry
     * @param artifact Artifact to add
     */
    fun addArtifact(artifact: Artifact) {
        artifacts[artifact.id] = artifact
    }

    /**
     * Get artifact from its id
     * @param id Artifact id
     */
    fun getArtifact(id: String): Artifact? {
        return artifacts[id]
    }

}