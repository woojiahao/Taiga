package me.chill.utility.jda

import me.chill.utility.embed.EmbedCreator
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.MessageEmbed

fun MessageChannel.send(message: String?) = this.sendMessage(message).queue()

fun MessageChannel.send(embed: MessageEmbed?) = this.sendMessage(embed).queue()

fun embed(create: EmbedCreator.() -> Unit): MessageEmbed? {
	val creator = EmbedCreator()
	creator.create()
	return creator.build()
}
