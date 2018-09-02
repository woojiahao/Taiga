package me.chill.roles

import me.chill.database.operations.getPrefix
import me.chill.settings.green
import me.chill.settings.happy
import me.chill.settings.red
import me.chill.settings.shock
import me.chill.utility.embed
import me.chill.utility.failureEmbed
import me.chill.utility.printMember
import me.chill.utility.send
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageChannel

fun assignRole(guild: Guild, channel: MessageChannel,
			   roleId: String, targetId: String, silent: Boolean = false) {
	if (!preChecking(guild, channel, roleId, targetId)) return

	val role = guild.getRoleById(roleId)
	val member = guild.getMemberById(targetId)

	guild.controller.addSingleRoleToMember(member, role).complete()
	if (!silent) channel.send(roleOperationSuccessEmbed("Successfully assigned role: **${role.name}** to ${printMember(member)}"))
}

fun permanentMute(guild: Guild, channel: MessageChannel, targetId: String): Boolean {
	val mutedRole = guild.getRolesByName("muted", true)[0]

	if (mutedRole == null) {
		channel.send(
			failureEmbed(
				"Mute Failed",
				"Unable to apply mute to user as the **muted** role does not exist, run `${getPrefix(guild.id)}setup`"
			)
		)
		return false
	}

	guild.controller.addSingleRoleToMember(guild.getMemberById(targetId), mutedRole).complete()
	return true
}

fun removeRole(guild: Guild, channel: MessageChannel,
			   roleId: String, targetId: String, silent: Boolean = false) {
	if (!preChecking(guild, channel, roleId, targetId)) return

	val role = guild.getRoleById(roleId)
	val member = guild.getMemberById(targetId)

	if (!member.roles.contains(role)) {
		channel.send(roleOperationFailureEmbed("Member: ${printMember(member)} does not have role: **${role.name}**"))
		return
	}

	guild.controller.removeSingleRoleFromMember(member, role).complete()
	if (!silent) channel.send(roleOperationSuccessEmbed("Successfully removed role: **${role.name}** from ${printMember(member)}"))
}

fun createRole(guild: Guild, roleName: String) {
	guild
		.controller
		.createRole()
		.setName(roleName)
		.setPermissions(emptyList())
		.complete()
}

private fun preChecking(guild: Guild, channel: MessageChannel,
						roleId: String, targetId: String): Boolean {
	val role = guild.getRoleById(roleId)
	if (role == null) {
		channel.send(roleOperationFailureEmbed("Role: **$roleId** does not exist in **${guild.name}**"))
		return false
	}

	if (role.position >= guild.getRolesByName("Taiga", false)[0].position) {
		channel.send(roleOperationFailureEmbed("Unable to assign role: **${role.name}** as it is higher level than me!"))
		return false
	}

	val member = guild.getMemberById(targetId)
	if (member == null) {
		channel.send(roleOperationFailureEmbed("Member: **$targetId** does not exist in **${guild.name}**"))
		return false
	}

	return true
}

private fun roleOperationStatusEmbed(title: String, message: String,
									 color: Int, thumbnail: String) =
	embed {
		this.title = title
		this.description = message
		this.color = color
		this.thumbnail = thumbnail
	}

private fun roleOperationSuccessEmbed(message: String) =
	roleOperationStatusEmbed("Role Operation Success!", message, green, happy)

private fun roleOperationFailureEmbed(message: String) =
	roleOperationStatusEmbed("Role Operation Failed!", message, red, shock)
