package me.chill.commands

import me.chill.arguments.types.*
import me.chill.embedManager
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.utility.int
import me.chill.utility.send
import me.chill.utility.str

// todo: Allow multiple embeds
@CommandCategory
fun embedCommands() = commands("Embed") {
  command("postembed") {
    expects(ChannelId())
    execute {
      guild.getTextChannelById(arguments[0]!!.str()).send(embedManager.getEmbed(guild.id))
    }
  }

  command("getembed") {
    execute {
      embedManager.getEmbed(guild.id)?.let { em -> respond(em) }
    }
  }

  command("clearembed") {
    execute {
      embedManager.clearEmbed(guild.id)
      respond("Embed cleared")
    }
  }

  command("setembedcolor") {
    expects(ColorCode())
    execute {
      embedManager.setColor(guild.id, arguments[0]!!.int())
      respond(embedManager.getEmbed(guild.id))
    }
  }

  command("setembedtitle") {
    expects(Sentence(1024))
    execute {
      embedManager.setTitle(guild.id, arguments[0]!!.str())
      respond(embedManager.getEmbed(guild.id))
    }
  }

  command("setembeddescription") {
    expects(Sentence(1024))
    execute {
      embedManager.setDescription(guild.id, arguments[0]!!.str())
      respond(embedManager.getEmbed(guild.id))
    }
  }

  command("clearembeddescription") {
    execute {
      embedManager.setDescription(guild.id, "")
      respond(embedManager.getEmbed(guild.id))
    }
  }

  command("setembedthumbnail") {
    expects(Url())
    execute {
      embedManager.setThumbnail(guild.id, arguments[0]!!.str())
      respond(embedManager.getEmbed(guild.id))
    }
  }

  command("clearembedthumbnail") {
    execute {
      embedManager.clearThumbnail(guild.id)
      respond(embedManager.getEmbed(guild.id))
    }
  }

  command("addfield") {
    execute {
      embedManager.addField(guild.id)
      respond(embedManager.getEmbed(guild.id))
    }
  }

  command("setfieldtitle") {
    expects(EmbedFieldId(), Sentence(1024))
    execute {
      embedManager.setFieldTitle(guild.id, arguments[0]!!.int(), arguments[1]!!.str())
      respond(embedManager.getEmbed(guild.id))
    }
  }

  command("setfielddescription") {
    expects(EmbedFieldId(), Sentence(1024))
    execute {
      embedManager.setFieldDescription(guild.id, arguments[0]!!.int(), arguments[1]!!.str())
      respond(embedManager.getEmbed(guild.id))
    }
  }

  command("removefield") {
    expects(EmbedFieldId())
    execute {
      embedManager.removeField(guild.id, arguments[0]!!.int())
      respond(embedManager.getEmbed(guild.id))
    }
  }
}

