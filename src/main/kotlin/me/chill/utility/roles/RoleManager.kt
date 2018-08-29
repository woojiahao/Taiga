package me.chill.utility.roles

import me.chill.utility.jda.embed
import me.chill.utility.jda.printMember
import me.chill.utility.jda.send
import me.chill.utility.settings.green
import me.chill.utility.settings.happy
import me.chill.utility.settings.red
import me.chill.utility.settings.shock
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageChannel

fun assignRole(guild: Guild, channel: MessageChannel,
			   roleId: String, targetId: String, silent: Boolean = false) {
	if (!preChecking(guild, channel, roleId, targetId)) return

	val role = guild.getRoleById(roleId)
	val member = guild.getMemberById(targetId)

	if (member.roles.contains(role)) {
		channel.send(roleOperationFailureEmbed("Member: ${printMember(member)} already has role: **${role.name}**"))
		return
	}

	guild.controller.addSingleRoleToMember(member, role).complete()
	if (!silent) channel.send(roleOperationSuccessEmbed("Successfully assigned role: **${role.name}** to ${printMember(member)}"))
}

fun removeRole(guild: Guild, channel: MessageChannel,
			   roleId: String, targetId: String) {
	if (!preChecking(guild, channel, roleId, targetId)) return

	val role = guild.getRoleById(roleId)
	val member = guild.getMemberById(targetId)

	if (!member.roles.contains(role)) {
		channel.send(roleOperationFailureEmbed("Member: ${printMember(member)} does not have role: **${role.name}**"))
		return
	}

	guild.controller.removeSingleRoleFromMember(member, role).complete()
	channel.send(roleOperationSuccessEmbed("Successfully removed role: **${role.name}** from ${printMember(member)}"))
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
