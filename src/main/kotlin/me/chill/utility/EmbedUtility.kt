package me.chill.utility

import me.chill.embed.EmbedCreator
import me.chill.settings.*
import net.dv8tion.jda.api.entities.MessageEmbed

inline fun embed(create: EmbedCreator.() -> Unit): MessageEmbed? {
  val creator = EmbedCreator()
  creator.create()
  return creator.build()
}

fun simpleEmbed(title: String, description: String, thumbnail: String?, color: Int?) =
  embed {
    this.title = title
    this.description = description
    this.thumbnail = thumbnail
    this.color = color
  }

fun successEmbed(title: String, description: String, thumbnail: String? = happy, color: Int? = green) =
  simpleEmbed(title, description, thumbnail, color)

fun failureEmbed(title: String, description: String, thumbnail: String? = shock, color: Int? = red) =
  simpleEmbed(title, description, thumbnail, color)

fun cleanEmbed(title: String, description: String) =
  simpleEmbed(title, description, null, green)

fun unknownErrorEmbed(commandName: String) =
  embed {
    title = "Something went wrong"
    description = "Something went wrong when using **$commandName**"
    color = red
    thumbnail = myBad

    field {
      title = "How to fix?"
      description = "You can join my [development server](https://discord.gg/xtDNfyw) and file an issue or open an issue on my [repository](https://github.com/woojiahao/Taiga)"
    }
  }