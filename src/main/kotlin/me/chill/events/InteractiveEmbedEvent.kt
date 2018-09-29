package me.chill.events

import me.chill.exception.ListenerEventException
import me.chill.interactiveEmbedManager
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class InteractiveEmbedEvent : ListenerAdapter() {
	override fun onMessageReactionAdd(event: MessageReactionAddEvent?) {
		event ?: throw ListenerEventException("On Message Reaction Add", "null event object")

		val messageId = event.messageId
		val emoteName = event.reactionEmote.name

		val hasEmbedSaved = interactiveEmbedManager.hasEmbed(messageId)
		val addedValidEmote = hasEmbedSaved && interactiveEmbedManager.hasReaction(messageId, emoteName)
		val botReacted = event.member.user.isBot
		if (!hasEmbedSaved || !addedValidEmote || botReacted) return

		interactiveEmbedManager.getEmbed(messageId).optionSelected(emoteName)
	}
}