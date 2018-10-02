# Recent Changes
## Command Heaven
Build **4.3** was released on **02 October 2018**
### Changes
1. `RegexArg` argument type was introduced for regular expression parsing
2. `nuke` is now overloaded to take in a regular expression such that the nuke will only apply to messages that contain the regular expression
3. Fixed the global flag issue, now global flags for each command is nullable so if it stays null, it will inherit from the set, else it will keep its state

### New Commands
1. viewbotpermissions
2. pin

### Contributors
* @Chil#4048
