package me.chill.embed.types

import com.google.gson.JsonObject
import me.chill.settings.green
import me.chill.utility.embed
import org.apache.commons.lang3.text.WordUtils

fun animeInformationEmbed(data: JsonObject) = embed {
	var name = data.getAsJsonObject("title")["english"]
	if (name.isJsonNull) {
		name = data.getAsJsonObject("title")["romaji"]
	}
	title = "${name.asString} (${data.getAsJsonObject("title")["native"].asString})"
	color = green
	if (!data["bannerImage"].isJsonNull) {
		image = data["bannerImage"].asString
	}

	field {
		title = "Status"
		description = WordUtils.capitalize(data["status"].asString.toLowerCase())
		inline = true
	}

	field {
		title = "Season"
		description = WordUtils.capitalize(data["season"].asString.toLowerCase())
		inline = true
	}

	field {
		val startDate = data.getAsJsonObject("startDate")
		title = "Start Date"
		description = "${startDate["day"].asString.padStart(2, '0')}/${startDate["month"].asString.padStart(2, '0')}/${startDate["year"].asString}"
		inline = true
	}

	field {
		val endDate = data.getAsJsonObject("endDate")
		title = "End Date"
		description = "${endDate["day"].asString.padStart(2, '0')}/${endDate["month"].asString.padStart(2, '0')}/${endDate["year"].asString}"
		inline = true
	}

	field {
		title = "Episode Count"
		description = "${data["episodes"].asInt} episodes"
		inline = true
	}

	field {
		title = "Episode Duration"
		description = "${data["duration"].asInt} minutes"
		inline = true
	}

	field {
		title = "Score"
		description = "${data["averageScore"]}/100"
		inline = true
	}

	field {
		title = "Popularity"
		description = "${data["popularity"]} followers"
		inline = true
	}

	field {}

	field {
		title = "Genres"
		description = data.getAsJsonArray("genres").joinToString(", ") { genre -> genre.asString }
	}

	field {
		title = "Studios"
		description = data.getAsJsonObject("studios")
			.getAsJsonArray("nodes").joinToString(", ") { studio -> studio.asJsonObject["name"].asString }
	}

	field {}

	field {
		val desc = data["description"].asString
		val finalDescription = StringBuilder()
		if (desc.length > 1024) {
			finalDescription.append(desc.substring(0, 950))
			finalDescription.append("...[Read More](https://anilist.co/anime/${data["id"]})")
		} else {
			finalDescription.append(desc)
		}
		title = "Description"
		description = finalDescription.toString()
	}

	footer {
		iconUrl = null
		message = "Powered by AniList"
	}
}