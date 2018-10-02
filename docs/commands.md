# Commands Overview
1. [Administration](commands.md?id=Administration)
2. [Moderation](commands.md?id=Moderation)
3. [Permission](commands.md?id=Permission)
4. [Utility](commands.md?id=Utility)
5. [Role](commands.md?id=Role)
6. [Raid](commands.md?id=Raid)
7. [Suggestion](commands.md?id=Suggestion)
8. [Macro](commands.md?id=Macro)
9. [Fun](commands.md?id=Fun)
10. [Invite](commands.md?id=Invite)

### [Administration](administration_commands.md)
|Name|Description|
|---|---|
|[`setchannel`](administration_commands.md?id=setchannel)|Sets the channel to a specific channel type|
|[`setup`](administration_commands.md?id=setup)|Sets up the bot for moderation like adding the muted roles and overriding channel permissions|
|[`preferences`](administration_commands.md?id=preferences)|Displays all the properties set for the server|
|[`set`](administration_commands.md?id=set)|Sets the value for a preference|
|[`get`](administration_commands.md?id=get)|Displays the details of a specific preference set|
|[`disable`](administration_commands.md?id=disable)|Disables a specific channel type|
|[`enable`](administration_commands.md?id=enable)|Enables a specific channel type|

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
|[`setpermission`](permission_commands.md?id=setpermission)|Sets the permission of a command/category to be available to a role and higher|
|[`viewpermissions`](permission_commands.md?id=viewpermissions)|Views all the permissions set in the server|
|[`setglobal`](permission_commands.md?id=setglobal)|Sets a single command or category of commands to be available to everyone|

### [Utility](utility_commands.md)
|Name|Description|
|---|---|
|[`help`](utility_commands.md?id=help)|Displays a help card for the user to learn more about a specific command or category, pass no arguments and it will display all commands available to you|
|[`invite`](utility_commands.md?id=invite)|Displays all invite links related to me such as my development server and to invite me to your server|
|[`source`](utility_commands.md?id=source)|Displays all links related to my source code|
|[`ping`](utility_commands.md?id=ping)|Displays the ping for me|
|[`serverinfo`](utility_commands.md?id=serverinfo)|Displays information about the server|
|[`botinfo`](utility_commands.md?id=botinfo)|Displays information about the bot|
|[`changelog`](utility_commands.md?id=changelog)|Displays the latest changelog if no argument is given, displays a specific changelog if one can be found|
|[`google`](utility_commands.md?id=google)|Performs and displays a google search of a query|

### [Role](role_commands.md)
|Name|Description|
|---|---|
|[`assign`](role_commands.md?id=assign)|Assigns a role to the specified user, the role must not be higher than my role|
|[`roles`](role_commands.md?id=roles)|Displays all roles and their IDs in the server|
|[`clearjoinrole`](role_commands.md?id=clearjoinrole)|Clears the join role|
|[`unassign`](role_commands.md?id=unassign)|Removes a role from the user, the role must not be higher than my role|

### [Raid](raid_commands.md)
|Name|Description|
|---|---|
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
|[`cat`](fun_commands.md?id=cat)|Displays a cat picture/GIF|
|[`dog`](fun_commands.md?id=dog)|Displays a dog picture/GIF|
|[`bird`](fun_commands.md?id=bird)|Displays a bird picture/GIF|
|[`anime`](fun_commands.md?id=anime)|Searches for animes based on a search term|

### [Invite](invite_commands.md)
|Name|Description|
|---|---|
|[`addinvite`](invite_commands.md?id=addinvite)|Adds an invite to the whitelist, only the owner can add invites|
|[`removeinvite`](invite_commands.md?id=removeinvite)|Removes an invite from the whitelist, only the owner can remove invites|
|[`whitelist`](invite_commands.md?id=whitelist)|Displays all the whitelisted invites for the server|
