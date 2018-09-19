package me.chill.commands

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import me.chill.arguments.types.CommandName
import me.chill.embed.types.*
import me.chill.framework.CommandCategory
import me.chill.framework.CommandContainer
import me.chill.framework.commands
import me.chill.settings.blue
import me.chill.settings.fondling
import me.chill.settings.serve
import me.chill.utility.jda.failureEmbed
import me.chill.utility.jda.simpleEmbed
import me.chill.utility.jda.successEmbed
import me.chill.utility.str
import net.dv8tion.jda.core.Permission
import java.io.File
import java.io.FileReader

@CommandCategory
fun utilityCommands() = commands("Utility") {
	setGlobal()
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
					"- [Invite me to your server!](${jda.asBot().getInviteUrl(Permission.ADMINISTRATOR)})\n" +
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
						"- [Website](https://woojiahao.github.io/Taiga)",
					serve,
					blue
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