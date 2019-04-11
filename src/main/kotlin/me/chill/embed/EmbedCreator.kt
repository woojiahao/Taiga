package me.chill.embed

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed

class EmbedCreator {
  val fields = mutableListOf<EmbedField>()

  var footer: EmbedFooter? = null

  var color: Int? = null
  var title: String? = null
  var thumbnail: String? = null
  var author: String? = null
  var description: String? = null
  var image: String? = null

  inline fun field(create: EmbedField.() -> Unit) {
    fields += EmbedField().apply(create)
  }

  fun getFieldQuantity() = fields.size

  fun setFieldTitle(fieldId: Int, title: String) {
    fields[fieldId - 1].title = title
  }

  fun setFieldDescription(fieldId: Int, description: String) {
    fields[fieldId - 1].description = description
  }

  fun removeField(fieldId: Int) = fields.removeAt(fieldId - 1)

  inline fun footer(create: EmbedFooter.() -> Unit) {
    footer = EmbedFooter().apply(create)
  }

  fun build(): MessageEmbed =
    EmbedBuilder()
      .apply {
        color?.let { setColor(it) }
        title?.let { setTitle(it) }
        thumbnail?.let { setThumbnail(it) }
        author?.let { setAuthor(it) }
        description?.let { setDescription(it) }
        image?.let { setImage(it) }

        footer?.let { setFooter(it.message, it.iconUrl) }

        this@EmbedCreator.fields.forEach { addField(it.title, it.description, it.inline) }
      }.build()
}