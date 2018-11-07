package me.chill.utility

import me.chill.credentials
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

fun Guild.hasMember(user: User) = getMember(user) != null

fun Guild.deleteMessagesFromChannel(channelId: String, messagesToDelete: List<Message>) =
  this.getTextChannelById(channelId).deleteMessages(messagesToDelete).queue()

fun MessageChannel.getMessageHistory(messagesToRetrieve: Int, filter: (Message) -> Boolean = { true }) =
  MessageHistory(this).retrievePast(messagesToRetrieve).complete().filter { filter(it) }

fun Member.hasPermission(guild: Guild, intendedRole: String?, vararg additionalExclusion: String): Boolean {
  val userIsBotOwner = user.id == credentials!!.botOwnerId
  if (isOwner || userIsBotOwner) return true

  intendedRole?.let {
    val isIntendedRoleEveryone = guild.getRolesByName("@everyone", false)[0].id == it
    if (isIntendedRoleEveryone) return true
  }

  val userHasRoles = roles.isNotEmpty()
  if (!userHasRoles && intendedRole != null) return false

  for (exclusion in additionalExclusion) {
    val match = guild.getRolesByName(exclusion, true)
    if (match.isEmpty()) continue
    val roleMatch = match[0]
    if (roles.any { it.id == roleMatch.id }) return true
  }

  intendedRole?.let {
    return roles[0].position >= guild.getRoleById(it).position
  }

  return false
}