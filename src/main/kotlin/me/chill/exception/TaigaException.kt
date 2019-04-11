package me.chill.exception

open class TaigaException(map: Map<String, String>) : Exception(generateExceptionMessage(map)) {
  companion object {
    fun generateExceptionMessage(data: Map<String, String>): String {
      val content = data
        .map { "${it.key}: ${it.value}" }
        .joinToString("\n\t")
      return "\n$content\n"
    }
  }
}