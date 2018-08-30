package me.chill.commands.arguments

// todo: split this into 2 classes, one to be returned from Argument, the other to be returned from parseArguments
data class ParseMap(var status: Boolean = true,
					var errMsg: String = "",
					val parseValue: String = "",
					val parsedValues: MutableList<String> = mutableListOf())