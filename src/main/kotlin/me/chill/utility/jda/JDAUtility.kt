package me.chill.utility.jda

import me.chill.utility.embed.EmbedCreator
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.MessageEmbed
import java.util.*
import kotlin.concurrent.timerTask

fun MessageChannel.send(message: String?) = this.sendMessage(message).queue()

fun MessageChannel.send(embed: MessageEmbed?) = this.sendMessage(embed).queue()

fun embed(create: EmbedCreator.() -> Unit): MessageEmbed? {
	val creator = EmbedCreator()
	creator.create()
	return creator.build()
}

fun printMember(member: Member) = "${member.asMention}(${member.effectiveName}#${member.user.discriminator})"
