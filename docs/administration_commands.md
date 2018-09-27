# Administration Commands
## setchannel
### Description {docsify-ignore}
Sets the logging, welcome or suggestion channel
### Syntax {docsify-ignore}

> setchannel { Target Type For Channel (Logging/Welcome/Suggestion) }

### Example {docsify-ignore}

> setchannel logging

## setup
### Description {docsify-ignore}
Sets up the bot for moderation like adding the muted roles and overriding channel permissions
### Syntax {docsify-ignore}

> setup

### Example {docsify-ignore}

> setup

## preferences
### Description {docsify-ignore}
Displays all the properties set for the server
### Syntax {docsify-ignore}

> getpreferences

### Example {docsify-ignore}

> getpreferences

## set
### Description {docsify-ignore}
Sets the value for a preference
### Syntax {docsify-ignore}

> set { Preference (prefix/multiplier/logging/join/suggestion/messagelimit/messageduration/raidexcluded/welcomeenabled/welcomemessage/joinrole) } { Value }

### Example {docsify-ignore}

> set messagelimit 5

## get
### Description {docsify-ignore}
Displays the details of a specific preference set
### Syntax {docsify-ignore}

> get { Preference (prefix/multiplier/logging/join/suggestion/messagelimit/messageduration/raidexcluded/welcomeenabled/welcomemessage/joinrole) }

### Example {docsify-ignore}

> get prefix

## disable
### Description {docsify-ignore}
Disables a specific logging type, welcome/logging/suggestion
### Syntax {docsify-ignore}

> disable { Logging Type (Welcome/Logging/Suggestion) }

### Example {docsify-ignore}

> disable logging

## enable
### Description {docsify-ignore}
Enables a specific logging type, welcome/logging/suggestion
### Syntax {docsify-ignore}

> enable { Logging Type (Welcome/Logging/Suggestion) }

### Example {docsify-ignore}

> enable logging

