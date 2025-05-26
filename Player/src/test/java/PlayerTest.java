
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.playersystem.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;
    private World world;
    private GameData gameData;

    static class TestEntity extends Entity {
        @Override
        public void handleCollision(GameData gameData, World world, Entity other) {

        }
    }

    @BeforeEach
    void setUp() {
        player = new Player();
        world = new World();
        gameData = new GameData();
        world.addEntity(player);
    }

    @Test
    void playerShouldDieWhenCollidingWithEnemy() {
        Entity enemy = new TestEntity();
        enemy.setType("ENEMY");
        world.addEntity(enemy);

        player.handleCollision(gameData, world, enemy);

        assertFalse(world.getEntities().contains(player), "Player should be removed after enemy collision");
        assertFalse(world.getEntities().contains(enemy), "Enemy should be removed after colliding with player");
    }

    @Test
    void playerShouldDieAfterTenEnemyBullets() {
        for (int i = 0; i < 10; i++) {
            Entity bullet = new TestEntity();
            bullet.setType("ENEMY_BULLET");
            world.addEntity(bullet);

            player.handleCollision(gameData, world, bullet);
        }

        assertEquals(0, player.getHealth(), "Player health should be 0 after 10 hits");
        assertFalse(world.getEntities().contains(player), "Player should be removed after 10 enemy bullet hits");
    }
}


