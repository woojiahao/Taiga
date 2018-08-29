package me.chill.utility.jda

import me.chill.utility.embed.EmbedCreator
import me.chill.utility.settings.green
import me.chill.utility.settings.happy
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

fun successEmbed(title: String, description: String, thumbnail: String = happy) =
	embed {
		this.title = title
		this.description = description
		this.thumbnail = thumbnail
		color = green
	}

fun printMember(member: Member) = "${member.asMention}(${member.effectiveName}#${member.user.discriminator})"

fun printChannel(channel: MessageChannel) = "${channel.name}(${channel.id})"