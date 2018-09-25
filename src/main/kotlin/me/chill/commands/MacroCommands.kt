package me.chill.commands

import me.chill.arguments.types.MacroName
import me.chill.arguments.types.Sentence
import me.chill.database.operations.addMacro
import me.chill.database.operations.editMacro
import me.chill.database.operations.getMacroList
import me.chill.database.operations.removeMacro
import me.chill.embed.types.listMacrosEmbed
import me.chill.embed.types.macroEditEmbed
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.utility.jda.failureEmbed
import me.chill.utility.jda.successEmbed
import me.chill.utility.str

@CommandCategory
fun macroCommands() = commands("Macro") {
	command("listmacros") {
		execute {
			val macroList = getMacroList(guild.id)
			respond(
				listMacrosEmbed(
					guild.name,
					macroList
						.asSequence()
						.sorted()
						.joinToString(", "),
					macroList.size
				)
			)
		}
	}

	command("addmacro") {
		expects(MacroName(), Sentence())
		execute {
			val macroName = arguments[0]!!.str()
			val macroDescription = arguments[1]!!.str()

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
			val macroName = arguments[0]!!.str()
			val macroDescription = arguments[1]!!.str()

			editMacro(guild.id, macroName, macroDescription)
			respond(macroEditEmbed("Edit Macro Success", guild.id, macroName, macroDescription))
		}
	}

	command("removemacro") {
		expects(MacroName(true))
		execute {
			val macroName = arguments[0]!!.str()

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