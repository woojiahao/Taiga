package me.chill.embed.interactive

import me.chill.interactiveEmbedManager
import me.chill.utility.embed
import net.dv8tion.jda.core.entities.Message

data class InteractiveEmbed(
	val message: Message,
	val pagination: Pagination,
	val action: (Message, String) -> Unit
) {
	fun optionSelected(option: String) {
		if (InteractiveEmote.isNumber(option)) {
			val selection = InteractiveEmote.getSelection(option).number
			val data = pagination.getCurrentPage()?.get(selection)
			data?.let { action(message, it) }
			message.clearReactions().complete()
		} else {
			val navigationButton = InteractiveEmote.getNavigation(option).name
			when (navigationButton) {
				"Previous" -> pagination.previousPage()
				"Next" -> pagination.nextPage()
			}

			updateOptions(message, interactiveEmbedManager.formatContent(pagination), pagination.toString())
			interactiveEmbedManager.populateReaction(message, pagination)
		}
	}
}

private fun updateOptions(message: Message, options: String, page: String) {
	message.editMessage(
		embed {
			title = message.embeds[0].title
			description = message.embeds[0].description
			color = message.embeds[0].colorRaw
			field {
				title = "Option:"
				description = "Select the option via the reactions\n\n$options"
			}

			footer {
				iconUrl = null
				this.message = page
			}
		}
	).queue()
}