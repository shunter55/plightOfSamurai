package com.mygdx.game.shapes;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.game.framework.core.renderer.WorldBodyAnimation;
import com.game.framework.core.world.WorldManager;
import com.game.framework.core2.bodies.WorldBody;
import com.game.framework.core2.builders.BodyBuilder;

public class Shape extends WorldBody {
   protected int weight;
   private int maxWeight = 2;
   BitmapFont font = new BitmapFont();

   public Shape(WorldManager manager, BodyBuilder builder) {
      super(manager, builder);
      weight = randomIntRange(1, maxWeight);
      increaseWeightRange();
      render.animations.addAnimation("square", new WorldBodyAnimation(
          1/4f, "shapes/square/square.jpg"
      ));
      render.animations.setDefaultAnimation("square");
   }

   public Shape setWeight(int weight) {
      this.weight = weight;
      return this;
   }

   public int getWeight() {
      return this.weight;
   }

   private void increaseWeightRange() {
      if (maxWeight < 600) {
         maxWeight += 5;
      }
   }

   public void addText(String text, Vector2 textPos) {
      this.render.text.setText(text);
      this.render.text.setOffset(textPos);
   }

   private static int randomIntRange(int min, int max) {
      return (int) (Math.random() * (max + 1 - min) + min);
   }
}
