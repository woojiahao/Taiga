package me.chill.embed.interactive

enum class InteractiveEmote(val unicode: String, val number: Int) {
	Previous("\uD83D\uDC48", -1),
	Next("\uD83D\uDC49", -1),
	Zero("0⃣", 0),
	One("1⃣", 1),
	Two("2⃣", 2),
	Three("3⃣", 3),
	Four("4⃣", 4),
	Five("5⃣", 5),
	Six("6⃣", 6),
	Seven("7⃣", 7),
	Eight("8⃣", 8),
	Nine("9⃣", 9);

	companion object {
		val numbering = values().filterNot { it == Next || it == Previous }
		val navigationButtons = values().filter { it == Next || it == Previous }

		fun isSupportedReactionEmote(attempt: String) = values().any { it.unicode == attempt }

		fun getNumberingNames() = numbering.map { it.name.toLowerCase() }.toTypedArray()

		fun getUnicode(list: List<InteractiveEmote>) = list.map { it.unicode }

		fun getSelection(selection: String) = values().first { it.unicode == selection }

		fun isNumber(selection: String) = getUnicode(numbering).contains(selection)

		fun getNavigation(selection: String) = navigationButtons.first { it.unicode == selection }
	}
}