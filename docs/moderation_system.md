# Moderation System
> 3 strikes and you're out!

Taiga's moderation system operates on the basis of allowing moderators of a server to keep track of a user's behaviour
through the use of strikes and to have a consistent system in moderating the system to ensure a fair and balanced 
chances are given to each and every user.

## Strikes
User infractions are handled via strikes, which can hold weights from 0 to 3. A user can be infracted using the 
`strike` command. 

Each strike is recorded under the user's history and can be viewed using the `history` command.

### Strike Expiration
Strikes expire after a month of their issue and will no longer count to the user's accumulated strike count when further 
strikes are issued. But their history will still retain for future moderation action consideration.

### Strike Accumulation
Strikes accumulate every time the user is infracted. Only strikes that have expired will not be counted during a new 
infraction. 

**For instance, a user already has a non-expired strike weight of 2:** 

* If they receive warn, they will receive the punishment of a strike 2 (day long mute) as their accumulated strike weight 
	is 2. 

* If they receive a strike of 1, they will have an accumulated strike weight of 3 and be banned.

### Strike Weights
#### Weight 0 / Warns
##### Use:
Strikes of 0 often serve as means to track a user's behavior.

##### Punishment:
Warns do not hold any punishment.

##### Command:
> warn { User ID/Mention } { Reason }

> strike { User ID/Mention } 0 { Reason }

#### Weight 1
##### Use:
Strikes of 1 often serve as a stern warning for the user and to infract for more serious offenses.

##### Punishment:
Users with an accumulated strike count of 1 will receive **an hour long mute.**

##### Command:
> strike { User ID/Mention } 1 { Reason }

#### Weight 2
##### Use:
Strikes of 2 often serve as a final warning for the user and to allow for any future infractions to pile on and effect a
potential re-mute or ban.

##### Punishment:
Users with an accumulated strike count of 2 will receive **an day long mute.**

##### Command:
> strike { User ID/Mention } 2 { Reason }

#### Weight 3
##### Use:
Strikes of 3 often serve as the last straw for the user.

##### Punishment:
Users with an accumulated strike count of 3 will receive **a permanent ban.**

##### Command:
> strike { User ID/Mention } 3 { Reason }

> ban { User ID/Mention } { Reason }