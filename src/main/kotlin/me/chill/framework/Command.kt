package me.chill.framework

import me.chill.arguments.Argument
import me.chill.arguments.types.RegexArg
import me.chill.arguments.types.Sentence
import me.chill.defaultPrefix
import me.chill.exception.EndArgumentException
import me.chill.framework.ContainerKey.*
import me.chill.utility.send
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed

val endArgumentList = arrayOf<Class<*>>(
  Sentence::class.java,
  RegexArg::class.java
)

class Command(val name: String, val category: String) {
  private var action: (Command.(Map<ContainerKey, Any?>) -> Unit)? = null
  private val commandInformation: MutableMap<ContainerKey, Any?> = mutableMapOf()
  private var isGlobal: Boolean? = null

  val guild get() = commandInformation[Server] as Guild
  val invoker get() = commandInformation[Invoker] as Member
  val channel get() = commandInformation[Channel] as MessageChannel
  val arguments get() = commandInformation[Input] as Array<*>
  val jda get() = commandInformation[Jda] as JDA
  val serverPrefix get() = commandInformation[ServerPrefix] as String

  @Suppress("UNCHECKED_CAST")
  val argumentTypes
    get() = commandInformation[ArgumentTypes] as Array<Argument>

  init {
    commandInformation[Jda] = null
    commandInformation[ServerPrefix] = defaultPrefix
    commandInformation[Server] = null
    commandInformation[Invoker] = null
    commandInformation[Channel] = null
    commandInformation[ArgumentTypes] = emptyArray<Argument>()
    commandInformation[Input] = emptyArray<String>()
  }

  fun expects(vararg args: Argument) {
    val endArgCheck = checkForEndArguments(*args)
    if (!endArgCheck.first) throw EndArgumentException(name, endArgCheck.second)

    this.commandInformation[ArgumentTypes] = args
  }

  fun execute(func: Command.(Map<ContainerKey, Any?>) -> Unit) {
    action = func
  }

  fun setGlobal(isGlobal: Boolean = true) {
    this.isGlobal = isGlobal
  }

  fun getGlobal() = isGlobal

  fun run(
    serverPrefix: String,
    jda: JDA,
    guild: Guild,
    invoker: Member,
    messageChannel: MessageChannel,
    input: List<String>?
  ) {
    commandInformation[Jda] = jda
    commandInformation[Server] = guild
    commandInformation[ServerPrefix] = serverPrefix
    commandInformation[Invoker] = invoker
    commandInformation[Channel] = messageChannel
    commandInformation[Input] = input

    action!!(commandInformation)
  }

  fun getAction() = action

  fun respond(embed: MessageEmbed?) = channel.send(embed)
  fun respond(message: String) = channel.send(message)

  private fun checkForEndArguments(vararg args: Argument) =
    when {
      args.any {
        endArgumentList.contains(it.javaClass)
      } && args.indexOf(
        args.find {
          endArgumentList.contains(it.javaClass)
        }) != args.size - 1 -> Pair(false, "End arguments must be placed at the end of the argument list")

      args.indexOf(
        args.find {
          endArgumentList.contains(it.javaClass)
        }) != args.lastIndexOf(
        args.find {
          endArgumentList.contains(it.javaClass)
        }) -> Pair(false, "End arguments cannot be repeated")

      args.filter {
        endArgumentList.contains(it.javaClass)
      }.size > 1 -> Pair(false, "Cannot have more than 1 end argument in a single argument list")

      else -> Pair(true, "")
    }
}