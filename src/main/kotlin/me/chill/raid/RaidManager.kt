package me.chill.raid

import me.chill.database.operations.addRaider
import me.chill.database.operations.getChannel
import me.chill.database.operations.getRaidMessageDuration
import me.chill.database.operations.getRaidMessageLimit
import me.chill.database.states.TargetChannel
import me.chill.roles.permanentMute
import me.chill.utility.*
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageChannel

class RaidManager {
	private val raiders = mutableMapOf<String, RaiderList>()

	fun manageRaid(guild: Guild, channel: MessageChannel, member: Member) {
		val serverId = guild.id
		val userId = member.user.id

		if (!hasServer(serverId)) raiders[serverId] = RaiderList(getRaidMessageDuration(serverId))
		if (!hasRaider(serverId, userId)) {
			raiders[serverId]!!.addRaider(userId)
		} else {
			val raider = raiders[serverId]!!.getRaider(userId)
			val raidMessageLimit = getRaidMessageLimit(serverId)
			raider.messageCount += 1

			if (raider.messageCount >= raidMessageLimit) {
				if (permanentMute(guild, channel, userId)) {
					addRaider(serverId, userId)

					member.sendPrivateMessage(
						failureEmbed(
							"Muted",
							"You have been muted for spamming in ${guild.name}",
							null
						)
					)

					val loggingChannel = guild.getTextChannelById(getChannel(TargetChannel.Logging, guild.id))
					loggingChannel.send(
						failureEmbed(
							"Raider Caught",
							"User: ${printMember(member)} has been muted for spamming in ${printChannel(channel)}",
							null
						)
					)

					val raiderMessageHistory = channel.getMessageHistory(50) { it.author.id == raider.userId }
					guild.deleteMessagesFromChannel(channel.id, raiderMessageHistory)
				}
			}
		}
	}

	private fun hasServer(serverId: String) = raiders.keys.contains(serverId)

	private fun hasRaider(serverId: String, userId: String) = raiders[serverId]!!.hasRaider(userId)
}