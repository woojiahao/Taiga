package me.chill.utility.embed

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed

class EmbedCreator {
	private val fields = mutableListOf<EmbedField>()

	var color: Int? = null

	var title: String? = null
	var thumbnail: String? = null
	var author: String? = null
	var description: String? = null
	var image: String? = null

	fun field(create: EmbedField.() -> Unit) {
		val embedField = EmbedField()
		embedField.create()
		fields.add(embedField)
	}

	fun build(): MessageEmbed? {
		val builder = EmbedBuilder()

		if (color != null) builder.setColor(color!!)

		if (title != null) builder.setTitle(title)
		if (thumbnail != null) builder.setThumbnail(thumbnail)
		if (author != null) builder.setAuthor(author)
		if (description != null) builder.setDescription(description)
		if (image != null) builder.setImage(image)

		fields.forEach {
			builder.addField(it.title, it.description, it.inline)
		}

		return builder.build()
	}
}