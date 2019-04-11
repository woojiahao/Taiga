package me.chill.embed.types

import me.chill.framework.Command
import me.chill.utility.findCommand
import me.chill.utility.syntax
import me.chill.settings.red
import me.chill.settings.shock
import me.chill.utility.embed

fun invalidArgumentsEmbed(serverPrefix: String, command: Command, errMsg: String) =
  embed {
    title = "Invalid Arguments"
    description = "Invalid arguments passed to the command: **${command.name}**"
    color = red
    thumbnail = shock

    field {
      title = "Error"
      description = errMsg
      inline = false
    }

    field {
      title = "Syntax"
      description = command.syntax
      inline = false
    }

    field {
      title = "Learn more"
      description = "Use the `${serverPrefix}help ${command.name}` to learn more about the command"
      inline = false
    }
  }

fun insufficientArgumentsEmbed(serverPrefix: String, commandName: String, expected: Array<Int>) =
  embed {
    title = "Insufficient Arguments"
    color = red
    thumbnail = shock
    description = "Command: **$commandName** requires **${expected.joinToString(" or ")}** arguments"

    field {
      title = "Syntax"
      description = "$serverPrefix${findCommand(commandName).syntax}"
      inline = false
    }

    field {
      title = "Example"
      description = "$serverPrefix${findCommand(commandName).example}"
      inline = false
    }
  }