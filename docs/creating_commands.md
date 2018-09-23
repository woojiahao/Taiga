# Creating Custom Commands
> Taiga introduces a simple to use [command framework](https://github.com/woojiahao/Taiga/tree/master/src/main/kotlin/me/chill/commands/framework) 
to speed up external use.

## New Category
When adding a new command, you might find that the command does not fit the theme of any of the existing categories 
available, namely:

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

If that is the case, you should create a new category to house similar commands so as to reduce the pollution of the 
other categories with unrelated commands:

1. Add a new `.kt` file in the [`commands`](https://github.com/woojiahao/Taiga/tree/master/src/main/kotlin/me/chill/commands) 
package, ideally, this class should follow the default naming convention of `<Category Name>Commands.kt`
2. Inside the newly-created file, add a [function](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-function.html) 
and inside of the body of the function, 
invoke the [`commands`](https://github.com/woojiahao/Taiga/blob/master/src/main/kotlin/me/chill/commands/framework/CommandContainer.kt#L33) 
function. You will also need to pass the name of the category to the `commands` method

    ```kotlin
    fun playCommands() = commands("Play") { }
    ```
    
3. Add the annotation `@CommandCategory` to the newly-created function, as this allows the command framework to detect 
this set of commands and seamlessly integrate them into Taiga

    ```kotlin
    @CommandCategory
    fun playCommands() = commands("Play") { }
    ```
    
4. You have created a new command category called `Play` successfully, you are now ready to add new commands to this 
category

## New Command
All commands that are created for a specific category will go within the `commands` lambda and follow the following syntax:
```kotlin
@CommandCategory
fun playCommands() = commands("Play") { 
    command("roll") {
        expects() // arguments needed for the command
        execute {
            // what the command does
        }
    }
}
```

### Specifying arguments
By default, Taiga has several argument types available to her, and these include:

1. [CategoryName](argument_types.md?id=categoryname)
2. [ChannelId](argument_types.md?id=channelid)
3. [CommandName](argument_types.md?id=commandname)
4. [Integer](argument_types.md?id=integer)
5. [RoleId](argument_types.md?id=roleid)
6. [Sentence](argument_types.md?id=sentence)
7. [UserId](argument_types.md?id=userid)
8. [Word](argument_types.md?id=word)
9. [MacroName](argument_types.md?id=macroname)
10. [SuggestionId](argument_types.md?id=suggestionid)
11. [Prefix](argument_types.md?id=prefix)
11. [ArgumentList](argument_types.md?id=argumentlist)

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

You can also create your own argument types, a guide for doing so can be found [here](creating_commands.md?id=new-argument-type)

### Retrieving command invocation information
Each `Command` object holds onto the following pieces of information about the command invocation:

* **Guild** - Guild that the command was invoked in 
* **Channel** - Message channel that the command was invoked in
* **Invoker** - Member who invoked the command
* **JDA** - JDA object and it can hold information about the bot, that is not just specific to the Guild
* **Input** - Arguments that the user has specified for the command
* **Server Prefix** - Server's prefix

To access any of these pieces of information, you can use the helper properties: `guild`, `channel`, 
`invoker`, `jda`, `arguments` and `serverPrefix` within the `command` function.

### Responding to the user
When creating commands, you will be needing to respond to the user, thus a utility function has been made available to 
the `Command` object to respond to the user in the same message channel in which they invoked the command.

```kotlin
@CommandCategory
fun playCommands() = commands("Play") { 
    command("roll") {
        execute {
            respond("You rolled a 6")
        }
    }
}
```

### Overloading commands
Taiga supports the overloading of existing commands. The only condition is that the number of arguments expected must differ,
otherwise, there will be an exception thrown during run time.

To overload the command, just name the new command to be the same as the original and then, modify the behavior and 
arguments list.

## New Argument Type
### Structure
All custom argument types in Taiga must do the following: 
* Implement the [`Argument`](https://github.com/woojiahao/Taiga/blob/master/src/main/kotlin/me/chill/arguments/Argument.kt) 
interface
* Override the `check` method 
* Return an [`ArgumentParseMap`](https://github.com/woojiahao/Taiga/blob/master/src/main/kotlin/me/chill/arguments/ArgumentParseMap.kt) 
which holds the information about the attempt to parse the argument

Every argument type receives 2 pieces of information to perform the checking:
1. **Guild** - Guild that the command was invoked in
2. **Arg** - Argument that the user passed to the command

#### Failing ArgumentParseMap
When returning a failing ArgumentParseMap, set the `status` property of the ArgumentParseMap to `false` and supply an 
error message to be displayed to the user to inform them of the reason why the argument was invalid.

```kotlin
// Example taken from ChannelId
if (toCheck.toLongOrNull() == null) {
	return ArgumentParseMap(false, "ID: **$toCheck** is not valid")
}
```

#### Passing ArgumentParseMap
When returning a passing ArgumentParseMap, set the `status` property of the ArgumentParseMap to `true` and supply the 
`parsedValue` regardless if the original argument had received any modifications.

```kotlin
// Example taken from ChannelId
var toCheck = arg
if (arg.startsWith("<#") && arg.endsWith(">")) toCheck = arg.substring(2, arg.length - 1)
return ArgumentParseMap(true, parsedValue = toCheck)
```

### Sample

```kotlin
class ChannelId : Argument {
	override fun check(guild: Guild, arg: String): ArgumentParseMap {
		var toCheck = arg
		if (arg.startsWith("<#") && arg.endsWith(">")) {
			toCheck = arg.substring(2, arg.length - 1)
		}

		return when {
			toCheck.toLongOrNull() == null -> 
				ArgumentParseMap(false, "ID: **$toCheck** is not valid")
			guild.getTextChannelById(toCheck) == null -> 
				ArgumentParseMap(false, "Channel by the ID of **$toCheck** is not found")
			else -> 
				ArgumentParseMap(true, parsedValue = toCheck)
		}
	}
}
```

## Adding Documentation
If you create a new command, you will need to create the relevant documentation for it. 

This documentation will not only be used for the `help` command, but also the documentation site which you can deploy
using GitHub pages.

There are scripts included in this repository written in Python to generate documentation and markdown page structure 
from the `help.json` file. 

All you need to do to add documentation for your new command is to add it to the `help.json` file in the following 
format:

```json
{
	"name": "",
	"description": "",
	"syntax": "",
	"example": ""
}
```

Then, to update all affected `.md` files, simply run the following Python script:

```bash
$ cd scripts
$ python update_docs.py
```

**Note:** You should have Python 3.6 and higher installed on your machine.

**Note for Mac OS/X and Linux:** Switch `python` to `python3` to run the commands.