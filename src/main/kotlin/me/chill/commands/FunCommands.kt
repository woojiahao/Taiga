package me.chill.commands

import com.google.gson.Gson
import com.google.gson.JsonObject
import me.chill.arguments.types.Sentence
import me.chill.arguments.types.UserId
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.utility.jda.unknownErrorEmbed
import me.chill.utility.str
import java.net.HttpURLConnection
import java.net.URL
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
			respond(cookieGiving(randomUser.asMention, invoker.asMention))
		}
	}

	command("cookie") {
		expects(UserId())
		execute {
			respond(cookieGiving(guild.getMemberById(arguments[0]!!.str()).asMention, invoker.asMention))
		}
	}

	command("meme") {
		execute {
			val api = "https://api.imgflip.com/get_memes"
			val url = URL(api)
			val conn = url.openConnection() as HttpURLConnection
			conn.requestMethod = "GET"
			conn.setRequestProperty("Content-Type", "application/json")
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36")

			if (conn.responseCode >= 400) {
				respond(unknownErrorEmbed("meme"))
				return@execute
			}

			val jsonResponse = conn.inputStream.bufferedReader().readLine()
			val data = Gson().fromJson(jsonResponse, JsonObject::class.java)

			if (!data["success"].asBoolean) {
				respond(unknownErrorEmbed("meme"))
				return@execute
			}

			val memes = data["data"].asJsonObject["memes"].asJsonArray.map { i -> i.asJsonObject["url"].asString }
			respond(memes[Random().nextInt(memes.size + 1)])
		}
	}
}

private fun cookieGiving(target: String, from: String) =
	"(づ｡◕‿‿◕｡)づ Have a cookie :cookie: $target courtesy of $from"