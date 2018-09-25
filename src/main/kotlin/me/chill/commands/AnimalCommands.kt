package me.chill.commands

import com.google.gson.JsonArray
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.utility.jda.unknownErrorEmbed
import me.chill.utility.readAPI
import me.chill.utility.strictReadAPI

@CommandCategory
fun animalCommands() = commands("Animal") {
	setGlobal()
	command("cat") {
		execute {
			val result = "https://aws.random.cat/meow".readAPI()
			respond(result["file"].asString)
		}
	}

	command("test") {
		execute {

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