package me.chill.utility.invite

import me.chill.database.operations.*
import me.chill.settings.red
import me.chill.utility.jda.embed
import me.chill.utility.jda.printChannel
import me.chill.utility.jda.printMember
import me.chill.utility.jda.send
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageChannel

private val inviteRegex = Regex("(https?://)?(www\\.)?(discord\\.(gg|io|me|li)|discordapp\\.com/invite)/.+[a-z]")

fun isInvite(message: String) = message.matches(inviteRegex)

fun containsInvite(message: String) = message.contains(inviteRegex)

fun extractInvite(message: String) =
	inviteRegex.find(message)!!.value

fun manageInviteSent(sender: Member, guild: Guild, channel: MessageChannel, message: Message) {
	val senderId = sender.user.id
	val guildId = guild.id

	if (!hasUser(senderId, guildId)) addUser(senderId, guildId)
	else incrementInviteCount(senderId, guildId)

	message.delete().complete()
	channel.send("${sender.asMention} do not send invites")
	guild.getTextChannelById(getChannel(TargetChannel.Logging, guildId)).send(inviteCaughtEmbed(sender, guild, channel, message))

	if (getUserCount(senderId, guildId) >= 5) {
		guild.controller.ban(sender, 1, "Spamming invite links").complete()
		removeUser(senderId, guildId)
	}
}


private fun inviteCaughtEmbed(member: Member, guild: Guild, channel: MessageChannel, message: Message) =
	embed {
		title = "Invite Sent"
		description = "${printMember(member)} attempted to send an invite in ${printChannel(channel)}"
		color = red

		field {
			title = "Message Containing Invite"
			description = message.contentDisplay
		}

		footer {
			this.message = "${getUserCount(member.user.id, guild.id)}/5 Invites Sent"
			iconUrl = null
		}
	}