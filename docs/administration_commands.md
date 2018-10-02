# Administration Commands
## setchannel
### Description {docsify-ignore}
Sets the channel to a specific channel type
### Syntax {docsify-ignore}

> setchannel { Channel Type }

### Potential Arguments {docsify-ignore}
#### Channel Type
logging, join, suggestion, useractivity

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

> set { Preference } { Value }

### Potential Arguments {docsify-ignore}
#### Preference
prefix, multiplier, logging, join, suggestion, useractivity, messagelimit, messageduration, raidexcluded, welcomemessage, joinrole, inviteexcluded

### Example {docsify-ignore}

> set messagelimit 5

## get
### Description {docsify-ignore}
Displays the details of a specific preference set
### Syntax {docsify-ignore}

> get { Preference }

### Potential Arguments {docsify-ignore}
#### Preference
prefix, multiplier, logging, join, suggestion, useractivity, messagelimit, messageduration, raidexcluded, welcomemessage, joinrole, inviteexcluded

### Example {docsify-ignore}

> get prefix

## disable
### Description {docsify-ignore}
Disables a specific channel type
### Syntax {docsify-ignore}

> disable { Channel Type }

### Potential Arguments {docsify-ignore}
#### Channel Type
logging, join, suggestion, useractivity

### Example {docsify-ignore}

> disable logging

## enable
### Description {docsify-ignore}
Enables a specific channel type
### Syntax {docsify-ignore}

> enable { Channel Type }

### Potential Arguments {docsify-ignore}
#### Channel Type
logging, join, suggestion, useractivity

### Example {docsify-ignore}

> enable logging

