package me.chill.commands

import me.chill.arguments.types.CommandName
import me.chill.framework.*
import me.chill.json.help.category
import me.chill.json.help.description
import me.chill.json.help.example
import me.chill.json.help.syntax
import me.chill.settings.*
import me.chill.utility.getDateTime
import me.chill.utility.int
import me.chill.utility.jda.embed
import me.chill.utility.jda.printMember
import me.chill.utility.jda.simpleEmbed
import me.chill.utility.jda.successEmbed
import me.chill.utility.str
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageEmbed
import java.io.File
import java.io.FileReader

@CommandCategory
fun utilityCommands() = commands("Utility") {
	command("ping") {
		execute {
			val jda = jda
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
					jda.selfUser.avatarUrl
				)
			)
		}
	}

	command("help") {
		expects(CommandName())
		execute {
			respond(commandInfoEmbed(CommandContainer.getCommand(arguments[0]!!.str())))
		}
	}

	command("serverinfo") {
		execute {
			respond(serverInfoEmbed(guild))
		}
	}

	command("botinfo") {
		execute {
			respond(botInfoEmbed(jda))
		}
	}

	command("changelog") {
		execute {
			val changelogsFolder = File("changelogs/")
			if (changelogsFolder.listFiles() == null || changelogsFolder.listFiles().isEmpty()) {
				respond(
					successEmbed(
						"No changelogs",
						"Nothing to report!",
						null
					)
				)
				return@execute
			}

			val latest = changelogsFolder
				.listFiles()
				.map { file ->
					val fileName = file.name
					fileName.substring(fileName.indexOf("_") + 1, fileName.indexOf(".")).int()
				}
				.max()

			val latestChangeLog = "changelogs/changelog_$latest.txt"
			respond(
				successEmbed(
					"${jda.selfUser.name} Changelogs",
					FileReader(File(latestChangeLog)).readLines().joinToString("\n"),
					null
				)
			)
		}
	}
}

private fun botInfoEmbed(jda: JDA) =
	embed {
		title = "${jda.selfUser.name} Info"
		color = orange
		thumbnail = jda.selfUser.avatarUrl
		field {
			title = "Servers"
			description = jda.guilds.size.toString()
			inline = true
		}

		field {
			title = "Members"
			description = jda.users.size.toString()
			inline = true
		}

		field {
			title = "Commands"
			description = CommandContainer.getCommandNames().size.toString()
			inline = true
		}

		field {
			title = "Categories"
			description = CommandContainer.commandSets.size.toString()
			inline = true
		}

		field {
			title = "Invite"
			description = "[Invite Me!](https://discordapp.com/oauth2/authorize?client_id=482340927709511682&scope=bot&permissions=8)"
			inline = true
		}

		field {
			title = "Ping"
			description = "${jda.ping}ms"
			inline = true
		}

		footer {
			message = getDateTime()
			iconUrl = jda.selfUser.avatarUrl
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
		val helpLink = "https://github.com/woojiahao/Taiga/wiki/Commands-%28${command.category}%29#${command.name}"
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
		field {
			title = "Learn More"
			description = "[Wiki page about ${command.name}]($helpLink)"
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
					.joinToString(", ")
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