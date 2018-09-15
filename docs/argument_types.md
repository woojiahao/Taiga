# Argument Types
## CategoryName
Arguments passed will be checked to confirm that they correspond to an existing command
category name.

## ChannelId
Arguments passed will be checked to confirm that the channel passed exists on the server.

### Parsing {docsify-ignore}
Channel mentions will be parsed to remove the `<#` at the start of the channel id and `>` at the end of the channel
id.

## CommandName
Arguments passed will be checked to confirm that they correspond to an existing command name.

## Integer
Arguments passed will be checked to confirm that they are a valid integer.

### Optional Checks {docsify-ignore}
* **lowerRange** - Integers passed will be checked to ensure they are not less than the specified lowerRange
* **upperRange** - Integers passed will be checked to ensure they are not greater than the specified upperRange

## RoleId
Arguments passed will be checked to confirm that they correspond to an existing role id of a role in the server

## Sentence
Arguments passed will be checked to confirm that the argument is not blank

## UserId
Arguments passed will be checked to confirm that the argument is a valid user id of a user in the server.

### Parsing {docsify-ignore}
User mentions will be parsed to remove the `<@` at the start of the user id and `>` at the end of the user id.

### Optional Checks {docsify-ignore}
* **globalSearch** - Enabling this check will change the scope of the user id search to be searching for an existing user
across all of Discord

## Word
Arguments passed will be checked to ensure that the word is not empty.

### Optional Checks {docsify-ignore}
* **inclusion** - Arguments must match the words in the inclusion scope
* **exclusion** - Arguments must not include the words in the exclusion scope

## MacroName
Arguments passed will be checked to ensure that the macro name is not an overlapping command name and is not an existing
macro name.

### Optional Checks {docsify-ignore}
* **isExisting** - Enabling this check will check if the macro name passed is an existing macro name

## SuggestionId
Arguments passed will be checked to confirm that the suggestion does exist on the server and the suggestion id is found
in the suggestion channel of the server.

## Prefix
Arguments passed will be checked to ensure they are within 3 characters long.