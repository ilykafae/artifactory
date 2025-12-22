package io.github.ilykafae.artifactory.models

data class Config(
    val allowArtifactDropping: Boolean = false,
    val allowArtifactInEchest: Boolean = false,
    val allowArtifactInShulker: Boolean = false,
    val allowArtifactInChest: Boolean = false,
    val allowArtifactInBarrel: Boolean = false,
    val allowArtifactInHopper: Boolean = false,
    val allowArtifactInDropper: Boolean = false,
    val allowArtifactInDispenser: Boolean = false,
)
