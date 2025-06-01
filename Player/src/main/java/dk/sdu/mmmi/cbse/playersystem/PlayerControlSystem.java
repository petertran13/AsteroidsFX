package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class PlayerControlSystem implements IEntityProcessingService {

    private static final double MOVE_SPEED = 1.5;
    private static final int ROTATION_SPEED = 2;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity player : world.getEntities(Player.class)) {
            initializePlayerShape(player);

            GameKeys keys = gameData.getKeys();

            rotatePlayer(player, keys);
            movePlayer(player, keys);
            handleShooting(player, keys, gameData, world);
            keepPlayerOnScreen(player, gameData);
        }
    }

    private void initializePlayerShape(Entity player) {
        player.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);
        player.setColor("GREEN");
    }

    private void rotatePlayer(Entity player, GameKeys keys) {
        if (keys.isDown(GameKeys.LEFT)) {
            player.setRotation(player.getRotation() - ROTATION_SPEED);
        }
        if (keys.isDown(GameKeys.RIGHT)) {
            player.setRotation(player.getRotation() + ROTATION_SPEED);
        }
    }

    private void movePlayer(Entity player, GameKeys keys) {
        double angle = Math.toRadians(player.getRotation());
        double dx = Math.cos(angle);
        double dy = Math.sin(angle);

        if (keys.isDown(GameKeys.UP)) {
            player.setX(player.getX() + dx * MOVE_SPEED);
            player.setY(player.getY() + dy * MOVE_SPEED);
        }

        if (keys.isDown(GameKeys.DOWN)) {
            player.setX(player.getX() - dx * MOVE_SPEED);
            player.setY(player.getY() - dy * MOVE_SPEED);
        }
    }


    private void handleShooting(Entity player, GameKeys keys, GameData gameData, World world) {
        if (keys.isPressed(GameKeys.SPACE)) {
            getBulletServices().stream()
                    .findFirst()
                    .ifPresent(spi -> world.addEntity(spi.createBullet(player, gameData)));
        }
    }

    private void keepPlayerOnScreen(Entity player, GameData gameData) {
        double screenW = gameData.getDisplayWidth();
        double screenH = gameData.getDisplayHeight();

        if (player.getX() < 0) player.setX(1);
        if (player.getX() > screenW) player.setX(screenW - 1);
        if (player.getY() < 0) player.setY(1);
        if (player.getY() > screenH) player.setY(screenH - 1);
    }

    private Collection<? extends BulletSPI> getBulletServices() {
        return ServiceLoader.load(BulletSPI.class).stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());
    }
}