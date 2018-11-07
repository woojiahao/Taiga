package me.chill.exception

open class TaigaException(map: Map<String, String>) : Exception(
  "\n${map.map { "\n\t${it.key}: ${it.value}" }.joinToString("")}\n"
)