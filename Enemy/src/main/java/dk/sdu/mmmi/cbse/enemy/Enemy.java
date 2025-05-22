package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

public class Enemy extends Entity {

    private int health = 5;

    public int getHealth() {
        return health;
    }

    public void decreaseHealth(int amount) {
        health -= amount;
    }

    @Override
    public void handleCollision(GameData gameData, World world, Entity other) {
        String otherType = other.getType();

        if ("PLAYER".equals(otherType)) {
            // Remove both enemy and player immediately
            world.removeEntity(this);
            world.removeEntity(other);
        }

        if ("ASTEROID".equals(otherType)) {
            // Your existing logic for asteroid collisions
            decreaseHealth(1);
            if (health <= 0) {
                world.removeEntity(this);
            }
        }

        if ("PLAYER_BULLET".equals(otherType)) {
            decreaseHealth(1);
            world.removeEntity(other);
            if (health <= 0) {
                world.removeEntity(this);
            }
        }
    }
}