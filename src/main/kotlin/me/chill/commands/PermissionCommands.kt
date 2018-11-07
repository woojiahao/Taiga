package me.chill.commands

import me.chill.arguments.types.ArgumentMix
import me.chill.arguments.types.CategoryName
import me.chill.arguments.types.CommandName
import me.chill.arguments.types.RoleId
import me.chill.database.operations.addPermission
import me.chill.database.operations.editPermission
import me.chill.database.operations.hasPermission
import me.chill.database.operations.removePermission
import me.chill.embed.types.listBotPermissionsEmbed
import me.chill.embed.types.listPermissionsEmbed
import me.chill.framework.CommandCategory
import me.chill.framework.CommandContainer
import me.chill.framework.commands
import me.chill.utility.str
import me.chill.utility.successEmbed
import org.apache.commons.lang3.text.WordUtils

@CommandCategory
fun permissionCommands() = commands("Permission") {
  command("viewbotpermissions") {
    execute { respond(listBotPermissionsEmbed(guild.getMember(jda.selfUser).permissions)) }
  }

  command("viewpermissions") { execute { respond(listPermissionsEmbed(guild)) } }

  command("setpermission") {
    expects(ArgumentMix("Invalid Command/Category Name", CommandName(), CategoryName()), RoleId())
    execute {
      val roles = guild.roles
      val serverId = guild.id

      val target = arguments[0]!!.str()
      val roleId = arguments[1]!!.str()

      val highestRole = roles[0].id

      when {
        CommandContainer.hasCommand(target) -> {
          handlePermissionAssignment(roleId, highestRole, target, serverId)
          respond(
            successEmbed(
              "Set Command Permission Success!",
              "Command: **$target** has been assigned to **${guild.getRoleById(roleId).name}**"
            )
          )
        }

        CommandContainer.hasCategory(target) -> {
          CommandContainer.getCommandSet(WordUtils.capitalize(target)).commands.forEach { command ->
            handlePermissionAssignment(roleId, highestRole, command.name, serverId)
          }
          respond(
            successEmbed(
              "Set Category Permission Success!",
              "All commands in **$categoryName** has been assigned to **${guild.getRoleById(roleId).name}**")
          )
        }
      }
    }
  }

  command("setglobal") {
    expects(ArgumentMix("Invalid Command/Category Name", CategoryName(), CommandName()))
    execute {
      val target = arguments[0]!!.str()
      val everyoneRole = guild.getRolesByName("@everyone", false)[0]

      when {
        CommandContainer.hasCommand(target) -> {
          if (hasPermission(target, guild.id)) editPermission(target, guild.id, everyoneRole.id)
          else addPermission(target, guild.id, everyoneRole.id)
          respond(
            successEmbed(
              "Set Permission Success!",
              "Command: **$target** is now available to everyone"
            )
          )
        }
        else -> {
          val commandNames = CommandContainer.getCommandSet(target).getCommandNames()
          commandNames.forEach { name ->
            if (hasPermission(name, guild.id)) editPermission(name, guild.id, everyoneRole.id)
            else addPermission(name, guild.id, everyoneRole.id)
          }
          respond(
            successEmbed(
              "Set Category Permission Success!",
              "All commands in **${arguments[0]!!.str()}** is now available to everyone"
            )
          )
        }
      }
    }
  }
}

private fun handlePermissionAssignment(roleId: String, highestRole: String, commandName: String, serverId: String) {
  if (roleId == highestRole) {
    removePermission(commandName, serverId)
  } else {
    if (hasPermission(commandName, serverId)) {
      editPermission(commandName, serverId, roleId)
    } else {
      addPermission(commandName, serverId, roleId)
    }
  }
}