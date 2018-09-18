# Writing Changelogs
Changelogs are used to log the changes per each build version of Taiga. 

If you have forked and hosted your own version of Taiga, you are advised to use changelogs to show your progress with the 
bot.

## Latest Changelog
You can view the latest changelogs of your bot using the `changelog` command.

## Creating A Changelog
### Changelog File
All changelogs are stored in the `changelogs` folder in the project root and follow the following naming convention:

> changelog_<latest_number>.json

Taiga will automatically search for the latest changelog based on the prior naming convention and retrieve that changelog's 
information.

### Changelog Content
All changelogs follow a specific format for the JSON so as to retrieve components of it to be displayed.

```json
{
	"buildNumber": "",
	"buildTitle": "",
	"changes": [],
	"releaseDate": "",
	"contributors": []
}
```

#### buildNumber
Build version of your latest changes.

#### buildTitle
Name of the latest build version or title of the set of changes.

#### changes
Array of strings for each change. 

Taiga automatically joins each and every change in this array to form the changelog content displayed.

#### releaseDate
Date of which the changes were released.

#### contributors
Array of strings for each contributor for that set of changes.

Taiga automatically joins each and every contributor into a list to be displayed.

### Sample Changelog
```json
{
	"buildNumber": "2.3",
	"buildTitle": "Documentation of argument types and minor bug fix",
	"changes": [
		"Update Prefix argument type such that the prefix cannot be all letters/digits",
		"Added `clearstrike` command",
		"On Join role assignments are now silent",
		"Updated documentation for the new `Prefix`, `ArgumentList` and `StrikeId` argument types",
		"Major update to `changelog` command such that it reads from a JSON file which extracts more useful information"
	],
	"releaseDate": "18 September 2018",
	"contributors": [
		"@Chill#4048"
	]
}
```
