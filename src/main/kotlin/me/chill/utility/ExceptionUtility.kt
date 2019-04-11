package me.chill.utility

import me.chill.exception.CommandException

fun commandErr(name: String, reason: String, predicate: () -> Boolean) {
  if (predicate()) throw CommandException(name, reason)
}