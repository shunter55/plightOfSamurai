package com.game.framework.core2.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.framework.core.bodies.BoxWorldBody;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.bodies.WorldBody;
import com.game.framework.core.world.WorldManager;

public class CircleBodyBuilder extends BodyBuilder<CircleBodyBuilder> {

   private float _radius = 1;

   public CircleBodyBuilder(WorldManager worldManager) {
      super(worldManager);
   }

   @Override
   public BodyObj build() {
      return createCircle(
          world.getWorld(),
          _type,
          _isSensor,
          _pos.x,
          _pos.y,
          _radius * Math.abs(_scale.x),
          _density,
          _restitution,
          _angleRadians
      );
   }

   @Override
   public CircleBodyBuilder copy() {
      CircleBodyBuilder copy = new CircleBodyBuilder(world).radius(_radius);
      return copy.copyFrom(this);
   }

   public CircleBodyBuilder radius(float radius) {
      this._radius = radius;
      return this;
   }

   /**
    * Create a Box2D Box Body.
    * @param world The world the Body will be created in.
    * @param type The BodyType of the Body
    * @param isSensor True if body is a sensor.
    * @param x The x position
    * @param y The y position
    * @param radius The radius of the circle.
    * @param density The density of the box.
    * @return The newly created Box Body.
    */
   private static BodyObj createCircle(World world, BodyDef.BodyType type, boolean isSensor, float x, float y, float radius, float density, float restitution, float radians) {
      // body definition
      BodyDef playerDef = new BodyDef();
      playerDef.type = type;

      // shape definition
      CircleShape circleShape = new CircleShape();
      circleShape.setRadius(radius);

      // fixture definition
      FixtureDef fixtureDef = new FixtureDef();
      fixtureDef.shape = circleShape;
      fixtureDef.density = density;
      fixtureDef.restitution = restitution;
      fixtureDef.isSensor = isSensor;

      Body body = world.createBody(playerDef);
      Fixture f = body.createFixture(fixtureDef);
      body.setTransform(x, y, radians);

      circleShape.dispose();

      return new BodyObj(body, new Vector2(x, y), new Vector2(radius, radius));
   }

}
