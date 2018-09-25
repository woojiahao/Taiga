package me.chill.commands

import com.google.gson.Gson
import com.google.gson.JsonObject
import me.chill.arguments.types.*
import me.chill.database.operations.getPrefix
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
import org.jsoup.Jsoup
import java.io.File
import java.io.FileReader
import java.net.URLEncoder

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
		expects(ArgumentMix(CategoryName(), CommandName()))
		execute {
			val attempt = arguments[0]!!.str()
			if (CommandContainer.hasCommand(attempt)) {
				respond(commandInfoEmbed(CommandContainer.getCommand(attempt)[0]))
			} else if (CommandContainer.hasCategory(attempt)) {
				respond(categoryInfoEmbed(CommandContainer.getCommandSet(attempt)))
			}
		}
	}

	command("help") {
		execute {
			respond(listCommandsEmbed(CommandContainer.commandSets, jda.selfUser.avatarUrl, getPrefix(guild.id)))
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
					fileName.substring(fileName.indexOf("_") + 1, fileName.lastIndexOf(".")).toInt()
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

			val log = extractChangelog(latestChangeLog, true)
			respond(
				changeLogEmbed(
					jda.selfUser.name,
					log[ChangeLogComponents.BuildVersion]!!,
					log[ChangeLogComponents.Changes]!!,
					log[ChangeLogComponents.Title]!!,
					log[ChangeLogComponents.ReleaseDate]!!,
					log[ChangeLogComponents.Contributors]!!,
					log[ChangeLogComponents.Commands]
				)
			)
		}
	}

	command("changelog") {
		expects(ChangeLog())
		execute {
			val log = extractChangelog(arguments[0]!!.str())
			respond(
				changeLogEmbed(
					jda.selfUser.name,
					log[ChangeLogComponents.BuildVersion]!!,
					log[ChangeLogComponents.Changes]!!,
					log[ChangeLogComponents.Title]!!,
					log[ChangeLogComponents.ReleaseDate]!!,
					log[ChangeLogComponents.Contributors]!!,
					log[ChangeLogComponents.Commands]
				)
			)
		}
	}

	// Command courtesy of HotBot
	command("google") {
		expects(Sentence())
		execute {
			val links = Jsoup
				.connect("http://www.google.com/search?q=" + URLEncoder.encode(arguments[0]!!.str(), "UTF-8"))
				.userAgent("Mozilla/5.0")
				.get()
				.select(".g>.r>a")

			respond(links.first().absUrl("href"))
		}
	}
}

private fun extractChangelog(changelog: String, isName: Boolean = false) =
	extract(if (isName) changelog else "changelogs/changelog_$changelog.json")

private fun extract(title: String): Map<ChangeLogComponents, String?> {
	val changelogDetails = Gson().fromJson<JsonObject>(FileReader(File(title)), JsonObject::class.java)

	val buildTitle = changelogDetails["buildTitle"].asString
	val changes = changelogDetails["changes"]
		.asJsonArray
		.mapIndexed { index, s -> "${index + 1}. ${s.asString}" }
		.joinToString("\n")
	val releaseDate = changelogDetails["releaseDate"].asString
	val contributors = changelogDetails["contributors"].asJsonArray.joinToString("\n") { "- ${it.asString}" }
	val buildVersion = changelogDetails["buildNumber"].asString
	val commands: String? =
		if (changelogDetails.has("commands")) {
			if (changelogDetails["commands"].asJsonArray.size() == 0) "No new commands added"
			else changelogDetails["commands"].asJsonArray.joinToString("\n") { "- `${it.asString}`" }
		} else {
			null
		}


	return mapOf(
		ChangeLogComponents.Title to buildTitle,
		ChangeLogComponents.Changes to changes,
		ChangeLogComponents.ReleaseDate to releaseDate,
		ChangeLogComponents.Contributors to contributors,
		ChangeLogComponents.BuildVersion to buildVersion,
		ChangeLogComponents.Commands to commands
	)
}

private enum class ChangeLogComponents {
	Title, Changes, ReleaseDate, Contributors, BuildVersion, Commands
}