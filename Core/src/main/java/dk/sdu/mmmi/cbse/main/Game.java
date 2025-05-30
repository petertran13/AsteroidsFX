package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

public class Game extends Application {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private final Text scoreText = new Text(10, 20, "Score: 0");
    private long lastScore = 0;

    public Game(List<IGamePluginService> igps, List<IEntityProcessingService> ieps, List<IPostEntityProcessingService> ipps) {}

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage window) {
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.getChildren().add(scoreText);

        Scene scene = new Scene(gameWindow);
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.LEFT) gameData.getKeys().setKey(GameKeys.LEFT, true);
            if (code == KeyCode.RIGHT) gameData.getKeys().setKey(GameKeys.RIGHT, true);
            if (code == KeyCode.UP) gameData.getKeys().setKey(GameKeys.UP, true);
            if (code == KeyCode.DOWN) gameData.getKeys().setKey(GameKeys.DOWN, true);
            if (code == KeyCode.SPACE) gameData.getKeys().setKey(GameKeys.SPACE, true);
            if (code == KeyCode.X) gameData.getKeys().setKey(GameKeys.X, true);
        });
        scene.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.LEFT) gameData.getKeys().setKey(GameKeys.LEFT, false);
            if (code == KeyCode.RIGHT) gameData.getKeys().setKey(GameKeys.RIGHT, false);
            if (code == KeyCode.UP) gameData.getKeys().setKey(GameKeys.UP, false);
            if (code == KeyCode.DOWN) gameData.getKeys().setKey(GameKeys.DOWN, false);
            if (code == KeyCode.SPACE) gameData.getKeys().setKey(GameKeys.SPACE, false);
            if (code == KeyCode.X) gameData.getKeys().setKey(GameKeys.X, false);
        });

        for (IGamePluginService plugin : getPluginServices()) {
            plugin.start(gameData, world);
        }

        for (Entity entity : world.getEntities()) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }

        render();
        window.setScene(scene);
        window.setTitle("ASTEROID");
        window.show();
    }

    void render() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
                gameData.getKeys().update();
                checkScoreUpdate();
            }
        }.start();
    }

    private void update() {
        for (IEntityProcessingService eps : getEntityProcessingServices()) {
            eps.process(gameData, world);
        }
        for (IPostEntityProcessingService peps : getPostEntityProcessingServices()) {
            peps.process(gameData, world);
        }
    }

    private void draw() {
        for (Entity e : polygons.keySet()) {
            if (!world.getEntities().contains(e)) {
                gameWindow.getChildren().remove(polygons.get(e));
                polygons.remove(e);
            }
        }

        for (Entity e : world.getEntities()) {
            Polygon poly = polygons.get(e);
            if (poly == null) {
                poly = new Polygon(e.getPolygonCoordinates());
                polygons.put(e, poly);
                gameWindow.getChildren().add(poly);
            }
            poly.setTranslateX(e.getX());
            poly.setTranslateY(e.getY());
            poly.setRotate(e.getRotation());
            if (e.getColor() == null) e.setColor("BLACK");
            poly.setFill(Color.valueOf(e.getColor()));
        }
    }

    private void checkScoreUpdate() {
        long currentScore = ScoreClient.getScore();
        if (currentScore != lastScore) {
            scoreText.setText("Score: " + currentScore);
            lastScore = currentScore;
        }
    }

    private Collection<? extends IGamePluginService> getPluginServices() {
        return ServiceLoader.load(IGamePluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return ServiceLoader.load(IEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
        return ServiceLoader.load(IPostEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
