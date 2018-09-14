# Moderation Commands
## echo
### Description {docsify-ignore}
Echos a message into another channel, the message to echo cannot contain mentions
### Syntax {docsify-ignore}

> echo { Channel Mention/ID } { Message }

### Example {docsify-ignore}

> echo #testing Hi, this is a test!

## nuke
### Description {docsify-ignore}
Nukes a certain number of messages, from 0 up to 99
### Syntax {docsify-ignore}

> nuke { Number to nuke, 0-99 }

### Example {docsify-ignore}

> nuke 10

## mute
### Description {docsify-ignore}
Mutes a user for a specified period of time, defaulted to minutes
### Syntax {docsify-ignore}

> mute { User Mention/ID } { Duration } { Reason }

### Example {docsify-ignore}

> mute @Chill 15 Stop

## history
### Description {docsify-ignore}
Displays the history of a specific member
### Syntax {docsify-ignore}

> history { User ID/Mention }

### Example {docsify-ignore}

> history @Chill

## strike
### Description {docsify-ignore}
Infracts a user
### Syntax {docsify-ignore}

> strike { User ID/Mention } { Strike weight (0-3) } { Strike reason }

### Example {docsify-ignore}

> strike @Chill 1 Big dum

## warn
### Description {docsify-ignore}
Infracts a user with a 0 weight strike
### Syntax {docsify-ignore}

> strike { User ID/Mention } { Strike reason }

### Example {docsify-ignore}

> warn @Chill Stop this nonsense

## wiperecord
### Description {docsify-ignore}
Wipes the user's record from the database
### Syntax {docsify-ignore}

> wiperecord { User ID/Mention }

### Example {docsify-ignore}

> wiperecord @Chill
