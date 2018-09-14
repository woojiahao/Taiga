# Raid Control
Taiga controls raid by managing 2 key components, a user's message speed (message limit) and how frequently they send 
messages (message duration)

## Message limit 
Taiga stores a preference for `RaidMessageLimit` and this tracks the number of messages that the user has sent within a 
specified period of time (message duration).

If the user exceeds the expected limit of messages, they will be caught as a raider and muted.

## Message duration
Taiga stores another preference that works with the message duration called `RaidMessageDuration` and this is a timer 
set for every user on their first new message that will work with the message limit to see how quickly they sent the 
messages. If they send the messages way too quickly, they will be caught as a raider and muted.

## Role exclusions
You might only want to catch raiders who are members with no roles or under a specific role and Taiga can manage that 
by using the `RaidRoleExclusion` preference and this will ensure that members with a role equal to or higher than the 
specified role is excluded from Taiga's watchful eye and will not be tracked for raid control.

## Managing raiders
After Taiga catches raiders, you can view all of them using the `viewraiders` command and then if you wish to free a 
raider, use `freeraider` and `freeallraiders` to free them all.