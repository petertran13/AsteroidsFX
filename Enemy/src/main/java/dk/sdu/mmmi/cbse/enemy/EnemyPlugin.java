package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

public class EnemyPlugin implements IGamePluginService {

    private Entity enemy1;
    private Entity enemy2;

    @Override
    public void start(GameData gameData, World world) {
        enemy1 = createEnemy(gameData, gameData.getDisplayWidth() * 0.75, gameData.getDisplayHeight() * 0.25);
        world.addEntity(enemy1);

        enemy2 = createEnemy(gameData, gameData.getDisplayWidth() * 0.25, gameData.getDisplayHeight() * 0.75);
        world.addEntity(enemy2);
    }

    private Entity createEnemy(GameData gameData, double x, double y) {
        Entity enemy = new Enemy();
        enemy.setType("ENEMY");

        enemy.setPolygonCoordinates(
                12, -1, 8, -1, 8, -3, 6, -3, 6, -5, -2, -5, -2, -7, 0, -7,
                0, -9, -10, -9, -10, -5, -8, -5, -8, -3, -6, -3, -6, -1,
                -10, -1, -10, 1, -6, 1, -6, 3, -8, 3, -8, 5, -10, 5,
                -10, 9, 0, 9, 0, 7, -2, 7, -2, 5, 2, 5, 2, 1, 4, 1,
                4, -1, 2, -1, 2, -3, 4, -3, 4, -1, 6, -1, 6, 1, 4, 1,
                4, 3, 2, 3, 2, 5, 6, 5, 6, 3, 8, 3, 8, 1, 12, 1
        );

        enemy.setColor("ORANGERED");
        enemy.setX(x);
        enemy.setY(y);
        enemy.setRadius(8);
        enemy.setRotation(Math.random() * 360);
        return enemy;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(enemy1);
        world.removeEntity(enemy2);
    }
}
