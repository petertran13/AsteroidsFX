module Asteroid {
    requires Common;
    requires CommonAsteroids;
    requires java.desktop;

    provides dk.sdu.mmmi.cbse.common.services.IGamePluginService with dk.sdu.mmmi.cbse.asteroid.AsteroidPlugin;
    provides dk.sdu.mmmi.cbse.common.services.IEntityProcessingService with dk.sdu.mmmi.cbse.asteroid.AsteroidControlSystem;
}
