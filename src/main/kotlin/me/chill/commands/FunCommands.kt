package me.chill.commands

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import khttp.post
import me.chill.arguments.types.EmoteName
import me.chill.arguments.types.Sentence
import me.chill.arguments.types.UserId
import me.chill.embed.types.animeInformationEmbed
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.interactiveEmbedManager
import me.chill.utility.*
import org.json.JSONObject
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
		execute { jda.getEmoteById(arguments[0]!!.str())?.let { e -> respond("<:${e.name}:${e.id}>") } }
	}

	command("cat") {
		execute { respond("https://aws.random.cat/meow".readAPI()["file"].asString) }
	}

	command("dog") {
		execute { respond("https://random.dog/woof.json".readAPI()["url"].asString) }
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

	command("anime") {
		expects(Sentence())
		execute {
			Thread {
				val searchTerm = arguments[0]!!.str()
				val endpoint = "https://graphql.anilist.co"
				val query = """
				query (${'$'}id: Int, ${'$'}page: Int, ${'$'}perPage: Int, ${'$'}search: String) {
					Page (page: ${'$'}page, perPage: ${'$'}perPage) {
					pageInfo {
						total
						currentPage
						lastPage
						hasNextPage
						perPage
					}

					media (id: ${'$'}id, search: ${'$'}search, type: ANIME) {
						id
						title {
							romaji
							english
							native
						}
						startDate {
							year
							month
							day
						}
						endDate {
							year
							month
							day
						}
						season
						status
						episodes
						duration
						genres
						averageScore
						bannerImage
						popularity
						description
						studios {
							nodes {
								name
							}
						}
					}
				}
			}
			""".trimIndent()
				val variables = "{ \"search\": \"$searchTerm\" }"

				val result = post(
					endpoint,
					mapOf(
						"Content-Type" to "application/json",
						"Accept" to "application/json"
					),
					json = JSONObject(mapOf("query" to query, "variables" to variables))
				).text

				val json = Gson().fromJson(result, JsonObject::class.java)
				if (json["data"] == null) {
					respond(unknownErrorEmbed("anime"))
					return@Thread
				}

				val animes = json["data"].asJsonObject["Page"].asJsonObject["media"].asJsonArray
				if (animes.size() == 0) {
					respond(
						failureEmbed(
							"Anime Search Fail",
							"Anime: **$searchTerm** was not found"
						)
					)
					return@Thread
				}

				if (animes.size() == 1) {
					respond(animeInformationEmbed(animes[0].asJsonObject))
				} else {
					val options = animes.map { anime ->
						var name = anime.asJsonObject.getAsJsonObject("title")["english"]
						if (name.isJsonNull) {
							name = anime.asJsonObject.getAsJsonObject("title")["romaji"]
						}
						name.asString
					}
					interactiveEmbedManager.send(
						options.toTypedArray(),
						channel,
						"Anime Search",
						"The search term: **$searchTerm** returned multiple results, select the one you wish to view"
					) { message, selection ->
						val selectedAnime = options.indexOfFirst { option -> option == selection }
						message.editMessage(animeInformationEmbed(animes[selectedAnime].asJsonObject)).complete()
					}
				}
			}.start()
		}
	}
}

private fun cookieGiving(target: String, from: String) = "(づ｡◕‿‿◕｡)づ Have a cookie :cookie: $target courtesy of $from"