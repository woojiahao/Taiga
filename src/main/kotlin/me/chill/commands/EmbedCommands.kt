package me.chill.commands

import me.chill.arguments.types.ChannelId
import me.chill.arguments.types.ColorCode
import me.chill.arguments.types.EmbedFieldId
import me.chill.arguments.types.Sentence
import me.chill.embedManager
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.utility.int
import me.chill.utility.send
import me.chill.utility.str

// todo: Allow multiple embeds
@CommandCategory
fun embedCommands() = commands("Embed") {
	command("postembed") {
		expects(ChannelId())
		execute {
			guild.getTextChannelById(arguments[0]!!.str()).send(embedManager.getEmbed(guild.id))
		}
	}

	command("getembed") {
		execute {
			embedManager.getEmbed(guild.id)?.let { em -> respond(em) }
		}
	}

	command("clearembed") {
		execute {
			embedManager.clearEmbed(guild.id)
			respond("Embed cleared")
		}
	}

	command("setembedcolor") {
		expects(ColorCode())
		execute {
			embedManager.setColor(guild.id, arguments[0]!!.int())
			respond(embedManager.getEmbed(guild.id))
		}
	}

	command("setembedtitle") {
		expects(Sentence())
	}

	command("setembeddescription") {
		expects(Sentence())
	}

	command("addembedfield") {

	}

	command("setembedfield") {
		expects(EmbedFieldId())
	}

	command("clearembedfield") {
		expects(EmbedFieldId())
	}
}

