# Creating Custom Commands
> Taiga introduces a simple to use [command framework](https://github.com/woojiahao/Taiga/tree/master/src/main/kotlin/me/chill/commands/framework) to speed up external use.

## New Category
When adding a new command, you might find that the command does not fit the theme of any of the existing categories available, namely:

* Administration
* Welcome
* Moderation
* Permission
* Utility
* Role
* Raid
* Suggestion
* Animal
* Macro

If that is the case, you should create a new category to house similar commands so as to reduce the pollution of the other categories with unrelated commands:

1. Add a new `.kt` file in the [`type`](https://github.com/woojiahao/Taiga/tree/master/src/main/kotlin/me/chill/commands/type) package, ideally, this class should follow the default naming convention of `<Category Name>Commands.kt`
2. Inside the newly-created file, add a [function](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-function.html) and inside of the body of the function, call the [`commands`](https://github.com/woojiahao/Taiga/blob/master/src/main/kotlin/me/chill/commands/framework/CommandContainer.kt#L33) function. You will also need to pass the name of the category to the `commands` method

    ```kotlin
    fun playCommands() = commands("Play") { }
    ```
3. Add the annotation `@CommandCategory` to the newly-created function, as this allows the command framework to detect this set of commands and seamlessly integrate them into Taiga

    ```kotlin
    @CommandCategory
    fun playCommands() = commands("Play") { }
    ```
4. You have created a new command category called `Play` successfully, you are now ready to add new commands to this category

## New Command
All commands that are created will go within the `commands` lambda and follow the following syntax:
```kotlin
@CommandCategory
fun playCommands() = commands("Play") { 
    command("roll") {
        expects() // this is used to highlight what arguments are needed for the command to work
        execute {
            // this is the code that gets executed when the command is invoked
        }
    }
}
```

### Specifying arguments
By default, Taiga has several argument types available to her, and these include:

1. [CategoryName](https://github.com/woojiahao/Taiga/blob/master/src/main/kotlin/me/chill/arguments/types/CategoryName.kt)
2. [ChannelId](https://github.com/woojiahao/Taiga/blob/master/src/main/kotlin/me/chill/arguments/types/ChannelId.kt)
3. [CommandName](https://github.com/woojiahao/Taiga/blob/master/src/main/kotlin/me/chill/arguments/types/CommandName.kt)
4. [Integer](https://github.com/woojiahao/Taiga/blob/master/src/main/kotlin/me/chill/arguments/types/Integer.kt)
5. [RoleId](https://github.com/woojiahao/Taiga/blob/master/src/main/kotlin/me/chill/arguments/types/RoleId.kt)
6. [Sentence](https://github.com/woojiahao/Taiga/blob/master/src/main/kotlin/me/chill/arguments/types/Sentence.kt)
7. [UserId](https://github.com/woojiahao/Taiga/blob/master/src/main/kotlin/me/chill/arguments/types/UserId.kt)
8. [Word](https://github.com/woojiahao/Taiga/blob/master/src/main/kotlin/me/chill/arguments/types/Word.kt)

To specify an argument, simply pass it to the command via the `expects` method
```kotlin
command("help") {
    expects(CommandName())
}
```

Some argument types can also take in optional arguments to create limits or ranges for the command
```kotlin
command("nuke") {
    expects(Integer(lowerRange = 0, upperRange = 10))
}
```

You can also create your own argument types, a guide for doing so can be found [here](https://github.com/woojiahao/Taiga/wiki/Adding-new-commands#adding-a-new-argument-type)

### Retrieving command invocation information
Each `Command` object holds onto the following pieces of information about the command invocation:

* Guild - This is the guild that the command was invoked in 
* Channel - This is the message channel that the command was invoked in
* Invoker - This is the member that invoked the command
* JDA - This is the JDA object and it can hold information about the bot, that is not just specific to the Guild
* Input - These are the arguments that the user has specified for the command

To access any of these pieces of information, you can use the helper functions: `getGuild()`, `getChannel()`, `getInvoker()`, `getJDA()` and `getArguments()` within the `command` function.

### Responding to the user
When creating commands, you will be needing to respond to the user, thus a utility function has been made available to the `Command` object to respond to the user in the same message channel in which they invoked the command.

```kotlin
@CommandCategory
fun playCommands() = commands("Play") { 
    command("roll") {
        expects() // this is used to highlight what arguments are needed for the command to work
        execute {
            respond("You rolled a 6")
        }
    }
}
```
## New Argument Type
### Structure
All argument types in Taiga implement the interface [`Argument`](https://github.com/woojiahao/Taiga/blob/master/src/main/kotlin/me/chill/arguments/Argument.kt) as well as override the `check` method which returns a [`ParseMap`](https://github.com/woojiahao/Taiga/blob/master/src/main/kotlin/me/chill/arguments/ParseMap.kt) which holds the information about the attempt to parse the argument.

Inside the `check` method, you will specify how the argument should be parsed and decide what constitutes a passing parse and a failing parse. When you decide on the passing/failing conditions, construct a ParseMap that handles each condition.

Every argument type receives both the guild that the command was invoked in as well as the actual argument passed through the command.

### Failing ParseMap
When returning a failing ParseMap, set the `status` property of the ParseMap to `false` and supply an error message to be displayed to the user to inform them of the reason why the argument was invalid, you can use discord formatting to format this text and the formatting will show up in the error embed.

### Passing ParseMap
When returning a passing ParseMap, you only need to set the `parseValue` to the value after any modifications, in the following example, channel ids can be in the Discord rich format, and in that case, we will be stripping the original argument of that rich format and simply returning the raw channel id.

```kotlin
class ChannelId : Argument {
    override fun check(guild: Guild, arg: String): ParseMap {
	var toCheck = arg
	if (arg.startsWith("<#") && arg.endsWith(">")) toCheck = arg.substring(2, arg.length - 1)

	if (toCheck.toLongOrNull() == null) {
		return ParseMap(false, "ID: **$toCheck** is not valid")
	}

	if (guild.getTextChannelById(toCheck) == null) {
		return ParseMap(false, "Channel by the ID of **$toCheck** is not found")
	}

	return ParseMap(parseValue = toCheck)
    }
}
```