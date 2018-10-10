package me.chill.embed

import net.dv8tion.jda.core.entities.MessageEmbed

class EmbedManager {
	private val embeds = mutableMapOf<String, EmbedCreator>()

	private fun addServer(serverId: String) {
		if (!embeds.keys.contains(serverId)) {
			embeds[serverId] = EmbedCreator()
			embeds[serverId]?.title = "Title"
			embeds[serverId]?.description = "Description"
		}
	}

	fun getEmbed(serverId: String): MessageEmbed? {
		addServer(serverId)
		return embeds[serverId]?.build()
	}

	fun clearEmbed(serverId: String) {
		addServer(serverId)
		embeds.remove(serverId)
	}

	fun setColor(serverId: String, color: Int) {
		addServer(serverId)
		embeds[serverId]?.color = color
	}
}