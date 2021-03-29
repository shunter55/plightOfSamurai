package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

import javax.swing.text.View;
import java.util.*;

enum Categories {
    BALL ((short)0x01),
    SHAPE ((short)0x02),
    STOPPER ((short)0x04),
    CATCHER ((short)0x08),
    FRAME ((short)0x10);

    private final short category;

    private Categories(short category) {
        this.category = category;
    }

    public short value() {
        return category;
    }
}

enum Percents {
    POWERUPS (10),
    SHAPES ((100 - POWERUPS.value())/3);

    private final float percents;

    private Percents(float percents) {
        this.percents = percents;
    }

    public float value() {
        return percents;
    }
}

public class PhysicsBalls implements Screen {

    private static Vector2 worldSize = new Vector2(5, 10);

    private WorldManager worldManager;
    private WorldRenderer worldRenderer;
    private ArrayList<Ball> balls = new ArrayList<Ball>();
    private final float GRAVITY = 9.8f;
    private final float DELTA = 0.01f;
    private LinkedList<Ball> ballChamber = new LinkedList();
    private ArrayList<Ball> ballCatcher = new ArrayList<Ball>();
    private Queue<Shape> shapes = new LinkedList();
    private WorldBody stopper;
    private WorldBody catcher;
    private Vector2 click;
    private int altrow = 0;
    private float ballRadius = 0.175f;
    private int maxShapeWeight = 2;

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

        createFrame();

        stopper = worldManager.addBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(0f, 2.7f)
                .size(0.4f,0.4f)
                .isSensor(true)
                .categoryBits(Categories.STOPPER.value())
                .maskBits((short)0x1F)
        );

        stopper.controller.setInputAdapter(new InputAdapter() {
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (isFiring == false) {
                    click = worldRenderer.unproject(new Vector2(screenX, screenY));
                    isFiring = true;
                }
                return true;
            }
        });

        catcher = worldManager.addBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(0f, -5.6f)
                .size(worldSize.y * 3 / 5, worldSize.y * 0.5f / 5)
                .isSensor(true)
                .categoryBits(Categories.CATCHER.value())
                .maskBits((short)0x1F)
        );

        catcher.controller.collisions.beginCollision(body -> {
            if (!ballCatcher.contains(body)) {
System.out.println(ballCatcher.size());
                ballCatcher.add((Ball) body);
System.out.println(ballCatcher.size());

                if (ballCatcher.size() == balls.size()) {
                    shiftShapes();
                    spawnShapes();
                    for (Ball ball : balls) {
                        ball.body.body.setTransform(randomRange(-2, 2), 5, 0);
                        ball.body.body.setLinearVelocity(0, 0);
                        ball.body.rebuildBody(new Vector2(1,1), 0f, (short)0x1F);
                        ballChamber.add(ball);
                    }
                    ballCatcher.clear();
                    isFiring = false;
                }
            }
            return null;
        });

        addBall(ballRadius, new Vector2(0,4));
        addBall(ballRadius, new Vector2(1.1f, 4));
        addBall(ballRadius, new Vector2(-1, 4));
        for (Ball ball: balls) {
            ballChamber.add(ball);
        }

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
            Collections.sort(ballChamber, new DistanceCompare());

            if (isFiring &&
                stopper.body.body.getPosition().dst(curBall.getPosition()) < DELTA) {
System.out.println(ballChamber.size());
                ballChamber.peek().body.rebuildBody(new Vector2(1, 1), 0.5f,
                    (short)(0x1F ^ Categories.BALL.value()));
                ballChamber.peek().setGrav(true);
                curBall.setTransform(stopper.body.body.getPosition(), 0);
                Vector2 dir = getUnitDir( curBall.getPosition(), click );
                float len = getDir( curBall.getPosition(), click ).len();
                curBall.setLinearVelocity( dir.scl(5 + 3 * len / 2) );
                ballChamber.remove();
            }
            // Not Firing.
            else {
                ballChamber.peek().body.move.moveTo(stopper.body.body.getPosition(),
                    5, false);
            }
        }
        else {
            System.out.println("end");
        }

        for (int i=0; i<balls.size(); i++) {
            if (balls.get(i).getGrav() == true) {
                balls.get(i).body.body.setLinearVelocity(
                    balls.get(i).body.body.getLinearVelocity().add(new Vector2(0, -GRAVITY * delta))
                );
            }
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
            if (rand < Percents.POWERUPS.value()) {
                shape = new CircleBodyBuilder(worldManager)
                    .type(BodyDef.BodyType.StaticBody)
                    .pos(xStartpt, yStartpt)
                    .isSensor(true)
                    .categoryBits(Categories.SHAPE.value())
                    .maskBits((short)0x1F)
                    .scale(0.2f, 0.2f);
                PowerUp newPowerUp = new PowerUp(worldManager, shape);
                addShapes(newPowerUp);
            }
            else if (rand < Percents.POWERUPS.value() + Percents.SHAPES.value()) {
                shape = new BoxBodyBuilder(worldManager)
                    .type(BodyDef.BodyType.StaticBody)
                    .pos(xStartpt, yStartpt)
                    .size(0.5f, 0.5f)
                    .angleDegree(randomRange(0, 360))
                    .categoryBits(Categories.SHAPE.value())
                    .maskBits((short)0x1F);
                Shape newShape = new Shape(worldManager, shape);
                newShape.setWeight(randomIntRange(maxShapeWeight/2, maxShapeWeight + 1));
                newShape.addText(Integer.toString(newShape.getWeight()), new Vector2(-4f, 5f));
                addShapes(newShape);
            }
            else if (rand < Percents.POWERUPS.value() + Percents.SHAPES.value() * 2) {
                shape = new CircleBodyBuilder(worldManager)
                    .type(BodyDef.BodyType.StaticBody)
                    .pos(xStartpt, yStartpt)
                    .categoryBits(Categories.SHAPE.value())
                    .maskBits((short)0x1F)
                    .scale(0.3f, 0.3f);
                Shape newShape = new Shape(worldManager, shape);
                newShape.setWeight(randomIntRange(maxShapeWeight/2, maxShapeWeight + 1));
                newShape.addText(Integer.toString(newShape.getWeight()), new Vector2(-4f, 5f));
                addShapes(newShape);
            }
            else{ }
            xStartpt += 1.0f;
        }
        altrow = (altrow + 1) % 2;
        increaseWeightRange();
    }

    private void shiftShapes() {
        for (WorldBody shape : shapes) {
            shape.body.body.setTransform(
                new Vector2(shape.body.body.getPosition().x,
                    shape.body.body.getPosition().y + 1.0f),
                    shape.body.getRotationRadians());
        }
    }

    private void addShapes(Shape shape) {
        shapes.add(shape);

        shape.controller.collisions.preCollision(body -> {
            if (isFiring && ((Ball)body).body.body.getLinearVelocity().len() < 1) {
                ((Ball)body).body.body.setLinearVelocity(randomRange(3, 4), randomRange(3, 4));
            }
            return null;
        });

        shape.controller.collisions.beginCollision(body -> {
            shape.setWeight( shape.getWeight() - ((Ball)body).getWeight() );
            shape.addText(Integer.toString(shape.getWeight()), new Vector2(0f, 0f));

            if (shape.getWeight() <= 0) {
                if (shape.getClass() == PowerUp.class) {
                    powerUps((PowerUp) shape, (Ball)body);
                }

                shapes.remove(shape);
                shape.destroy();
            }

            return null;
        });
    }

    private void powerUps(PowerUp shape, Ball body) {
        if (shape.getType() == 0) {
            ballChamber.add(addBall(ballRadius, new Vector2(randomRange(-3, 3),5)));
        }
        else if (shape.getType() == 1) {
            increaseBallSize(body);
        }
        else if (shape.getType() == 2) {
            splitBigBall(body);
        }
    }

    private void increaseBallSize(Ball body) {
        if (ballIsSmall(body)) {
            body.body.rebuildBody(new Vector2(1.2f, 1.2f));
            body.setWeight(body.getWeight() + 1);
        }
        else {
            for (int i = 0; ; i++) {
                if (i == balls.size()) {
                    ballChamber.add(addBall(ballRadius, new Vector2(randomRange(-3, 3),5)));
                    break;
                }
                Ball current = balls.get(i);
                if (ballIsSmall(current)) {
                    current.body.rebuildBody(new Vector2(1.2f, 1.2f));
                    current.setWeight(current.getWeight() + 1);
                    break;
                }
            }
        }
    }

    private void splitBigBall(Ball body) {
        if (ballIsBig(body)) {
            body.body.rebuildBody(new Vector2(1/1.2f, 1/1.2f));
            body.setWeight(body.getWeight() - 1);
            addBall(ballRadius, new Vector2(randomRange(-3, 3),5));
        }
        else {
            for (int i = 0; ; i++) {
                if (i == balls.size()) {
                    ballChamber.add(addBall(ballRadius, new Vector2(randomRange(-3, 3),5)));
                    break;
                }
                Ball current = balls.get(i);
                if (ballIsBig(current)) {
                    current.body.rebuildBody(new Vector2(1/1.2f, 1/1.2f));
                    current.setWeight(current.getWeight() - 1);
                    addBall(ballRadius, new Vector2(randomRange(-3, 3),5));
                    break;
                }
            }
        }
    }
    private boolean ballIsBig(Ball ball) {
        return ball.body.getDimensions().equals( new Vector2(ballRadius*1.2f, ballRadius*1.2f));
    }

    private boolean ballIsSmall(Ball ball) {
        return !ballIsBig(ball);
    }

    private Ball addBall(float size, Vector2 pos) {
        CircleBodyBuilder circle = new CircleBodyBuilder(worldManager)
            .pos(pos)
            .restitution(0f)
            .categoryBits(Categories.BALL.value())
            .maskBits((short)0x1F);

        circle.scale(size, size);
        Ball ball = new Ball(worldManager, circle);
        balls.add( ball );

        return ball;
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

    private void increaseWeightRange() {
        if (maxShapeWeight < 600) {
            maxShapeWeight += 5;
        }
    }

    private void createFrame() {
        worldManager.addBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(-3.0f, 0f)
                .categoryBits(Categories.FRAME.value())
                .maskBits((short)0x1F)
                .size(0.01f, worldSize.y)
        );

        worldManager.addBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(3.0f, 0f)
                .categoryBits(Categories.FRAME.value())
                .maskBits((short)0x1F)
                .size(0.01f, worldSize.y)
        );

        worldManager.addBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(-1.6f, 3.5f)
                .categoryBits(Categories.FRAME.value())
                .maskBits((short)0x1F)
                .size(worldSize.x * 3 / 5, 0.01f)
                .angleRadian(-(float)Math.PI/8)
        );

        worldManager.addBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(1.6f, 3.5f)
                .categoryBits(Categories.FRAME.value())
                .maskBits((short)0x1F)
                .size(worldSize.x * 3 / 5, 0.01f)
                .angleRadian((float)Math.PI/8)
        );

        worldManager.addBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(-1.7f, 3f)
                .categoryBits(Categories.FRAME.value())
                .maskBits((short)0x1F)
                .size(0.01f, worldSize.y * 1.3f / 5)
                .angleRadian((float)Math.PI/2)
        );

        worldManager.addBody(
            new BoxBodyBuilder(worldManager)
                .type(BodyDef.BodyType.StaticBody)
                .pos(1.7f, 3f)
                .categoryBits(Categories.FRAME.value())
                .maskBits((short)0x1F)
                .size(0.01f, worldSize.y * 1.3f / 5)
                .angleRadian((float)Math.PI/2)
        );
    }

}
