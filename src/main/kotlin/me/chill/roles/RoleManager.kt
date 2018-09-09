package me.chill.roles

import me.chill.utility.jda.failureEmbed
import me.chill.utility.jda.printMember
import me.chill.utility.jda.send
import me.chill.utility.jda.successEmbed
import net.dv8tion.jda.core.entities.*

fun assignRole(guild: Guild, channel: MessageChannel, roleId: String, targetId: String) {
	val preCheckingResults = preChecking(guild, roleId, targetId)
	if (preCheckingResults != null) {
		channel.send(preCheckingResults)
		return
	}
	val role = guild.getRoleById(roleId)
	val member = guild.getMemberById(targetId)

	guild.addRoleToUser(member, role)
	channel.send(roleOperationSuccessEmbed("Successfully assigned role: **${role.name}** to ${printMember(member)}"))
}

fun assignRole(guild: Guild, roleId: String, targetId: String) {
	val preCheckingResults = preChecking(guild, roleId, targetId)
	if (preCheckingResults != null) return

	guild.addRoleToUser(guild.getMemberById(targetId), guild.getRoleById(roleId))
}

fun removeRole(guild: Guild, channel: MessageChannel, roleId: String, targetId: String): Boolean {
	val preCheckingResults = preChecking(guild, roleId, targetId)
	if (preCheckingResults != null) {
		channel.send(preCheckingResults)
		return false
	}

	val role = guild.getRoleById(roleId)
	val member = guild.getMemberById(targetId)

	if (!member.roles.contains(role)) {
		channel.send(roleOperationFailureEmbed("Member: ${printMember(member)} does not have role: **${role.name}**"))
		return false
	}

	guild.removeRoleFromUser(member, role)
	channel.send(roleOperationSuccessEmbed("Successfully removed role: **${role.name}** from ${printMember(member)}"))
	return true
}

fun removeRole(guild: Guild, roleId: String, targetId: String) {
	val preCheckingResults = preChecking(guild, roleId, targetId)
	if (preCheckingResults != null) return

	guild.removeRoleFromUser(guild.getMemberById(targetId), guild.getRoleById(roleId))
}

fun Guild.hasRole(roleName: String, ignoreCase: Boolean = false) = getRolesByName(roleName, ignoreCase).isNotEmpty()

fun Guild.getRole(roleName: String, ignoreCase: Boolean = false) = getRolesByName(roleName, ignoreCase).first()!!

fun Guild.getMutedRole() = if (getRolesByName("muted", false).isEmpty()) null else getRolesByName("muted", false).first()!!

fun Guild.addRoleToUser(member: Member, role: Role) = controller.addSingleRoleToMember(member, role).queue()

fun Guild.removeRoleFromUser(member: Member, role: Role) = controller.removeSingleRoleFromMember(member, role).queue()

fun Guild.createRole(roleName: String) =
	controller
		.createRole()
		.setName(roleName)
		.setPermissions(emptyList())
		.queue()

private fun preChecking(guild: Guild, roleId: String,
						targetId: String): MessageEmbed? {
	val role = guild.getRoleById(roleId)
	return when {
		role == null -> roleOperationFailureEmbed("Role: **$roleId** does not exist in **${guild.name}**")
		role.position >= guild.getRolesByName(guild.jda.selfUser.name, false)[0].position -> roleOperationFailureEmbed("Unable to assign role: **${role.name}** as it is higher level than me!")
		guild.getMemberById(targetId) == null -> roleOperationFailureEmbed("Member: **$targetId** does not exist in **${guild.name}**")
		else -> null
	}
}

private fun roleOperationSuccessEmbed(message: String) =
	successEmbed("Role Operation Success!", message)

private fun roleOperationFailureEmbed(message: String) =
	failureEmbed("Role Operation Failed!", message)
