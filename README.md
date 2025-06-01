
To start the AsteroidFX game and ensure that the point system is active during gameplay,
you first need to run Maven to download and build all dependencies:

mvn clean install

After that, run the PointSystem class to activate the point tracking feature.

Now you can start the game by running:

mvn exec:exec

To see your score on the localthost type:

http://localhost:8080/score?point=0
