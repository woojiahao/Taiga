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
11. [Fun](commands.md?id=Fun)

### [Administration](administration_commands.md)
|Name|Description|
|---|---|
|[`setjoin`](administration_commands.md?id=setjoin)|Sets the join channel, uses the default channel if not set|
|[`setlog`](administration_commands.md?id=setlog)|Sets the logging channel, uses the default channel if not set|
|[`setsuggestion`](administration_commands.md?id=setsuggestion)|Sets the suggestion channel, uses the default channel if not set|
|[`setup`](administration_commands.md?id=setup)|Sets up the bot for moderation like adding the muted roles and overriding channel permissions|
|[`getprefix`](administration_commands.md?id=getprefix)|Displays the current prefix set for the current server|
|[`setprefix`](administration_commands.md?id=setprefix)|Sets the current prefix for the server, prefix cannot be more than 3 characters long and cannot be composed of all letters/digits|
|[`gettimemultiplier`](administration_commands.md?id=gettimemultiplier)|Displays the current time multiplier|
|[`settimemultiplier`](administration_commands.md?id=settimemultiplier)|Sets the time multiplier, multipliers include: S, M, H, D|
|[`getpreferences`](administration_commands.md?id=getpreferences)|Displays all the properties set for the server|
|[`addinvite`](administration_commands.md?id=addinvite)|Adds an invite to the whitelist, only the owner can add invites|
|[`removeinvite`](administration_commands.md?id=removeinvite)|Removes an invite from the whitelist, only the owner can remove invites|
|[`whitelist`](administration_commands.md?id=whitelist)|Displays all the whitelisted invites for the server|

### [Welcome](welcome_commands.md)
|Name|Description|
|---|---|
|[`disablewelcome`](welcome_commands.md?id=disablewelcome)|Disables bot welcomes for new members|
|[`enablewelcome`](welcome_commands.md?id=enablewelcome)|Enables bot welcomes for new members|
|[`getwelcomeenabled`](welcome_commands.md?id=getwelcomeenabled)|Displays whether bot welcomes are enabled or disabled|
|[`getwelcomemessage`](welcome_commands.md?id=getwelcomemessage)|Displays what the bot will print when a new member joins the server|
|[`setwelcomemessage`](welcome_commands.md?id=setwelcomemessage)|Sets the welcome message for new members|

### [Moderation](moderation_commands.md)
|Name|Description|
|---|---|
|[`echo`](moderation_commands.md?id=echo)|Echos a message into another channel, the message to echo cannot contain mentions|
|[`nuke`](moderation_commands.md?id=nuke)|Nukes a certain number of messages, from 0 up to 99|
|[`mute`](moderation_commands.md?id=mute)|Mutes a user for a specified period of time, defaulted to minutes|
|[`history`](moderation_commands.md?id=history)|Displays the history of a specific member, if no arguments are given, the invoker's history is retrieved|
|[`strike`](moderation_commands.md?id=strike)|Infracts a user|
|[`warn`](moderation_commands.md?id=warn)|Infracts a user with a 0 weight strike|
|[`wiperecord`](moderation_commands.md?id=wiperecord)|Wipes the user's record from the database|
|[`ban`](moderation_commands.md?id=ban)|Bans a user/list of users (up to 30) from the server at once|
|[`unban`](moderation_commands.md?id=unban)|Unbans a user and leaves a mark on their history|
|[`gag`](moderation_commands.md?id=gag)|Temporarily mutes a user for 5 minutes to allow moderators to handle an ongoing situation|
|[`clearstrike`](moderation_commands.md?id=clearstrike)|Removes a strike from a user|

### [Permission](permission_commands.md)
|Name|Description|
|---|---|
|[`setpermission`](permission_commands.md?id=setpermission)|Sets the permission of a command to be available to a role and higher|
|[`setpermissioncategory`](permission_commands.md?id=setpermissioncategory)|Sets the permission of all commands in a category to be available to the same role and higher|
|[`viewpermissions`](permission_commands.md?id=viewpermissions)|Views all the permissions set in the server|
|[`setglobal`](permission_commands.md?id=setglobal)|Sets a single command or category of commands to be available to everyone|

### [Utility](utility_commands.md)
|Name|Description|
|---|---|
|[`help`](utility_commands.md?id=help)|Displays a help card for the user to learn more about a specific command or category, pass no arguments and it will display all commands available to you|
|[`invite`](utility_commands.md?id=invite)|Displays all invite links related to me such as my development server and to invite me to your server|
|[`ping`](utility_commands.md?id=ping)|Displays the ping for me|
|[`serverinfo`](utility_commands.md?id=serverinfo)|Displays information about the server|
|[`source`](utility_commands.md?id=source)|Displays all links related to my source code|
|[`botinfo`](utility_commands.md?id=botinfo)|Displays information about the bot|
|[`changelog`](utility_commands.md?id=changelog)|Displays the latest changelog if no argument is given, displays a specific changelog if one can be found|
|[`google`](utility_commands.md?id=google)|Performs and displays a google search of a query|

### [Role](role_commands.md)
|Name|Description|
|---|---|
|[`assign`](role_commands.md?id=assign)|Assigns a role to the specified user, the role must not be higher than my role|
|[`getjoinrole`](role_commands.md?id=getjoinrole)|Displays the join role that will be assigned to new members on join in the server|
|[`roles`](role_commands.md?id=roles)|Displays all roles and their IDs in the server|
|[`setjoinrole`](role_commands.md?id=setjoinrole)|Sets the join role that will be assigned to new members on join in the server|
|[`clearjoinrole`](role_commands.md?id=clearjoinrole)|Clears the join role|
|[`unassign`](role_commands.md?id=unassign)|Removes a role from the user, the role must not be higher than my role|

### [Raid](raid_commands.md)
|Name|Description|
|---|---|
|[`setraidmessageduration`](raid_commands.md?id=setraidmessageduration)|Sets how long the user messages are tracked in seconds for raid control|
|[`getraidmessagelimit`](raid_commands.md?id=getraidmessagelimit)|Displays how many user messages are tracked for raid control|
|[`setraidmessagelimit`](raid_commands.md?id=setraidmessagelimit)|Sets the number of user messages are tracked for raid control|
|[`getraidroleexcluded`](raid_commands.md?id=getraidroleexcluded)|Displays what roles are being excluded from raid filter|
|[`setraidroleexcluded`](raid_commands.md?id=setraidroleexcluded)|Sets the role that is being excluded from the raid filter, any role higher than this is also automatically excluded|
|[`getraidmessageduration`](raid_commands.md?id=getraidmessageduration)|Displays how long user messages are tracked in seconds for raid control|
|[`viewraiders`](raid_commands.md?id=viewraiders)|Displays all the raiders in the server|
|[`freeraider`](raid_commands.md?id=freeraider)|Removes a user from the raid list and unmutes them|
|[`freeallraiders`](raid_commands.md?id=freeallraiders)|Removes all users from the raid list and unmutes them|

### [Suggestion](suggestion_commands.md)
|Name|Description|
|---|---|
|[`poolinfo`](suggestion_commands.md?id=poolinfo)|Displays information about the suggestion pool|
|[`pooltop`](suggestion_commands.md?id=pooltop)|Displays the latest suggestion in the pool|
|[`poolaccept`](suggestion_commands.md?id=poolaccept)|Accepts the latest suggestion in the pool|
|[`pooldeny`](suggestion_commands.md?id=pooldeny)|Denies the latest suggestion in the pool|
|[`suggest`](suggestion_commands.md?id=suggest)|Adds a suggestion to the suggestion pool|
|[`respond`](suggestion_commands.md?id=respond)|Responds to a suggestion|

### [Animal](animal_commands.md)
|Name|Description|
|---|---|
|[`cat`](animal_commands.md?id=cat)|Displays a cat picture/GIF|
|[`dog`](animal_commands.md?id=dog)|Displays a dog picture/GIF|
|[`bird`](animal_commands.md?id=bird)|Displays a bird picture/GIF|

### [Macro](macro_commands.md)
|Name|Description|
|---|---|
|[`listmacros`](macro_commands.md?id=listmacros)|Displays all the macros on the server|
|[`addmacro`](macro_commands.md?id=addmacro)|Adds a macro to the server|
|[`editmacro`](macro_commands.md?id=editmacro)|Edits an existing macro on the server|
|[`removemacro`](macro_commands.md?id=removemacro)|Removes an existing macro from the server|

### [Fun](fun_commands.md)
|Name|Description|
|---|---|
|[`clapify`](fun_commands.md?id=clapify)|Clapifies a sentence|
|[`flip`](fun_commands.md?id=flip)|Flips a coin|
|[`cookie`](fun_commands.md?id=cookie)|Give someone a cookie, if you don't pass any arguments, a cookie is given to a random stranger|
|[`meme`](fun_commands.md?id=meme)|Displays a random popular meme from Imgflip|
|[`joke`](fun_commands.md?id=joke)|Tells a joke|
|[`emote`](fun_commands.md?id=emote)|Displays an emote based on the name if it can be found in any of the servers Taiga is in|
