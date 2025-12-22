package io.github.ilykafae.artifactory.models

internal data class Config(
    val allowArtifactInEchest: Boolean = false,
    val allowArtifactInShulker: Boolean = false,
    val allowArtifactInChest: Boolean = false,
    val allowArtifactInBarrel: Boolean = false,
    val allowArtifactInHopper: Boolean = false,
    val allowArtifactInDropper: Boolean = false,
    val allowArtifactInDispenser: Boolean = false,

    val ignoredArtifacts: MutableList<String> = mutableListOf(
        "1_bill"
    ),
)
