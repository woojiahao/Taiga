package me.chill.embed.types

import me.chill.settings.green
import me.chill.settings.orange
import me.chill.settings.red
import me.chill.suggestion.UserSuggestion
import me.chill.utility.embed
import net.dv8tion.jda.api.entities.User
import org.joda.time.format.DateTimeFormat


fun publicSuggestionEmbed(suggesterName: String, suggesterAvatar: String,
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

    suggestionResponseReason?.let {
      field {
        title = "Reason"
        description = suggestionResponseReason
      }
    }
  }

fun suggestionInformationEmbed(suggester: User, suggestion: UserSuggestion) =
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

fun suggestionDisabledEmbed(serverPrefix: String) =
  embed {
    title = "Suggestion System Disabled"
    description = "Unable to use suggestion commands as the suggestion system is disabled"
    color = red

    field {
      title = "How to enable it?"
      description = "You can enable the suggestion system via `${serverPrefix}enable suggestion`"
    }
  }