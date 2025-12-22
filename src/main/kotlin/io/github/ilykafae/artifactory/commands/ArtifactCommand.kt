package io.github.ilykafae.artifactory.commands

import io.github.ilykafae.artifactory.Artifactory
import io.github.ilykafae.artifactory.artifact.Artifact
import io.github.ilykafae.artifactory.artifact.ArtifactHelper.getArtifact
import io.github.ilykafae.artifactory.artifact.ArtifactRegistry
import io.github.ilykafae.cafelib.libs.Helper.getPlainName
import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.datacomponent.DataComponentTypes
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.text.get

internal class ArtifactCommand : BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        val sender = stack.sender

        if (args.isEmpty()) return

        when (args[0]) {
            "artifact" -> {
                if (!sender.isOp) {
                    sender.sendMessage("${Artifactory.COMMAND_PREFIX} §cYou need OP to use this command!")
                    return
                }
                when (args.size) {
                    2 -> {
                        if (sender !is Player) {
                            sender.sendMessage("${Artifactory.COMMAND_PREFIX} §cThis can only be used by a player!")
                            return
                        }
                        val art: Artifact? = ArtifactRegistry.getArtifact(args[1])
                        if (art == null) {
                            sender.sendMessage("${Artifactory.COMMAND_PREFIX} §cInvalid artifact id: ${args[1]}")
                            return
                        } else {
                            sender.give(art.getItemStack())
                            sender.sendMessage("${Artifactory.COMMAND_PREFIX} §fGave ${sender.name} ${art.getItemStack().getPlainName()}§f x 1")
                            return
                        }
                    }
                    3 -> {
                        if (sender !is Player) {
                            sender.sendMessage("${Artifactory.COMMAND_PREFIX} §cThis can only be used by a player!")
                            return
                        }
                        val art: Artifact? = ArtifactRegistry.getArtifact(args[1])
                        if (art == null) {
                            sender.sendMessage("${Artifactory.COMMAND_PREFIX} §cInvalid artifact id: ${args[1]}")
                            return
                        } else {
                            if (args[2].toIntOrNull() != null) {
                                sender.give(art.getItemStack(args[2].toInt()))
                                sender.sendMessage("${Artifactory.COMMAND_PREFIX} §fGave ${sender.name} ${art.getItemStack().getPlainName()}§f x ${args[2]}")
                            } else {
                                sender.sendMessage("${Artifactory.COMMAND_PREFIX} §cInvalid amount: ${args[2]}")
                                return
                            }
                        }
                    }
                    4 -> {
                        val art: Artifact? = ArtifactRegistry.getArtifact(args[1])
                        if (art == null) {
                            sender.sendMessage("${Artifactory.COMMAND_PREFIX} §cInvalid artifact id: ${args[1]}")
                            return
                        } else {
                            if (args[2].toIntOrNull() != null) {
                                if (Bukkit.getPlayer(args[3]) != null) {
                                    Bukkit.getPlayer(args[3])!!.give(art.getItemStack(args[2].toInt()))
                                    sender.sendMessage("${Artifactory.COMMAND_PREFIX} §fGave ${Bukkit.getPlayer(args[3])!!.name} ${art.getItemStack().getPlainName()}§f x ${args[2]}")
                                } else {
                                    sender.sendMessage("${Artifactory.COMMAND_PREFIX} §cCannot find player: ${args[3]}")
                                    return
                                }
                            } else {
                                sender.sendMessage("${Artifactory.COMMAND_PREFIX} §cInvalid amount: ${args[2]}")
                                return
                            }
                        }
                    }
                    else -> return
                }
            }
            "transfer" -> {
                if (args.size != 2) return
                if (sender !is Player) {
                    sender.sendMessage("${Artifactory.COMMAND_PREFIX} §cThis can only be used by a player!")
                    return
                }

                val art: Artifact? = sender.inventory.itemInMainHand.getArtifact()
                if (art == null) {
                    sender.sendMessage("${Artifactory.COMMAND_PREFIX} §cYou are not holding an artifact!")
                    return
                }

                if (Bukkit.getPlayer(args[1]) == null) {
                    sender.sendMessage("${Artifactory.COMMAND_PREFIX} §cInvalid player: ${args[1]}")
                    return
                }

                sender.inventory.remove(art.getItemStack(1))
                Bukkit.getPlayer(args[1])!!.give(art.getItemStack(1))
                sender.sendMessage("${Artifactory.COMMAND_PREFIX} §fTransferred ${art.getItemStack().getPlainName()} to ${args[1]}")
                Bukkit.getPlayer(args[1])!!.sendMessage("${Artifactory.COMMAND_PREFIX} §f${sender.name} transferred ${art.getItemStack().getPlainName()} to you")
                return
            }
            else -> return
        }
    }

    override fun suggest(stack: CommandSourceStack, args: Array<out String>): Collection<String> {
        when (args.size) {
            1 -> return listOf("artifact", "transfer").filter { it.startsWith(args[0], ignoreCase = true) }
            2 -> {
                return when (args[0]) {
                    "artifact" -> ArtifactRegistry.getRegisterArtifacts().filter { it.startsWith(args[1], ignoreCase = true) }
                    "transfer" -> Bukkit.getOnlinePlayers().map { it.name }.filter { it.startsWith(args[1], ignoreCase = true) }
                    else -> emptyList()
                }
            }
            4 -> {
                return when (args[0]) {
                    "artifact" -> Bukkit.getOnlinePlayers().map { it.name }.filter { it.startsWith(args[3], ignoreCase = true) }
                    else -> emptyList()
                }
            }
            else -> return emptyList()
        }
    }
}
