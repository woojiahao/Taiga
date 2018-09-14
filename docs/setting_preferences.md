# Setting Preferences
Taiga provides users with the ability to customize how they want her to operate.

You can view an overview of the preferences that Taiga has by using the `getpreferences` command.

1. [Prefix](setting_preferences.md?id=prefix)
2. [Channel Assignment](setting_preferences.md?id=channel-assignment)
3. [Raid Preferences](setting_preferences.md?id=raid-preferences)
4. [New Members On Join](setting_preferences.md?id=new-members-on-join)
5. [Moderation](setting_preferences.md?id=moderation)

## Prefix
> Taiga's default prefix is `?`

You can customize the prefix that you want Taiga to respond to on your server. 

Changing the prefix requires the use of the 
[`setprefix`](https://github.com/woojiahao/Taiga/wiki/Commands-(Administration)#setprefix) command. 

### Restrictions {docsify-ignore}
Prefixes can only be up to 3 characters long and it is advisable that the prefix you choose is something members don't 
often include at the start of their messages.

## Channel Assignment
Taiga requires 3 channels:

1. **Log** - where all the command invoke history will be stored, this is useful to track abusers of the bot
2. **Suggestion** - where any server related suggestions can go to 
3. **Join** - where all welcome messages are sent to

> By default, these channels will default to the default channel in the server, so it is ideal to change them before 
using Taiga.

These channels can be set using `setlog`, `setjoin` and `setsuggestion` in each of the desired channels.

## Raid Preferences
These are the preferences used for raid control. Taiga's raid control system is explained in detail [here](raid_control.md)

1. **Raid Message Limit** - number of messages that can be sent within a period of time
2. **Raid Message Duration** - how long user messages will be tracked
3. **Raid Role Excluded** - the matching role and any role higher than this will not be monitored for raid control

These can be set using the `setraidmessagelimit`, `setmessageduration` and `setraidroleexcluded` commands.

## New Members On Join
Taiga allows the following to be customized about welcome messages:

* Welcome Message 
* Enable/Disable Welcome Messages
* Role Assignment On Member Join

### Setting the welcome message {docsify-ignore}
Use the command `setwelcomemessage` to configure the welcome message shown in the welcome embed

### Disabling/Enabling welcome messages {docsify-ignore}
Disable or welcome the welcome messages using `disablewelcome` or `enablewelcome`

### Setting new member default roles {docsify-ignore}
New members can be assigned a role by default by using the `setjoinrole` command

## Moderation
The time multiplier is used primarily for muting users as that specified the default duration of the mute. 
More information about this can be found in the 
[moderation system section](moderation_system.md) for Taiga.

You can change the time multiplier for Taiga to use seconds (S), minutes (M), hours (H) or days (D) and it can be set 
using `settimemultiplier`

