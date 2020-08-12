package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.game.framework.core.bodies.builders.BoxBuilder;
import com.game.framework.core.bodies.update.UpdateMethods;
import com.game.framework.core.controller.InputProcessor;
import com.game.framework.core.renderer.WorldBodyAnimation;
import com.game.framework.core.renderer.WorldRenderer;
import com.game.framework.core.world.WorldManager;
import com.game.framework.core2.bodies.WorldBody;
import com.game.framework.core2.builders.BodyBuilder;
import com.game.framework.core2.builders.BoxBodyBuilder;
import com.game.framework.core2.builders.CircleBodyBuilder;
import com.mygdx.game.GameMain;
import com.mygdx.game.shapes.Ball;
import com.mygdx.game.shapes.PowerUp;
import com.mygdx.game.shapes.Shape;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PhysicsBalls implements Screen {

    private static Vector2 worldSize = new Vector2(5, 10);

    private WorldManager worldManager;
    private WorldRenderer worldRenderer;
    private ArrayList<Ball> balls = new ArrayList<Ball>();
    private final float GRAVITY = 9.8f;
    private final float DELTA = 0.01f;
    private Queue<Ball> ballChamber = new LinkedList();
    private ArrayList<Ball> ballCatcher = new ArrayList<Ball>();
    private Queue<Shape> shapes = new LinkedList();
    private WorldBody stopper;
    private WorldBody catcher;
    private Vector2 click;
    private int altrow = 0;

    private boolean isFiring = false;

    public PhysicsBalls(GameMain game) {
        start();
    }

    public void start() {
        if (worldManager != null)
            worldManager.dispose();
        if (worldRenderer != null)
            worldRenderer.dispose();
        worldManager = new WorldManager();
        worldRenderer = new WorldRenderer(WorldRenderer.CameraMode.Zoom, worldSize.x, worldSize.y);

        worldManager.addBody(
            new BoxBodyBuilder(worldManager)
            .type(BodyDef.BodyType.StaticBody)
            .pos(-3.0f, 0f)
            .size(0.01f, 5f)
        );

        worldManager.addBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(3.0f, 0f)
                .size(0.01f, 5f)
        );

        worldManager.addBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(-1.6f, 3.5f)
                .size(1.5f, 0.01f)
                .angleRadian(-(float)Math.PI/8)
        );

        worldManager.addBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(1.6f, 3.5f)
                .size(1.5f, 0.01f)
                .angleRadian((float)Math.PI/8)
        );

        worldManager.addBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(-1.7f, 3f)
                .size(0.01f, 1.3f)
                .angleRadian((float)Math.PI/2)
        );

        worldManager.addBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(1.7f, 3f)
                .size(0.01f, 1.3f)
                .angleRadian((float)Math.PI/2)
        );

        stopper = worldManager.addBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(0f, 2.7f)
                .size(0.2f, 0.2f)
                .isSensor(true)
        );

        stopper.controller.setInputAdapter(new InputAdapter() {
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                click = worldRenderer.unproject(new Vector2(screenX, screenY));
                isFiring = true;
                return true;
            }
        });

        stopper.controller.collisions.beginCollision(body -> {
            if (!ballChamber.contains(body))
                ballChamber.add((Ball)body);
            return null;
        });

        catcher = worldManager.addBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(0f, -5.6f)
                .size(3.0f, 0.5f)
                .isSensor(true)
        );

        catcher.controller.collisions.beginCollision(body -> {
            ballCatcher.add((Ball)body);
            return null;
        });

        addBall(0.175f, new Vector2(0,4));
        addBall(0.175f, new Vector2(1.1f, 4));
        addBall(0.175f, new Vector2(-1, 4));

        spawnShapes();

    }

    @Override
    public void show() {
        InputProcessor.getInputProcessor();
    }

    @Override
    public void render(float delta) {
        // Background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (ballChamber.size() > 0) {
            Body curBall = ballChamber.peek().body.body;
            // Firing and Ball is in center.
            if (isFiring &&
                stopper.body.body.getPosition().dst(curBall.getPosition()) < DELTA) {
                ballChamber.peek().setGrav(true);
                curBall.setTransform(stopper.body.body.getPosition(), 0);
                Vector2 dir = getUnitDir( curBall.getPosition(), click );
                float len = getDir( curBall.getPosition(), click ).len();
                curBall.setLinearVelocity( dir.scl(5 + 3 * len / 2) );
                ballChamber.remove();
                //isFiring = false;
            }
            // Not Firing.
            else {
                ballChamber.peek().setGrav(false);
                ballChamber.peek().body.move.moveTo(stopper.body.body.getPosition(),
                    5, false);

            }
        }

        for (int i=0; i<balls.size(); i++) {
            if (balls.get(i).getGrav() == true) {
                balls.get(i).body.body.setLinearVelocity(
                    balls.get(i).body.body.getLinearVelocity().add(new Vector2(0, -GRAVITY * delta))
                );
            }
        }

        if (ballCatcher.size() == balls.size()) {
            shiftShapes();
            spawnShapes();
            for (Ball ball : balls) {
                ball.body.body.setTransform(randomRange(-3, 3),5,0);
                ball.body.body.setLinearVelocity(0,0);
            }
            ballCatcher.clear();
            isFiring = false;
        }

        // Update the world.
        worldManager.updatePhysics(1f / 60f);

        // Render the world.
        worldRenderer.render(worldManager);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        worldManager.dispose();
        worldRenderer.dispose();
    }

    private void spawnShapes() {
        float xStartpt;
        float yStartpt = -4.5f;

        if (altrow == 0)
            xStartpt = -2.5f;
        else
            xStartpt = -1.5f;

        for (int i = 0; i < 6 - altrow; i++) {
            BodyBuilder shape;
            int rand = randomIntRange(0, 100);
            System.out.println(rand);
            if (rand < 10) {
                shape = new CircleBodyBuilder(worldManager)
                    .type(BodyDef.BodyType.StaticBody)
                    .pos(xStartpt, yStartpt)
                    .isSensor(true)
                    .scale(0.2f, 0.2f);
                addShapes(new PowerUp(worldManager, shape));
            }
            else if (rand < 40) {
                shape = new BoxBodyBuilder(worldManager)
                    .type(BodyDef.BodyType.StaticBody)
                    .pos(xStartpt, yStartpt)
                    .size(0.25f, 0.25f)
                    .angleDegree(randomRange(0, 360));
                addShapes(new Shape(worldManager, shape));
            }
            else if (rand < 70) {
                shape = new CircleBodyBuilder(worldManager)
                    .type(BodyDef.BodyType.StaticBody)
                    .pos(xStartpt, yStartpt)
                    .scale(0.3f, 0.3f);
                addShapes(new Shape(worldManager, shape));
            }
            else{ }
            xStartpt += 1.0f;
        }
        altrow = (altrow + 1) % 2;
    }

    private void shiftShapes() {
        System.out.println(shapes.size());
        for (WorldBody shape : shapes) {
            shape.body.body.setTransform(
                new Vector2(shape.body.body.getPosition().x,
                    shape.body.body.getPosition().y + 1.0f),
                    shape.body.getRotationRadians());
        }
    }

    private void addShapes(Shape shape) {
        shapes.add(shape);

        shape.controller.collisions.beginCollision(body -> {
            shape.setWeight( shape.getWeight() - ((Ball)body).getWeight() );
            if (shape.getWeight() <= 0) {
                if (shape.getClass() == PowerUp.class) {
                    System.out.println("A");
                    powerUps((PowerUp) shape);
                    System.out.println("B");
                }

                shapes.remove(shape);
                shape.destroy();
            }
            return null;
        });
    }

    private void powerUps(PowerUp shape) {
        if (shape.getType() == 0) {
            addBall(0.175f, new Vector2(randomRange(-3, 3),5));
        }
        else if (shape.getType() == 1) {
            addBall(0.175f, new Vector2(randomRange(-3, 3),5));
        }
        else if (shape.getType() == 2) {
            addBall(0.175f, new Vector2(randomRange(-3, 3),5));
        }
    }

    private void addBall(float size, Vector2 pos) {
        CircleBodyBuilder ball = (CircleBodyBuilder)new CircleBodyBuilder(worldManager).pos(pos).restitution(0.5f);
        ball.scale(size, size);
        balls.add( new Ball(worldManager, ball) );

        //worldManager.addBody(ball);
    }

    private static Vector2 getDir(Vector2 posA, Vector2 posB) {
        return new Vector2(posB.x - posA.x, posB.y - posA.y);
    }

    private static Vector2 getUnitDir(Vector2 posA, Vector2 posB) {
        Vector2 freq = getDir(posA, posB);
        return new Vector2(freq.x / freq.len(), freq.y / freq.len());
    }

    private static float randomRange(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }

    private static int randomIntRange(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

}
