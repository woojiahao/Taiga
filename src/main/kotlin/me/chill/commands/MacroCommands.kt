package me.chill.commands

import me.chill.arguments.types.MacroName
import me.chill.arguments.types.Sentence
import me.chill.database.operations.addMacro
import me.chill.database.operations.editMacro
import me.chill.database.operations.getMacroList
import me.chill.database.operations.removeMacro
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.settings.green
import me.chill.utility.embed
import me.chill.utility.failureEmbed
import me.chill.utility.successEmbed

@CommandCategory
fun macroCommands() = commands("Macro") {
	command("listmacros") {
		execute {
			val macroList = getMacroList(getGuild().id)
			respond(listMacrosEmbed(getGuild().name, macroList.sorted().joinToString(", "), macroList.size))
		}
	}

	command("addmacro") {
		expects(MacroName(), Sentence())
		execute {
			val guild = getGuild()
			val args = getArguments()
			val macroName = args[0] as String
			val macroDescription = args[1] as String

			if (getMacroList(guild.id).size >= 50) {
				respond(
					failureEmbed(
						"Add Macro Failed",
						"You have reached the macro limit for the server"
					)
				)
				return@execute
			}

			addMacro(guild.id, macroName, macroDescription)
			respond(macroEditEmbed("Add Macro Success", guild.id, macroName, macroDescription))
		}
	}

	command("editmacro") {
		expects(MacroName(true), Sentence())
		execute {
			val guild = getGuild()
			val args = getArguments()
			val macroName = args[0] as String
			val macroDescription = args[1] as String

			editMacro(guild.id, macroName, macroDescription)
			respond(macroEditEmbed("Edit Macro Success", guild.id, macroName, macroDescription))
		}
	}

	command("removemacro") {
		expects(MacroName(true))
		execute {
			val guild = getGuild()
			val macroName = getArguments()[0] as String

			removeMacro(guild.id, macroName)
			respond(
				successEmbed(
					"Remove Macro Success",
					"Macro: **$macroName** as been removed",
					null
				)
			)
		}
	}
}

private fun macroEditEmbed(title: String, serverId: String, macroName: String, macroDescription: String) =
	embed {
		this.title = title
		color = green
		description = "**$macroName** will now respond with: **$macroDescription**"
		footer {
			iconUrl = null
			message = "${getMacroList(serverId).size}/50 macros"
		}
	}

private fun listMacrosEmbed(serverName: String, description: String, serverMacroSize: Int) =
	embed {
		title = "$serverName Macros"
		this.description = description
		color = green
		footer {
			iconUrl = null
			message = "$serverMacroSize/50 macros"
		}
	}