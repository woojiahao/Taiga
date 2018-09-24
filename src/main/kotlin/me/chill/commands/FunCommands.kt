package me.chill.commands

import me.chill.arguments.types.Sentence
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.utility.str
import java.util.*

@CommandCategory
fun funCommands() = commands("Fun") {
	command("clapify") {
		expects(Sentence())
		execute {
			respond(arguments[0]!!.str().split(Regex("\\s+")).joinToString(" :clap: "))
		}
	}

	command("flip") {
		execute {
			respond(
				if (Random().nextBoolean()) "Heads"
				else "Tails"
			)
		}
	}
}