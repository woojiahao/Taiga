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

### [Administration](commands_information/Administration_commands.md)
|Name|Description|
|---|---|
|[`setjoin`](commands_information/Administration_commands.md?id=setjoin)|Sets the join channel, uses the default channel if not set|
|[`setlog`](commands_information/Administration_commands.md?id=setlog)|Sets the logging channel, uses the default channel if not set|
|[`setsuggestion`](commands_information/Administration_commands.md?id=setsuggestion)|Sets the suggestion channel, uses the default channel if not set|
|[`setup`](commands_information/Administration_commands.md?id=setup)|Sets up the bot for moderation like adding the muted roles and overriding channel permissions|
|[`getprefix`](commands_information/Administration_commands.md?id=getprefix)|Displays the current prefix set for the current server|
|[`setprefix`](commands_information/Administration_commands.md?id=setprefix)|Sets the current prefix for the server, prefix cannot be more than 3 characters long|
|[`gettimemultiplier`](commands_information/Administration_commands.md?id=gettimemultiplier)|Displays the current time multiplier|
|[`settimemultiplier`](commands_information/Administration_commands.md?id=settimemultiplier)|Sets the time multiplier, multipliers include: S, M, H, D|
|[`getpreferences`](commands_information/Administration_commands.md?id=getpreferences)|Displays all the properties set for the server|

### [Welcome](commands_information/Welcome_commands.md)
|Name|Description|
|---|---|
|[`disablewelcome`](commands_information/Welcome_commands.md?id=disablewelcome)|Disables bot welcomes for new members|
|[`enablewelcome`](commands_information/Welcome_commands.md?id=enablewelcome)|Enables bot welcomes for new members|
|[`getwelcomeenabled`](commands_information/Welcome_commands.md?id=getwelcomeenabled)|Displays whether bot welcomes are enabled or disabled|
|[`getwelcomemessage`](commands_information/Welcome_commands.md?id=getwelcomemessage)|Displays what the bot will print when a new member joins the server|
|[`setwelcomemessage`](commands_information/Welcome_commands.md?id=setwelcomemessage)|Sets the welcome message for new members|

### [Moderation](commands_information/Moderation_commands.md)
|Name|Description|
|---|---|
|[`echo`](commands_information/Moderation_commands.md?id=echo)|Echos a message into another channel, the message to echo cannot contain mentions|
|[`nuke`](commands_information/Moderation_commands.md?id=nuke)|Nukes a certain number of messages, from 0 up to 99|
|[`mute`](commands_information/Moderation_commands.md?id=mute)|Mutes a user for a specified period of time, defaulted to minutes|
|[`history`](commands_information/Moderation_commands.md?id=history)|Displays the history of a specific member|
|[`strike`](commands_information/Moderation_commands.md?id=strike)|Infracts a user|
|[`warn`](commands_information/Moderation_commands.md?id=warn)|Infracts a user with a 0 weight strike|
|[`wiperecord`](commands_information/Moderation_commands.md?id=wiperecord)|Wipes the user's record from the database|

### [Permission](commands_information/Permission_commands.md)
|Name|Description|
|---|---|
|[`setpermission`](commands_information/Permission_commands.md?id=setpermission)|Sets the permission of a command to be available to a role and higher|
|[`setpermissioncategory`](commands_information/Permission_commands.md?id=setpermissioncategory)|Sets the permission of all commands in a category to be available to the same role and higher|
|[`viewpermissions`](commands_information/Permission_commands.md?id=viewpermissions)|Views all the permissions set in the server|

### [Utility](commands_information/Utility_commands.md)
|Name|Description|
|---|---|
|[`commands`](commands_information/Utility_commands.md?id=commands)|Displays all the commands available to the user|
|[`help`](commands_information/Utility_commands.md?id=help)|Displays a help card for the user to learn more about a specific command|
|[`invite`](commands_information/Utility_commands.md?id=invite)|Displays all invite links related to me such as my development server and to invite me to your server|
|[`ping`](commands_information/Utility_commands.md?id=ping)|Displays the ping for me|
|[`serverinfo`](commands_information/Utility_commands.md?id=serverinfo)|Displays information about the server|
|[`source`](commands_information/Utility_commands.md?id=source)|Displays all links related to my source code|
|[`botinfo`](commands_information/Utility_commands.md?id=botinfo)|Displays information about the bot|

### [Role](commands_information/Role_commands.md)
|Name|Description|
|---|---|
|[`assign`](commands_information/Role_commands.md?id=assign)|Assigns a role to the specified user, the role must not be higher than my role|
|[`getjoinrole`](commands_information/Role_commands.md?id=getjoinrole)|Displays the join role that will be assigned to new members on join in the server|
|[`roles`](commands_information/Role_commands.md?id=roles)|Displays all roles and their IDs in the server|
|[`setjoinrole`](commands_information/Role_commands.md?id=setjoinrole)|Sets the join role that will be assigned to new members on join in the server|
|[`clearjoinrole`](commands_information/Role_commands.md?id=clearjoinrole)|Clears the join role|
|[`unassign`](commands_information/Role_commands.md?id=unassign)|Removes a role from the user, the role must not be higher than my role|

### [Raid](commands_information/Raid_commands.md)
|Name|Description|
|---|---|
|[`setraidmessageduration`](commands_information/Raid_commands.md?id=setraidmessageduration)|Sets how long the user messages are tracked in seconds for raid control|
|[`getraidmessagelimit`](commands_information/Raid_commands.md?id=getraidmessagelimit)|Displays how many user messages are tracked for raid control|
|[`setraidmessagelimit`](commands_information/Raid_commands.md?id=setraidmessagelimit)|Sets the number of user messages are tracked for raid control|
|[`getraidroleexcluded`](commands_information/Raid_commands.md?id=getraidroleexcluded)|Displays what roles are being excluded from raid filter|
|[`setraidroleexcluded`](commands_information/Raid_commands.md?id=setraidroleexcluded)|Sets the role that is being excluded from the raid filter, any role higher than this is also automatically excluded|
|[`getraidmessageduration`](commands_information/Raid_commands.md?id=getraidmessageduration)|Displays how long user messages are tracked in seconds for raid control|
|[`viewraiders`](commands_information/Raid_commands.md?id=viewraiders)|Displays all the raiders in the server|
|[`freeraider`](commands_information/Raid_commands.md?id=freeraider)|Removes a user from the raid list and unmutes them|
|[`freeallraiders`](commands_information/Raid_commands.md?id=freeallraiders)|Removes all users from the raid list and unmutes them|

### [Suggestion](commands_information/Suggestion_commands.md)
|Name|Description|
|---|---|
|[`poolinfo`](commands_information/Suggestion_commands.md?id=poolinfo)|Displays information about the suggestion pool|
|[`pooltop`](commands_information/Suggestion_commands.md?id=pooltop)|Displays the latest suggestion in the pool|
|[`poolaccept`](commands_information/Suggestion_commands.md?id=poolaccept)|Accepts the latest suggestion in the pool|
|[`pooldeny`](commands_information/Suggestion_commands.md?id=pooldeny)|Denies the latest suggestion in the pool|
|[`suggest`](commands_information/Suggestion_commands.md?id=suggest)|Adds a suggestion to the suggestion pool|
|[`respond`](commands_information/Suggestion_commands.md?id=respond)|Responds to a suggestion|

### [Animal](commands_information/Animal_commands.md)
|Name|Description|
|---|---|
|[`cat`](commands_information/Animal_commands.md?id=cat)|Displays a cat picture/GIF|
|[`dog`](commands_information/Animal_commands.md?id=dog)|Displays a dog picture/GIF|
|[`bird`](commands_information/Animal_commands.md?id=bird)|Displays a bird picture/GIF|

### [Macro](commands_information/Macro_commands.md)
|Name|Description|
|---|---|
|[`listmacros`](commands_information/Macro_commands.md?id=listmacros)|Displays all the macros on the server|
|[`addmacro`](commands_information/Macro_commands.md?id=addmacro)|Adds a macro to the server|
|[`editmacro`](commands_information/Macro_commands.md?id=editmacro)|Edits an existing macro on the server|
|[`removemacro`](commands_information/Macro_commands.md?id=removemacro)|Removes an existing macro from the server|