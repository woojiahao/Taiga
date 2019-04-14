package me.chill.framework

import me.chill.arguments.Argument
import me.chill.arguments.types.ArgumentMix
import me.chill.exception.CommandException
import me.chill.utility.commandErr
import org.apache.commons.lang3.text.WordUtils
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner

class CommandContainer private constructor() {
  companion object {
    val commandSets = mutableListOf<CommandSet>()

    val commandList
      get() = commandSets.map { it.commands }.flatten()

    fun loadContainer() {
      Reflections("me.chill.commands", MethodAnnotationsScanner()).also {
        it.getMethodsAnnotatedWith(CommandCategory::class.java).forEach { cc -> cc.invoke(null) }
      }
      checkCommands()
    }

    fun hasCommand(command: String) = commandSets.any { it.hasCommand(command) }

    fun hasCategory(category: String) = commandSets.any { it.categoryName == WordUtils.capitalize(category) }

    fun getCommandSet(category: String) = commandSets.first { it.categoryName == category }

    fun getCommand(command: String) = commandList.filter { it.name == command }

    fun getCommandNames() = commandList.map { it.name }.distinct()

    fun getGlobalCommands() =
      commandList
        .filter { it.isGlobal == true }
        .map { it.name }
        .distinct()
  }
}

inline fun commands(categoryName: String, create: CommandSet.() -> Unit) {
  val set = CommandSet(categoryName)
  set.create()
  for (command in set.commands) {
    if (command.isGlobal == null) {
      command.setGlobal(set.getGlobal())
    } else {
      command.isGlobal?.let {
        command.setGlobal(it)
      }
    }
  }
  CommandContainer.commandSets.add(set)
}

private fun checkCommands() {
  CommandContainer.commandList.forEach {
    it.action ?: throw CommandException(it.name, "All commands must implement an execute body")

    commandErr(it.name, "Command name must start with letter or digit") { !it.name[0].isLetterOrDigit() }

    // TODO: Properly handle category name checking
    commandErr(it.name, "Command names should not be the same as a category name") {
      WordUtils.capitalize(it.name) == it.category
    }

    val isArgumentMixPredicate: (Argument) -> Boolean = { arg -> arg::class.java == ArgumentMix::class.java }
    val hasArgumentMix = it.argumentTypes.any(isArgumentMixPredicate)
    if (hasArgumentMix) {
      commandErr(it.name, "Unable to create an ArgumentMix of the same argument type") {
        val argMix = it.argumentTypes.first(isArgumentMixPredicate) as ArgumentMix
        val uniqueArgumentsInMix = argMix.arguments.distinctBy { mix -> mix::class.java }

        uniqueArgumentsInMix.size != argMix.arguments.size
      }
    }

    val overloadedCommands = CommandContainer.getCommand(it.name)
    overloadedCommands.forEach { overloadedCommand ->
      with(overloadedCommand) {
        commandErr(
          it.name,
          "Unable to overload command with the same number of argument types: ${argumentTypes.size}"
        ) {
          this != it && argumentTypes.size == it.argumentTypes.size
        }
      }
    }
  }
}