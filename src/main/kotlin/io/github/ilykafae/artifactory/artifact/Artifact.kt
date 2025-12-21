package io.github.ilykafae.artifactory.artifact

import io.github.ilykafae.artifactory.Artifactory
import io.github.ilykafae.artifactory.artifact.component.ArtifactComponentManager
import io.github.ilykafae.artifactory.artifact.component.ArtifactComponentType
import io.github.ilykafae.artifactory.artifact.component.components.AbilityComponent
import io.github.ilykafae.artifactory.artifact.component.components.LoreComponent
import io.github.ilykafae.artifactory.artifact.component.components.NameComponent
import io.github.ilykafae.artifactory.artifact.component.components.PassiveComponent
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemLore
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

/**
 * Must also register artifact to ArtifactRegistry
 * @param id Artifact ID
 * @param material Base material for artifact
 */
abstract class Artifact(
    val id: String,
    val material: Material
) {

    val artifactComponentManager = ArtifactComponentManager()

    init {
        init()

        artifactComponentManager.get(ArtifactComponentType.PASSIVE)?.forEach { passive ->
            artifactComponentManager.addComponent((passive as PassiveComponent).loreComponent)
        }

        artifactComponentManager.get(ArtifactComponentType.ABILITY)?.forEach { ability ->
            artifactComponentManager.addComponent((ability as AbilityComponent).loreComponent)
        }
    }

    /**
     * Initialize artifact components
     */
    abstract fun init()

    fun getItemStack(): ItemStack = getItemStack(1)

    fun getItemStack(n: Int): ItemStack {
        val stack = ItemStack(material, n)
        val meta = stack.itemMeta

        meta.persistentDataContainer.set(ArtifactRegistry.artifactIdentifer, PersistentDataType.STRING, this.id)
        stack.itemMeta = meta

        if (artifactComponentManager.getFirst(ArtifactComponentType.NAME) != null) {
            val comp: NameComponent = artifactComponentManager.getFirst(ArtifactComponentType.NAME)!! as NameComponent
            stack.setData(
                DataComponentTypes.ITEM_NAME,
                comp.name
            )
        }

        if (artifactComponentManager.get(ArtifactComponentType.LORE) != null) {
            if (artifactComponentManager.get(ArtifactComponentType.LORE)!!.isNotEmpty()) {
                @Suppress("UNCHECKED_CAST")
                val loreComps: List<LoreComponent> = artifactComponentManager.get(ArtifactComponentType.LORE) as List<LoreComponent>
                val loreBuiler: ItemLore.Builder = ItemLore.lore()

                loreComps.forEach { loreComp ->
                    loreComp.lore.forEach { lore ->
                        loreBuiler.addLine(lore)
                    }
                }

                stack.setData(
                    DataComponentTypes.LORE,
                    loreBuiler.build()
                )
            }
        }

        return stack
    }

}