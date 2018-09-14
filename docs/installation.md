# Installation Guide
Taiga can be hosted on both a local machine and over a Heroku instance.
## Prerequisites
**Either:**
* [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven](https://maven.apache.org/) 
* [Git](https://git-scm.com/downloads)
* [PostgreSQL](https://www.postgresql.org/docs/9.3/static/tutorial-install.html)

**Heroku:**
* [Heroku](https://devcenter.heroku.com/articles/heroku-cli)

## Local
### Steps: {docsify-ignore}
1. Clone the repository

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
5. Edit the `config/config.json` file and include your `bot token`, `database url` and `prefix`

	```bash
	$ vim config/config.json
	```
6. Re-run the bot

	```bash
	$ java -jar target/Taiga-1.0-SNAPSHOT-jar-with-dependencies.jar
	```

## Heroku
### Steps: {docsify-ignore}
1. Clone this repository

	```bash
	$ git clone https://github.com/woojiahao/Taiga.git
	```
2. Navigate into the project folder

	```bash
	$ cd Taiga
	```
3. Create a Heroku instance

	```bash
	$ heroku create
	```
4. Add the [Heroku Postgres](https://devcenter.heroku.com/articles/heroku-postgresql) add-on to the Heroku instance

	```bash
	$ heroku addons:create heroku-postgresql:hobby-dev
	```
5. Add [environment variables](https://devcenter.heroku.com/articles/config-vars) for the `BOT_TOKEN` and `PREFIX`

	```bash
	$ heroku config:set BOT_TOKEN=<insert your bot token>
	$ heroku config:set PREFIX=<insert your prefix>
	```
6. Push the repository to Heroku

	```bash
	$ git push heroku master
	```
7. Enable the [worker dyno](https://www.heroku.com/dynos)

	```bash
	$ heroku ps:scale worker=1
	```
