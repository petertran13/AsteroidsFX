package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

public class Player extends Entity {

    private int health = 10;

    public int getHealth() {
        return health;
    }

    public void decreaseHealth(int amount) {
        health -= amount;
    }

    @Override
    public void handleCollision(GameData gameData, World world, Entity other) {
        String otherType = other.getType();

        if ("ENEMY".equals(otherType)) {
            // Remove both player and enemy immediately
            world.removeEntity(this);
            world.removeEntity(other);
        }

        if ("ASTEROID".equals(otherType)) {
            decreaseHealth(1);
            if (health <= 0) {
                world.removeEntity(this);
            }
        }

        if ("ENEMY_BULLET".equals(otherType)) {
            decreaseHealth(1);
            world.removeEntity(other);
            if (health <= 0) {
                world.removeEntity(this);
            }
        }
    }
}