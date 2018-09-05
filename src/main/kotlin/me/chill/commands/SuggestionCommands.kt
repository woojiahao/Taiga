package me.chill.commands

import me.chill.arguments.types.Sentence
import me.chill.database.operations.*
import me.chill.database.states.TargetChannel
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.settings.blue
import me.chill.settings.orange
import me.chill.suggestion.UserSuggestion
import me.chill.utility.embed
import me.chill.utility.findUser
import me.chill.utility.simpleEmbed
import me.chill.utility.successEmbed
import net.dv8tion.jda.core.entities.User
import org.joda.time.format.DateTimeFormat

@CommandCategory
fun suggestionCommands() = commands("Suggestion") {
	command("poolinfo") {
		execute {
			respond(
				simpleEmbed(
					"${getGuild().name} Suggestion Pool",
					"There are **${getPoolSize(getGuild().id)}** suggestion(s)",
					null,
					blue
				)
			)
		}
	}

	command("pooltop") {
		execute {
			val latestSuggestion = getLatestSuggestionInPool(getGuild().id)
			respond(suggestionInformationEmbed(getJDA().findUser(latestSuggestion.suggesterId), latestSuggestion))
		}
	}

	command("suggest") {
		expects(Sentence())
		execute {
			addSuggestionToPool(getGuild().id, getInvoker().user.id, getArguments()[0] as String)
			respond(
				successEmbed(
					"Suggestion Added",
					"Your suggestion has been added to the pool and will be subjected to review",
					null
				)
			)
		}
	}

	command("pooldeny") {
		execute {
			val latestSuggestion = getLatestSuggestionInPool(getGuild().id)
			denyLatestSuggestionInPool(getGuild().id)
			respond("Suggestion Denied:")
			respond(suggestionInformationEmbed(getJDA().findUser(latestSuggestion.suggesterId), latestSuggestion))
		}
	}

	command("poolaccept") {
		execute {
			val guild = getGuild()
			val latestSuggestion = getLatestSuggestionInPool(guild.id)

			val suggester = getJDA().findUser(latestSuggestion.suggesterId)

			respond("Suggestion Accepted:")
			respond(suggestionInformationEmbed(suggester, latestSuggestion))

			val suggestionChannelId = me.chill.database.operations.getChannel(TargetChannel.Suggestion, guild.id)
			val suggestionChannel = guild.getTextChannelById(suggestionChannelId)
			val messageAction = suggestionChannel.sendMessage(
				publicSuggestionEmbed(
					suggester.name,
					suggester.avatarUrl,
					latestSuggestion.suggestionDescription
				)
			)
			val messageId = messageAction.complete().id
			acceptLatestSuggestionInPool(guild.id, messageId)
		}
	}
}

private fun publicSuggestionEmbed(suggesterName: String, suggesterAvater: String, suggestion: String) =
	embed {
		title = "Suggestion by $suggesterName"
		description = suggestion
		color = orange
		thumbnail = suggesterAvater

		field {
			title = "Status"
			description = "In Review"
		}
	}

private fun suggestionInformationEmbed(suggester: User, suggestion: UserSuggestion) =
	embed {
		title = "Suggestion by ${suggester.name}"
		description = suggestion.suggestionDescription
		color = orange
		thumbnail = suggester.avatarUrl
		field {
			title = "Creation Date"
			description = DateTimeFormat.forPattern("dd/MM/yyyy").print(suggestion.suggestionDate)
		}

		field {
			title = "User ID"
			description = suggestion.suggesterId
		}
	}