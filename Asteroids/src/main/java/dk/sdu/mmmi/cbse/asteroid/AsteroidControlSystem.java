package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Random;

public class AsteroidControlSystem implements IEntityProcessingService {

    private static final double MOVE_SPEED = 1.0;
    private static final int MIN_ASTEROIDS = 7;
    private final Random random = new Random();

    @Override
    public void process(GameData gameData, World world) {
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            moveRandomly(asteroid, gameData);
        }

        int currentCount = world.getEntities(Asteroid.class).size();

        for (int i = currentCount; i < MIN_ASTEROIDS; i++) {
            Entity newAsteroid = createAsteroid(gameData, random, 3);
            world.addEntity(newAsteroid);
        }
    }

    private void moveRandomly(Entity asteroid, GameData gameData) {
        double angle = Math.toRadians(asteroid.getRotation());
        double dx = Math.cos(angle) * MOVE_SPEED;
        double dy = Math.sin(angle) * MOVE_SPEED;

        asteroid.setX(asteroid.getX() + dx);
        asteroid.setY(asteroid.getY() + dy);

        if (asteroid.getX() < 0) asteroid.setX(gameData.getDisplayWidth());
        else if (asteroid.getX() > gameData.getDisplayWidth()) asteroid.setX(0);

        if (asteroid.getY() < 0) asteroid.setY(gameData.getDisplayHeight());
        else if (asteroid.getY() > gameData.getDisplayHeight()) asteroid.setY(0);
    }

    private Entity createAsteroid(GameData gameData, Random random, int size) {
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
