# Database Optimisation
Taiga was built with the 10,000 row limit that Heroku Postgres imposes on Hobby dev accounts. As such, a degree of 
performance is sacrificed in order to ensure that this row limit is not quickly exceeded.

If you are wishing to host your own instance of Taiga with no restrictions on the number of rows that you can keep and 
you wish to improve the performance of Taiga, perform the following changes to reduce the loading time of Taiga.

## Optimising Storage Of Permissions
Permissions are only recorded when the command is available to any role other than the highest role in the server.

This results in a huge performance penalty when using the `viewpermissions` command. 

### PermissionOperations.kt
To assist with batch inserts and batch removal of permissions, add the following method to `PermissionOperations.kt`

```kotlin
fun addBatchPermissions(commandNames: Array<String>, 
						serverId: String, highestRoleId: String) {
	transaction {
		Permission.batchInsert(commandNames.asIterable()) { commandName ->
			this[Permission.commandName] = commandName
			this[Permission.serverId] = serverId
			this[permission] = highestRoleId
		}
	}
}

fun removeBatchPermissions(serverId: String) {
	transaction {
		Permission.deleteWhere { Permission.serverId eq serverId }
	}
}
```
	
### OnJoinEvent.kt
Rather than logging just the commands that have a permission other than the highest role, log every single command's 
permission into the database when Taiga joins the server.

This edit goes into the `OnGuildJoin()` method of the `OnJoinEvent` listener.

```kotlin
class OnJoinEvent : ListenerAdapter() {
	override fun onGuildJoin(event: GuildJoinEvent?) {
		// ...
		addBatchPermissions(
			CommandContainer.getCommandNames(), 
			serverId, 
			event.guild.roles[0].id
		)
	}
}
```

### OnLeaveEvent.kt
When the bot leaves the server, you can clean up all of the preset permissions.

**Note:** If you want the bot to store these information in the even when the bot returns, you can opt out of this edit.

This edit goes into the `OnGuildLeave()` method of the `OnLeaveEvent` listener.

```kotlin
class OnLeaveEvent : ListenerAdapter() {
	override fun OnGuildLeave(event: GuildLeaveEvent?) {
		// ...
		removeBatchPermissions(serverId)
	}
}
```

### PermissionCommands.kt
Now that all permissions are being stored into the database regardless of the permission level, we can re-edit the way 
of retrieving the permission map to reduce overhead.

This edit goes into the `generatePermissionList()` method of the `PermissionCommands.kt` command set.

```kotlin
private fun generatePermissionsList(guild: Guild, commandNames: Array<String>) =
	commandNames
		.map { "**$it** :: ${guild.getRoleById(getPermission(it, guild.id)).name}" }
		.joinToString("\n") { "- $it" }
```
