package me.chill.utility

import net.dv8tion.jda.core.entities.*

/**
 * Checks that the role to assign and user to assign is valid before performing assignment
 * Assigns a role to a user and announces the status of the assignment in the channel where
 * 	the command is invoked
 */
fun assignRole(guild: Guild, channel: MessageChannel, roleId: String, targetId: String) {
	val preCheckingResults = preChecking(guild, roleId, targetId)
	preCheckingResults?.let {
		channel.send(preCheckingResults)
		return
	}
	val role = guild.getRoleById(roleId)
	val member = guild.getMemberById(targetId)

	guild.addRoleToUser(member, role)
	channel.send(roleOperationSuccessEmbed("Successfully assigned role: **${role.name}** to ${printMember(member)}"))
}

/**
 * Checks that the role to assign and user to assign is valid before performing assignment
 * Assigns a role to a user
 */
fun assignRole(guild: Guild, roleId: String, targetId: String) {
	val preCheckingResults = preChecking(guild, roleId, targetId)
	preCheckingResults?.let { return }

	guild.addRoleToUser(guild.getMemberById(targetId), guild.getRoleById(roleId))
}

/**
 * Checks that the role to remove and user to remove from is valid before performing assignment
 * Removes a role from a user and announces the status of the assignment in the channel where
 * the command is invoked
 */
fun removeRole(guild: Guild, channel: MessageChannel, roleId: String, targetId: String) {
	val preCheckingResults = preChecking(guild, roleId, targetId)
	preCheckingResults?.let {
		channel.send(preCheckingResults)
		return
	}

	val role = guild.getRoleById(roleId)
	val member = guild.getMemberById(targetId)

	guild.removeRoleFromUser(member, role)
	channel.send(roleOperationSuccessEmbed("Successfully removed role: **${role.name}** from ${printMember(member)}"))
}

/**
 * Checks that the role to remove and user to remove from is valid before performing assignment
 * Removes a role from a user
 */
fun removeRole(guild: Guild, roleId: String, targetId: String) {
	val preCheckingResults = preChecking(guild, roleId, targetId)
	preCheckingResults?.let { return }

	guild.removeRoleFromUser(guild.getMemberById(targetId), guild.getRoleById(roleId))
}

fun Guild.hasRole(roleName: String, ignoreCase: Boolean = false) = getRolesByName(roleName, ignoreCase).isNotEmpty()

fun Guild.getRole(roleName: String, ignoreCase: Boolean = false) = getRolesByName(roleName, ignoreCase).first()!!

fun Guild.getMutedRole() = if (getRolesByName("muted", false).isEmpty()) null else getRolesByName("muted", false).first()!!

/**
 * Assigns a role to a user
 */
fun Guild.addRoleToUser(member: Member, role: Role) = controller.addSingleRoleToMember(member, role).queue()


/**
 * Removes a role from a user
 */
fun Guild.removeRoleFromUser(member: Member, role: Role) = controller.removeSingleRoleFromMember(member, role).queue()

fun Guild.createRole(roleName: String) =
	controller
		.createRole()
		.setName(roleName)
		.setPermissions(emptyList())
		.queue()

/**
 * Performs checking to ensure that a role operation attempt is valid
 * Status of the check is represented by a message embed.
 * 	If a message embed is present, the pre-checking failed, else, it passed
 */
private fun preChecking(guild: Guild, roleId: String, targetId: String): MessageEmbed? {
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
