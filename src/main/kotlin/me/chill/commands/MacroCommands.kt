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
import me.chill.utility.jda.embed
import me.chill.utility.jda.failureEmbed
import me.chill.utility.jda.successEmbed

@CommandCategory
fun macroCommands() = commands("Macro") {
	command("listmacros") {
		execute {
			val macroList = getMacroList(guild.id)
			respond(listMacrosEmbed(guild.name, macroList.sorted().joinToString(", "), macroList.size))
		}
	}

	command("addmacro") {
		expects(MacroName(), Sentence())
		execute {
			val macroName = arguments[0] as String
			val macroDescription = arguments[1] as String

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
			val macroName = arguments[0] as String
			val macroDescription = arguments[1] as String

			editMacro(guild.id, macroName, macroDescription)
			respond(macroEditEmbed("Edit Macro Success", guild.id, macroName, macroDescription))
		}
	}

	command("removemacro") {
		expects(MacroName(true))
		execute {
			val macroName = arguments[0] as String

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