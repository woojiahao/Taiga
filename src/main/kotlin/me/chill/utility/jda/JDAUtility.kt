package me.chill.utility.jda

import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.*

fun MessageChannel.send(message: String?) = sendMessage(message).queue()
fun MessageChannel.send(embed: MessageEmbed?) = sendMessage(embed).queue()

fun printMember(member: Member) = "${member.asMention}(${member.effectiveName}#${member.user.discriminator})"

fun printChannel(channel: MessageChannel) = "<#${channel.id}>(${channel.name}::${channel.id})"

fun Member.sendPrivateMessage(message: String) =
	user.openPrivateChannel().queue { it.send(message) }
fun Member.sendPrivateMessage(embed: MessageEmbed?) =
	user.openPrivateChannel().queue { it.send(embed) }

fun JDA.findUser(userId: String) = retrieveUserById(userId).complete()

fun Guild.deleteMessagesFromChannel(channelId: String, messagesToDelete: List<Message>) =
	this.getTextChannelById(channelId).deleteMessages(messagesToDelete).queue()

fun MessageChannel.getMessageHistory(messagesToRetrieve: Int, filter: (Message) -> Boolean = { true }) =
	MessageHistory(this).retrievePast(messagesToRetrieve).complete().filter { filter(it) }