package me.chill.embed.interactive

import me.chill.settings.green
import me.chill.utility.embed
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class InteractiveEmbedManager {
  private val interactiveEmbeds = mutableListOf<InteractiveEmbed>()

  private fun generatePagination(data: List<String>): Pagination {
    var counter = 1
    val distribution = data.chunked(10).associate { counter++ to it }
    return Pagination(distribution.size, distribution)
  }

  private fun generateReactionList(pagination: Pagination) =
    mutableListOf<String>().apply {
      if (pagination.hasMoreThanOnePage) this += InteractiveEmote.Previous.unicode

      addAll(
        InteractiveEmote.getUnicode(
          InteractiveEmote.numbering.subList(0, pagination.currentPageContent.size)
        )
      )

      if (pagination.hasMoreThanOnePage) this += InteractiveEmote.Next.unicode
    }.toList()

  fun send(
    data: List<String>,
    channel: MessageChannel,
    title: String,
    description: String,
    action: (Message, String, Int) -> Unit
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

  fun formatContent(pagination: Pagination) =
    if (pagination.currentPageContent.isEmpty()) {
      "No content to be displayed"
    } else {
      InteractiveEmote
        .numberingNames
        .zip(pagination.currentPageContent)
        .joinToString("\n\n") { ":${it.first}: ${it.second}" }
    }

  fun populateReaction(message: Message, pagination: Pagination) {
    message.clearReactions().queue()
    generateReactionList(pagination).forEach { message.addReaction(it).complete() }
  }

  fun clearEmbed(id: String) =
    interactiveEmbeds
      .find { it.message.id == id }
      ?.let { interactiveEmbeds.remove(it) }

  fun hasReaction(id: String, reactionName: String) =
    generateReactionList(getEmbed(id).pagination).contains(reactionName)

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