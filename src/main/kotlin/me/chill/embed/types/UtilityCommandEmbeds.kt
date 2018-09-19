package me.chill.embed.types

import me.chill.framework.Command
import me.chill.framework.CommandContainer
import me.chill.framework.CommandSet
import me.chill.json.help.category
import me.chill.json.help.description
import me.chill.json.help.example
import me.chill.json.help.syntax
import me.chill.settings.*
import me.chill.utility.getDateTime
import me.chill.utility.jda.embed
import me.chill.utility.jda.printMember
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageEmbed

fun changeLogEmbed(botName: String, buildVersion: String,
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
				message = "Released on: $releaseDate"
				iconUrl = null
			}
		}

fun botInfoEmbed(jda: JDA) =
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

fun serverInfoEmbed(guild: Guild) =
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

fun commandInfoEmbed(command: Command) =
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

fun listCommandsEmbed(commandSets: List<CommandSet>, avatarUrl: String) =
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

fun pingEmbed(latency: Long): MessageEmbed? {
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