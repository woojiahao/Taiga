package me.chill.database

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

enum class TargetChannel {
	Logging, Suggestion, Join
}

fun addServer(serverId: String, defaultChannelId: String) {
	transaction {
		ChannelAssignment.insert {
			it[ChannelAssignment.serverId] = serverId
			it[joinChannelId] = defaultChannelId
			it[loggingChannelId] = defaultChannelId
			it[suggestionChannelId] = defaultChannelId
		}
	}
}

fun removeServer(serverId: String) {
	transaction {
		ChannelAssignment.deleteWhere {
			ChannelAssignment.serverId eq serverId
		}
	}
}

fun editChannel(targetChannel: TargetChannel, serverId: String, newChannelId: String) {
	transaction {
		ChannelAssignment.update({ ChannelAssignment.serverId eq serverId }) {
			it[selectColumn(targetChannel)] = newChannelId
		}
	}
}

fun getChannel(targetChannel: TargetChannel, serverId: String): String {
	var channelId = ""
	transaction {
		val columnToSelect = selectColumn(targetChannel)
		val results = ChannelAssignment
			.slice(columnToSelect)
			.select {
				ChannelAssignment.serverId eq serverId
			}.first()
		channelId = results[columnToSelect]
	}
	return channelId
}

private fun selectColumn(targetChannel: TargetChannel) =
	when (targetChannel) {
		TargetChannel.Logging -> ChannelAssignment.loggingChannelId
		TargetChannel.Join -> ChannelAssignment.joinChannelId
		TargetChannel.Suggestion -> ChannelAssignment.suggestionChannelId
	}
