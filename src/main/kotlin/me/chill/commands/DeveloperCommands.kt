package me.chill.commands

import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.settings.blue
import me.chill.settings.serve
import me.chill.utility.simpleEmbed

@CommandCategory
fun developerCommands() = commands("Developer") {
	command("source") {
		execute {
			respond(
				simpleEmbed(
					"Sources",
					"- [GitHub repository](https://github.com/woojiahao/Taiga)\n" +
						"- [Website](https://woojiahao.github.io/Taiga)",
					serve,
					blue
				)
			)
		}
	}
}