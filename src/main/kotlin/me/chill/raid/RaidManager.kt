package me.chill.raid

import me.chill.database.operations.*
import me.chill.database.states.TargetChannel
import me.chill.utility.*
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageChannel

class RaidManager {
	private val raiders = mutableMapOf<String, RaiderList>()

	/**
	 * When the user is to be managed, checks are made to ensure that the server is being watched and the
	 * 	raider is being tracked
	 */
	fun manageRaid(guild: Guild, channel: MessageChannel, member: Member) {
		val serverId = guild.id
		val userId = member.user.id

		if (!hasServer(serverId)) raiders[serverId] = RaiderList(getRaidMessageDuration(serverId))

		if (!hasRaider(serverId, userId)) {
			raiders[serverId]!!.addRaider(userId)
			return
		}

		val raider = raiders[serverId]!!.getRaider(userId)
		val raidMessageLimit = getRaidMessageLimit(serverId)
		raider.messageCount++

		if (raider.messageCount >= raidMessageLimit) {
			if (guild.getMutedRole() == null) {
				channel.send(
					failureEmbed(
						"Mute Failed",
						"Unable to apply mute to user as the **muted** role does not exist, run `${getPrefix(guild.id)}setup`"
					)
				)
				return
			} else {
				raiderCaught(guild, member, channel)
			}
		}
	}

	private fun raiderCaught(guild: Guild, member: Member, channel: MessageChannel) {
		guild.addRoleToUser(member, guild.getMutedRole()!!)
		addRaider(guild.id, member.user.id)

		member.sendPrivateMessage(
			failureEmbed(
				"Muted",
				"You have been muted for spamming in ${guild.name}",
				null
			)
		)

		val loggingChannel = guild.getTextChannelById(TargetChannel.Logging.get(guild.id))
		loggingChannel.send(
			failureEmbed(
				"Raider Caught",
				"User: ${printMember(member)} has been muted for spamming in ${printChannel(channel)}",
				null
			)
		)

		guild.deleteMessagesFromChannel(
			channel.id,
			channel.getMessageHistory(50) { it.author.id == member.user.id }
		)
	}

	private fun hasServer(serverId: String) = raiders.keys.contains(serverId)

	private fun hasRaider(serverId: String, userId: String) = raiders[serverId]!!.hasRaider(userId)
}