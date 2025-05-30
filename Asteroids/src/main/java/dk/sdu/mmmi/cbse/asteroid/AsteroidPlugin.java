package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

import java.util.Random;

public class AsteroidPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            Entity asteroid = createAsteroid(gameData, random);
            world.addEntity(asteroid);
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            world.removeEntity(asteroid);
        }
    }

    private Entity createAsteroid(GameData gameData, Random random) {
        int size = random.nextInt(3) + 1;
        Asteroid asteroid = new Asteroid(size);

        double scale = size / 3.0;

        asteroid.setPolygonCoordinates(getBaseShapeScaled(scale));
        asteroid.setX(random.nextInt(gameData.getDisplayWidth()));
        asteroid.setY(random.nextInt(gameData.getDisplayHeight()));
        asteroid.setRadius((float)(60 * scale));
        asteroid.setRotation(random.nextInt(360));
        asteroid.setColor("GRAY");

        return asteroid;
    }

    private double[] getBaseShapeScaled(double scale) {
        double[] baseShape = new double[]{
                20, 0, 16, 10, 8, 16, -4, 14, -12, 10,
                -16, 0, -12, -10, -6, -14, 4, -12, 12, -8, 18, -2
        };
        double[] scaledShape = new double[baseShape.length];
        for (int i = 0; i < baseShape.length; i++) {
            scaledShape[i] = baseShape[i] * scale;
        }
        return scaledShape;
    }
}
