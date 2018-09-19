package me.chill.framework

import me.chill.arguments.Argument
import me.chill.arguments.types.Sentence
import me.chill.credentials
import me.chill.exception.TaigaException
import me.chill.framework.ContainerKey.*
import me.chill.utility.jda.send
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.MessageEmbed

class Command(var name: String) {
	private var action: (Command.(Map<ContainerKey, Any?>) -> Unit)? = null

	private val commandInformation: MutableMap<ContainerKey, Any?> = mutableMapOf()

	private var isGlobal: Boolean = false

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
		commandInformation[ServerPrefix] = credentials!!.defaultPrefix!!
		commandInformation[Server] = null
		commandInformation[Invoker] = null
		commandInformation[Channel] = null
		commandInformation[ArgumentTypes] = emptyArray<Argument>()
		commandInformation[Input] = emptyArray<String>()
	}

	fun expects(vararg args: Argument) {
		val sentenceNotLast = args.any { it is Sentence } && args.indexOf(args.find { it is Sentence }) != args.size - 1
		val moreThanOneSentence = args.indexOf(args.find { it is Sentence }) != args.lastIndexOf(args.find { it is Sentence })
		val sentenceExceptionMessage = "Every command can only have 1 Sentence argument type and it must be placed at the end of the argument list"
		if (sentenceNotLast || moreThanOneSentence) throw TaigaException(sentenceExceptionMessage)

		this.commandInformation[ArgumentTypes] = args
	}

	fun execute(func: Command.(Map<ContainerKey, Any?>) -> Unit) {
		action = func
	}

	fun setGlobal(isGlobal: Boolean = true) {
		this.isGlobal = isGlobal
	}

	fun getGlobal() = isGlobal

	fun run(serverPrefix: String, jda: JDA, guild: Guild,
			invoker: Member, messageChannel: MessageChannel,
			input: Array<String>?) {
		commandInformation[Jda] = jda
		commandInformation[Server] = guild
		commandInformation[ServerPrefix] = serverPrefix
		commandInformation[Invoker] = invoker
		commandInformation[Channel] = messageChannel
		commandInformation[Input] = input
		this.action!!(commandInformation)
	}

	fun respond(embed: MessageEmbed?) = channel.send(embed)
	fun respond(message: String) = channel.send(message)
}