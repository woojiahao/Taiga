package me.chill.commands

import me.chill.arguments.types.Sentence
import me.chill.arguments.types.UserId
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.utility.str
import java.util.*

@CommandCategory
fun funCommands() = commands("Fun") {
	setGlobal()
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

	command("cookie") {
		execute {
			val memberList = guild.members
			val randomUser = memberList[Random().nextInt(memberList.size + 1)]
			respond("(づ｡◕‿‿◕｡)づ Have a cookie :cookie: ${randomUser.asMention} courtesy of ${invoker.asMention}")
		}
	}

	command("cookie") {
		expects(UserId())
		execute {
			respond(cookieGiving(guild.getMemberById(arguments[0]!!.str()).asMention, invoker.asMention))
		}
	}
}

private fun cookieGiving(target: String, from: String) =
	"(づ｡◕‿‿◕｡)づ Have a cookie :cookie: $target courtesy of $from"