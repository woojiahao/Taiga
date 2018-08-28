package me.chill.utility.jda

import me.chill.utility.embed.EmbedCreator
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.MessageEmbed

fun MessageChannel.send(message: String?) = sendMessage(message).queue()

fun MessageChannel.send(embed: MessageEmbed?) = sendMessage(embed).queue()

// todo: add functions for a generic success and failure embed
fun embed(create: EmbedCreator.() -> Unit): MessageEmbed? {
	val creator = EmbedCreator()
	creator.create()
	return creator.build()
}

fun printMember(member: Member) = "${member.asMention}(${member.effectiveName}#${member.user.discriminator})"
