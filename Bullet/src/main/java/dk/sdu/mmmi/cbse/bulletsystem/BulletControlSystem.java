package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {

    private static final double BULLET_SPEED = 4.0;
    private static final double SPAWN_DISTANCE = 10.0;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(Bullet.class)) {
            moveBullet(entity);
        }
    }

    private void moveBullet(Entity bullet) {
        double angleRadians = Math.toRadians(bullet.getRotation());
        double deltaX = Math.cos(angleRadians) * BULLET_SPEED;
        double deltaY = Math.sin(angleRadians) * BULLET_SPEED;

        bullet.setX(bullet.getX() + deltaX);
        bullet.setY(bullet.getY() + deltaY);
    }

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Bullet bullet = new Bullet();
        bullet.setPolygonCoordinates(-3, -3, 3, -3, 3, -1, 1, -1, 1, 1, 3, 1, 3, 3, -3, 3);

        double angleRadians = Math.toRadians(shooter.getRotation());
        double offsetX = Math.cos(angleRadians) * SPAWN_DISTANCE;
        double offsetY = Math.sin(angleRadians) * SPAWN_DISTANCE;

        bullet.setX(shooter.getX() + offsetX);
        bullet.setY(shooter.getY() + offsetY);
        bullet.setRotation(shooter.getRotation());
        bullet.setRadius(2.0f);
        bullet.setColor("RED");

        if (shooter.getClass().getSimpleName().equals("Player")) {
            bullet.setType("PLAYER_BULLET");
        } else {
            bullet.setType("ENEMY_BULLET");
        }

        return bullet;
    }
}
