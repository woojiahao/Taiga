package me.chill.commands.container

import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageChannel

class Command(var name: String) {
	var args: MutableMap<String, Any?> = mutableMapOf()
		private set
	private var action: (Command.(Map<String, Any?>) -> Unit)? = null

	init {
		args["jda"] = null
		args["guild"] = null
		args["invoker"] = null
		args["originalMessageChannel"] = null
		args["input"] = null
	}

	fun expects(vararg args: Any) {
		this.args["input"] = args
	}

	fun behavior(func: Command.(Map<String, Any?>) -> Unit) {
		action = func
	}

	fun execute(jda: JDA, guild: Guild, invoker: Member, originalMessageChannel: MessageChannel, input: Array<String>?) {
		args["jda"] = jda
		args["guild"] = guild
		args["invoker"] = invoker
		args["originalMessageChannel"] = originalMessageChannel
		args["input"] = input
		this.action!!(args)
	}
}