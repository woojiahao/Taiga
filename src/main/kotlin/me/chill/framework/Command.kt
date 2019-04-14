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
  private val commandInformation = mutableMapOf(
    JDA to null,
    SERVER_PREFIX to defaultPrefix,
    SERVER to null,
    INVOKER to null,
    CHANNEL to null,
    ARGUMENT_TYPE to emptyList<Argument>(),
    INPUT to emptyList<String>()
  )

  var action: (Command.(Map<ContainerKey, Any?>) -> Unit)? = null
    private set

  var isGlobal: Boolean? = null
    private set

  val guild
    get() = commandInformation[SERVER] as Guild

  val invoker
    get() = commandInformation[INVOKER] as Member

  val channel
    get() = commandInformation[CHANNEL] as MessageChannel

  val arguments
    get() = commandInformation[INPUT] as List<*>

  val jda
    get() = commandInformation[JDA] as JDA

  val serverPrefix
    get() = commandInformation[SERVER_PREFIX] as String

  @Suppress("UNCHECKED_CAST")
  val argumentTypes: List<Argument>
    get() = commandInformation[ARGUMENT_TYPE] as List<Argument>

  @Suppress("UNCHECKED_CAST")
  fun expects(vararg args: Argument) {
    val endArgCheck = checkForEndArguments(*args)
    if (!endArgCheck.first) throw EndArgumentException(name, endArgCheck.second)

    commandInformation[ARGUMENT_TYPE] = args.toList()
  }

  fun execute(func: Command.(Map<ContainerKey, Any?>) -> Unit) {
    action = func
  }

  fun setGlobal(isGlobal: Boolean = true) {
    this.isGlobal = isGlobal
  }

  fun run(
    serverPrefix: String,
    jda: JDA,
    guild: Guild,
    invoker: Member,
    messageChannel: MessageChannel,
    input: List<String>?
  ) {
    commandInformation[JDA] = jda
    commandInformation[SERVER] = guild
    commandInformation[SERVER_PREFIX] = serverPrefix
    commandInformation[INVOKER] = invoker
    commandInformation[CHANNEL] = messageChannel
    commandInformation[INPUT] = input

    action?.invoke(this, commandInformation)
  }

  fun respond(embed: MessageEmbed?) = channel.send(embed)
  fun respond(message: String) = channel.send(message)

  @Suppress("UNCHECKED_CAST")
  fun <T> getArgument(index: Int) = arguments[index] as T

  private fun checkForEndArguments(vararg args: Argument) =
    when {
      args.any { endArgumentList.contains(it::class.java) }
          && args.indexOf(args.find { endArgumentList.contains(it::class.java) })
          != args.size - 1 ->
        Pair(false, "End arguments must be placed at the end of the argument list")

      args.indexOf(
        args.find { endArgumentList.contains(it.javaClass) })
          != args.lastIndexOf(args.find { endArgumentList.contains(it.javaClass) }
      ) ->
        Pair(false, "End arguments cannot be repeated")

      args.filter { endArgumentList.contains(it.javaClass) }.size > 1 ->
        Pair(false, "Cannot have more than 1 end argument in a single argument list")

      else -> Pair(true, "")
    }
}