package me.chill.commands

import me.chill.arguments.types.Sentence
import me.chill.database.operations.editWelcomeMessage
import me.chill.embed.types.newMemberJoinEmbed
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.utility.str

@CommandCategory
fun welcomeCommands() = commands("Welcome") {
	command("setwelcomemessage") {
		expects(Sentence())
		execute {
			editWelcomeMessage(guild.id, arguments[0]!!.str())
			respond(newMemberJoinEmbed(guild, invoker))
		}
	}
}