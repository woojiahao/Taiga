# Suggestions System 
Taiga supports the use of suggestions in order to allow members of your server to provide feedback and suggestions on the
server.

## Set up
Before enabling the suggestion system, ensure that you have run `setsuggestion` in the appropriate channel that you want
the suggestions to appear in first.

## Making Suggestions
Suggestions can be made via the `suggest` command, and when a suggestion is made, it will be added to the suggestion 
pool.

> suggest { Suggestion }

## Suggestion Pool
When a user suggests something, their suggestions will first be stored in the suggestion pool, which prevents spam
suggestions from getting through.

You can interact with the pool via the following commands

* **poolinfo** - View how many suggestions are in the pool
* **pooltop** - Displays the latest suggestion in the pool
* **poolaccept** - Accepts the latest suggestion in the pool
* **pooldeny** - Denies the latest suggestion in the pool

### Accepting Suggestions {docsify-ignore}
When a suggestion is accepted from the pool, it is posted up onto the suggestion channel and it will be open for community
voting.

## Suggestion Response
If you wish to interact with the suggestions and respond to them, you can do so using the `respond` command.

> respond { Suggestion Message ID } { Accepted/Declined } { Reason }
