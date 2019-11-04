# Memorize the Unlock Pattern (Android Game)
@author Xuewei Wang  

The CSE298 (Mobile App) final project. An Andorid Game designed to test the users' graphic memory ability. 

## Game Design
### Overall
A game contains multiple round of unlock pattern memorizing in a timed period. At each round, an unlock pattern (an ordered series of dots in a square dot matrix) is shown and quickly disappears. Then the user is prompt to recreate the pattern.
### Difficulty
The game has three difficulties: easy, medium, and hard. Dot matrix size and the complexity of patterns increase when difficulty increases.
### Scoring & Multiplayer
All scores and patterns of games will be stored at a database running separately on a remote server. The highest score of a game will be displayed at the main activity. Users could "challenge" a record by selecting it, and the same series of pattern will be served in the game.

## Implementation
A Django backend is used for setting up backend routes (HTTP REST) and managing the SQLite database. Android uses the Volley library for HTTP connection.

## Test and Deployment
This app is only tested in localhost and has never been deployed at an actual server (Due to lack of time and money). Try the following steps for a deployment:

* Make sure you have the latest Django package. If not: ___conda install django___. In comand line, run ___python backend/manage.py migrate___ to create all necessary tables. Then, ___python backend/manage.py runserver [address: port]___ (default to 127.0.0.1:8000)
* In the android file: ___androidunlock/app/src/main/java/xuw220/cse298/lehigh/edu/androidunlock/RESTfulActivity.java___, their is a static field called "BASE_URL". Change it accrodingly. If you run the server at localhost and intend to run the app in an emulator, then no need to change (for emulators, http://10.0.2.2 is mapped to the host machine's localhost).
* Now you are good to go. But if you want to manage the database, then in comand line, run ___python backend/manage.py createsuperuser___ and create your own username and password. Use this credential to login to [URL sever run on]/admin/.

**Note: game records will not shown if the server is not running or is not properly connected.**
