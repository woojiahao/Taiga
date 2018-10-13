package me.chill.embed

import net.dv8tion.jda.core.entities.MessageEmbed

class EmbedManager {
	private val embeds = mutableMapOf<String, EmbedCreator>()

	private fun addServer(serverId: String) {
		if (!embeds.keys.contains(serverId)) {
			embeds[serverId] = EmbedCreator()
			embeds[serverId]?.title = "Title"
		}
	}

	fun getEmbed(serverId: String): MessageEmbed? {
		addServer(serverId)
		return embeds[serverId]?.build()
	}

	fun hasField(serverId: String, fieldId: Int): Boolean {
		addServer(serverId)
		return fieldId > 0 && fieldId <= embeds[serverId]?.getFieldQuantity()!!
	}

	fun clearEmbed(serverId: String) {
		addServer(serverId)
		embeds.remove(serverId)
	}

	fun setColor(serverId: String, color: Int) {
		addServer(serverId)
		embeds[serverId]?.color = color
	}

	fun setTitle(serverId: String, title: String) {
		addServer(serverId)
		embeds[serverId]?.title = title
	}

	fun setDescription(serverId: String, description: String) {
		addServer(serverId)
		embeds[serverId]?.description = description
	}

	fun setThumbnail(serverId: String, thumbnail: String) {
		addServer(serverId)
		embeds[serverId]?.thumbnail = thumbnail
	}

	fun addField(serverId: String) {
		addServer(serverId)
		embeds[serverId]?.field {}
	}

	fun setFieldTitle(serverId: String, fieldId: Int, title: String) {
		addServer(serverId)
		embeds[serverId]?.setFieldTitle(fieldId, title)
	}

	fun setFieldDescription(serverId: String, fieldId: Int, title: String) {
		addServer(serverId)
		embeds[serverId]?.setFieldDescription(fieldId, title)
	}

	fun removeField(serverId: String, fieldId: Int) {
		addServer(serverId)
		embeds[serverId]?.removeField(fieldId)
	}

	fun clearThumbnail(serverId: String) {
		addServer(serverId)
		embeds[serverId]?.thumbnail = null
	}
}