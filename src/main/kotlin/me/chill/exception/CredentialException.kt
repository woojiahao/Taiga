package me.chill.exception

class CredentialException(credential: String, message: String) : TaigaException(
	"\n\n\tCredential: $credential" +
		"\n\tReason: $message\n" +
		"\n\tFix: https://woojiahao.github.io/Taiga/#/installation?id=credential-exceptions"
)