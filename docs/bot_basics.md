# Bot Basics
## Prefix
> The default prefix to invoke Taiga is `?`.

This can be customized for each server, by running the `setprefix` command.

## Set Up
### Permissions
Move Taiga's bot role `Taiga` above the roles you wish to be able to manage via Taiga.

> If Taiga does not have the right permission to use a specific command, you will be prompted in the channel where that
command was invoked, add the permission to Taiga and the command will work the next time it is used.

### Muted Role
The `muted` role is used in order to moderate and control raiders. As such, it is important for it to be set up and 
create channel overrides. You can learn more about Taiga's [moderation system](moderation_system.md) and [raid 
control.](raid_control.md)

> When set up, the `muted` role will prevent the user from speaking in any channel where the channel override is present.

**Note:** Before proceeding, ensure that you don't have another role called `muted` (casing does not matter), if you do
have one, be sure to remove it.

Run the `?setup` command to allow Taiga to create a `muted` role as well as include channel overrides for that role. 

After the role has been added, move it above the roles you wish to be able to mute via Taiga.

### Customizing Preferences
Taiga offers a wide array of preferences that you can tweak, a detailed guide on that can be found [here.](setting_preferences.md)

## Command Use
> All commands by default are only available to only the Owner and the people who hold the highest role in the server.

You can configure which commands are available to users by using the `setpermission` and `setpermissioncategory` commands