# Argument Types
## End Arguments
End arguments are arguments that have to be placed at the end of an argument list for a command. If such is not done so,
an EndArgumentException is thrown during command initialization.

The current list of end arguments include:

* Sentence

## Types
### CategoryName
Arguments passed will be checked to confirm that they correspond to an existing command
category name.

### ChannelId
Arguments passed will be checked to confirm that the channel passed exists on the server.

#### Parsing {docsify-ignore}
Channel mentions will be parsed to remove the `<#` at the start of the channel id and `>` at the end of the channel
id.

### CommandName
Arguments passed will be checked to confirm that they correspond to an existing command name.

### Integer
Arguments passed will be checked to confirm that they are a valid integer.

#### Optional Checks {docsify-ignore}
* **lowerRange** - Integers passed will be checked to ensure they are not less than the specified lowerRange
* **upperRange** - Integers passed will be checked to ensure they are not greater than the specified upperRange

### RoleId
Arguments passed will be checked to confirm that they correspond to an existing role id of a role in the server

### Sentence
Arguments passed will be checked to confirm that the argument is not blank

### UserId
Arguments passed will be checked to confirm that the argument is a valid user id of a user in the server.

#### Parsing {docsify-ignore}
User mentions will be parsed to remove the `<@` at the start of the user id and `>` at the end of the user id.

#### Optional Checks {docsify-ignore}
* **globalSearch** - Enabling this check will change the scope of the user id search to be searching for an existing user
across all of Discord

### Word
Arguments passed will be checked to ensure that the word is not empty.

#### Optional Checks {docsify-ignore}
* **inclusion** - Arguments must match the words in the inclusion scope
* **exclusion** - Arguments must not include the words in the exclusion scope

### MacroName
Arguments passed will be checked to ensure that the macro name is not an overlapping command name and is not an existing
macro name.

#### Optional Checks {docsify-ignore}
* **isExisting** - Enabling this check will check if the macro name passed is an existing macro name

### SuggestionId
Arguments passed will be checked to confirm that the suggestion does exist on the server and the suggestion id is found
in the suggestion channel of the server.

### Prefix
Arguments passed will be checked to ensure they are within 3 characters long and are not composed solely of letters/digits.

### ArgumentList
Arguments passed will be composed of a singular other argument type and separated by a pipe. 

Every argument in the list is checked based on the specified argument type.

Argument lists cannot be spaced.

#### Mandatory Checks {docsify-ignore}
* **argumentType** - Argument type that each and every one in the argument list will be checked against

#### Optional Checks {docsify-ignore}
* **pipes** - Changes what the separators will be used between each argument in the list, defaults to `,`

### ArgumentMix
Arguments passed will be checked against a set of argument types and if one succeeds, the value is assumed to the that
passing value.

ArgumentMix cannot have the same argument type passed to it.

#### Mandatory Checks {docsify-ignore}
* **arguments** - List of argument types to check for

### ChangeLog
Arguments passed will be checked to ensure they correspond to an existing changelog number.

### DiscordInvite
Argument passed will be checked to ensure they are a valid Discord invite.

#### Optional Checks {docsify-ignore}
* **isExisting** - Checks if the invite is an existing one stored in the server's whitelist

### EmoteName
Arguments passed will be checked to ensure they are a valid emote name from any server Taiga is in.

Returns the first emote that matches the name passed.

### StrikeId
Arguments passed will be checked to ensure they are an existing strike id within the server.



