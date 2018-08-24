package me.chill.commands.container

import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageChannel

class Command(var name: String) {
	var args: MutableMap<ContainerKeys, Any?> = mutableMapOf()
		private set
	private var action: (Command.(Map<ContainerKeys, Any?>) -> Unit)? = null

	init {
		args[ContainerKeys.Jda] = null
		args[ContainerKeys.Guild] = null
		args[ContainerKeys.Invoker] = null
		args[ContainerKeys.Channel] = null
		args[ContainerKeys.Input] = null
	}

	fun expects(vararg args: Any) {
		this.args[ContainerKeys.Input] = args
	}

	fun behavior(func: Command.(Map<ContainerKeys, Any?>) -> Unit) {
		action = func
	}

	fun execute(jda: JDA, guild: Guild, invoker: Member, originalMessageChannel: MessageChannel, input: Array<String>?) {
		args[ContainerKeys.Jda] = jda
		args[ContainerKeys.Guild] = guild
		args[ContainerKeys.Invoker] = invoker
		args[ContainerKeys.Channel] = originalMessageChannel
		args[ContainerKeys.Input] = input
		this.action!!(args)
	}
}