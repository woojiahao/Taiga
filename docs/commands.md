# Commands Overview
1. [Administration](commands.md?id=Administration)
2. [Welcome](commands.md?id=Welcome)
3. [Moderation](commands.md?id=Moderation)
4. [Permission](commands.md?id=Permission)
5. [Utility](commands.md?id=Utility)
6. [Role](commands.md?id=Role)
7. [Raid](commands.md?id=Raid)
8. [Suggestion](commands.md?id=Suggestion)
9. [Animal](commands.md?id=Animal)
10. [Macro](commands.md?id=Macro)

### [Administration](Administration_commands.md)
|Name|Description|
|---|---|
|[`setjoin`](Administration_commands.md?id=setjoin)|Sets the join channel, uses the default channel if not set|
|[`setlog`](Administration_commands.md?id=setlog)|Sets the logging channel, uses the default channel if not set|
|[`setsuggestion`](Administration_commands.md?id=setsuggestion)|Sets the suggestion channel, uses the default channel if not set|
|[`setup`](Administration_commands.md?id=setup)|Sets up the bot for moderation like adding the muted roles and overriding channel permissions|
|[`getprefix`](Administration_commands.md?id=getprefix)|Displays the current prefix set for the current server|
|[`setprefix`](Administration_commands.md?id=setprefix)|Sets the current prefix for the server, prefix cannot be more than 3 characters long|
|[`gettimemultiplier`](Administration_commands.md?id=gettimemultiplier)|Displays the current time multiplier|
|[`settimemultiplier`](Administration_commands.md?id=settimemultiplier)|Sets the time multiplier, multipliers include: S, M, H, D|
|[`getpreferences`](Administration_commands.md?id=getpreferences)|Displays all the properties set for the server|

### [Welcome](Welcome_commands.md)
|Name|Description|
|---|---|
|[`disablewelcome`](Welcome_commands.md?id=disablewelcome)|Disables bot welcomes for new members|
|[`enablewelcome`](Welcome_commands.md?id=enablewelcome)|Enables bot welcomes for new members|
|[`getwelcomeenabled`](Welcome_commands.md?id=getwelcomeenabled)|Displays whether bot welcomes are enabled or disabled|
|[`getwelcomemessage`](Welcome_commands.md?id=getwelcomemessage)|Displays what the bot will print when a new member joins the server|
|[`setwelcomemessage`](Welcome_commands.md?id=setwelcomemessage)|Sets the welcome message for new members|

### [Moderation](Moderation_commands.md)
|Name|Description|
|---|---|
|[`echo`](Moderation_commands.md?id=echo)|Echos a message into another channel, the message to echo cannot contain mentions|
|[`nuke`](Moderation_commands.md?id=nuke)|Nukes a certain number of messages, from 0 up to 99|
|[`mute`](Moderation_commands.md?id=mute)|Mutes a user for a specified period of time, defaulted to minutes|
|[`history`](Moderation_commands.md?id=history)|Displays the history of a specific member|
|[`strike`](Moderation_commands.md?id=strike)|Infracts a user|
|[`warn`](Moderation_commands.md?id=warn)|Infracts a user with a 0 weight strike|
|[`wiperecord`](Moderation_commands.md?id=wiperecord)|Wipes the user's record from the database|

### [Permission](Permission_commands.md)
|Name|Description|
|---|---|
|[`setpermission`](Permission_commands.md?id=setpermission)|Sets the permission of a command to be available to a role and higher|
|[`setpermissioncategory`](Permission_commands.md?id=setpermissioncategory)|Sets the permission of all commands in a category to be available to the same role and higher|
|[`viewpermissions`](Permission_commands.md?id=viewpermissions)|Views all the permissions set in the server|

### [Utility](Utility_commands.md)
|Name|Description|
|---|---|
|[`commands`](Utility_commands.md?id=commands)|Displays all the commands available to the user|
|[`help`](Utility_commands.md?id=help)|Displays a help card for the user to learn more about a specific command|
|[`invite`](Utility_commands.md?id=invite)|Displays all invite links related to me such as my development server and to invite me to your server|
|[`ping`](Utility_commands.md?id=ping)|Displays the ping for me|
|[`serverinfo`](Utility_commands.md?id=serverinfo)|Displays information about the server|
|[`source`](Utility_commands.md?id=source)|Displays all links related to my source code|
|[`botinfo`](Utility_commands.md?id=botinfo)|Displays information about the bot|

### [Role](Role_commands.md)
|Name|Description|
|---|---|
|[`assign`](Role_commands.md?id=assign)|Assigns a role to the specified user, the role must not be higher than my role|
|[`getjoinrole`](Role_commands.md?id=getjoinrole)|Displays the join role that will be assigned to new members on join in the server|
|[`roles`](Role_commands.md?id=roles)|Displays all roles and their IDs in the server|
|[`setjoinrole`](Role_commands.md?id=setjoinrole)|Sets the join role that will be assigned to new members on join in the server|
|[`clearjoinrole`](Role_commands.md?id=clearjoinrole)|Clears the join role|
|[`unassign`](Role_commands.md?id=unassign)|Removes a role from the user, the role must not be higher than my role|

### [Raid](Raid_commands.md)
|Name|Description|
|---|---|
|[`setraidmessageduration`](Raid_commands.md?id=setraidmessageduration)|Sets how long the user messages are tracked in seconds for raid control|
|[`getraidmessagelimit`](Raid_commands.md?id=getraidmessagelimit)|Displays how many user messages are tracked for raid control|
|[`setraidmessagelimit`](Raid_commands.md?id=setraidmessagelimit)|Sets the number of user messages are tracked for raid control|
|[`getraidroleexcluded`](Raid_commands.md?id=getraidroleexcluded)|Displays what roles are being excluded from raid filter|
|[`setraidroleexcluded`](Raid_commands.md?id=setraidroleexcluded)|Sets the role that is being excluded from the raid filter, any role higher than this is also automatically excluded|
|[`getraidmessageduration`](Raid_commands.md?id=getraidmessageduration)|Displays how long user messages are tracked in seconds for raid control|
|[`viewraiders`](Raid_commands.md?id=viewraiders)|Displays all the raiders in the server|
|[`freeraider`](Raid_commands.md?id=freeraider)|Removes a user from the raid list and unmutes them|
|[`freeallraiders`](Raid_commands.md?id=freeallraiders)|Removes all users from the raid list and unmutes them|

### [Suggestion](Suggestion_commands.md)
|Name|Description|
|---|---|
|[`poolinfo`](Suggestion_commands.md?id=poolinfo)|Displays information about the suggestion pool|
|[`pooltop`](Suggestion_commands.md?id=pooltop)|Displays the latest suggestion in the pool|
|[`poolaccept`](Suggestion_commands.md?id=poolaccept)|Accepts the latest suggestion in the pool|
|[`pooldeny`](Suggestion_commands.md?id=pooldeny)|Denies the latest suggestion in the pool|
|[`suggest`](Suggestion_commands.md?id=suggest)|Adds a suggestion to the suggestion pool|
|[`respond`](Suggestion_commands.md?id=respond)|Responds to a suggestion|

### [Animal](Animal_commands.md)
|Name|Description|
|---|---|
|[`cat`](Animal_commands.md?id=cat)|Displays a cat picture/GIF|
|[`dog`](Animal_commands.md?id=dog)|Displays a dog picture/GIF|
|[`bird`](Animal_commands.md?id=bird)|Displays a bird picture/GIF|

### [Macro](Macro_commands.md)
|Name|Description|
|---|---|
|[`listmacros`](Macro_commands.md?id=listmacros)|Displays all the macros on the server|
|[`addmacro`](Macro_commands.md?id=addmacro)|Adds a macro to the server|
|[`editmacro`](Macro_commands.md?id=editmacro)|Edits an existing macro on the server|
|[`removemacro`](Macro_commands.md?id=removemacro)|Removes an existing macro from the server|

