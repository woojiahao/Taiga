package me.chill.commands.type

import me.chill.commands.arguments.types.Word
import me.chill.commands.framework.*
import me.chill.settings.*
import me.chill.utility.embed
import me.chill.utility.getDateTime
import me.chill.utility.printMember
import me.chill.utility.simpleEmbed
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageEmbed

@CommandCategory
fun utilityCommands() = commands {
	name = "Utility"
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
			val commandName = getArguments()[0] as String
			respond(commandInfoEmbed(CommandContainer.getCommand(commandName)))
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
		title = "Help - ${command.name}"
		color = green
		field {
			title = "Syntax"
			description = "$command"
		}
	}

private fun listCommandsEmbed(commandSets: List<CommandSet>, avatarUrl: String) =
	embed {
		title = "Commands"
		color = green
		thumbnail = avatarUrl
		commandSets.forEach { set ->
			field {
				title = set.name
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