package me.chill.commands.categories

import me.chill.commands.container.CommandContainer
import me.chill.commands.container.ContainerKeys
import me.chill.commands.container.command
import me.chill.commands.container.commands
import me.chill.gifs.*
import me.chill.utility.blue
import me.chill.utility.green
import me.chill.utility.jda.embed
import me.chill.utility.jda.send
import me.chill.utility.red
import me.chill.utility.yellow
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.MessageEmbed

fun utilityCommands() = commands {
	command("ping") {
		execute {
			val messageChannel = args[ContainerKeys.Channel] as MessageChannel
			val jda = args[ContainerKeys.Jda] as JDA
			val latency = jda.ping
			messageChannel.send(pingEmbed(latency))
		}
	}

	command("invite") {
		execute {
			val messageChannel = args[ContainerKeys.Channel] as MessageChannel
			messageChannel.send(inviteEmbed())
		}
	}

	command("source") {
		execute {
			val messageChannel = args[ContainerKeys.Channel] as MessageChannel
			messageChannel.send(sourceEmbed())
		}
	}

	command("commands") {
		execute {
			val channel = args[ContainerKeys.Channel] as MessageChannel
			val jda = args[ContainerKeys.Jda] as JDA

			val commands = CommandContainer.getCommandNames()
			val botIcon = jda.selfUser.avatarUrl
			channel.send(listCommandsEmbed(commands, botIcon))
		}
	}
}

private fun listCommandsEmbed(commandNames: Array<String>, avatarUrl: String) =
	embed {
		title = "Commands"
		description = commandNames.sorted().joinToString("\n") {
			"- $it"
		}
		color = green
		thumbnail = avatarUrl
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
		this.color = pingEmbed@color
		this.thumbnail = pingEmbed@thumbnail
	}
}

private fun inviteEmbed() =
	embed {
		title = "Invite Taiga"
		description = "[Invite me](https://discordapp.com/oauth2/authorize?client_id=482340927709511682&scope=bot&permissions=8) to your server!"
		color = blue
		thumbnail = fondling
	}

private fun sourceEmbed() =
	embed {
		title = "Source Code"
		description = "[GitHub repository](https://github.com/woojiahao/Taiga)"
		color = blue
		thumbnail = serve
	}