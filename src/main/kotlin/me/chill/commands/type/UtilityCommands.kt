package me.chill.commands.type

import me.chill.commands.arguments.types.Word
import me.chill.commands.framework.*
import me.chill.json.help.category
import me.chill.json.help.description
import me.chill.json.help.example
import me.chill.json.help.syntax
import me.chill.settings.*
import me.chill.utility.*
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageEmbed

@CommandCategory
fun utilityCommands() = commands("Utility") {
	command("ping") {
		execute {
			val jda = getJDA()
			val latency = jda.ping
			respond(pingEmbed(latency))
		}
	}

	command("invite") {
		execute {
			respond(
				simpleEmbed(
					"Invites",
					"- [Invite me to your server!](https://discordapp.com/oauth2/authorize?client_id=482340927709511682&scope=bot&permissions=8)\n" +
						"- [Join my development server](https://discord.gg/xtDNfyw)",
					fondling,
					blue
				)
			)
		}
	}

	command("source") {
		execute {
			respond(
				simpleEmbed(
					"Sources",
					"- [GitHub repository](https://github.com/woojiahao/Taiga)\n" +
						"- [Wiki](https://github.com/woojiahao/Taiga/wiki)",
					serve,
					blue
				)
			)
		}
	}

	command("commands") {
		execute {
			respond(
				listCommandsEmbed(
					CommandContainer.commandSets,
					getJDA().selfUser.avatarUrl
				)
			)
		}
	}

	command("help") {
		expects(Word())
		execute {
			val args = getArguments()
			val commandName = args[0] as String
			if (!CommandContainer.hasCommand(commandName)) {
				respond(
					failureEmbed(
						"Command Does Not Exist",
						"Command: **$commandName** does not exist"
					)
				)
				return@execute
			}
			respond(commandInfoEmbed(CommandContainer.getCommand(getArguments()[0] as String)))
		}
	}

	command("serverinfo") {
		execute {
			respond(serverInfoEmbed(getGuild()))
		}
	}
}

private fun serverInfoEmbed(guild: Guild) =
	embed {
		title = guild.name
		color = orange
		thumbnail = guild.iconUrl

		field {
			title = "Owner"
			description = printMember(guild.owner)
			inline = true
		}

		field {
			val onlineMemberCount = guild.members.filter { it.onlineStatus == OnlineStatus.ONLINE }.size

			title = "Users"
			description = "$onlineMemberCount/${guild.members.size}"
			inline = true
		}

		field {
			title = "Region"
			description = guild.region.getName()
			inline = true
		}

		field {
			title = "Roles"
			description = guild.roles.size.toString()
			inline = true
		}

		field {
			title = "Text Channels"
			description = guild.textChannels.size.toString()
			inline = true
		}

		field {
			title = "Voice Channels"
			description = guild.voiceChannels.size.toString()
			inline = true
		}

		footer {
			message = getDateTime()
			iconUrl = guild.jda.selfUser.avatarUrl
		}
	}

private fun commandInfoEmbed(command: Command) =
	embed {
		title = "${command.category} - ${command.name}"
		color = cyan
		description = command.description
		field {
			title = "Syntax"
			description = command.syntax
		}
		field {
			title = "Example"
			description = command.example
		}
	}

private fun listCommandsEmbed(commandSets: List<CommandSet>, avatarUrl: String) =
	embed {
		title = "Commands"
		color = green
		thumbnail = avatarUrl
		commandSets.forEach { set ->
			field {
				title = set.categoryName
				description = set
					.getCommandNames()
					.joinToString("\n") { commandName ->
						"- $commandName"
					}
				inline = false
			}
		}
	}

private fun pingEmbed(latency: Long): MessageEmbed? {
	var color: Int? = null
	var thumbnail: String? = null

	when {
		latency < 30 -> {
			color = green
			thumbnail = happy
		}
		latency < 60 -> {
			color = yellow
			thumbnail = clap
		}
		latency > 90 -> {
			color = red
			thumbnail = noWay
		}
	}

	return embed {
		title = "Pong! \uD83C\uDFD3"
		description = "Ping took **${latency}ms**"
		this.color = pingEmbed@ color
		this.thumbnail = pingEmbed@ thumbnail
	}
}