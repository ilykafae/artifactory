package io.github.ilykafae.artifactory.artifact.component.components

import io.github.ilykafae.artifactory.artifact.component.ArtifactComponent
import io.github.ilykafae.artifactory.artifact.component.ArtifactComponentType
import net.kyori.adventure.text.Component
import kotlin.math.abs

class LoreComponent(
    private val stringLore: MutableList<String>
) : ArtifactComponent {

    private val lineLength = 25
    private val ignore: String = "[]"

    val lore: List<Component> by lazy {
        val wlore = mutableListOf<Component>()

        stringLore.forEach { entry ->
            val iline = mutableListOf<String>()
            if (entry.trim().startsWith(ignore)) {
                wlore.add(Component.text("§7${entry.replace(ignore, "").trimStart()}"))
                return@forEach
            } else {
                val line: List<String> = entry.split(Regex("\\s+"))
                var len = 0
                line.forEachIndexed { index, word ->
                    if (index == 0) {
                        iline.add(word)
                        len += word.length
                    } else {
                        if (abs(word.length + len - lineLength) >= abs(lineLength - len)) {
                            wlore.add(Component.text("§7" + iline.joinToString(" ")))
                            iline.clear()
                            iline.add(word)
                            len = word.length
                        } else {
                            iline.add(word)
                            len += word.length
                        }
                    }
                }
            }

            if (iline.isNotEmpty()) {
                wlore.add(Component.text("§7" + iline.joinToString(" ")))
            }
        }

        wlore.addFirst(Component.empty())
        return@lazy wlore
    }

    override val type = ArtifactComponentType.LORE

}