package me.chill.commands

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import me.chill.arguments.types.CommandName
import me.chill.framework.*
import me.chill.json.help.category
import me.chill.json.help.description
import me.chill.json.help.example
import me.chill.json.help.syntax
import me.chill.settings.*
import me.chill.utility.getDateTime
import me.chill.utility.jda.*
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
			respond(commandInfoEmbed(CommandContainer.getCommand(arguments[0]!!.str())[0]))
		}
	}

	command("help") {
		execute {
			respond(listCommandsEmbed(CommandContainer.commandSets, jda.selfUser.avatarUrl))
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
					fileName.substring(fileName.indexOf("_") + 1, fileName.lastIndexOf("."))
				}
				.max()!!

			val latestChangeLog = "changelogs/changelog_$latest.json"
			if (!File(latestChangeLog).exists()) {
				respond(
					failureEmbed(
						"Changelog Reading Failed",
						"Unable to locate changelog file: **$latestChangeLog**"
					)
				)
				return@execute
			}

			val gson = GsonBuilder().create()
			val changelogDetails = gson.fromJson<JsonObject>(FileReader(File(latestChangeLog)), JsonObject::class.java)

			val buildTitle = changelogDetails["buildTitle"].asString
			val changes = changelogDetails["changes"].asJsonArray.mapIndexed { index, s -> "${index + 1}. ${s.asString}" }.joinToString("\n")
			val releaseDate = changelogDetails["releaseDate"].asString
			val contributors = changelogDetails["contributors"].asJsonArray.joinToString("\n") { cont -> "- ${cont.asString}" }
			val buildVersion = changelogDetails["buildNumber"].asString

			respond(
				changeLogEmbed(jda.selfUser.name, buildVersion, changes, buildTitle, releaseDate, contributors)
			)
		}
	}
}

private fun changeLogEmbed(botName: String, buildVersion: String,
						   changelogContents: String, buildTitle: String,
						   releaseDate: String, contributors: String) =
	embed {
		title = "$botName Changelogs"
		color = green

		field {
			title = "Build Title"
			description = buildTitle
		}

		field {
			title = "Changes"
			description = changelogContents
		}

		field {
			title = "Contributors"
			description = contributors
		}

		field {
			title = "Build Version"
			description = buildVersion
			inline = true
		}

		field {
			title = "Learn More"
			description = "[GitHub Repository](https://github.com/woojiahao/Taiga)"
			inline = true
		}

		footer {
			message = "Released on : $releaseDate"
			iconUrl = null
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
		val helpLink = "https://woojiahao.github.io/Taiga/#/${command.category.toLowerCase()}_commands?id=${command.name.toLowerCase()}"
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
			description = "[Documentation on ${command.name}]($helpLink)"
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
	val status = when {
		latency < 30 -> PingStatus.Good
		latency < 60 -> PingStatus.Average
		latency > 90 -> PingStatus.Slow
		else -> PingStatus.Good
	}

	return embed {
		title = "Pong! \uD83C\uDFD3"
		description = "Ping took **${latency}ms**"
		color = status.color
		thumbnail = status.thumbnail
	}
}

private enum class PingStatus(val color: Int, val thumbnail: String) {
	Good(green, happy), Average(yellow, clap), Slow(red, noWay)
}