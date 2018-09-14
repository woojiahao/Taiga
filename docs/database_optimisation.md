# Database Optimisation
Taiga was built with the 10,000 row limit that Heroku Postgres imposes on Hobby dev accounts. As such, a degree of performance is sacrificed in order to ensure that this row limit is not quickly exceeded.

If you are wishing to host your own instance of Taiga with no restrictions on the number of rows that you can keep and you wish to improve the performance of Taiga, perform the following changes to reduce the loading time of Taiga.

## Optimising Storage Of Permissions
Permissions are only recorded when the command is available to any role other than the highest role in the server.

This results in a huge performance penalty when using the `viewpermissions` command. 

If you have additional rows to spare in your own database, perform the following replacements to the following files:

* PermissionOperations.kt
	
	Rather than only logging the 
	
* OnJoinEvent.kt
	
	

* OnLeaveEvent.kt
* PermissionCommands.kt
