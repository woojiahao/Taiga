package me.chill.events

import me.chill.exception.ListenerEventException
import me.chill.interactiveEmbedManager
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class InteractiveEmbedEvent : ListenerAdapter() {
  override fun onMessageReactionAdd(event: MessageReactionAddEvent?) {
    with(event) {
      this ?: throw ListenerEventException("On Message Reaction Add", "null event object")

      val emoteName = reactionEmote.name

      val hasEmbedSaved = interactiveEmbedManager.hasEmbed(messageId)
      val addedValidEmote = hasEmbedSaved && interactiveEmbedManager.hasReaction(messageId, emoteName)
      val botReacted = member.user.isBot
      if (!hasEmbedSaved || !addedValidEmote || botReacted) return

      interactiveEmbedManager.getEmbed(messageId).optionSelected(emoteName)

    }
  }
}