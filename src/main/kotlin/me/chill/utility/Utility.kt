package me.chill.utility

import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.MessageEmbed

fun MessageChannel.send(message: String) = this.sendMessage(message).queue()

fun MessageChannel.send(embed: MessageEmbed) = this.sendMessage(embed).queue()
