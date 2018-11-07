package me.chill.exception

class CredentialException(credential: String, message: String) : TaigaException(
  mapOf(
    "Credential" to credential,
    "Reason" to message,
    "Fix" to "https://woojiahao.github.io/Taiga/#/installation?id=credential-exceptions"
  )
)