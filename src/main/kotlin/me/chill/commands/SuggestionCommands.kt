package me.chill.commands

import me.chill.arguments.types.Sentence
import me.chill.arguments.types.SuggestionId
import me.chill.arguments.types.Word
import me.chill.database.operations.*
import me.chill.database.states.TargetChannel
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.settings.blue
import me.chill.settings.green
import me.chill.settings.orange
import me.chill.settings.red
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
			if (getPoolSize(getGuild().id) == 0) {
				respond(
					simpleEmbed(
						"No Suggestion In Pool",
						"There are no suggestions in the pool",
						null,
						blue
					)
				)
			} else {
				val latestSuggestion = getLatestSuggestionInPool(getGuild().id)
				respond(suggestionInformationEmbed(getJDA().findUser(latestSuggestion.suggesterId), latestSuggestion))
			}
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
			val message = messageAction.complete()
			val messageId = message.id
			message.addReaction("\uD83D\uDC4D").complete()
			message.addReaction("\uD83D\uDC4E").complete()
			acceptLatestSuggestionInPool(guild.id, messageId)
		}
	}

	command("respond") {
		expects(
			SuggestionId(),
			Word(inclusion = arrayOf("Accepted", "Declined")),
			Sentence()
		)
		execute {
			val guild = getGuild()
			val args = getArguments()
			val messageId = args[0] as String
			val status = args[1] as String

			val suggestionChannel = guild.getTextChannelById(me.chill.database.operations.getChannel(TargetChannel.Suggestion, guild.id))
			val message = suggestionChannel.getMessageById(messageId).complete()
			val original = message.embeds[0]
			val suggesterName = original.title.substring(original.title.lastIndexOf(" "))

			respond("Suggestion Responded To:")
			respond(original)

			message.editMessage(
				publicSuggestionEmbed(
					suggesterName,
					original.thumbnail.url,
					original.description,
					when (status.toLowerCase()) {
						"accepted" -> green
						"declined" -> red
						else -> orange
					},
					args[2] as String
				)
			).queue()
			clearSuggestion(guild.id, messageId)
		}
	}
}

private fun publicSuggestionEmbed(suggesterName: String, suggesterAvatar: String,
								  suggestion: String, suggestionColor: Int? = null,
								  suggestionResponseReason: String? = null) =
	embed {
		title = "Suggestion by $suggesterName"
		description = suggestion
		color = suggestionColor ?: orange
		thumbnail = suggesterAvatar

		field {
			title = "Status"
			description = when (suggestionColor) {
				green -> "Accepted"
				red -> "Declined"
				else -> "In Review"
			}
		}

		if (suggestionResponseReason != null) {
			field {
				title = "Reason"
				description = suggestionResponseReason
			}
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