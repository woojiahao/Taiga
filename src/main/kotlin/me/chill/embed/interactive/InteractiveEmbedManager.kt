package me.chill.embed.interactive

import me.chill.settings.green
import me.chill.utility.embed
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageChannel

class InteractiveEmbedManager {
	private val interactiveEmbeds = mutableListOf<InteractiveEmbed>()

	private fun generatePagination(data: Array<String>): Pagination {
		var counter = 1
		val distribution = data.toList().chunked(10).associate { counter++ to it }
		return Pagination(distribution.size, distribution)
	}
	private fun generateReactionList(pagination: Pagination): List<String> {
		val reactionList = mutableListOf<String>()
		if (pagination.hasMoreThanOnePage()) {
			reactionList.add(InteractiveEmote.Previous.unicode)
		}

		pagination.getCurrentPage()?.let {
			reactionList.addAll(InteractiveEmote.getUnicode(InteractiveEmote.numbering.subList(0, it.size)))
		}

		if (pagination.hasMoreThanOnePage()) {
			reactionList.add(InteractiveEmote.Next.unicode)
		}

		return reactionList
	}

	fun send(
		data: Array<String>,
		channel: MessageChannel,
		title: String,
		description: String,
		action: (Message, String) -> Unit
	) {
		val pagination = generatePagination(data)

		val message = channel.sendMessage(
			reactionEmbed(
				title,
				formatContent(pagination),
				description,
				pagination
			)
		).complete()

		populateReaction(message, pagination)
		interactiveEmbeds.add(InteractiveEmbed(message, pagination, action))
	}

	fun formatContent(pagination: Pagination): String {
		val content = pagination.getCurrentPage()?.let {
			InteractiveEmote.getNumberingNames().zip(it).joinToString("\n\n") { entry -> ":${entry.first}: ${entry.second}" }
		}
		content ?: return "No content to be displayed"

		return content
	}

	fun populateReaction(message: Message, pagination: Pagination) {
		message.clearReactions().queue()
		generateReactionList(pagination).forEach { message.addReaction(it).complete() }
	}

	fun hasReaction(id: String, reactionName: String) = generateReactionList(getEmbed(id).pagination).contains(reactionName)

	fun hasEmbed(id: String) = interactiveEmbeds.any { it.message.id == id }

	fun getEmbed(id: String) = interactiveEmbeds.first { it.message.id == id }
}

private fun reactionEmbed(title: String, content: String, description: String, pagination: Pagination) =
	embed {
		this.title = title
		color = green
		this.description = description

		field {
			this.title = "Options:"
			this.description = content
		}

		footer {
			iconUrl = null
			message = pagination.toString()
		}
	}