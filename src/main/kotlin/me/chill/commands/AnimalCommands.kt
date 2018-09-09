package me.chill.commands

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.utility.jda.failureEmbed
import java.net.HttpURLConnection
import java.net.URL

@CommandCategory
fun animalCommands() = commands("Animal") {
	command("cat") {
		execute {
			val result = Gson().fromJson(URL("https://aws.random.cat/meow").readText(), JsonObject::class.java)
			respond(result["file"].asString)
		}
	}

	command("dog") {
		execute {
			val result = Gson().fromJson(URL("https://random.dog/woof.json").readText(), JsonObject::class.java)
			respond(result["url"].asString)
		}
	}

	command("bird") {
		execute {
			val api = "http://shibe.online/api/birds?count=1&urls=true&httpsUrls=true"
			val url = URL(api)
			val conn = url.openConnection() as HttpURLConnection
			conn.requestMethod = "GET"
			conn.setRequestProperty("Content-Type", "application/json")
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36")

			if (conn.responseCode >= 400) {
				respond(
					failureEmbed(
						"Bird API Failed",
						"Something bad happened when calling the bird API"
					)
				)
			}

			val jsonResponse = conn.inputStream.bufferedReader().readLine()
			respond(Gson().fromJson(jsonResponse, JsonArray::class.java)[0].asString)
		}
	}
}