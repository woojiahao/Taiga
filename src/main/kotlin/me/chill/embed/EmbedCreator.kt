package me.chill.embed

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed

class EmbedCreator {
	private val fields = mutableListOf<EmbedField>()

	private var footer: EmbedFooter? = null

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

	fun footer(create: EmbedFooter.() -> Unit) {
		val embedFooter = EmbedFooter()
		embedFooter.create()
		footer = embedFooter
	}

	fun build(): MessageEmbed? {
		val builder = EmbedBuilder()

		color?.let { builder.setColor(color!!) }
		title?.let { builder.setTitle(title) }
		thumbnail?.let { builder.setThumbnail(thumbnail) }
		author?.let { builder.setAuthor(author) }
		description?.let { builder.setDescription(description) }
		image?.let { builder.setImage(image) }

		footer?.let { builder.setFooter(footer!!.message, footer!!.iconUrl) }

		fields.forEach { builder.addField(it.title, it.description, it.inline) }

		return builder.build()
	}
}