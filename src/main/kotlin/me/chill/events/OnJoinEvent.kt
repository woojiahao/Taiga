package me.chill.events

import me.chill.database.operations.*
import me.chill.database.states.TargetChannel
import me.chill.embed.types.newMemberJoinEmbed
import me.chill.exception.TaigaException
import me.chill.framework.CommandContainer
import me.chill.roles.addRoleToUser
import me.chill.roles.assignRole
import me.chill.roles.getMutedRole
import me.chill.utility.getDateTime
import me.chill.utility.jda.failureEmbed
import me.chill.utility.jda.printMember
import me.chill.utility.jda.send
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.events.guild.GuildJoinEvent
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class OnJoinEvent : ListenerAdapter() {
	override fun onGuildMemberJoin(event: GuildMemberJoinEvent?) {
		event ?: throw TaigaException("Event object was null during member join")

		val guild = event.guild
		val guildId = guild.id
		val joinChannel = guild.getTextChannelById(getChannel(TargetChannel.Join, guildId))
		val loggingChannel = guild.getTextChannelById(getChannel(TargetChannel.Logging, guildId))
		val member = event.member

		if (!getWelcomeDisabled(guildId)) joinChannel.send(newMemberJoinEmbed(guild, member))

		if (hasJoinRole(guildId)) assignRole(guild, getJoinRole(guildId)!!, member.user.id)

		if (hasRaider(guildId, member.user.id)) {
			if (guild.getMutedRole() == null) {
				loggingChannel.send(
					failureEmbed(
						"Mute Failed",
						"Unable to apply mute to user as the **muted** role does not exist, run `${getPrefix(guild.id)}setup`"
					)
				)
			} else {
				guild.addRoleToUser(member, guild.getMutedRole()!!)
			}
			loggingChannel.send(
				failureEmbed(
					"Raider Rejoin",
					"Raider: ${printMember(member)} as rejoined the server"
				)
			)
		}
	}

	override fun onGuildJoin(event: GuildJoinEvent?) {
		event ?: throw TaigaException("Event object was null during bot join")

		val serverId = event.guild.id
		val defaultChannelId = event.guild.defaultChannel!!.id

		println("Joined ${event.guild.name}::$serverId on ${getDateTime()}")
		addServerPreference(serverId, defaultChannelId)
		loadGlobalPermissions(event.guild)
	}
}

private fun loadGlobalPermissions(guild: Guild) {
	val globalCommands = CommandContainer.getGlobalCommands()
	val everyoneRoleId = guild.getRolesByName("@everyone", false)[0].id
	batchAddPermissions(globalCommands, guild.id, everyoneRoleId)
}