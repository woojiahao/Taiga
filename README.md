# Taiga
|**Prefix:** ?|**Creator:** @Chill#4048|**Language:** Kotlin|**Library:** JDA|[Invite Link](https://discordapp.com/oauth2/authorize?client_id=482340927709511682&scope=bot&permissions=8)|[Development Server](https://discord.gg/xtDNfyw)|[Website](https://woojiahao.github.io/Taiga)|
|---|---|---|---|---|---|---|

## Commands Overview
More information about commands can be found on the [website](https://woojiahao.github.io/Taiga)

1. [Administration](https://github.com/woojiahao/Taiga#Administration)
2. [Welcome](https://github.com/woojiahao/Taiga#Welcome)
3. [Moderation](https://github.com/woojiahao/Taiga#Moderation)
4. [Permission](https://github.com/woojiahao/Taiga#Permission)
5. [Utility](https://github.com/woojiahao/Taiga#Utility)
6. [Role](https://github.com/woojiahao/Taiga#Role)
7. [Raid](https://github.com/woojiahao/Taiga#Raid)
8. [Suggestion](https://github.com/woojiahao/Taiga#Suggestion)
9. [Animal](https://github.com/woojiahao/Taiga#Animal)
10. [Macro](https://github.com/woojiahao/Taiga#Macro)

### Administration
|Name|Description|
|---|---|
|`setjoin`|Sets the join channel, uses the default channel if not set|
|`setlog`|Sets the logging channel, uses the default channel if not set|
|`setsuggestion`|Sets the suggestion channel, uses the default channel if not set|
|`setup`|Sets up the bot for moderation like adding the muted roles and overriding channel permissions|
|`getprefix`|Displays the current prefix set for the current server|
|`setprefix`|Sets the current prefix for the server, prefix cannot be more than 3 characters long|
|`gettimemultiplier`|Displays the current time multiplier|
|`settimemultiplier`|Sets the time multiplier, multipliers include: S, M, H, D|
|`getpreferences`|Displays all the properties set for the server|

### Welcome
|Name|Description|
|---|---|
|`disablewelcome`|Disables bot welcomes for new members|
|`enablewelcome`|Enables bot welcomes for new members|
|`getwelcomeenabled`|Displays whether bot welcomes are enabled or disabled|
|`getwelcomemessage`|Displays what the bot will print when a new member joins the server|
|`setwelcomemessage`|Sets the welcome message for new members|

### Moderation
|Name|Description|
|---|---|
|`echo`|Echos a message into another channel, the message to echo cannot contain mentions|
|`nuke`|Nukes a certain number of messages, from 0 up to 99|
|`mute`|Mutes a user for a specified period of time, defaulted to minutes|
|`history`|Displays the history of a specific member|
|`strike`|Infracts a user|
|`warn`|Infracts a user with a 0 weight strike|
|`wiperecord`|Wipes the user's record from the database|
|`ban`|Bans a user from the server, regardless of whether they are on the server or not|
|`banall`|Bans all users from the server|
|`unban`|Unbans a user and leaves a mark on their history|
|`gag`|Temporarily mutes a user for 5 minutes to allow moderators to handle an ongoing situation|
|`clearstrike`|Removes a strike from a user|

### Permission
|Name|Description|
|---|---|
|`setpermission`|Sets the permission of a command to be available to a role and higher|
|`setpermissioncategory`|Sets the permission of all commands in a category to be available to the same role and higher|
|`viewpermissions`|Views all the permissions set in the server|

### Utility
|Name|Description|
|---|---|
|`commands`|Displays all the commands available to the user|
|`help`|Displays a help card for the user to learn more about a specific command|
|`invite`|Displays all invite links related to me such as my development server and to invite me to your server|
|`ping`|Displays the ping for me|
|`serverinfo`|Displays information about the server|
|`source`|Displays all links related to my source code|
|`botinfo`|Displays information about the bot|

### Role
|Name|Description|
|---|---|
|`assign`|Assigns a role to the specified user, the role must not be higher than my role|
|`getjoinrole`|Displays the join role that will be assigned to new members on join in the server|
|`roles`|Displays all roles and their IDs in the server|
|`setjoinrole`|Sets the join role that will be assigned to new members on join in the server|
|`clearjoinrole`|Clears the join role|
|`unassign`|Removes a role from the user, the role must not be higher than my role|

### Raid
|Name|Description|
|---|---|
|`setraidmessageduration`|Sets how long the user messages are tracked in seconds for raid control|
|`getraidmessagelimit`|Displays how many user messages are tracked for raid control|
|`setraidmessagelimit`|Sets the number of user messages are tracked for raid control|
|`getraidroleexcluded`|Displays what roles are being excluded from raid filter|
|`setraidroleexcluded`|Sets the role that is being excluded from the raid filter, any role higher than this is also automatically excluded|
|`getraidmessageduration`|Displays how long user messages are tracked in seconds for raid control|
|`viewraiders`|Displays all the raiders in the server|
|`freeraider`|Removes a user from the raid list and unmutes them|
|`freeallraiders`|Removes all users from the raid list and unmutes them|

### Suggestion
|Name|Description|
|---|---|
|`poolinfo`|Displays information about the suggestion pool|
|`pooltop`|Displays the latest suggestion in the pool|
|`poolaccept`|Accepts the latest suggestion in the pool|
|`pooldeny`|Denies the latest suggestion in the pool|
|`suggest`|Adds a suggestion to the suggestion pool|
|`respond`|Responds to a suggestion|

### Animal
|Name|Description|
|---|---|
|`cat`|Displays a cat picture/GIF|
|`dog`|Displays a dog picture/GIF|
|`bird`|Displays a bird picture/GIF|

### Macro
|Name|Description|
|---|---|
|`listmacros`|Displays all the macros on the server|
|`addmacro`|Adds a macro to the server|
|`editmacro`|Edits an existing macro on the server|
|`removemacro`|Removes an existing macro from the server|

## Credits
Taiga's command framework is heavily inspired by @Fox#0001 HotBot.

* [HotBot's Repository](https://gitlab.com/Aberrantfox/hotbot)
* [HotBot in action](https://discord.gg/programming)