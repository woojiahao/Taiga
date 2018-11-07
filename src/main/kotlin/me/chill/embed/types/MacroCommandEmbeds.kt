package me.chill.embed.types

import me.chill.database.operations.getMacroList
import me.chill.settings.green
import me.chill.utility.embed


fun macroEditEmbed(title: String, serverId: String, macroName: String, macroDescription: String) =
  embed {
    this.title = title
    color = green
    description = "**$macroName** will now respond with: **$macroDescription**"
    footer {
      iconUrl = null
      message = "${getMacroList(serverId).size}/50 macros"
    }
  }

fun listMacrosEmbed(serverName: String, description: String, serverMacroSize: Int) =
  embed {
    title = "$serverName Macros"
    this.description = description
    color = green
    footer {
      iconUrl = null
      message = "$serverMacroSize/50 macros"
    }
  }