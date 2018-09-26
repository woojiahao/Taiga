package me.chill.commands

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.chill.arguments.types.EmoteName
import me.chill.arguments.types.Sentence
import me.chill.arguments.types.UserId
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.utility.jda.unknownErrorEmbed
import me.chill.utility.readAPI
import me.chill.utility.str
import me.chill.utility.strictReadAPI
import java.util.*

@CommandCategory
fun funCommands() = commands("Fun") {
	setGlobal()
	command("clapify") {
		expects(Sentence())
		execute { respond(arguments[0]!!.str().split(Regex("\\s+")).joinToString(" :clap: ")) }
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
			val memberList = guild.members.filter { member -> !member.user.isBot }
			val randomUser = memberList[Random().nextInt(memberList.size + 1)]
			respond(cookieGiving(randomUser.asMention, invoker.asMention))
		}
	}

	command("cookie") {
		expects(UserId())
		execute { respond(cookieGiving(guild.getMemberById(arguments[0]!!.str()).asMention, invoker.asMention)) }
	}

	command("meme") {
		execute {
			val (status, data) = "https://api.imgflip.com/get_memes".strictReadAPI<JsonObject>()

			if (!status || data == null || !data["success"].asBoolean) {
				respond(unknownErrorEmbed("meme"))
				return@execute
			}

			val memes = data["data"].asJsonObject["memes"].asJsonArray.map { i -> i.asJsonObject["url"].asString }
			respond(memes[Random().nextInt(memes.size + 1)])
		}
	}

	command("joke") {
		execute {
			val data = "https://08ad1pao69.execute-api.us-east-1.amazonaws.com/dev/random_joke".readAPI()
			Thread {
				respond("**Q:** ${data["setup"].asString}")
				Thread.sleep(1000)
				respond("**A:** ${data["punchline"]}")
			}.start()
		}
	}

	command("emote") {
		expects(EmoteName())
		execute {
			val emote = jda.getEmoteById(arguments[0]!!.str())
			emote?.let { e ->
				respond("<:${e.name}:${e.id}>")
			}
		}
	}

	command("cat") {
		execute {
			val result = "https://aws.random.cat/meow".readAPI()
			respond(result["file"].asString)
		}
	}

	command("dog") {
		execute {
			val result = "https://random.dog/woof.json".readAPI()
			respond(result["url"].asString)
		}
	}

	command("bird") {
		execute {
			val (status, data) = "http://shibe.online/api/birds?count=1&urls=true&httpsUrls=true".strictReadAPI<JsonArray>()

			if (!status || data == null) {
				respond(unknownErrorEmbed("bird"))
				return@execute
			}

			respond(data[0].asString)
		}
	}
}

private fun cookieGiving(target: String, from: String) = "(づ｡◕‿‿◕｡)づ Have a cookie :cookie: $target courtesy of $from"