package me.chill.arguments

open class ArgumentParseMap(val message: String) {
  val isError
    get() = this is ErrorParseMap

  val isSuccess
    get() = this is SuccessParseMap
}

fun createParseMap(predicate: Boolean, successMsg: String, errorMessage: String) =
  if (predicate) SuccessParseMap(successMsg)
  else ErrorParseMap(errorMessage)