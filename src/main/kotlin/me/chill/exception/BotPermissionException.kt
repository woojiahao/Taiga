package me.chill.exception

class BotPermissionException(val reason: String) : TaigaException(mapOf("Reason" to reason))