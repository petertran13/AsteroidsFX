package dk.sdu.mmmi.cbse.common.asteroids;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.main.ScoreClient;

public class Asteroid extends Entity {

    private int size = 3;

    public Asteroid() {
        setType("ASTEROID");
    }

    public Asteroid(int size) {
        this.size = size;
        setType("ASTEROID");
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void handleCollision(GameData gameData, World world, Entity other) {
        String otherType = other.getType();

        if ("PLAYER_BULLET".equals(otherType)) {
            world.removeEntity(other);

            if (size > 1) {
                for (int i = 0; i < 2; i++) {
                    Asteroid smaller = new Asteroid(size - 1);
                    smaller.setX(this.getX() + (i == 0 ? -15 : 15));
                    smaller.setY(this.getY() + (i == 0 ? -15 : 15));
                    smaller.setRadius(this.getRadius() / 2);
                    smaller.setRotation(Math.random() * 360);
                    smaller.setColor("GRAY");

                    double[] oldShape = this.getPolygonCoordinates();
                    if (oldShape != null) {
                        double[] newShape = new double[oldShape.length];
                        for (int j = 0; j < oldShape.length; j++) {
                            newShape[j] = oldShape[j] * 0.5;
                        }
                        smaller.setPolygonCoordinates(newShape);
                    } else {
                        smaller.setPolygonCoordinates(-15, -15, 15, -15, 15, 15, -15, 15);
                    }

                    world.addEntity(smaller);
                }
                world.removeEntity(this);
            } else {
                world.removeEntity(this);
                ScoreClient.sendPoints(50);
            }

        } else if ("ENEMY_BULLET".equals(otherType)) {
            world.removeEntity(other);
            world.removeEntity(this);
        }
    }
}
