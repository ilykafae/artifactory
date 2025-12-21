package io.github.ilykafae.artifactory.artifact.component

class ArtifactComponentManager {

    private val components = mutableMapOf<ArtifactComponentType, MutableList<ArtifactComponent>>()

    fun addComponent(component: ArtifactComponent) {
        components.getOrPut(component.type) { mutableListOf() }.add(component)
    }

    /**
     * Get an artifact component
     * @param type Type of artifact component
     */
    fun get(type: ArtifactComponentType): MutableList<ArtifactComponent>? {
        return components[type]
    }

    /**
     * Fine the first artifact component
     * @param type Type of artifact component
     */
    fun getFirst(type: ArtifactComponentType): ArtifactComponent? {
        return components[type]?.get(0)
    }

}