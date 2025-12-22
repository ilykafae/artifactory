package io.github.ilykafae.artifactory.models

internal data class Profile(
    var main: String? = null,
    var off: String? = null,

    val cooldowns: MutableMap<String, MutableMap<Int, Int>> = mutableMapOf()
)
