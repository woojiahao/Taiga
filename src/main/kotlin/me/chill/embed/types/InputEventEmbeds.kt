package me.chill.embed.types

import me.chill.framework.Command
import me.chill.json.help.findCommand
import me.chill.json.help.syntax
import me.chill.settings.red
import me.chill.settings.shock
import me.chill.utility.jda.embed

fun invalidArgumentsEmbed(serverPrefix: String, command: Command, errMsg: String) =
	embed {
		title = "Invalid Arguments"
		description = "Invalid arguments passed to the command: **${command.name}**"
		color = red
		thumbnail = shock

		field {
			title = "Error"
			description = errMsg
			inline = false
		}

		field {
			title = "Syntax"
			description = command.syntax
			inline = false
		}

		field {
			title = "Learn more"
			description = "Use the `${serverPrefix}help ${command.name}` to learn more about the command"
			inline = false
		}
	}

fun insufficientArgumentsEmbed(serverPrefix: String, command: Command, expected: Int) =
	embed {
		title = "Insufficient Arguments"
		color = red
		thumbnail = shock
		description = "Command: **${command.name}** requires **$expected** arguments"

		field {
			title = "Syntax"
			description = "$serverPrefix${findCommand(command.name).syntax}"
			inline = false
		}

		field {
			title = "Example"
			description = "$serverPrefix${findCommand(command.name).example}"
			inline = false
		}
	}