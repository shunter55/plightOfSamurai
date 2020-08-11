package com.game.framework.core2.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.framework.core.bodies.BoxWorldBody;
import com.game.framework.core.bodies.Function;
import com.game.framework.core.bodies.WorldBody;
import com.game.framework.core.world.WorldManager;

public class CircleBodyBuilder extends BodyBuilder {

   private BodyDef.BodyType _type = BodyDef.BodyType.DynamicBody;
   private boolean _isSensor = false;
   private Vector2 _pos = new Vector2(0, 0);
   private float _radius = 1;
   private float _density = 1;

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
          _radians
      );
   }

   @Override
   public BodyBuilder copy() {
      return new CircleBodyBuilder(world).type(_type).isSensor(_isSensor).pos(_pos).radius(_radius).scale(_scale).density(_density);
   }

   public CircleBodyBuilder type(BodyDef.BodyType type) {
      this._type = type;
      return this;
   }

   public CircleBodyBuilder isSensor(boolean isSensor) {
      this._isSensor = isSensor;
      return this;
   }

   public CircleBodyBuilder pos(Vector2 pos) {
      this._pos = pos;
      return this;
   }

   public CircleBodyBuilder pos(float x, float y) {
      this._pos.x = x;
      this._pos.y = y;
      return this;
   }

   public CircleBodyBuilder posX(float x) {
      this._pos.x = x;
      return this;
   }

   public CircleBodyBuilder posY(float y) {
      this._pos.y = y;
      return this;
   }

   public CircleBodyBuilder radius(float radius) {
      this._radius = radius;
      return this;
   }

   public CircleBodyBuilder scale(Vector2 scale) {
      super.scale(scale);
      return this;
   }

   public CircleBodyBuilder scaleX(float x) {
      super.scaleX(x);
      return this;
   }

   public CircleBodyBuilder scaleY(float y) {
      super.scaleY(y);
      return this;
   }

   public CircleBodyBuilder density(float density) {
      this._density = density;
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
