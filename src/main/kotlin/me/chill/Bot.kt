package me.chill

import me.chill.database.setupDatabase
import me.chill.embed.EmbedManager
import me.chill.embed.interactive.InteractiveEmbedManager
import me.chill.events.*
import me.chill.framework.CommandContainer
import me.chill.json.help.CommandInfo
import me.chill.json.help.loadHelp
import me.chill.raid.RaidManager
import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity

var commandInfo: List<CommandInfo>? = null
val raidManger = RaidManager()
val interactiveEmbedManager = InteractiveEmbedManager()
val embedManager = EmbedManager()

fun main() {
  setupDatabase(databaseUrl)
  CommandContainer.loadContainer()
  commandInfo = loadHelp()

  val jda = JDABuilder(AccountType.BOT)
    .apply {
      setStatus(OnlineStatus.ONLINE)
      setToken(token)
      setActivity(Activity.playing("${defaultPrefix}help"))
      addEventListeners(
        OnJoinEvent(),
        OnLeaveEvent(),
        OnChannelCreationEvent(),
        InputEvent(),
        InteractiveEmbedEvent(),
        OnUserActivityEvent()
      )
    }.build()

  botOwnerId = jda.retrieveApplicationInfo().complete().owner.id
}