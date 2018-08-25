package me.chill.utility.roles

import me.chill.gifs.happy
import me.chill.gifs.shock
import me.chill.utility.green
import me.chill.utility.jda.embed
import me.chill.utility.jda.printMember
import me.chill.utility.jda.send
import me.chill.utility.red
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageChannel

fun assignRole(guild: Guild, channel: MessageChannel,
			   roleId: String, targetId: String) {
	val role = guild.getRoleById(roleId)
	if (role == null) {
		channel.send(assignFailureEmbed("Role: **$roleId** does not exist in **${guild.name}**"))
		return
	}

	if (role.position >= guild.getRolesByName("Taiga", false)[0].position) {
		channel.send(assignFailureEmbed("Unable to assign role: **${role.name}** as it is higher level than me!"))
		return
	}

	val member = guild.getMemberById(targetId)
	if (member == null) {
		channel.send(assignFailureEmbed("Member: **$targetId** does not exist in **${guild.name}**"))
		return
	}

	if (member.roles.contains(role)) {
		channel.send(assignFailureEmbed("Member: ${printMember(member)} already has role: **${role.name}**"))
		return
	}

	guild.controller.addSingleRoleToMember(member, role).complete()
	channel.send(assignSuccessEmbed("Successfully assigned role: **${role.name}** to ${printMember(member)}"))
}

private fun assignStatusEmbed(title: String, message: String,
							  color: Int, thumbnail: String) =
	embed {
		this.title = title
		this.description = message
		this.color = color
		this.thumbnail = thumbnail
	}

private fun assignSuccessEmbed(message: String) =
	assignStatusEmbed("Role assignment success", message, green, happy)

private fun assignFailureEmbed(message: String) =
	assignStatusEmbed("Role assignment failed", message, red, shock)
