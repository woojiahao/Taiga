package me.chill.utility

import me.chill.embed.EmbedCreator
import me.chill.settings.green
import me.chill.settings.happy
import me.chill.settings.red
import me.chill.settings.shock
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.MessageEmbed

fun MessageChannel.send(message: String?) = sendMessage(message).queue()

fun MessageChannel.send(embed: MessageEmbed?) = sendMessage(embed).queue()

// todo: add functions for a generic success and failure embed
inline fun embed(create: EmbedCreator.() -> Unit): MessageEmbed? {
	val creator = EmbedCreator()
	creator.create()
	return creator.build()
}

fun simpleEmbed(title: String, description: String, thumbnail: String?, color: Int?) =
	embed {
		this.title = title
		this.description = description
		this.thumbnail = thumbnail
		this.color = color
	}

fun successEmbed(title: String, description: String, thumbnail: String = happy, color: Int = green) =
	simpleEmbed(title, description, thumbnail, color)

fun failureEmbed(title: String, description: String, thumbnail: String = shock, color: Int = red) =
	simpleEmbed(title, description, thumbnail, color)

fun printMember(member: Member) = "${member.asMention}(${member.effectiveName}#${member.user.discriminator})"

fun printChannel(channel: MessageChannel) = "${channel.name}(${channel.id})"

fun Member.sendPrivateMessage(message: String) =
	user.openPrivateChannel().queue { it.send(message) }

fun Member.sendPrivateMessage(embed: MessageEmbed?) =
	user.openPrivateChannel().queue { it.send(embed) }
