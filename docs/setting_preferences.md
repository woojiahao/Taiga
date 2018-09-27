# Setting Preferences
Taiga provides users with the ability to customize how they want her to operate.

You can view an overview of the preferences that Taiga has by using the `preferences` command.

## Prefix
> Taiga's default prefix is `?`

You can customize the prefix that you want Taiga to respond to on your server. 

Changing the prefix requires the use of the `set prefix { desired prefix }` command. 

### Restrictions {docsify-ignore}
* Prefixes can only be up to 3 characters long 
* Prefixes must contain at least 1 symbol

## Channel Assignment
Taiga requires 3 channels:

1. **Logging** - where all the command invoke history will be stored, this is useful to track abusers of the bot
2. **Suggestion** - where any server related suggestions can go to 
3. **Join** - where all welcome messages are sent to

> By default, these channels will default to the default channel in the server, so it is ideal to change them before 
using Taiga.

These channels can be set using `setchannel logging`, `setchannel join` and `setchannel suggestion` in each of the 
desired channels.

Alternatively, if you wish to set a channel without being within it, you can use `set { channel type } { target channel }`.

## Enabling Channel Type Features
By default, when Taiga is added to your server, all 3 channel types and their features will be disabled. This is due to
unwanted logging and welcomes causing issues with a server.

To utilise the channel type features, you will need to enable each of them by using the `enable { channel type }` command.

This is key as without enabling them, some features of the bot might be locked out, such as the [suggestion system](suggestion.md)
as well as welcomes for the user.

## Raid Preferences
These are the preferences used for raid control. Taiga's raid control system is explained in detail [here](raid_control.md)

1. **Raid Message Limit** - number of messages that can be sent within a period of time
2. **Raid Message Duration** - how long user messages will be tracked
3. **Raid Role Excluded** - the matching role and any role higher than this will not be monitored for raid control

These can be set using the `set messagelimit { limit }`, `set messageduration { duration (in s) }` 
and `set raidexcluded { role }` commands.

## New Members On Join
Taiga allows the following to be customized about welcome messages:

* Welcome Message 
* Enable/Disable Welcome Messages
* Role Assignment On Member Join

### Setting the welcome message {docsify-ignore}
Use the command `set welcomemessage { message }` to configure the welcome message shown in the welcome embed

### Disabling/Enabling welcome messages {docsify-ignore}
Disable or welcome the welcome messages using `disable welcome` or `enable welcome`

### Setting new member default roles {docsify-ignore}
New members can be assigned a role by default by using the `set joinrole { role }` command

## Moderation
The time multiplier is used primarily for muting users as that specified the default duration of the mute. 
More information about this can be found in the 
[moderation system section](moderation_system.md?id=time-multipliers) for Taiga.

You can change the time multiplier for Taiga to use seconds (S), minutes (M), hours (H) or days (D) and it can be set 
using `set multiplier { multiplier }`

