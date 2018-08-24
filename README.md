# Taiga
Discord administration bot made with Kotlin and JDA based around Taiga Aisaka

## Setting up
Taiga can be run as a local instance and through Heroku

### Local instance
#### Pre-requisites:
1. [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
2. [Maven](https://maven.apache.org/) 
3. [Git](https://git-scm.com/downloads)
4. [Heroku](https://devcenter.heroku.com/articles/heroku-cli)
5. Database of your choosing

#### Steps:
1. Clone this repository
	```bash
	$ git clone https://github.com/woojiahao/Taiga.git
	```
2. Navigate into the project folder
	```bash
	$ cd Taiga
	```
3. Create a `.jar` for the bot
	```bash
	$ mvn clean install
	```
4. Run the bot once to create the `config.json` file
	```bash
	$ java -jar target/Taiga-1.0-SNAPSHOT-jar-with-dependencies.jar
	```
5. Edit the `config/config.json` file and include the `bot token`, `database url` and `prefix`
	```bash
	$ vim config/config.json
	```
6. Re-run the bot
	```bash
	$ java -jar target/Taiga-1.0-SNAPSHOT-jar-with-dependencies.jar
	```

