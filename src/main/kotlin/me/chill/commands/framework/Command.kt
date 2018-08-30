package me.chill.commands.framework

import me.chill.commands.arguments.Argument
import me.chill.commands.arguments.types.Sentence
import me.chill.commands.framework.ContainerKey.*
import me.chill.credentials
import me.chill.exception.TaigaException
import me.chill.utility.jda.send
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.MessageEmbed

class Command(var name: String, val category: String) {
	private var action: (Command.(Map<ContainerKey, Any?>) -> Unit)? = null

	var commandInformation: MutableMap<ContainerKey, Any?> = mutableMapOf()
		private set

	init {
		commandInformation[Jda] = null
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

	fun run(jda: JDA, guild: Guild, invoker: Member, messageChannel: MessageChannel, input: Array<String>?) {
		commandInformation[Jda] = jda
		commandInformation[Server] = guild
		commandInformation[Invoker] = invoker
		commandInformation[Channel] = messageChannel
		commandInformation[Input] = input
		this.action!!(commandInformation)
	}

	fun getGuild() = commandInformation[Server] as Guild
	fun getInvoker() = commandInformation[Invoker] as Member
	fun getChannel() = commandInformation[Channel] as MessageChannel
	fun getArguments() = commandInformation[Input] as Array<*>
	fun getJDA() = commandInformation[Jda] as JDA
	fun getArgumentTypes() = commandInformation[ArgumentTypes] as Array<Argument>

	fun respond(embed: MessageEmbed?) = getChannel().send(embed)
	fun respond(message: String) = getChannel().send(message)

	override fun toString() =
		StringBuilder()
			.append(
				"`${credentials!!.prefix}$name ")
			.append(
				getArguments()
					.joinToString(" ") {
						"{ $it }"
					}
			)
			.append("`")
			.toString()
}