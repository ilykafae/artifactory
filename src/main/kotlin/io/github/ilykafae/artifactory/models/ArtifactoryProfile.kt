package io.github.ilykafae.artifactory.models

data class ArtifactoryProfile(
    val main: String? = null,
    val off: String? = null,

    var crystals: Int = 4,

    val cooldowns: MutableMap<String, MutableMap<Int, Int>> = mutableMapOf()
)
