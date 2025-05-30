package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class EnemyControlSystem implements IEntityProcessingService {

    private static final int ROTATION_SPEED = 2;
    private static final int SHOOT_PROBABILITY = 1;
    private static final double MOVE_SPEED = 1.0;

    private final Random random = new Random();

    @Override
    public void process(GameData gameData, World world) {
        // Keep at least 2 enemies alive
        int enemyCount = world.getEntities(Enemy.class).size();
        while (enemyCount < 2) {
            Entity newEnemy = createEnemy(gameData);
            world.addEntity(newEnemy);
            enemyCount++;
        }

        for (Entity enemy : world.getEntities(Enemy.class)) {
            moveRandomly(enemy);
            rotateRandomly(enemy);
            shootRandomly(enemy, gameData, world);
            enemyOnScreen(enemy, gameData);
        }
    }

    private Entity createEnemy(GameData gameData) {
        Enemy enemy = new Enemy();

        double x = random.nextDouble() * gameData.getDisplayWidth();
        double y = random.nextDouble() * gameData.getDisplayHeight();

        enemy.setPolygonCoordinates(
                0, -10,
                10, 10,
                -10, 10
        );

        enemy.setColor("ORANGERED");
        enemy.setX(x);
        enemy.setY(y);
        enemy.setRadius(8);
        enemy.setRotation(random.nextDouble() * 360);

        return enemy;
    }

    private void moveRandomly(Entity enemy) {
        double angle = Math.toRadians(enemy.getRotation());
        double dx = Math.cos(angle) * MOVE_SPEED;
        double dy = Math.sin(angle) * MOVE_SPEED;

        enemy.setX(enemy.getX() + dx);
        enemy.setY(enemy.getY() + dy);
    }

    private void rotateRandomly(Entity enemy) {
        if (random.nextBoolean()) {
            enemy.setRotation(enemy.getRotation() + ROTATION_SPEED);
        } else {
            enemy.setRotation(enemy.getRotation() - ROTATION_SPEED);
        }
    }

    private void shootRandomly(Entity enemy, GameData gameData, World world) {
        if (random.nextInt(100) < SHOOT_PROBABILITY) {
            getBulletServices().stream()
                    .findFirst()
                    .ifPresent(spi -> world.addEntity(spi.createBullet(enemy, gameData)));
        }
    }

    private void enemyOnScreen(Entity enemy, GameData gameData) {
        boolean bounced = false;

        if (enemy.getX() < 0) {
            enemy.setX(1);
            bounced = true;
        } else if (enemy.getX() > gameData.getDisplayWidth()) {
            enemy.setX(gameData.getDisplayWidth() - 1);
            bounced = true;
        }

        if (enemy.getY() < 0) {
            enemy.setY(1);
            bounced = true;
        } else if (enemy.getY() > gameData.getDisplayHeight()) {
            enemy.setY(gameData.getDisplayHeight() - 1);
            bounced = true;
        }

        if (bounced) {
            enemy.setRotation(random.nextInt(360));
        }
    }

    private Collection<? extends BulletSPI> getBulletServices() {
        return ServiceLoader.load(BulletSPI.class).stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());
    }
}
